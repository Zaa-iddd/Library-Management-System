package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagementsystem_users.functions.Book;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout myBooksContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        TextView viewBooks = findViewById(R.id.viewBooks);
        viewBooks.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, MainDashActivity.class)));

        myBooksContainer = findViewById(R.id.myBooksContainer);

        // Create a dummy list of favorite books
        List<Book> favoriteBooks = new ArrayList<>();
        String[] titles = {"One Piece", "The Lord of the Rings", "The Hitchhiker's Guide to the Galaxy", "The Alchemist", "The Da Vinci Code"};
        String[] authors = {"Zaid Competente", "J.R.R. Tolkien", "Douglas Adams", "Paulo Coelho", "Dan Brown"};
        int[] covers = {R.drawable.sample_book, R.drawable.sample_book, R.drawable.sample_book, R.drawable.sample_book, R.drawable.sample_book};

        for (int i = 0; i < titles.length; i++) {
            favoriteBooks.add(new Book(titles[i], authors[i], covers[i]));
        }

        // Add the favorite books to the layout
        for (Book book : favoriteBooks) {
            addBookToLayout(book);
        }
    }

    private void addBookToLayout(Book book) {
        View bookView = LayoutInflater.from(this).inflate(R.layout.favorite_book, myBooksContainer, false);

        TextView title = bookView.findViewById(R.id.textView2);
        TextView author = bookView.findViewById(R.id.textView5);
        ImageView cover = bookView.findViewById(R.id.imageView2);

        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        cover.setImageResource(book.getCoverResourceId());

        myBooksContainer.addView(bookView);
    }
}
