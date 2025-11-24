package com.example.librarymanagementsystem_users.models;

public class LoginRequestDto {
    private String username;
    private String password;

    public LoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters are needed by Gson for serialization
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
