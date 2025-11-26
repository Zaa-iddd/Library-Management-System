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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem_users.functions.Book;
import com.example.librarymanagementsystem_users.functions.BorrowedBook;
import com.example.librarymanagementsystem_users.functions.RequestedBook;
import com.example.librarymanagementsystem_users.models.BorrowHistory;
import com.example.librarymanagementsystem_users.reotrfit.BookApi;
import com.example.librarymanagementsystem_users.reotrfit.RetrofitService;
import com.example.librarymanagementsystem_users.reotrfit.UserApi;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBooksDashActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnBorrowed, btnFavorite, btnRequested, btnHistory, selectedButton, btScan, btnSearch, btHome;
    private ImageView backButton;
    private RecyclerView favoriteBooksRecyclerView, requestedBooksRecyclerView, borrowedBooksRecyclerView, historyBooksRecyclerView;
    private FavoriteBookAdapter favoriteBookAdapter;
    private RequestedBookAdapter requestedBookAdapter;
    private BorrowedBookAdapter borrowedBookAdapter;
    private HistoryBookAdapter historyBookAdapter;
    private List<Book> favoriteBookList = new ArrayList<>();
    private List<RequestedBook> requestedBookList = new ArrayList<>();
    private List<BorrowedBook> borrowedBookList = new ArrayList<>();
    private List<Book> historyBookList = new ArrayList<>();
    private FrameLayout favoriteContent, borrowedContent, requestedContent, historyContent;
    private SearchView searchView;

    private BookApi bookApi;
    private UserApi userApi;
    private long userId;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybooks_dash);

        bookApi = RetrofitService.getBookApi();
        userApi = RetrofitService.getUserApi();
        userId = getIntent().getLongExtra("USER_ID", 0L);

        // Initialize buttons
        btnBorrowed = findViewById(R.id.btnBorrowed);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnRequested = findViewById(R.id.btnRequested);
        btnHistory = findViewById(R.id.btnHistory);
        backButton = findViewById(R.id.backButton);
        btScan = findViewById(R.id.btScan);
        btnSearch = findViewById(R.id.btnSearch);
        btHome = findViewById(R.id.btHome);

        // Initialize search view
        searchView = findViewById(R.id.searchView);

        // Initialize content views from XML
        favoriteBooksRecyclerView = findViewById(R.id.favoriteBooksRecyclerView);
        requestedBooksRecyclerView = findViewById(R.id.requestedBooksRecyclerView);
        borrowedBooksRecyclerView = findViewById(R.id.borrowedBooksRecyclerView);
        historyBooksRecyclerView = findViewById(R.id.historyBooksRecyclerView);
        favoriteContent = findViewById(R.id.favorite_content);
        borrowedContent = findViewById(R.id.borrowed_content);
        requestedContent = findViewById(R.id.requested_content);
        historyContent = findViewById(R.id.history_content);

        // Set click listeners
        btnBorrowed.setOnClickListener(this);
        btnFavorite.setOnClickListener(this);
        btnRequested.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
        backButton.setOnClickListener(this);
        btScan.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btHome.setOnClickListener(this);

        favoriteBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestedBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        borrowedBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set initial state
        String fragmentToLoad = getIntent().getStringExtra("fragmentToLoad");
        if (fragmentToLoad != null && fragmentToLoad.equals("borrowed")) {
            selectedButton = btnBorrowed;
            btnBorrowed.setSelected(true);
            showContent(borrowedContent);
            loadAndFilterBorrowedBooks();
        } else {
            selectedButton = btnFavorite;
            btnFavorite.setSelected(true);
            showContent(favoriteContent);
            loadAndFilterFavoriteBooks();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnFavorite || id == R.id.btnBorrowed || id == R.id.btnRequested || id == R.id.btnHistory) {
            if (selectedButton != null) {
                selectedButton.setSelected(false);
            }
            selectedButton = (Button) v;
            selectedButton.setSelected(true);

            if (id == R.id.btnFavorite) {
                showContent(favoriteContent);
                loadAndFilterFavoriteBooks();
            } else if (id == R.id.btnBorrowed) {
                showContent(borrowedContent);
                loadAndFilterBorrowedBooks();
            } else if (id == R.id.btnRequested) {
                showContent(requestedContent);
                loadAndFilterRequestedBooks();
            } else if (id == R.id.btnHistory) {
                showContent(historyContent);
                loadAndFilterHistoryBooks();
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
        borrowedContent.setVisibility(View.GONE);
        requestedContent.setVisibility(View.GONE);
        historyContent.setVisibility(View.GONE);
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

                    favoriteBookList = allBooks.stream()
                            .filter(book -> favoriteBookIds.contains(String.valueOf(book.getId())))
                            .collect(Collectors.toList());

                    String query = searchView.getQuery().toString();
                    List<Book> filteredList = favoriteBookList.stream()
                            .filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase()))
                            .collect(Collectors.toList());

                    favoriteBookAdapter = new FavoriteBookAdapter(MyBooksDashActivity.this, filteredList, userId);
                    favoriteBooksRecyclerView.setAdapter(favoriteBookAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Book>> call, @NonNull Throwable t) {
                Toast.makeText(MyBooksDashActivity.this, "Error loading favorites: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAndFilterRequestedBooks() {
        if (userId == 0) {
            Toast.makeText(this, "Please log in to view requested books.", Toast.LENGTH_SHORT).show();
            return;
        }

        userApi.getBorrowHistory(userId).enqueue(new Callback<List<BorrowHistory>>() {
            @Override
            public void onResponse(Call<List<BorrowHistory>> call, Response<List<BorrowHistory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BorrowHistory> borrowHistories = response.body();

                    requestedBookList = borrowHistories.stream()
                            .filter(history -> history.getBook() != null && "Pending".equalsIgnoreCase(history.getStatus()))
                            .map(history -> new RequestedBook(history.getBook().getTitle(), history.getStatus()))
                            .collect(Collectors.toList());

                    requestedBookAdapter = new RequestedBookAdapter(MyBooksDashActivity.this, requestedBookList);
                    requestedBooksRecyclerView.setAdapter(requestedBookAdapter);
                } else {
                    Toast.makeText(MyBooksDashActivity.this, "Failed to load requested books", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BorrowHistory>> call, Throwable t) {
                Toast.makeText(MyBooksDashActivity.this, "Error loading requested books: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAndFilterBorrowedBooks() {
        if (userId == 0) {
            Toast.makeText(this, "Please log in to view borrowed books.", Toast.LENGTH_SHORT).show();
            return;
        }

        userApi.getBorrowHistory(userId).enqueue(new Callback<List<BorrowHistory>>() {
            @Override
            public void onResponse(Call<List<BorrowHistory>> call, Response<List<BorrowHistory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BorrowHistory> borrowHistories = response.body();

                    borrowedBookList = borrowHistories.stream()
                            .filter(history -> history.getBook() != null && "Borrowed".equalsIgnoreCase(history.getStatus()))
                            .map(history -> new BorrowedBook(
                                    history.getBook().getId(),
                                    history.getBook().getTitle(),
                                    history.getReturnDate() != null ? history.getReturnDate() : "N/A"
                            ))
                            .collect(Collectors.toList());

                    borrowedBookAdapter = new BorrowedBookAdapter(
                            MyBooksDashActivity.this,
                            borrowedBookList,
                            userId
                    );

                    borrowedBooksRecyclerView.setAdapter(borrowedBookAdapter);
                } else {
                    String errorMsg = "Failed to load borrowed books. Code: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMsg += ", " + response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e("MyBooksDashActivity", "Error reading error body for borrowed books", e);
                    }
                    Toast.makeText(MyBooksDashActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    Log.e("MyBooksDashActivity", errorMsg);
                }
            }

            @Override
            public void onFailure(Call<List<BorrowHistory>> call, Throwable t) {
                Log.e("MyBooksDashActivity", "Error loading borrowed books", t);
                Toast.makeText(MyBooksDashActivity.this, "Error loading borrowed books: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAndFilterHistoryBooks() {
        if (userId == 0) {
            Toast.makeText(this, "Please log in to view history.", Toast.LENGTH_SHORT).show();
            return;
        }

        userApi.getBorrowHistory(userId).enqueue(new Callback<List<BorrowHistory>>() {
            @Override
            public void onResponse(Call<List<BorrowHistory>> call, Response<List<BorrowHistory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BorrowHistory> borrowHistories = response.body();
                    historyBookList = borrowHistories.stream()
                            .filter(history -> history.getBook() != null && !"Borrowed".equalsIgnoreCase(history.getStatus()) && !"Pending".equalsIgnoreCase(history.getStatus()))
                            .map(BorrowHistory::getBook)
                            .collect(Collectors.toList());
                    historyBookAdapter = new HistoryBookAdapter(MyBooksDashActivity.this, historyBookList, userId);
                    historyBooksRecyclerView.setAdapter(historyBookAdapter);
                } else {
                    String errorMsg = "Failed to load history. Code: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMsg += ", " + response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e("MyBooksDashActivity", "Error reading error body for history", e);
                    }
                    Toast.makeText(MyBooksDashActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    Log.e("MyBooksDashActivity", errorMsg);
                }
            }

            @Override
            public void onFailure(Call<List<BorrowHistory>> call, Throwable t) {
                Log.e("MyBooksDashActivity", "Error loading history", t);
                Toast.makeText(MyBooksDashActivity.this, "Error loading history: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
