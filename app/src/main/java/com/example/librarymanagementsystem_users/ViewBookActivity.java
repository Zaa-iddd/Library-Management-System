package com.example.librarymanagementsystem_users;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagementsystem_users.functions.Book;

public class ViewBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_book);

        ImageView bookCover = findViewById(R.id.imageView);
        TextView bookTitle = findViewById(R.id.bookTitle);
        TextView bookDescription = findViewById(R.id.bookDescription);
        Button borrowButton = findViewById(R.id.borrowButton);
        Button returnButton = findViewById(R.id.returnButton);

        Book book = (Book) getIntent().getSerializableExtra("book");

        if (book != null) {
            bookCover.setImageResource(book.getCoverResourceId());
            bookTitle.setText(book.getTitle());
            bookDescription.setText(book.getDescription());
        }

        borrowButton.setOnClickListener(v -> {
            Toast.makeText(ViewBookActivity.this, "Book Borrowed!", Toast.LENGTH_SHORT).show();
        });

        returnButton.setOnClickListener(v -> finish());
    }
}
