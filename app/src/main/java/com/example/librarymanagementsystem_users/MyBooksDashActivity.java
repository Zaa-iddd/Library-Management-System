package com.example.librarymanagementsystem_users;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem_users.functions.Book;
import com.example.librarymanagementsystem_users.reotrfit.BookApi;
import com.example.librarymanagementsystem_users.reotrfit.RetrofitService;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBooksDashActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnFavorite, selectedButton, btScan, btnSearch, btHome;
    private ImageView backButton;
    private RecyclerView favoriteBooksRecyclerView;
    private FavoriteBookAdapter favoriteBookAdapter;
    private final List<Book> favoriteBookList = new ArrayList<>();
    private FrameLayout favoriteContent;
    private SearchView searchView;

    private BookApi bookApi;
    private long userId;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybooks_dash);

        bookApi = RetrofitService.getBookApi();
        userId = getIntent().getLongExtra("USER_ID", 0L);

        // Initialize buttons
        btnFavorite = findViewById(R.id.btnFavorite);
        backButton = findViewById(R.id.backButton);
        btScan = findViewById(R.id.btScan);
        btnSearch = findViewById(R.id.btnSearch);
        btHome = findViewById(R.id.btHome);

        // Initialize search view
        searchView = findViewById(R.id.searchView);

        // Initialize content views from XML
        favoriteBooksRecyclerView = findViewById(R.id.favoriteBooksRecyclerView);
        favoriteContent = findViewById(R.id.favorite_content);

        // Set click listeners
        btnFavorite.setOnClickListener(this);
        backButton.setOnClickListener(this);
        btScan.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btHome.setOnClickListener(this);

        // Setup RecyclerViews and Adapters
        favoriteBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteBookAdapter = new FavoriteBookAdapter(this, favoriteBookList, userId);
        favoriteBooksRecyclerView.setAdapter(favoriteBookAdapter);

        // Set initial state
        selectedButton = btnFavorite;
        btnFavorite.setSelected(true);
        showContent(favoriteContent);
        loadAndFilterFavoriteBooks();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnFavorite) {
            if (selectedButton != null) {
                selectedButton.setSelected(false);
            }
            selectedButton = (Button) v;
            selectedButton.setSelected(true);
            showContent(favoriteContent);
            loadAndFilterFavoriteBooks();
        } else if (id == R.id.backButton) {
            finish();
        } else if (id == R.id.btScan) {
            // QR Code scan logic
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setPrompt("Scan a QR code");
            integrator.initiateScan();
        } else if (id == R.id.btnSearch) {
            loadAndFilterFavoriteBooks();
        } else if (id == R.id.btHome) {
            Intent intent = new Intent(MyBooksDashActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    private void showContent(View contentToShow) {
        favoriteContent.setVisibility(View.GONE);
        if (contentToShow != null) {
            contentToShow.setVisibility(View.VISIBLE);
        }
    }

    private void loadAndFilterFavoriteBooks() {
        SharedPreferences sharedPreferences = getSharedPreferences("favorites", MODE_PRIVATE);
        Set<String> favoriteBookIds = sharedPreferences.getStringSet("favorite_books", new HashSet<>());

        bookApi.getAllBooks().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(@NonNull Call<List<Book>> call, @NonNull Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<Book> allBooks = response.body();

                    List<Book> favorites = allBooks.stream()
                            .filter(book -> favoriteBookIds.contains(String.valueOf(book.getId())))
                            .collect(Collectors.toList());

                    String query = searchView.getQuery().toString();
                    List<Book> filteredList = favorites.stream()
                            .filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase()))
                            .collect(Collectors.toList());

                    favoriteBookList.clear();
                    favoriteBookList.addAll(filteredList);
                    favoriteBookAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Book>> call, @NonNull Throwable t) {
                Toast.makeText(MyBooksDashActivity.this, "Error loading favorites: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                try {
                    long bookId = Long.parseLong(result.getContents());
                    Intent intent = new Intent(MyBooksDashActivity.this, ViewBookActivity.class);
                    intent.putExtra("BOOK_ID", bookId);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid QR code", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
