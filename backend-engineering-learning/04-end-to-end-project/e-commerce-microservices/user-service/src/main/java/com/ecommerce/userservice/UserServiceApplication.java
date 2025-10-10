package com.ecommerce.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * User Service - Handles user authentication, profile management, and user preferences
 * 
 * This microservice is responsible for:
 * - User registration and authentication
 * - Profile management and preferences
 * - JWT token generation and validation
 * - Role-based access control
 * - User session management
 * 
 * Architecture Decisions:
 * - Uses PostgreSQL for primary data storage (ACID compliance for user data)
 * - Redis for caching user sessions and frequently accessed data
 * - JWT for stateless authentication across microservices
 * - Spring Security for comprehensive security implementation
 * 
 * @author Backend Engineering Learning
 * @version 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}