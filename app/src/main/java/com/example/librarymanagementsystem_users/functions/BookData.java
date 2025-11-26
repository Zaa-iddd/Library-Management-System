package com.example.librarymanagementsystem_users.functions;

import android.content.Context;

import java.util.List;

public class BookData {

    private static BookApi bookApi;

    // Method to initialize the API. You can switch between StaticBookApi and a future
    // DatabaseBookApi here.
    public static void initialize(Context context) {
        // Right now, we are using the static API. When you have a database, you can
        // swap this with your database implementation.
        bookApi = new StaticBookApi();
    }

    /**
     * Returns the entire library catalog.
     */
    public static List<Book> getBooks() {
        return bookApi.getBooks();
    }

    /**
     * Gets the list of books the current user has marked as favorite.
     */
    public static List<Book> getFavoriteBooks(Context context) {
        return bookApi.getFavoriteBooks(context);
    }

    /**
     * Gets the list of books the current user has borrowed.
     */
    public static List<Book> getBorrowedBooks() {
        return bookApi.getBorrowedBooks();
    }

    /**
     * Gets the user's book history, which includes favorite and borrowed books.
     */
    public static List<Book> getHistoryBooks(Context context) {
        return bookApi.getHistoryBooks(context);
    }

    /**
     * Gets a list of books that are currently trending.
     */
    public static List<Book> getTrendingBooks() {
        return bookApi.getTrendingBooks();
    }
}