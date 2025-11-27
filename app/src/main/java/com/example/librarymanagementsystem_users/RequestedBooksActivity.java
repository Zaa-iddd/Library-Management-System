package com.example.librarymanagementsystem_users;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem_users.functions.RequestedBook;
import com.example.librarymanagementsystem_users.models.BorrowHistory;
import com.example.librarymanagementsystem_users.reotrfit.RetrofitService;
import com.example.librarymanagementsystem_users.reotrfit.UserApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestedBooksActivity extends AppCompatActivity {

    private static final String TAG = "RequestedBooksActivity";

    private RecyclerView requestedBooksRecyclerView;
    private RequestedBookAdapter requestedBookAdapter;
    private final List<RequestedBook> requestedBookList = new ArrayList<>();
    private ImageView backButton;
    private UserApi userApi;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_books);

        // Get user ID from intent
        userId = getIntent().getLongExtra("USER_ID", 0L);
        if (userId == 0) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userApi = RetrofitService.getUserApi();

        backButton = findViewById(R.id.backButton);
        requestedBooksRecyclerView = findViewById(R.id.requestedBooksRecyclerView);

        // Setup RecyclerView and Adapter
        requestedBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestedBookAdapter = new RequestedBookAdapter(this, requestedBookList);
        requestedBooksRecyclerView.setAdapter(requestedBookAdapter);

        loadRequestedBooks();

        backButton.setOnClickListener(v -> finish());
    }

    private void loadRequestedBooks() {
        userApi.getBorrowHistory(userId).enqueue(new Callback<List<BorrowHistory>>() {
            @Override
            public void onResponse(@NonNull Call<List<BorrowHistory>> call, @NonNull Response<List<BorrowHistory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BorrowHistory> borrowHistories = response.body();

                    List<RequestedBook> newRequestedList = borrowHistories.stream()
                            .filter(history -> history.getBook() != null && "Pending".equalsIgnoreCase(history.getStatus()))
                            .map(history -> new RequestedBook(
                                    history.getBook().getId(),
                                    history.getBook().getTitle(),
                                    history.getBook().getAuthor(),
                                    history.getStatus(),
                                    history.getBook().getCover_image_url()
                            ))
                            .collect(Collectors.toList());

                    requestedBookList.clear();
                    requestedBookList.addAll(newRequestedList);
                    requestedBookAdapter.notifyDataSetChanged();

                } else {
                    String errorMsg = "Failed to load requested books. Code: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMsg += ", " + response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    Toast.makeText(RequestedBooksActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    Log.e(TAG, errorMsg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BorrowHistory>> call, @NonNull Throwable t) {
                Log.e(TAG, "Error loading requested books", t);
                Toast.makeText(RequestedBooksActivity.this, "Error loading requested books: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
