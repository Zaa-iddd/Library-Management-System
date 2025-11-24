package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_book);

        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(v -> {
            finish();
        });

        Button addToFavoritesButton = findViewById(R.id.addToFavoritesButton);
        addToFavoritesButton.setOnClickListener(v -> {
            // You can add your logic here for adding the book to favorites
            Toast.makeText(ViewBookActivity.this, "Added to Favorites!", Toast.LENGTH_SHORT).show();
        });

        addBookViews();
    }

    private void addBookViews() {
        LinearLayout booksContainer = findViewById(R.id.books_container);
        LayoutInflater inflater = LayoutInflater.from(this);
        final int booksPerRow = 3;
        final int numBooks = 6; // NUMBER OF BOOKS TO DISPLAY

        LinearLayout.LayoutParams bookLayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        bookLayoutParams.setMarginEnd(16);

        LinearLayout rowLayout = null;

        for (int i = 0; i < numBooks; i++) {
            if (i % booksPerRow == 0) {
                rowLayout = new LinearLayout(this);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                rowLayout.setWeightSum(booksPerRow);
                booksContainer.addView(rowLayout);
            }

            View bookView = inflater.inflate(R.layout.item_book, rowLayout, false);
            bookView.setLayoutParams(bookLayoutParams);
            rowLayout.addView(bookView);

            bookView.setOnClickListener(v -> {
                Intent intent = new Intent(ViewBookActivity.this, ViewBookActivity.class);
                startActivity(intent);
            });
        }
    }
}
