package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem_users.functions.Book;
import com.example.librarymanagementsystem_users.functions.BookData;
import com.example.librarymanagementsystem_users.functions.RequestedBook;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MyBooksDashActivity extends AppCompatActivity implements View.OnClickListener {

    // Buttons for tabs and actions
    Button btnBorrowed, btnFavorite, btnRequested, btScan, btnSearch, btHome;
    ImageView backButton;

    // RecyclerView and its adapter
    RecyclerView favoriteBooksRecyclerView, requestedBooksRecyclerView;
    FavoriteBookAdapter favoriteBookAdapter;
    RequestedBookAdapter requestedBookAdapter;

    // Data list
    List<Book> favoriteBookList;
    List<RequestedBook> requestedBookList;

    // UI elements
    private Button selectedButton;
    private SearchView searchView;
    private View favoriteContent, borrowedPlaceholder, requestedContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybooks_dash);

        // Initialize buttons
        btnBorrowed = findViewById(R.id.btnBorrowed);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnRequested = findViewById(R.id.btnRequested);
        backButton = findViewById(R.id.backButton);
        btScan = findViewById(R.id.btScan);
        btnSearch = findViewById(R.id.btnSearch);
        btHome = findViewById(R.id.btHome);

        // Initialize search view
        searchView = findViewById(R.id.searchView);

        // Initialize content views from XML
        favoriteBooksRecyclerView = findViewById(R.id.favoriteBooksRecyclerView);
        requestedBooksRecyclerView = findViewById(R.id.requestedBooksRecyclerView);
        favoriteContent = findViewById(R.id.favorite_content);
        borrowedPlaceholder = findViewById(R.id.borrowed_placeholder);
        requestedContent = findViewById(R.id.requested_content);

        // Set click listeners
        btnBorrowed.setOnClickListener(this);
        btnFavorite.setOnClickListener(this);
        btnRequested.setOnClickListener(this);
        backButton.setOnClickListener(this);
        btScan.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btHome.setOnClickListener(this);

        favoriteBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestedBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set initial state
        selectedButton = btnFavorite;
        btnFavorite.setSelected(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnFavorite || id == R.id.btnBorrowed || id == R.id.btnRequested) {
            if (selectedButton != null) {
                selectedButton.setSelected(false);
            }
            selectedButton = (Button) v;
            selectedButton.setSelected(true);

            if (id == R.id.btnFavorite) {
                showContent(favoriteContent);
                loadAndFilterFavoriteBooks();
            } else if (id == R.id.btnBorrowed) {
                showContent(borrowedPlaceholder);
            } else if (id == R.id.btnRequested) {
                showContent(requestedContent);
                loadAndFilterRequestedBooks();
            }
        } else if (id == R.id.backButton) {
            finish();
        } else if (id == R.id.btScan) {
            // QR Code scan logic
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setPrompt("Scan a QR code");
            integrator.initiateScan();
        } else if (id == R.id.btnSearch) {
            if (selectedButton.getId() == R.id.btnFavorite) {
                loadAndFilterFavoriteBooks();
            }
        } else if (id == R.id.btHome) {
            Intent intent = new Intent(MyBooksDashActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    private void showContent(View contentToShow) {
        favoriteContent.setVisibility(View.GONE);
        borrowedPlaceholder.setVisibility(View.GONE);
        requestedContent.setVisibility(View.GONE);
        if (contentToShow != null) {
            contentToShow.setVisibility(View.VISIBLE);
        }
    }

    private void loadAndFilterFavoriteBooks() {
        favoriteBookList = BookData.getFavoriteBooks(this);

        String query = searchView.getQuery().toString();

        List<Book> filteredList = new ArrayList<>();
        if (favoriteBookList != null) {
            filteredList = favoriteBookList.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }

        favoriteBookAdapter = new FavoriteBookAdapter(this, filteredList);
        favoriteBooksRecyclerView.setAdapter(favoriteBookAdapter);
    }

    private void loadAndFilterRequestedBooks() {
        SharedPreferences requestedBooksPrefs = getSharedPreferences("requested_books", MODE_PRIVATE);
        Set<String> requests = requestedBooksPrefs.getStringSet("requested_books_set", new HashSet<>());
        requestedBookList = new ArrayList<>();
        for (String request : requests) {
            requestedBookList.add(new RequestedBook(request, "Pending"));
        }

        requestedBookAdapter = new RequestedBookAdapter(this, requestedBookList);
        requestedBooksRecyclerView.setAdapter(requestedBookAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int selectedId = selectedButton.getId();
        if (selectedId == R.id.btnFavorite) {
            showContent(favoriteContent);
            loadAndFilterFavoriteBooks();
        } else if (selectedId == R.id.btnBorrowed) {
            showContent(borrowedPlaceholder);
        } else if (selectedId == R.id.btnRequested) {
            showContent(requestedContent);
            loadAndFilterRequestedBooks();
        }
    }
}
