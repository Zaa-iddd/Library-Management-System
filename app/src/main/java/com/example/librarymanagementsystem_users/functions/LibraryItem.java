package com.example.librarymanagementsystem_users.functions;

public abstract class LibraryItem {
    private String title;
    private String author;

    public LibraryItem(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public void borrow() {
        System.out.println("Item has been borrowed.");
    }
    public abstract void displayInfo();//obj 2 use abstract method to display info

}
