package com.example.librarymanagementsystem_users;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_book);

        sharedPreferences = getSharedPreferences("favorites", MODE_PRIVATE);

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
            Toast.makeText(ViewBookActivity.this, "Book Borrowed!", Toast.LENGTH_SHORT).show();
        });

        backButton.setOnClickListener(v -> finish());
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
