package com.example.librarymanagementsystem_users.reotrfit;

import com.example.librarymanagementsystem_users.functions.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookApi {

    /**
     * Fetches a list of all books from the server.
     * Corresponds to the endpoint: GET /books
     */
    @GET("books")
    Call<List<Book>> getAllBooks();

    /**
     * Fetches a single book by its unique ID.
     * Corresponds to the endpoint: GET /books/{id}
     * @param id The ID of the book to retrieve.
     */
    @GET("books/{id}")
    Call<Book> getBookById(@Path("id") Long id);

    /**
     * Searches for books based on a query string (e.g., title, author).
     * Corresponds to the endpoint: GET /books/search?query=...
     * @param query The search term.
     */
    @GET("books/search")
    Call<List<Book>> searchBooks(@Query("query") String query);

    /**
     * Fetches books belonging to a specific genre.
     * Corresponds to the endpoint: GET /books/genre?genre=...
     * @param genre The genre to filter by.
     */
    @GET("books/genre")
    Call<List<Book>> getBooksByGenre(@Query("genre") String genre);

    /**
     * Sends a request to borrow a book.
     * Corresponds to the endpoint: POST /borrow/{bookId}/{userId}
     * @param bookId The ID of the book to be borrowed.
     * @param userId The ID of the user borrowing the book.
     */
    @POST("borrow/{bookId}/{userId}")
    Call<Void> requestBook(@Path("bookId") long bookId, @Path("userId") long userId);
}
