package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem_users.functions.Book;
import com.example.librarymanagementsystem_users.functions.BookData;
import com.google.android.material.card.MaterialCardView;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView trendingBooksRecyclerView;
    private TrendingBookAdapter trendingBookAdapter;
    private List<Book> trendingBookList;

    private RecyclerView favoriteBooksRecyclerView;
    private FavoriteBookAdapter favoriteBookAdapter;
    private List<Book> favoriteBookList;

    private SharedPreferences sharedPreferences;
    private long userId; // logged-in user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        // Get the user ID passed from LoginActivity first
        userId = getIntent().getLongExtra("USER_ID", 0);
        sharedPreferences = getSharedPreferences("favorites", MODE_PRIVATE);

        // Navigate to MainDashActivity
        TextView viewBooks = findViewById(R.id.viewBooks);
        viewBooks.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainDashActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        // Navigate to MyBooksDashActivity
        TextView viewMyBook = findViewById(R.id.viewMyBook);
        viewMyBook.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MyBooksDashActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        // Trending Books RecyclerView
        trendingBooksRecyclerView = findViewById(R.id.trendingBooksRecyclerView);
        trendingBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        trendingBookList = new BookData().getTrendingBooks();
        trendingBookAdapter = new TrendingBookAdapter(this, trendingBookList);
        trendingBooksRecyclerView.setAdapter(trendingBookAdapter);

        // Favorite Books RecyclerView
        favoriteBooksRecyclerView = findViewById(R.id.favoriteBooksRecyclerView);
        favoriteBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadFavoriteBooks();

        // Search
        SearchView searchView = findViewById(R.id.searchView);
        Button searchButton = findViewById(R.id.button);
        searchButton.setOnClickListener(v -> {
            String query = searchView.getQuery().toString();
            Intent intent = new Intent(HomeActivity.this, MainDashActivity.class);
            intent.putExtra("SEARCH_QUERY", query);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        // Bottom Navigation
        Button btHome = findViewById(R.id.btHome);
        btHome.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, HomeActivity.class).putExtra("USER_ID", userId));
            finish();
        });

        Button btScan = findViewById(R.id.btScan);
        btScan.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CaptureActivity.class);
            startActivity(intent);
        });

        Button btMyBooks = findViewById(R.id.btMyBooks);
        btMyBooks.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MyBooksDashActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        // Corrected: Changed type to MaterialCardView to match the layout XML
        MaterialCardView profileCard = findViewById(R.id.btEditProfile);
        profileCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavoriteBooks();
    }

    private void loadFavoriteBooks() {
        Set<String> favoriteBookTitles = sharedPreferences.getStringSet("favorite_books", new HashSet<>());
        List<Book> allBooks = BookData.getBooks();

        favoriteBookList = allBooks.stream()
                .filter(book -> favoriteBookTitles.contains(book.getTitle()))
                .collect(Collectors.toList());

        List<Book> limitedFavoriteBooks;
        if (favoriteBookList.size() > 2) {
            limitedFavoriteBooks = new ArrayList<>(favoriteBookList.subList(0, 2));
        } else {
            limitedFavoriteBooks = new ArrayList<>(favoriteBookList);
        }

        favoriteBookAdapter = new FavoriteBookAdapter(this, limitedFavoriteBooks);
        favoriteBooksRecyclerView.setAdapter(favoriteBookAdapter);
    }
}
