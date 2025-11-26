package com.example.librarymanagementsystem_users;

import android.content.Intent;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyBooksDashActivity extends AppCompatActivity implements View.OnClickListener {

    // Buttons for tabs and actions
    Button btnHistory, btnBorrowed, btnFavorite, btScan, btnSearch, btHome;
    ImageView backButton;

    // RecyclerView and its adapter
    RecyclerView favoriteBooksRecyclerView;
    FavoriteBookAdapter bookAdapter;

    // Data list
    List<Book> favoriteBookList;

    // UI elements
    private Button selectedButton;
    private SearchView searchView;
    private View favoriteContent, borrowedPlaceholder, historyPlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybooks_dash);

        // Initialize buttons
        btnHistory = findViewById(R.id.btnHistory);
        btnBorrowed = findViewById(R.id.btnBorrowed);
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
        borrowedPlaceholder = findViewById(R.id.borrowed_placeholder);
        historyPlaceholder = findViewById(R.id.history_placeholder);

        // Set click listeners
        btnHistory.setOnClickListener(this);
        btnBorrowed.setOnClickListener(this);
        btnFavorite.setOnClickListener(this);
        backButton.setOnClickListener(this);
        btScan.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btHome.setOnClickListener(this);

        favoriteBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set initial state
        selectedButton = btnFavorite;
        btnFavorite.setSelected(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnFavorite || id == R.id.btnBorrowed || id == R.id.btnHistory) {
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
            } else if (id == R.id.btnHistory) {
                showContent(historyPlaceholder);
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
        historyPlaceholder.setVisibility(View.GONE);
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

        bookAdapter = new FavoriteBookAdapter(this, filteredList);
        favoriteBooksRecyclerView.setAdapter(bookAdapter);
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
        } else if (selectedId == R.id.btnHistory) {
            showContent(historyPlaceholder);
        }
    }
}
