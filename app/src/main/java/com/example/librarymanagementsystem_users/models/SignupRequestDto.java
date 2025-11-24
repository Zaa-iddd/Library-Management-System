package com.example.librarymanagementsystem_users.models;

public class SignupRequestDto {
    private String username;
    private String email;
    private String password;

    public SignupRequestDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters are needed for Gson serialization
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
