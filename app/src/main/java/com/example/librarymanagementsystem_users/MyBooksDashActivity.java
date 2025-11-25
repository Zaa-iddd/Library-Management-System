package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem_users.functions.Book;
import com.example.librarymanagementsystem_users.functions.BookData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MyBooksDashActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnHistory, btnBorrowed, btnReturned;
    View historyContent, borrowedContent, returnedContent;
    ImageView backButton;
    RecyclerView favoriteBooksRecyclerView;
    FavoriteBookAdapter favoriteBookAdapter;
    List<Book> favoriteBookList;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybooks_dash);

        sharedPreferences = getSharedPreferences("favorites", MODE_PRIVATE);

        btnHistory = findViewById(R.id.btnHistory);
        btnBorrowed = findViewById(R.id.btnBorrowed);
        btnReturned = findViewById(R.id.btnReturned);
        backButton = findViewById(R.id.backButton);

        historyContent = findViewById(R.id.history_content);
        borrowedContent = findViewById(R.id.borrowed_content);
        returnedContent = findViewById(R.id.returned_content);
        favoriteBooksRecyclerView = findViewById(R.id.favoriteBooksRecyclerView);

        btnHistory.setOnClickListener(this);
        btnBorrowed.setOnClickListener(this);
        btnReturned.setOnClickListener(this);
        backButton.setOnClickListener(this);

        favoriteBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadFavoriteBooks();
        
        showContent(borrowedContent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnHistory) {
            showContent(historyContent);
        } else if (id == R.id.btnBorrowed) {
            showContent(borrowedContent);
        } else if (id == R.id.btnReturned) {
            showContent(returnedContent);
        } else if (id == R.id.backButton) {
            startActivity(new Intent(MyBooksDashActivity.this, HomeActivity.class));
            finish();
        }
    }

    private void showContent(View contentToShow) {
        historyContent.setVisibility(View.GONE);
        borrowedContent.setVisibility(View.GONE);
        returnedContent.setVisibility(View.GONE);
        contentToShow.setVisibility(View.VISIBLE);
    }

    private void loadFavoriteBooks() {
        Set<String> favoriteBookTitles = sharedPreferences.getStringSet("favorite_books", new HashSet<>());
        List<Book> allBooks = BookData.getBooks();

        favoriteBookList = allBooks.stream()
                .filter(book -> favoriteBookTitles.contains(book.getTitle()))
                .collect(Collectors.toList());

        favoriteBookAdapter = new FavoriteBookAdapter(this, favoriteBookList);
        favoriteBooksRecyclerView.setAdapter(favoriteBookAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavoriteBooks();
    }
}
