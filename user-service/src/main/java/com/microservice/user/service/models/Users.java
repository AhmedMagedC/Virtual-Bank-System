package com.microservice.user.service.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Users {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true,  columnDefinition = "VARCHAR(50)")
    private String username;

    @Email
    @Column(nullable = false, unique = true,  columnDefinition = "VARCHAR(100)")
    private String email;

    @Column(nullable = false,  columnDefinition = "VARCHAR(255)")
    private String passwordHash;

    @Column(nullable = false,  columnDefinition = "VARCHAR(50)")
    private String firstName;

    @Column(nullable = false,  columnDefinition = "VARCHAR(50)")
    private String lastName;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Users(){}

    public Users(String username, String email, String passwordHash, String firstName,
                 String lastName) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
     }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}
