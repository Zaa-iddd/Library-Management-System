package com.example.librarymanagementsystem_users.reotrfit;

import com.example.librarymanagementsystem_users.functions.Book;
import com.example.librarymanagementsystem_users.models.BorrowHistory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookApi {

    // --------------------- BOOK ENDPOINTS ---------------------

    /** GET /books */
    @GET("books")
    Call<List<Book>> getAllBooks();

    /** GET /books/{id} */
    @GET("books/{id}")
    Call<Book> getBookById(@Path("id") Long id);

    /** GET /books/search?query=... */
    @GET("books/search")
    Call<List<Book>> searchBooks(@Query("query") String query);

    /** GET /books/genre?genre=... */
    @GET("books/genre")
    Call<List<Book>> getBooksByGenre(@Query("genre") String genre);


    // --------------------- BORROW REQUEST ENDPOINTS ---------------------

    /**
     * POST /borrow/{userId}/{bookId}
     * (Matches your backend controller)
     */
    @POST("borrow/{userId}/{bookId}")
    Call<BorrowHistory> borrowBook(
            @Path("userId") long userId,
            @Path("bookId") long bookId
    );

    // --------------------- BORROW HISTORY ---------------------

    /**
     * GET /borrow
     */
    @GET("borrow")
    Call<List<BorrowHistory>> getAllBorrowHistory();

    /**
     * GET /borrow/user/{userId}
     * (This is the one your user dashboard needs)
     */
    @GET("borrow/user/{userId}")
    Call<List<BorrowHistory>> getBorrowHistoryByUser(
            @Path("userId") long userId
    );

    /**
     * GET /borrow/book/{bookId}
     */
    @GET("borrow/book/{bookId}")
    Call<List<BorrowHistory>> getBorrowHistoryByBook(
            @Path("bookId") long bookId
    );

    /**
     * GET /borrow/{historyId}
     */
    @GET("borrow/{historyId}")
    Call<BorrowHistory> getBorrowHistoryById(
            @Path("historyId") long historyId
    );
}
