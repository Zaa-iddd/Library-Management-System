package com.example.librarymanagementsystem_users;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.librarymanagementsystem_users.functions.Book;

import java.util.HashSet;
import java.util.Set;

public class ViewBookActivity extends AppCompatActivity {

    private ImageView favoriteButton;
    private ImageView backButton;
    private Book book;
    private SharedPreferences sharedPreferences;
    private SharedPreferences requestedBooksPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_book);

        sharedPreferences = getSharedPreferences("favorites", MODE_PRIVATE);
        requestedBooksPrefs = getSharedPreferences("requested_books", MODE_PRIVATE);

        ImageView bookCover = findViewById(R.id.imageView);
        TextView bookTitle = findViewById(R.id.bookTitle);
        TextView bookDescription = findViewById(R.id.bookDescription);
        TextView bookStatus = findViewById(R.id.bookStatus);
        Button borrowButton = findViewById(R.id.borrowButton);
        backButton = findViewById(R.id.backButton);
        favoriteButton = findViewById(R.id.favoriteButton);

        book = (Book) getIntent().getSerializableExtra("book");

        if (book != null) {
            book.setFavorite(isFavorite(book.getTitle()));

            bookCover.setImageResource(book.getCoverResourceId());
            bookTitle.setText(book.getTitle());
            bookDescription.setText(book.getDescription());

            if (book.isAvailable()) {
                bookStatus.setText("Available");
                bookStatus.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green)));
                borrowButton.setEnabled(true);
            } else {
                bookStatus.setText("Unavailable");
                bookStatus.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange)));
                borrowButton.setEnabled(false);
            }

            updateFavoriteButton();
        }

        favoriteButton.setOnClickListener(v -> {
            if (book != null) {
                toggleFavorite(book.getTitle());
                book.setFavorite(isFavorite(book.getTitle()));
                updateFavoriteButton();
            }
        });

        borrowButton.setOnClickListener(v -> {
            showConfirmationDialog();
        });

        backButton.setOnClickListener(v -> finish());
    }

    private void showConfirmationDialog() {
        Dialog dialog = new Dialog(ViewBookActivity.this);
        dialog.setContentView(R.layout.dialog_borrow_confirmation);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        Button cancelButton = dialog.findViewById(R.id.cancelButton);
        Button confirmButton = dialog.findViewById(R.id.confirmButton);

        cancelButton.setOnClickListener(v1 -> dialog.dismiss());

        confirmButton.setOnClickListener(v1 -> {
            addRequest(book.getTitle());
            Toast.makeText(ViewBookActivity.this, "Borrow request sent!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void addRequest(String bookTitle) {
        Set<String> requests = requestedBooksPrefs.getStringSet("requested_books_set", new HashSet<>());
        SharedPreferences.Editor editor = requestedBooksPrefs.edit();
        requests.add(bookTitle);
        editor.putStringSet("requested_books_set", requests);
        editor.apply();
    }

    private void updateFavoriteButton() {
        if (book.isFavorite()) {
            favoriteButton.setImageResource(R.drawable.heart_red);
        } else {
            favoriteButton.setImageResource(R.drawable.heart_gray);
        }
    }

    private boolean isFavorite(String bookTitle) {
        Set<String> favorites = sharedPreferences.getStringSet("favorite_books", new HashSet<>());
        return favorites.contains(bookTitle);
    }

    private void toggleFavorite(String bookTitle) {
        Set<String> favorites = sharedPreferences.getStringSet("favorite_books", new HashSet<>());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (favorites.contains(bookTitle)) {
            favorites.remove(bookTitle);
        } else {
            favorites.add(bookTitle);
        }

        editor.putStringSet("favorite_books", favorites);
        editor.apply();
    }
}
