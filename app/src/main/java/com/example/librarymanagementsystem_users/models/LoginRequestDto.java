package com.example.librarymanagementsystem_users.models;

public class LoginRequestDto {
    private String usernameOrEmail;
    private String password;

    // Empty constructor (needed by Retrofit/Gson)
    public LoginRequestDto() {}

    // Constructor with fields
    public LoginRequestDto(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
