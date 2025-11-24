package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_book);

        TextView bookTitle = findViewById(R.id.bookTitle);
        ImageView bookCover = findViewById(R.id.imageView);
        TextView bookDescription = findViewById(R.id.bookDescription);

        // Get data from intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("bookTitle");
        int coverResourceId = intent.getIntExtra("bookCover", 0);
        String description = intent.getStringExtra("bookDescription");

        // Set data to views
        bookTitle.setText(title);
        if (coverResourceId != 0) {
            bookCover.setImageResource(coverResourceId);
        }
        bookDescription.setText(description);

        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(v -> finish());

        Button borrowButton = findViewById(R.id.borrowButton);
        borrowButton.setOnClickListener(v -> {
            // You can add your logic here for borrowing the book
            Toast.makeText(ViewBookActivity.this, "Book Borrowed!", Toast.LENGTH_SHORT).show();
        });
    }
}
