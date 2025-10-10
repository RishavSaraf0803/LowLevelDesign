package com.ecommerce.userservice.service;

import com.ecommerce.userservice.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Audit Service - Handles security and compliance logging
 * 
 * In a production environment, this would:
 * - Store audit logs in dedicated database/data warehouse
 * - Integrate with SIEM systems
 * - Provide audit trail for compliance (GDPR, SOX, etc.)
 * - Support audit log retention policies
 */
@Service
public class AuditService {
    
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    
    public void logUserCreation(User user) {
        auditLogger.info("USER_CREATED - userId: {}, email: {}", user.getId(), user.getEmail());
    }
    
    public void logUserUpdate(User user) {
        auditLogger.info("USER_UPDATED - userId: {}, email: {}", user.getId(), user.getEmail());
    }
    
    public void logUserDeletion(User user) {
        auditLogger.warn("USER_DELETED - userId: {}, email: {}", user.getId(), user.getEmail());
    }
    
    public void logSuccessfulLogin(User user) {
        auditLogger.info("LOGIN_SUCCESS - userId: {}, email: {}", user.getId(), user.getEmail());
    }
    
    public void logFailedLogin(String emailOrUsername, String reason) {
        auditLogger.warn("LOGIN_FAILED - identifier: {}, reason: {}", emailOrUsername, reason);
    }
    
    public void logEmailVerification(User user) {
        auditLogger.info("EMAIL_VERIFIED - userId: {}, email: {}", user.getId(), user.getEmail());
    }
    
    public void logPasswordChange(User user) {
        auditLogger.info("PASSWORD_CHANGED - userId: {}, email: {}", user.getId(), user.getEmail());
    }
}