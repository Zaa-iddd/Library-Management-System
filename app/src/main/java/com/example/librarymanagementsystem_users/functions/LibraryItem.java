package com.example.librarymanagementsystem_users.functions;

public abstract class LibraryItem {
    public void borrow() {
        System.out.println("Item has been borrowed.");
    }
    public abstract void displayInfo();//obj 2 use abstract method to display info

}
