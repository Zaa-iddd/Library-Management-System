package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem_users.functions.Book;
import com.example.librarymanagementsystem_users.functions.BookData;
import com.google.android.material.card.MaterialCardView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    
    // -- State --
    private String currentGenre = "All";
    private long userId; // logged-in user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dash);

        userId = getIntent().getLongExtra("USER_ID", 0);

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

        BookData.initialize(this);

        setupRecyclerView();

        // -- UI & Event Listener Setup --

        backButton.setOnClickListener(v -> finish());

        // Re-enable the search button.
        btnSearch.setVisibility(View.VISIBLE);
        btnSearch.setOnClickListener(v -> {
            filterBooks(currentGenre, searchView.getQuery().toString());
            searchView.clearFocus();
        });

        // Restore the listener and fix the typing issue.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterBooks(currentGenre, query);
                searchView.clearFocus(); // Hide keyboard
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Return false to allow the SearchView to update its text internally.
                return false;
            }
        });

        // Setup genre filter buttons
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

            // Clear search when changing genre and re-filter.
            searchView.setQuery("", false);
            searchView.clearFocus();
            filterBooks(currentGenre, null);
        };

        btnAll.setOnClickListener(genreClickListener);
        btnAction.setOnClickListener(genreClickListener);
        btnRomance.setOnClickListener(genreClickListener);
        btnComedy.setOnClickListener(genreClickListener);
        btnHorror.setOnClickListener(genreClickListener);
        btnThriller.setOnClickListener(genreClickListener);
        
        // -- Initial Data Load --
        boolean showAllTrending = getIntent().getBooleanExtra("SHOW_ALL_TRENDING", false);
        String query = getIntent().getStringExtra("SEARCH_QUERY");

        if (showAllTrending) {
            showTrendingBooks();
        } else if (query != null && !query.isEmpty()) {
            searchView.setQuery(query, true);
            filterBooks("All", query);
        } else {
            // Default view
            btnAll.setSelected(true);
            currentGenre = "All";
            bookGenre.setText("All Books");
            filterBooks(currentGenre, null);
        }

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
        bookAdapter = new BookAdapter(this, bookList);
        booksRecyclerView.setAdapter(bookAdapter);
        booksRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void showTrendingBooks() {
        bookGenre.setText("Trending Books");
        currentGenre = "All"; // Trending books are from all genres.
        List<Book> trendingBooks = BookData.getTrendingBooks();
        bookList.clear();
        bookList.addAll(trendingBooks);
        bookAdapter.notifyDataSetChanged();
    }

    /**
     * Filters books based on the selected genre and a search query.
     * This uses Java Streams for a modern, efficient implementation.
     */
    private void filterBooks(String genre, String query) {
        List<Book> allBooks = BookData.getBooks();
        Stream<Book> bookStream = allBooks.stream();

        // 1. Filter by genre
        if (genre != null && !genre.equalsIgnoreCase("All")) {
            bookStream = bookStream.filter(book -> book.getGenre().equalsIgnoreCase(genre));
        }

        // 2. Filter by search query (title or author)
        if (query != null && !query.isEmpty()) {
            final String lowerCaseQuery = query.toLowerCase();
            bookStream = bookStream.filter(book ->
                    book.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                    book.getAuthor().toLowerCase().contains(lowerCaseQuery));
        }

        List<Book> filteredBooks = bookStream.collect(Collectors.toList());

        // Update the RecyclerView
        bookList.clear();
        bookList.addAll(filteredBooks);
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
