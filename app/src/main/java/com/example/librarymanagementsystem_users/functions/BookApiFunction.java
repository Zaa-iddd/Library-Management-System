package com.example.librarymanagementsystem_users.functions;

import android.content.Context;

import java.util.List;

import retrofit2.Callback;

public interface BookApiFunction {

    /**
     * Fetch complete book list asynchronously.
     */
    void getBooks(Callback<List<Book>> callback);

    /**
     * Fetch favorite books for the current user.
     * Context is passed so a local userId from SharedPreferences can be read if needed.
     */
    void getFavoriteBooks(Context context, Callback<List<Book>> callback);

    /**
     * Fetch borrowed books (for the current user or globally depending on backend).
     */
    void getBorrowedBooks(Callback<List<Book>> callback);

    /**
     * Fetch history books for the current user (favorites + borrowed).
     */
    void getHistoryBooks(Context context, Callback<List<Book>> callback);

    /**
     * Fetch trending books.
     */
    void getTrendingBooks(Callback<List<Book>> callback);
}
