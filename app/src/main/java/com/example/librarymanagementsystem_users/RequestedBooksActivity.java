package com.example.librarymanagementsystem_users;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem_users.functions.Book;
import com.example.librarymanagementsystem_users.functions.RequestedBook;
import com.example.librarymanagementsystem_users.reotrfit.BookApi;
import com.example.librarymanagementsystem_users.reotrfit.RetrofitService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestedBooksActivity extends AppCompatActivity {

    private RecyclerView requestedBooksRecyclerView;
    private RequestedBookAdapter requestedBookAdapter;
    private List<RequestedBook> requestedBookList;
    private SharedPreferences requestedBooksPrefs;
    private ImageView backButton;
    private BookApi bookApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_books);

        requestedBooksPrefs = getSharedPreferences("requested_books", MODE_PRIVATE);
        bookApi = RetrofitService.getBookApi();

        backButton = findViewById(R.id.backButton);
        requestedBooksRecyclerView = findViewById(R.id.requestedBooksRecyclerView);
        requestedBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadRequestedBooks();

        backButton.setOnClickListener(v -> finish());
    }

    private void loadRequestedBooks() {
        Set<String> requests = requestedBooksPrefs.getStringSet("requested_books_set", new HashSet<>());

        // TODO: This is inefficient. Add an endpoint to the API to get multiple books by ID.
        bookApi.getAllBooks().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> allBooks = response.body();
                    requestedBookList = allBooks.stream()
                            .filter(book -> requests.contains(book.getTitle()))
                            .map(book -> new RequestedBook(
                                    book.getId(),
                                    book.getTitle(),
                                    book.getAuthor(),
                                    "Pending",
                                    book.getCover_image_url()
                            ))
                            .collect(Collectors.toList());

                    requestedBookAdapter = new RequestedBookAdapter(RequestedBooksActivity.this, requestedBookList);
                    requestedBooksRecyclerView.setAdapter(requestedBookAdapter);
                } else {
                    Toast.makeText(RequestedBooksActivity.this, "Failed to load requested books", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Toast.makeText(RequestedBooksActivity.this, "Error loading requested books: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestBook(long bookId, long userId) {
        Call<Void> call = bookApi.requestBook(bookId, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RequestedBooksActivity.this, "Book requested successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RequestedBooksActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RequestedBooksActivity.this, "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
