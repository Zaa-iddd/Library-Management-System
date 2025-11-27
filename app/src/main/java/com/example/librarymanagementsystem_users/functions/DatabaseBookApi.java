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
        api.getBooksByGenre("Science Fiction").enqueue(callback);
    }

    @Override
    public void getFavoriteBooks(Context context, Callback<List<Book>> callback) {

        api.getBooksByGenre("Science Fiction").enqueue(callback);
    }

    @Override
    public void getBorrowedBooks(Callback<List<Book>> callback) {

        api.getBooksByGenre("Science Fiction").enqueue(callback);
    }

    @Override
    public void getHistoryBooks(Context context, Callback<List<Book>> callback) {

        api.getBooksByGenre("Science Fiction").enqueue(callback);
    }

    @Override
    public void getTrendingBooks(Callback<List<Book>> callback) {

        api.getBooksByGenre("Science Fiction").enqueue(callback);
    }
}
