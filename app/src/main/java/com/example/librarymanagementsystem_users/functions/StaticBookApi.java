package com.example.librarymanagementsystem_users.functions;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.librarymanagementsystem_users.reotrfit.BookApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StaticBookApi implements BookApi {

    private static final List<Book> libraryCatalog = new ArrayList<>(Arrays.asList(
            new Book(1L, "J.R.R. Tolkien", 5, "", "Action", "English", 1178, "1954-07-29", "Allen & Unwin", "Available", "A classic high-fantasy novel by J.R.R. Tolkien.", "The Lord of the Rings", 5),
            new Book(2L, "Jane Austen", 3, "", "Romance", "English", 279, "1813-01-28", "T. Egerton, Whitehall", "Available", "A romantic novel of manners written by Jane Austen in 1813.", "Pride and Prejudice", 3),
            new Book(3L, "Douglas Adams", 0, "", "Comedy", "English", 224, "1979-10-12", "Pan Books", "Unavailable", "A comedy science fiction series created by Douglas Adams.", "The Hitchhiker's Guide to the Galaxy", 2),
            new Book(4L, "Harper Lee", 10, "", "Thriller", "English", 324, "1960-07-11", "J. B. Lippincott & Co.", "Available", "A novel by Harper Lee published in 1960.", "To Kill a Mockingbird", 10),
            new Book(5L, "J.D. Salinger", 0, "", "Horror", "English", 224, "1951-07-16", "Little, Brown and Company", "Unavailable", "A novel by J. D. Salinger, partially published in serial form in 1945–1946 and as a novel in 1951.", "The Catcher in the Rye", 1),
            new Book(6L, "F. Scott Fitzgerald", 7, "", "Romance", "English", 180, "1925-04-10", "Charles Scribner's Sons", "Available", "A 1925 novel by American writer F. Scott Fitzgerald.", "The Great Gatsby", 7),
            new Book(7L, "Frank Herbert", 4, "", "Action", "English", 412, "1965-08-01", "Chilton Books", "Available", "A landmark science fiction novel.", "Dune", 4)
    ));

    @Override
    public List<Book> getBooks() {
        return new ArrayList<>(libraryCatalog);
    }

    @Override
    public List<Book> getFavoriteBooks(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE);
        Set<String> userFavoriteBookTitles = sharedPreferences.getStringSet("favorite_books", null);

        if (userFavoriteBookTitles == null) {
            userFavoriteBookTitles = new HashSet<>(Arrays.asList("The Lord of the Rings", "The Great Gatsby"));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet("favorite_books", userFavoriteBookTitles);
            editor.apply();
        }

        Set<String> finalUserFavoriteBookTitles = userFavoriteBookTitles;
        return libraryCatalog.stream()
                .filter(book -> finalUserFavoriteBookTitles.contains(book.getTitle()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getBorrowedBooks() {
        return libraryCatalog.stream()
                .filter(book -> "Unavailable".equalsIgnoreCase(book.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getHistoryBooks(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE);
        Set<String> userFavoriteBookTitles = sharedPreferences.getStringSet("favorite_books", new HashSet<>());

        return libraryCatalog.stream()
                .filter(book -> userFavoriteBookTitles.contains(book.getTitle()) || "Unavailable".equalsIgnoreCase(book.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getTrendingBooks() {
        return libraryCatalog.stream().limit(3).collect(Collectors.toList());
    }
}
