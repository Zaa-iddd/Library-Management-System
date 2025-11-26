package com.example.librarymanagementsystem_users.functions;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.librarymanagementsystem_users.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookData {

    // This represents the entire library catalog. In a real app, this would come from a database.
    private static final List<Book> libraryCatalog = new ArrayList<>(Arrays.asList(
            new Book("The Lord of the Rings", "J.R.R. Tolkien", R.drawable.sample_book, "A classic high-fantasy novel by J.R.R. Tolkien.", "Action", true, false, true),
            new Book("Pride and Prejudice", "Jane Austen", R.drawable.sample_book, "A romantic novel of manners written by Jane Austen in 1813.", "Romance", true, false, true),
            new Book("The Hitchhiker's Guide to the Galaxy", "Douglas Adams", R.drawable.sample_book, "A comedy science fiction series created by Douglas Adams.", "Comedy", true, false, false),
            new Book("To Kill a Mockingbird", "Harper Lee", R.drawable.sample_book, "A novel by Harper Lee published in 1960.", "Thriller", false, false, true),
            new Book("The Catcher in the Rye", "J.D. Salinger", R.drawable.sample_book, "A novel by J. D. Salinger, partially published in serial form in 1945–1946 and as a novel in 1951.", "Horror", false, false, false),
            new Book("The Great Gatsby", "F. Scott Fitzgerald", R.drawable.sample_book, "A 1925 novel by American writer F. Scott Fitzgerald.", "Romance", false, false, true),
            new Book("Dune", "Frank Herbert", R.drawable.sample_book, "A landmark science fiction novel.", "Action", false, false, true)
    ));

    /**
     * Returns the entire library catalog.
     */
    public static List<Book> getBooks() {
        return new ArrayList<>(libraryCatalog);
    }

    /**
     * Gets the list of books the current user has marked as favorite.
     */
    public static List<Book> getFavoriteBooks(Context context) {
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

    /**
     * Gets the list of books the current user has borrowed.
     */
    public static List<Book> getBorrowedBooks() {
        return libraryCatalog.stream()
                .filter(book -> !book.isAvailable()) // !isAvailable means borrowed
                .collect(Collectors.toList());
    }

    /**
     * Gets the user's book history, which includes favorite and borrowed books.
     */
    public static List<Book> getHistoryBooks(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE);
        Set<String> userFavoriteBookTitles = sharedPreferences.getStringSet("favorite_books", new HashSet<>());

        return libraryCatalog.stream()
                .filter(book -> userFavoriteBookTitles.contains(book.getTitle()) || !book.isAvailable())
                .collect(Collectors.toList());
    }

    /**
     * Gets a list of books that are currently trending.
     */
    public static List<Book> getTrendingBooks() {
        return libraryCatalog.stream().filter(Book::isTrending).collect(Collectors.toList());
    }
}
