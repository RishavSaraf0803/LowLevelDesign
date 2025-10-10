package com.ecommerce.userservice.domain;

/**
 * Role Type Enumeration
 * 
 * Distinguishes between system-defined roles and user-defined roles
 * for proper authorization and role management.
 */
public enum RoleType {
    /**
     * System-defined roles that cannot be modified by users
     * Examples: ADMIN, USER, MODERATOR
     */
    SYSTEM,
    
    /**
     * User-defined roles created for specific organizational needs
     * Examples: SALES_MANAGER, CONTENT_EDITOR, REGIONAL_ADMIN
     */
    USER_DEFINED
}