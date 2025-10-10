package com.ecommerce.userservice.domain;

/**
 * User Status Enumeration
 * 
 * Represents the various states a user account can be in.
 * This enables fine-grained control over user access and account lifecycle.
 */
public enum UserStatus {
    /**
     * User account is active and can access the system
     */
    ACTIVE,
    
    /**
     * User account is temporarily suspended (can be reactivated)
     */
    SUSPENDED,
    
    /**
     * User account is inactive (user requested deactivation)
     */
    INACTIVE,
    
    /**
     * User account is pending email verification
     */
    PENDING_VERIFICATION,
    
    /**
     * User account has been soft deleted (can be recovered)
     */
    DELETED,
    
    /**
     * User account is locked due to security concerns
     */
    LOCKED
}