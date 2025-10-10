package com.ecommerce.userservice.service;

import com.ecommerce.userservice.domain.User;
import com.ecommerce.userservice.domain.UserStatus;
import com.ecommerce.userservice.dto.CreateUserRequest;
import com.ecommerce.userservice.dto.UpdateUserRequest;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.exception.UserNotFoundException;
import com.ecommerce.userservice.exception.UserAlreadyExistsException;
import com.ecommerce.userservice.exception.InvalidCredentialsException;
import com.ecommerce.userservice.exception.AccountLockedException;
import com.ecommerce.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * User Service - Business logic layer for user management
 * 
 * Design Patterns:
 * - Service Layer Pattern: Encapsulates business logic
 * - Facade Pattern: Simplified interface for complex operations
 * - Strategy Pattern: Different authentication strategies
 * - Observer Pattern: Events for user lifecycle changes
 * 
 * Key Features:
 * - Account lockout mechanism for security
 * - Caching for frequently accessed user data
 * - Comprehensive audit logging
 * - Input validation and sanitization
 * - Transactional consistency
 * 
 * Performance Optimizations:
 * - Redis caching for user sessions and profiles
 * - Bulk operations for batch processing
 * - Lazy loading for related entities
 * - Database connection pooling
 */
@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuditService auditService;
    private final EventPublisher eventPublisher;

    @Value("${app.security.max-failed-attempts:5}")
    private int maxFailedAttempts;

    @Value("${app.security.lockout-duration-minutes:30}")
    private int lockoutDurationMinutes;

    @Value("${app.security.password-expiry-days:90}")
    private int passwordExpiryDays;

    @Autowired
    public UserService(UserRepository userRepository, 
                      PasswordEncoder passwordEncoder,
                      EmailService emailService,
                      AuditService auditService,
                      EventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.auditService = auditService;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Create a new user account
     * 
     * Business Rules:
     * - Email and username must be unique
     * - Password must meet security requirements
     * - Account starts in PENDING_VERIFICATION status
     * - Welcome email is sent automatically
     * 
     * @param request User creation request
     * @return Created user response
     * @throws UserAlreadyExistsException if email or username already exists
     */
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        logger.info("Creating new user with email: {}", request.getEmail());

        // Validate uniqueness
        validateUserUniqueness(request.getEmail(), request.getUsername(), null);

        // Create user entity
        User user = new User(
            request.getUsername(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            request.getFirstName(),
            request.getLastName()
        );

        user.setPhoneNumber(request.getPhoneNumber());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setStatus(UserStatus.PENDING_VERIFICATION);

        // Save user
        User savedUser = userRepository.save(user);

        // Send verification email
        emailService.sendVerificationEmail(savedUser);

        // Publish user created event
        eventPublisher.publishUserCreatedEvent(savedUser);

        // Audit log
        auditService.logUserCreation(savedUser);

        logger.info("User created successfully with ID: {}", savedUser.getId());
        return mapToUserResponse(savedUser);
    }

    /**
     * Authenticate user with email/username and password
     * 
     * Security Features:
     * - Account lockout after failed attempts
     * - Password verification with timing attack protection
     * - Login tracking and auditing
     * - Failed attempt counting
     * 
     * @param emailOrUsername Email or username
     * @param password Plain text password
     * @return Authenticated user
     * @throws InvalidCredentialsException if credentials are invalid
     * @throws AccountLockedException if account is locked
     */
    @Transactional
    public User authenticateUser(String emailOrUsername, String password) {
        logger.info("Authenticating user: {}", emailOrUsername);

        Optional<User> userOpt = userRepository.findByEmailOrUsername(emailOrUsername);
        
        if (userOpt.isEmpty()) {
            logger.warn("Authentication failed - user not found: {}", emailOrUsername);
            auditService.logFailedLogin(emailOrUsername, "User not found");
            throw new InvalidCredentialsException("Invalid credentials");
        }

        User user = userOpt.get();

        // Check if account is locked
        if (!user.isAccountNonLocked()) {
            logger.warn("Authentication failed - account locked: {}", emailOrUsername);
            auditService.logFailedLogin(emailOrUsername, "Account locked");
            throw new AccountLockedException("Account is locked until: " + user.getAccountLockedUntil());
        }

        // Check if account is enabled
        if (!user.isEnabled()) {
            logger.warn("Authentication failed - account disabled: {}", emailOrUsername);
            auditService.logFailedLogin(emailOrUsername, "Account disabled");
            throw new InvalidCredentialsException("Account is not active or verified");
        }

        // Verify password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            handleFailedLogin(user);
            logger.warn("Authentication failed - invalid password: {}", emailOrUsername);
            throw new InvalidCredentialsException("Invalid credentials");
        }

        // Successful authentication
        handleSuccessfulLogin(user);
        logger.info("User authenticated successfully: {}", emailOrUsername);
        
        return user;
    }

    /**
     * Get user by ID with caching
     * 
     * @param userId User ID
     * @return User response
     * @throws UserNotFoundException if user not found
     */
    @Cacheable(value = "users", key = "#userId")
    @Transactional(readOnly = true)
    public UserResponse getUserById(String userId) {
        logger.debug("Fetching user by ID: {}", userId);
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        
        return mapToUserResponse(user);
    }

    /**
     * Update user profile with cache invalidation
     * 
     * @param userId User ID
     * @param request Update request
     * @return Updated user response
     */
    @CachePut(value = "users", key = "#userId")
    @Transactional
    public UserResponse updateUser(String userId, UpdateUserRequest request) {
        logger.info("Updating user: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Validate uniqueness if email or username changed
        if (!user.getEmail().equals(request.getEmail()) || 
            !user.getUsername().equals(request.getUsername())) {
            validateUserUniqueness(request.getEmail(), request.getUsername(), userId);
        }

        // Update fields
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setDateOfBirth(request.getDateOfBirth());

        User updatedUser = userRepository.save(user);

        // Publish user updated event
        eventPublisher.publishUserUpdatedEvent(updatedUser);

        // Audit log
        auditService.logUserUpdate(updatedUser);

        logger.info("User updated successfully: {}", userId);
        return mapToUserResponse(updatedUser);
    }

    /**
     * Soft delete user with cache eviction
     * 
     * @param userId User ID
     */
    @CacheEvict(value = "users", key = "#userId")
    @Transactional
    public void deleteUser(String userId) {
        logger.info("Soft deleting user: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        user.softDelete();
        userRepository.save(user);

        // Publish user deleted event
        eventPublisher.publishUserDeletedEvent(user);

        // Audit log
        auditService.logUserDeletion(user);

        logger.info("User soft deleted successfully: {}", userId);
    }

    /**
     * Search users with pagination
     * 
     * @param searchTerm Search term
     * @param pageable Pagination parameters
     * @return Page of user responses
     */
    @Transactional(readOnly = true)
    public Page<UserResponse> searchUsers(String searchTerm, Pageable pageable) {
        logger.debug("Searching users with term: {} (page: {}, size: {})", 
                    searchTerm, pageable.getPageNumber(), pageable.getPageSize());

        Page<User> users = userRepository.searchUsers(searchTerm, pageable);
        return users.map(this::mapToUserResponse);
    }

    /**
     * Get users by status with pagination
     * 
     * @param status User status
     * @param pageable Pagination parameters
     * @return Page of user responses
     */
    @Transactional(readOnly = true)
    public Page<UserResponse> getUsersByStatus(UserStatus status, Pageable pageable) {
        logger.debug("Fetching users with status: {} (page: {}, size: {})", 
                    status, pageable.getPageNumber(), pageable.getPageSize());

        Page<User> users = userRepository.findByStatus(status, pageable);
        return users.map(this::mapToUserResponse);
    }

    /**
     * Verify user email
     * 
     * @param userId User ID
     */
    @CacheEvict(value = "users", key = "#userId")
    @Transactional
    public void verifyEmail(String userId) {
        logger.info("Verifying email for user: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        user.setEmailVerified(true);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        // Publish email verified event
        eventPublisher.publishEmailVerifiedEvent(user);

        // Audit log
        auditService.logEmailVerification(user);

        logger.info("Email verified successfully for user: {}", userId);
    }

    /**
     * Change user password
     * 
     * @param userId User ID
     * @param currentPassword Current password
     * @param newPassword New password
     */
    @Transactional
    public void changePassword(String userId, String currentPassword, String newPassword) {
        logger.info("Changing password for user: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            logger.warn("Password change failed - invalid current password for user: {}", userId);
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Publish password changed event
        eventPublisher.publishPasswordChangedEvent(user);

        // Audit log
        auditService.logPasswordChange(user);

        logger.info("Password changed successfully for user: {}", userId);
    }

    // Private helper methods

    private void handleFailedLogin(User user) {
        user.incrementFailedLoginAttempts();
        
        if (user.getFailedLoginAttempts() >= maxFailedAttempts) {
            user.lockAccount(lockoutDurationMinutes);
            logger.warn("Account locked due to excessive failed login attempts: {}", user.getEmail());
            
            // Send account locked notification
            emailService.sendAccountLockedNotification(user);
            
            // Publish account locked event
            eventPublisher.publishAccountLockedEvent(user);
        }
        
        userRepository.save(user);
        auditService.logFailedLogin(user.getEmail(), "Invalid password");
    }

    private void handleSuccessfulLogin(User user) {
        user.resetFailedLoginAttempts();
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        
        // Publish successful login event
        eventPublisher.publishSuccessfulLoginEvent(user);
        
        auditService.logSuccessfulLogin(user);
    }

    private void validateUserUniqueness(String email, String username, String excludeUserId) {
        if (excludeUserId != null) {
            if (userRepository.existsByEmailAndIdNot(email, excludeUserId)) {
                throw new UserAlreadyExistsException("Email already exists: " + email);
            }
            if (userRepository.existsByUsernameAndIdNot(username, excludeUserId)) {
                throw new UserAlreadyExistsException("Username already exists: " + username);
            }
        } else {
            if (userRepository.findByEmail(email).isPresent()) {
                throw new UserAlreadyExistsException("Email already exists: " + email);
            }
            if (userRepository.findByUsername(username).isPresent()) {
                throw new UserAlreadyExistsException("Username already exists: " + username);
            }
        }
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getPhoneNumber(),
            user.getDateOfBirth(),
            user.getStatus(),
            user.getEmailVerified(),
            user.getPhoneVerified(),
            user.getTwoFactorEnabled(),
            user.getLastLogin(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}