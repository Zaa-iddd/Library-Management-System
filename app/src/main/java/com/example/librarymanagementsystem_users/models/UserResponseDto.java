package com.example.librarymanagementsystem_users.models;

// IMPORTANT: Update these fields to match the UserResponseDto in your Spring Boot backend.
public class UserResponseDto {
    private long id;
    private String username;
    private String email;

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
