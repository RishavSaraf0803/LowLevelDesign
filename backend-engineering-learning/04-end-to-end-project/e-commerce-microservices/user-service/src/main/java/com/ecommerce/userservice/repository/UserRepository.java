package com.ecommerce.userservice.repository;

import com.ecommerce.userservice.domain.User;
import com.ecommerce.userservice.domain.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * User Repository - Data access layer for User entities
 * 
 * Design Patterns:
 * - Repository Pattern: Encapsulates data access logic
 * - Query Methods: Spring Data JPA method name conventions
 * - Custom Queries: Complex business logic queries with @Query
 * - Soft Delete: Filters out deleted users by default
 * 
 * Performance Considerations:
 * - Uses indexed fields for efficient queries
 * - Implements pagination for large result sets
 * - Provides bulk operations for batch processing
 * - Includes query optimization hints
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // Basic finder methods with soft delete filtering
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND (u.email = :emailOrUsername OR u.username = :emailOrUsername)")
    Optional<User> findByEmailOrUsername(@Param("emailOrUsername") String emailOrUsername);

    // Status-based queries
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND u.status = :status")
    Page<User> findByStatus(@Param("status") UserStatus status, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND u.status IN :statuses")
    List<User> findByStatusIn(@Param("statuses") List<UserStatus> statuses);

    // Security-related queries
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND u.failedLoginAttempts >= :threshold")
    List<User> findUsersWithExcessiveFailedLogins(@Param("threshold") int threshold);

    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND u.accountLockedUntil IS NOT NULL AND u.accountLockedUntil < :now")
    List<User> findUsersWithExpiredLocks(@Param("now") LocalDateTime now);

    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND u.lastLogin < :cutoffDate")
    Page<User> findInactiveUsers(@Param("cutoffDate") LocalDateTime cutoffDate, Pageable pageable);

    // Verification-related queries
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND u.emailVerified = false AND u.createdAt < :cutoffDate")
    List<User> findUnverifiedUsers(@Param("cutoffDate") LocalDateTime cutoffDate);

    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND u.twoFactorEnabled = true")
    Page<User> findUsersWithTwoFactorEnabled(Pageable pageable);

    // Role-based queries
    @Query("SELECT u FROM User u JOIN u.roles r WHERE u.deletedAt IS NULL AND r.name = :roleName")
    Page<User> findByRoleName(@Param("roleName") String roleName, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE u.deletedAt IS NULL AND r.id = :roleId")
    List<User> findByRoleId(@Param("roleId") String roleId);

    // Search functionality
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND " +
           "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<User> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Bulk operations
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = :loginTime WHERE u.id = :userId")
    void updateLastLogin(@Param("userId") String userId, @Param("loginTime") LocalDateTime loginTime);

    @Modifying
    @Query("UPDATE User u SET u.failedLoginAttempts = :attempts WHERE u.id = :userId")
    void updateFailedLoginAttempts(@Param("userId") String userId, @Param("attempts") int attempts);

    @Modifying
    @Query("UPDATE User u SET u.accountLockedUntil = :lockUntil WHERE u.id = :userId")
    void lockUserAccount(@Param("userId") String userId, @Param("lockUntil") LocalDateTime lockUntil);

    @Modifying
    @Query("UPDATE User u SET u.accountLockedUntil = NULL, u.failedLoginAttempts = 0 WHERE u.id = :userId")
    void unlockUserAccount(@Param("userId") String userId);

    @Modifying
    @Query("UPDATE User u SET u.emailVerified = true WHERE u.id = :userId")
    void markEmailAsVerified(@Param("userId") String userId);

    @Modifying
    @Query("UPDATE User u SET u.phoneVerified = true WHERE u.id = :userId")
    void markPhoneAsVerified(@Param("userId") String userId);

    @Modifying
    @Query("UPDATE User u SET u.deletedAt = :deletedAt, u.status = 'DELETED' WHERE u.id = :userId")
    void softDeleteUser(@Param("userId") String userId, @Param("deletedAt") LocalDateTime deletedAt);

    // Statistics and reporting
    @Query("SELECT COUNT(u) FROM User u WHERE u.deletedAt IS NULL AND u.status = :status")
    long countByStatus(@Param("status") UserStatus status);

    @Query("SELECT COUNT(u) FROM User u WHERE u.deletedAt IS NULL AND u.createdAt >= :startDate AND u.createdAt <= :endDate")
    long countUsersCreatedBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(u) FROM User u WHERE u.deletedAt IS NULL AND u.lastLogin >= :startDate AND u.lastLogin <= :endDate")
    long countActiveUsersBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Existence checks (for unique constraint validation)
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.deletedAt IS NULL AND u.email = :email AND u.id != :excludeId")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("excludeId") String excludeId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.deletedAt IS NULL AND u.username = :username AND u.id != :excludeId")
    boolean existsByUsernameAndIdNot(@Param("username") String username, @Param("excludeId") String excludeId);

    // Native query for complex reporting (example)
    @Query(value = "SELECT DATE(created_at) as date, COUNT(*) as registrations " +
                   "FROM users " +
                   "WHERE deleted_at IS NULL AND created_at >= :startDate " +
                   "GROUP BY DATE(created_at) " +
                   "ORDER BY date DESC", 
           nativeQuery = true)
    List<Object[]> getRegistrationStatistics(@Param("startDate") LocalDateTime startDate);
}