package com.example.librarymanagementsystem_users.models;

// This class name now matches the DTO expected by the Spring Boot server.
public class UserRequestDto {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;

    public UserRequestDto(String username, String email, String password, String firstName, String lastName, String address, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Getters are required for Gson to serialize the object.
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
