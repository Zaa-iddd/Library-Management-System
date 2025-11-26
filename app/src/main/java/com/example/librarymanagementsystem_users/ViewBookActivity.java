package com.example.librarymanagementsystem_users;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.librarymanagementsystem_users.functions.Book;
import com.example.librarymanagementsystem_users.reotrfit.BookApi;
import com.example.librarymanagementsystem_users.reotrfit.RetrofitService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewBookActivity extends AppCompatActivity {

    private static final String TAG = "ViewBookActivity";

    private ImageView favoriteButton;
    private Button borrowButton;
    private TextView bookTitle, bookDescription, bookStatus, bookAvailability, bookAuthor, bookGenre, bookPublisher, bookPublicationDate, bookPages, bookLanguage, bookTotalCopies;

    private Book book;
    private SharedPreferences favoritesPrefs;
    private long userId;
    private long bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_book);

        favoritesPrefs = getSharedPreferences("favorites", MODE_PRIVATE);

        // Get IDs from the intent
        userId = getIntent().getLongExtra("USER_ID", 0);
        bookId = getIntent().getLongExtra("BOOK_ID", 0);

        // If no book ID, we can't do anything
        if (bookId == 0) {
            Toast.makeText(this, "Failed to load book details: No Book ID.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        fetchBookDetails(bookId);

        // Setup listeners that don't depend on the book object yet
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    private void initializeViews() {
        bookTitle = findViewById(R.id.bookTitle);
        bookDescription = findViewById(R.id.bookDescription);
        bookStatus = findViewById(R.id.bookStatus);
        bookAvailability = findViewById(R.id.bookAvailability);
        bookAuthor = findViewById(R.id.bookAuthor);
        bookGenre = findViewById(R.id.bookGenre);
        bookPublisher = findViewById(R.id.bookPublisher);
        bookPublicationDate = findViewById(R.id.bookPublicationDate);
        bookPages = findViewById(R.id.bookPages);
        bookLanguage = findViewById(R.id.bookLanguage);
        bookTotalCopies = findViewById(R.id.bookTotalCopies);
        borrowButton = findViewById(R.id.borrowButton);
        favoriteButton = findViewById(R.id.favoriteButton);
    }

    private void fetchBookDetails(long bookId) {
        Log.d(TAG, "Fetching details for book ID: " + bookId);
        BookApi bookApi = RetrofitService.getBookApi();
        bookApi.getBookById(bookId).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(@NonNull Call<Book> call, @NonNull Response<Book> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Book details fetched successfully.");
                    ViewBookActivity.this.book = response.body();
                    populateUi();
                } else {
                    String errorMsg = "Failed to load book details. Code: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMsg += ", " + response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    Toast.makeText(ViewBookActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    Log.e(TAG, errorMsg);
                    finish(); // Can't show details, so close activity
                }
            }

            @Override
            public void onFailure(@NonNull Call<Book> call, @NonNull Throwable t) {
                String errorMsg = "Failed to load book details: " + t.getMessage();
                Toast.makeText(ViewBookActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error fetching book details", t);
                finish(); // Can't show details, so close activity
            }
        });
    }

    private void populateUi() {
        if (book == null) {
            Toast.makeText(this, "Failed to display book details.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Populate data
        bookTitle.setText(book.getTitle());
        bookDescription.setText(book.getSummary());
        bookStatus.setText(book.getStatus());
        bookAuthor.setText(book.getAuthor());
        bookGenre.setText(book.getGenre());
        bookPublisher.setText(book.getPublisher());
        bookPublicationDate.setText(book.getPublication_date());
        bookLanguage.setText(book.getLanguage());
        bookAvailability.setText(String.valueOf(book.getCopies_available()));
        bookPages.setText(String.valueOf(book.getNumber_of_pages()));
        bookTotalCopies.setText(String.valueOf(book.getTotal_copies()));

        // Status color
        if ("Available".equalsIgnoreCase(book.getStatus())) {
            bookStatus.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.green)));
            borrowButton.setEnabled(true);
        } else {
            bookStatus.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.orange)));
            borrowButton.setEnabled(false);
        }

        // Setup listeners that depend on the book object
        favoriteButton.setOnClickListener(v -> toggleFavorite(book.getId()));
        borrowButton.setOnClickListener(v -> showConfirmationDialog());

        // Initial favorite icon state
        updateFavoriteButton();
    }

    // ------------------ FAVORITES SYSTEM ------------------

    private boolean isFavorite(long bookId) {
        Set<String> favorites = favoritesPrefs.getStringSet("favorite_books", new HashSet<>());
        return favorites.contains(String.valueOf(bookId));
    }

    private void toggleFavorite(long bookId) {
        Set<String> oldFavorites = favoritesPrefs.getStringSet("favorite_books", new HashSet<>());
        Set<String> newFavorites = new HashSet<>(oldFavorites);

        String idString = String.valueOf(bookId);

        if (newFavorites.contains(idString)) {
            newFavorites.remove(idString);
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
        } else {
            newFavorites.add(idString);
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
        }

        favoritesPrefs.edit().putStringSet("favorite_books", newFavorites).apply();
        updateFavoriteButton();
    }

    private void updateFavoriteButton() {
        if (book == null) return; // safety

        if (isFavorite(book.getId())) {
            favoriteButton.setImageResource(R.drawable.heart_red);
        } else {
            favoriteButton.setImageResource(R.drawable.heart_gray);
        }
    }

    // ------------------ BORROW REQUEST ------------------

    private void showConfirmationDialog() {
        if (book == null) {
            Toast.makeText(this, "Cannot borrow, book details not loaded.", Toast.LENGTH_SHORT).show();
            return;
        }
        Dialog dialog = new Dialog(ViewBookActivity.this);
        dialog.setContentView(R.layout.dialog_borrow_confirmation);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        Button cancelButton = dialog.findViewById(R.id.cancelButton);
        Button confirmButton = dialog.findViewById(R.id.confirmButton);

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        confirmButton.setOnClickListener(v -> {
            requestBook(book.getId(), userId);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void requestBook(long bookId, long userId) {
        BookApi bookApi = RetrofitService.getBookApi();
        Call<Void> call = bookApi.requestBook(bookId, userId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ViewBookActivity.this, "Book requested successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ViewBookActivity.this, "Request failed (server error)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(ViewBookActivity.this, "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
