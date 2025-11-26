package com.example.librarymanagementsystem_users;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem_users.functions.RequestedBook;
import com.example.librarymanagementsystem_users.reotrfit.BookApi;
import com.example.librarymanagementsystem_users.reotrfit.RetrofitService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestedBooksActivity extends AppCompatActivity {

    private RecyclerView requestedBooksRecyclerView;
    private RequestedBookAdapter requestedBookAdapter;
    private List<RequestedBook> requestedBookList;
    private SharedPreferences requestedBooksPrefs;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_books);

        requestedBooksPrefs = getSharedPreferences("requested_books", MODE_PRIVATE);

        backButton = findViewById(R.id.backButton);
        requestedBooksRecyclerView = findViewById(R.id.requestedBooksRecyclerView);
        requestedBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestedBookList = new ArrayList<>();
        Set<String> requests = requestedBooksPrefs.getStringSet("requested_books_set", new HashSet<>());

        for (String request : requests) {
            requestedBookList.add(new RequestedBook(request, "Pending"));
        }

        requestedBookAdapter = new RequestedBookAdapter(this, requestedBookList);
        requestedBooksRecyclerView.setAdapter(requestedBookAdapter);

        backButton.setOnClickListener(v -> finish());
    }

    private void requestBook(long bookId, long userId) {
        BookApi bookApi = RetrofitService.getBookApi();
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
