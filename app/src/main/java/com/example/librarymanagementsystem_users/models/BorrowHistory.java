package com.example.librarymanagementsystem_users.models;

import com.example.librarymanagementsystem_users.functions.Book;
import com.google.gson.annotations.SerializedName;

// This is the POJO for the Android client. No JPA annotations.
public class BorrowHistory {

    @SerializedName("id")
    private Long id;

    // Assumes the JSON from the server will contain a nested 'user' object.
    // You must have a UserResponseDto.java class in this package.
    @SerializedName("user")
    private UserResponseDto user;

    // Assumes the JSON from the server will contain a nested 'book' object.
    @SerializedName("book")
    private Book book;

    // The server will send dates as Strings in the JSON.
    @SerializedName("borrowDate")
    private String borrowDate;

    @SerializedName("returnDate")
    private String returnDate;

    @SerializedName("status")
    private String status;

    // --- GETTERS AND SETTERS for all fields ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserResponseDto getUser() {
        return user;
    }

    public void setUser(UserResponseDto user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
