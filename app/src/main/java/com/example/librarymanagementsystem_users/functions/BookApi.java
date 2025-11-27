package com.example.librarymanagementsystem_users.functions;

import android.content.Context;

import java.util.List;

public interface BookApi {

    List<Book> getBooks();

    List<Book> getFavoriteBooks(Context context);

    List<Book> getBorrowedBooks();

    List<Book> getHistoryBooks(Context context);

    List<Book> getTrendingBooks();
}
