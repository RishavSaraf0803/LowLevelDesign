package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.domain.UserStatus;
import com.ecommerce.userservice.dto.CreateUserRequest;
import com.ecommerce.userservice.dto.UpdateUserRequest;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * User Controller - REST API endpoints for user management
 * 
 * Design Principles:
 * - RESTful API design with proper HTTP methods and status codes
 * - Comprehensive input validation and error handling
 * - OpenAPI/Swagger documentation for API discoverability
 * - Pagination support for list endpoints
 * - Security considerations with rate limiting and authentication
 * 
 * API Features:
 * - CRUD operations for user management
 * - Search and filtering capabilities
 * - Bulk operations for administrative tasks
 * - Health checks and monitoring endpoints
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "APIs for user account management and authentication")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Create a new user account
     */
    @Operation(summary = "Create a new user account", 
               description = "Register a new user with email verification")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "User already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        
        logger.info("Creating user with email: {}", request.getEmail());
        
        UserResponse user = userService.createUser(request);
        
        logger.info("User created successfully with ID: {}", user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Get user by ID
     */
    @Operation(summary = "Get user by ID", 
               description = "Retrieve user information by unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId) {
        
        logger.debug("Fetching user with ID: {}", userId);
        
        UserResponse user = userService.getUserById(userId);
        
        return ResponseEntity.ok(user);
    }

    /**
     * Update user profile
     */
    @Operation(summary = "Update user profile", 
               description = "Update user profile information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "409", description = "Email or username already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId,
            @Valid @RequestBody UpdateUserRequest request) {
        
        logger.info("Updating user with ID: {}", userId);
        
        UserResponse user = userService.updateUser(userId, request);
        
        logger.info("User updated successfully: {}", userId);
        return ResponseEntity.ok(user);
    }

    /**
     * Delete user (soft delete)
     */
    @Operation(summary = "Delete user account", 
               description = "Soft delete user account (can be recovered)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId) {
        
        logger.info("Deleting user with ID: {}", userId);
        
        userService.deleteUser(userId);
        
        logger.info("User deleted successfully: {}", userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search users with pagination
     */
    @Operation(summary = "Search users", 
               description = "Search users by name, email, or username with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<UserResponse>> searchUsers(
            @Parameter(description = "Search term")
            @RequestParam(required = false) String q,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction")
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        logger.debug("Searching users with term: '{}' (page: {}, size: {})", q, page, size);
        
        // Validate pagination parameters
        if (page < 0) page = 0;
        if (size < 1 || size > 100) size = 20;
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<UserResponse> users;
        if (q != null && !q.trim().isEmpty()) {
            users = userService.searchUsers(q.trim(), pageable);
        } else {
            users = userService.getUsersByStatus(UserStatus.ACTIVE, pageable);
        }
        
        logger.debug("Found {} users", users.getTotalElements());
        return ResponseEntity.ok(users);
    }

    /**
     * Get users by status
     */
    @Operation(summary = "Get users by status", 
               description = "Retrieve users filtered by account status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status parameter"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<UserResponse>> getUsersByStatus(
            @Parameter(description = "User status", required = true)
            @PathVariable UserStatus status,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction")
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        logger.debug("Fetching users with status: {} (page: {}, size: {})", status, page, size);
        
        // Validate pagination parameters
        if (page < 0) page = 0;
        if (size < 1 || size > 100) size = 20;
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<UserResponse> users = userService.getUsersByStatus(status, pageable);
        
        logger.debug("Found {} users with status {}", users.getTotalElements(), status);
        return ResponseEntity.ok(users);
    }

    /**
     * Verify user email
     */
    @Operation(summary = "Verify user email", 
               description = "Mark user email as verified")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email verified successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{userId}/verify-email")
    public ResponseEntity<Void> verifyEmail(
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId) {
        
        logger.info("Verifying email for user: {}", userId);
        
        userService.verifyEmail(userId);
        
        logger.info("Email verified successfully for user: {}", userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Change user password
     */
    @Operation(summary = "Change user password", 
               description = "Change user password with current password verification")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password changed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid current password"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{userId}/change-password")
    public ResponseEntity<Void> changePassword(
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        
        logger.info("Changing password for user: {}", userId);
        
        userService.changePassword(userId, currentPassword, newPassword);
        
        logger.info("Password changed successfully for user: {}", userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Health check endpoint
     */
    @Operation(summary = "Health check", 
               description = "Check service health status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service is healthy"),
        @ApiResponse(responseCode = "503", description = "Service is unhealthy")
    })
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        // In production, this would check database connectivity,
        // external service dependencies, etc.
        return ResponseEntity.ok("User Service is healthy");
    }

    /**
     * Get service metrics
     */
    @Operation(summary = "Get service metrics", 
               description = "Retrieve basic service metrics for monitoring")
    @GetMapping("/metrics")
    public ResponseEntity<String> getMetrics() {
        // In production, this would return actual metrics
        // like user count, active sessions, response times, etc.
        return ResponseEntity.ok("Metrics endpoint - integrate with Prometheus/Micrometer");
    }
}