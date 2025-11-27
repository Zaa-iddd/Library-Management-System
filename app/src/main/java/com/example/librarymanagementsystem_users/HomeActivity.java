package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem_users.functions.Book;
import com.example.librarymanagementsystem_users.functions.BookData;
import com.example.librarymanagementsystem_users.models.UserResponseDto;
import com.example.librarymanagementsystem_users.reotrfit.RetrofitService;
import com.example.librarymanagementsystem_users.reotrfit.UserApi;
import com.google.android.material.card.MaterialCardView;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView trendingBooksRecyclerView;
    private TrendingBookAdapter trendingBookAdapter;
    private List<Book> trendingBookList;

    private RecyclerView favoriteBooksRecyclerView;
    private FavoriteBookAdapter favoriteBookAdapter;
    private List<Book> favoriteBookList;

    private SharedPreferences sharedPreferences;
    private long userId; // logged-in user ID
    private UserApi userApi;
    private TextView usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        sharedPreferences = getSharedPreferences("favorites", MODE_PRIVATE);

        // Initialize BookData
        BookData.initialize(this);

        // Get the user ID passed from LoginActivity first, or from SharedPreferences
        userId = getIntent().getLongExtra("USER_ID", 0);
        if (userId == 0) {
            userId = sharedPreferences.getLong("USER_ID", 0);
        } else {
            // Save the userId to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("USER_ID", userId);
            editor.apply();
        }

        usernameTextView = findViewById(R.id.textView6);
        userApi = RetrofitService.getUserApi();
        loadUserProfile();

        // Show main_dash on "View All" click for trending books
        TextView viewBooks = findViewById(R.id.viewBooks);
        viewBooks.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainDashActivity.class);
            intent.putExtra("SHOW_ALL_TRENDING", true);
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
        trendingBookList = BookData.getTrendingBooks();
        trendingBookAdapter = new TrendingBookAdapter(this, trendingBookList);
        trendingBooksRecyclerView.setAdapter(trendingBookAdapter);

        // Favorite Books RecyclerView
        favoriteBooksRecyclerView = findViewById(R.id.favoriteBooksRecyclerView);
        favoriteBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadFavoriteBooks();

        // Search on home screen
        SearchView homeSearchView = findViewById(R.id.homeSearchView);
        Button homeSearchButton = findViewById(R.id.homeSearchButton);
        homeSearchButton.setVisibility(View.VISIBLE);
        homeSearchButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainDashActivity.class);
            intent.putExtra("SEARCH_QUERY", homeSearchView.getQuery().toString());
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        homeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(HomeActivity.this, MainDashActivity.class);
                intent.putExtra("SEARCH_QUERY", query);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Bottom Navigation
        Button btHome = findViewById(R.id.btHome);
        btHome.setOnClickListener(v -> {
            // Already on home, do nothing or refresh
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

        // Profile card
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

    private void loadUserProfile() {
        userApi.getUserById(userId).enqueue(new Callback<UserResponseDto>() {
            @Override
            public void onResponse(@NonNull Call<UserResponseDto> call, @NonNull Response<UserResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponseDto user = response.body();
                    usernameTextView.setText("Hello " + user.getUsername() + "!");
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponseDto> call, @NonNull Throwable t) {
                // Handle failure
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
