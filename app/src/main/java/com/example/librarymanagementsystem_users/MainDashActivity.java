package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem_users.functions.Book;
import com.example.librarymanagementsystem_users.reotrfit.BookApi;
import com.example.librarymanagementsystem_users.reotrfit.RetrofitService;
import com.google.android.material.card.MaterialCardView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainDashActivity extends AppCompatActivity {

    // -- Views and Adapters --
    Button btnAll, btnAction, btnRomance, btnComedy, btnHorror, btnThriller, btScan, btHome, btMyBooks, btnSearch;
    MaterialCardView profileButton;
    ImageView backButton;
    TextView bookGenre;
    SearchView searchView;
    RecyclerView booksRecyclerView;
    BookAdapter bookAdapter;
    List<Book> bookList = new ArrayList<>();
    List<Book> allBooks = new ArrayList<>(); // To store the master list of books
    BookApi bookApi;

    // -- State --
    private String currentGenre = "All";
    private long userId; // logged-in user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dash);

        userId = getIntent().getLongExtra("USER_ID", 0);
        bookApi = RetrofitService.getBookApi();

        if (userId == 0) {
            Toast.makeText(this, "Guest mode", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "User ID: " + userId, Toast.LENGTH_SHORT).show();
        }

        // -- View Binding --
        btnAll = findViewById(R.id.btnAll);
        btnAction = findViewById(R.id.btnAction);
        btnRomance = findViewById(R.id.btnRomance);
        btnComedy = findViewById(R.id.btnComedy);
        btnHorror = findViewById(R.id.btnHorror);
        btnThriller = findViewById(R.id.btnThriller);
        profileButton = findViewById(R.id.btEditProfile);
        btScan = findViewById(R.id.btScan);
        btHome = findViewById(R.id.btHome);
        btMyBooks = findViewById(R.id.btMyBooks);
        bookGenre = findViewById(R.id.bookGenre);
        searchView = findViewById(R.id.searchView);
        btnSearch = findViewById(R.id.searchButton);
        booksRecyclerView = findViewById(R.id.books_container);
        backButton = findViewById(R.id.backButton);

        setupRecyclerView();

        // -- UI & Event Listener Setup --

        backButton.setOnClickListener(v -> finish());

        btnSearch.setOnClickListener(v -> {
            applyFilters(currentGenre, searchView.getQuery().toString());
            searchView.clearFocus();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                applyFilters(currentGenre, query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Live search can be implemented here if desired
                return false;
            }
        });

        View.OnClickListener genreClickListener = v -> {
            resetButtons();
            v.setSelected(true);
            Button selectedButton = (Button) v;
            currentGenre = selectedButton.getText().toString();

            if (currentGenre.equals("All")) {
                bookGenre.setText("All Books");
            } else {
                bookGenre.setText(currentGenre + " Books");
            }

            applyFilters(currentGenre, searchView.getQuery().toString());
        };

        btnAll.setOnClickListener(genreClickListener);
        btnAction.setOnClickListener(genreClickListener);
        btnRomance.setOnClickListener(genreClickListener);
        btnComedy.setOnClickListener(genreClickListener);
        btnHorror.setOnClickListener(genreClickListener);
        btnThriller.setOnClickListener(genreClickListener);

        // -- Initial Data Load --
        loadAllBooks();

        // -- Navigation --
        btHome.setOnClickListener(v -> {
            Intent homeIntent = new Intent(MainDashActivity.this, HomeActivity.class);
            homeIntent.putExtra("USER_ID", userId);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(homeIntent);
        });

        btMyBooks.setOnClickListener(v -> {
            Intent myBooksIntent = new Intent(MainDashActivity.this, MyBooksDashActivity.class);
            myBooksIntent.putExtra("USER_ID", userId);
            startActivity(myBooksIntent);
        });

        btScan.setOnClickListener(v -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(MainDashActivity.this);
            intentIntegrator.setPrompt("Scan a barcode or QR Code");
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.initiateScan();
        });

        profileButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(MainDashActivity.this, ProfileActivity.class);
            profileIntent.putExtra("USER_ID", userId);
            startActivity(profileIntent);
        });
    }

    private void setupRecyclerView() {
        bookAdapter = new BookAdapter(this, bookList, userId);
        booksRecyclerView.setAdapter(bookAdapter);
        booksRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void loadAllBooks() {
        bookApi.getAllBooks().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(@NonNull Call<List<Book>> call, @NonNull Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allBooks.clear();
                    allBooks.addAll(response.body());

                    // Apply initial filters from intent
                    String query = getIntent().getStringExtra("SEARCH_QUERY");
                    searchView.setQuery(query, false);
                    btnAll.setSelected(true);
                    bookGenre.setText("All Books");
                    applyFilters("All", query);
                } else {
                    Toast.makeText(MainDashActivity.this, "Error loading books", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Book>> call, @NonNull Throwable t) {
                Toast.makeText(MainDashActivity.this, "Error loading books: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyFilters(String genre, String query) {
        List<Book> filteredList = new ArrayList<>();

        // 1. Filter by genre
        if (genre == null || genre.equalsIgnoreCase("All")) {
            filteredList.addAll(allBooks);
        } else {
            for (Book book : allBooks) {
                if (genre.equalsIgnoreCase(book.getGenre())) {
                    filteredList.add(book);
                }
            }
        }

        // 2. Filter by search query
        if (query != null && !query.isEmpty()) {
            String lowerCaseQuery = query.toLowerCase();
            filteredList = filteredList.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(lowerCaseQuery) || book.getAuthor().toLowerCase().contains(lowerCaseQuery))
                    .collect(Collectors.toList());
        }

        // Update the adapter
        bookList.clear();
        bookList.addAll(filteredList);
        bookAdapter.notifyDataSetChanged();
    }

    private void resetButtons() {
        Button[] buttons = {btnAll, btnAction, btnRomance, btnComedy, btnHorror, btnThriller};
        for (Button b : buttons) {
            b.setSelected(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), intentResult.getContents(), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
