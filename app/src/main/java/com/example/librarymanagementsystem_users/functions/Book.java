package com.example.librarymanagementsystem_users.functions;

public class Book extends LibraryItem {
    private int coverResourceId;

    public Book(String title, String author, int coverResourceId) {
        super(title, author);
        this.coverResourceId = coverResourceId;
    }

    public int getCoverResourceId() {
        return coverResourceId;
    }

    public void setCoverResourceId(int coverResourceId) {
        this.coverResourceId = coverResourceId;
    }

    @Override
    public void displayInfo() {
        // Not implemented
    }
}
