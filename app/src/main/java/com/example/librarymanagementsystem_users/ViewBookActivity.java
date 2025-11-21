package com.example.librarymanagementsystem_users;

import android.os.Bundle;
import android.widget.Button;
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
    }
}
