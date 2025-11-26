package com.example.librarymanagementsystem_users;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem_users.functions.RequestedBook;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
}
