package com.example.librarymanagementsystem_users.functions;

import android.content.Context;

import java.util.List;

import retrofit2.Callback;

public interface BookApiFunction {


    void getBooks(Callback<List<Book>> callback);


    void getFavoriteBooks(Context context, Callback<List<Book>> callback);

    void getBorrowedBooks(Callback<List<Book>> callback);


    void getHistoryBooks(Context context, Callback<List<Book>> callback);


    void getTrendingBooks(Callback<List<Book>> callback);
}
