package com.example.librarymanagementsystem_users.models;

import com.example.librarymanagementsystem_users.functions.Book;
import com.google.gson.annotations.SerializedName;


public class BorrowHistory {

    @SerializedName("id")
    private Long id;


    @SerializedName("user")
    private UserResponseDto user;


    @SerializedName("book")
    private Book book;

    @SerializedName("borrowDate")
    private String borrowDate;

    @SerializedName("returnDate")
    private String returnDate;

    @SerializedName("status")
    private String status;

    // setters and getters

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
