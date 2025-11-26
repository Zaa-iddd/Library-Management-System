package com.example.librarymanagementsystem_users.functions;

import android.content.Context;

import com.example.librarymanagementsystem_users.reotrfit.BookApi;
import com.example.librarymanagementsystem_users.reotrfit.RetrofitService;

import java.util.List;

import retrofit2.Callback;

public class DatabaseBookApi implements BookApiFunction {

    private final BookApi api = RetrofitService.getBookApi();

    @Override
    public void getBooks(Callback<List<Book>> callback) {
        api.getAllBooks().enqueue(callback);
    }

    @Override
    public void getFavoriteBooks(Context context, Callback<List<Book>> callback) {
        // If your backend has a favorites endpoint, call it here.
        // Example (if you add endpoint): api.getFavoritesForUser(userId).enqueue(callback);
        // For now fall back to all books (your app can filter locally or you can implement endpoint).
        api.getAllBooks().enqueue(callback);
    }

    @Override
    public void getBorrowedBooks(Callback<List<Book>> callback) {
        // Replace with actual backend call when available.
        api.getAllBooks().enqueue(callback);
    }

    @Override
    public void getHistoryBooks(Context context, Callback<List<Book>> callback) {
        // Replace with actual backend call when available.
        api.getAllBooks().enqueue(callback);
    }

    @Override
    public void getTrendingBooks(Callback<List<Book>> callback) {
        // Replace with actual backend endpoint when available.
        api.getAllBooks().enqueue(callback);
    }
}
