package com.ecommerce.userservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Permission Entity - Defines granular permissions for fine-grained access control
 * 
 * Design Decisions:
 * - Resource-based permissions (e.g., user:read, product:write)
 * - Hierarchical permission structure with categories
 * - Immutable permissions for security (no update timestamps)
 * - String-based permission names for flexibility
 */
@Entity
@Table(name = "permissions", indexes = {
    @Index(name = "idx_permission_name", columnList = "name"),
    @Index(name = "idx_permission_resource", columnList = "resource"),
    @Index(name = "idx_permission_action", columnList = "action")
})
@EntityListeners(AuditingEntityListener.class)
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Permission name is required")
    @Size(max = 100, message = "Permission name cannot exceed 100 characters")
    private String name;

    @Column(nullable = false)
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @Column(nullable = false)
    @NotBlank(message = "Resource is required")
    @Size(max = 50, message = "Resource cannot exceed 50 characters")
    private String resource;

    @Column(nullable = false)
    @NotBlank(message = "Action is required")
    @Size(max = 50, message = "Action cannot exceed 50 characters")
    private String action;

    @Column(nullable = false)
    @Size(max = 50, message = "Category cannot exceed 50 characters")
    private String category;

    @Column(nullable = false)
    private Boolean active = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public Permission() {}

    public Permission(String name, String description, String resource, String action, String category) {
        this.name = name;
        this.description = description;
        this.resource = resource;
        this.action = action;
        this.category = category;
    }

    // Business methods
    public String getFullPermissionName() {
        return resource + ":" + action;
    }

    public boolean matches(String resource, String action) {
        return this.resource.equals(resource) && this.action.equals(action);
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Permission permission = (Permission) obj;
        return name.equals(permission.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}