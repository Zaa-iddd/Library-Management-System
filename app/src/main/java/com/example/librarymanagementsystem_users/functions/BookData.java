package com.example.librarymanagementsystem_users.functions;

import com.example.librarymanagementsystem_users.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class BookData {

    public static List<Book> getBooks() {
        Book[] books = {
                new Book("The Lord of the Rings", "J.R.R. Tolkien", R.drawable.sample_book, "A classic high-fantasy novel by J.R.R. Tolkien.", "Action", true, true),
                new Book("Pride and Prejudice", "Jane Austen", R.drawable.sample_book, "A romantic novel of manners written by Jane Austen in 1813.", "Romance", true, false),
                new Book("The Hitchhiker's Guide to the Galaxy", "Douglas Adams", R.drawable.sample_book, "A comedy science fiction series created by Douglas Adams.", "Comedy", true, true),
                new Book("The Lord of the Rings", "J.R.R. Tolkien", R.drawable.sample_book, "A classic high-fantasy novel by J.R.R. Tolkien.", "Action", true, true), // Duplicate
                new Book("To Kill a Mockingbird", "Harper Lee", R.drawable.sample_book, "A novel by Harper Lee published in 1960.", "Thriller", false, false),
                new Book("The Catcher in the Rye", "J.D. Salinger", R.drawable.sample_book, "A novel by J. D. Salinger, partially published in serial form in 1945–1946 and as a novel in 1951.", "Horror", false, true),
                new Book("The Great Gatsby", "F. Scott Fitzgerald", R.drawable.sample_book, "A 1925 novel by American writer F. Scott Fitzgerald.", "Romance", false, false),
                new Book("Pride and Prejudice", "Jane Austen", R.drawable.sample_book, "A romantic novel of manners written by Jane Austen in 1813.", "Romance", true, false), // Duplicate
                new Book("Dune", "Frank Herbert", R.drawable.sample_book, "A landmark science fiction novel.", "Action", false, true)
        };

        // Use a LinkedHashSet to maintain insertion order while ensuring uniqueness
        LinkedHashSet<Book> uniqueBooks = new LinkedHashSet<>(Arrays.asList(books));
        return new ArrayList<>(uniqueBooks);
    }

    public List<Book> getTrendingBooks() {
        return getBooks().stream().filter(Book::isTrending).collect(Collectors.toList());
    }

    public List<Book> getFavoriteBooks() {
        return getBooks().stream().filter(Book::isFavorite).collect(Collectors.toList());
    }
}
