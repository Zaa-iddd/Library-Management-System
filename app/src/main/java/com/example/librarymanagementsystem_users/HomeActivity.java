package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem_users.functions.Book;
import com.example.librarymanagementsystem_users.functions.BookData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView trendingBooksRecyclerView;
    private TrendingBookAdapter trendingBookAdapter;
    private List<Book> trendingBookList;

    private RecyclerView favoriteBooksRecyclerView;
    private FavoriteBookAdapter favoriteBookAdapter;
    private List<Book> favoriteBookList;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        sharedPreferences = getSharedPreferences("favorites", MODE_PRIVATE);

        TextView viewBooks = findViewById(R.id.viewBooks);
        viewBooks.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, MainDashActivity.class)));

        TextView viewMyBook = findViewById(R.id.viewMyBook);
        viewMyBook.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, MyBooksDashActivity.class)));

        // Trending Books
        trendingBooksRecyclerView = findViewById(R.id.trendingBooksRecyclerView);
        trendingBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        trendingBookList = new BookData().getTrendingBooks();
        trendingBookAdapter = new TrendingBookAdapter(this, trendingBookList);
        trendingBooksRecyclerView.setAdapter(trendingBookAdapter);

        // Favorite Books
        favoriteBooksRecyclerView = findViewById(R.id.favoriteBooksRecyclerView);
        favoriteBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadFavoriteBooks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavoriteBooks();
    }

    private void loadFavoriteBooks() {
        Set<String> favoriteBookTitles = sharedPreferences.getStringSet("favorite_books", new HashSet<>());
        List<Book> allBooks = BookData.getBooks();

        favoriteBookList = allBooks.stream()
                .filter(book -> favoriteBookTitles.contains(book.getTitle()))
                .collect(Collectors.toList());

        List<Book> limitedFavoriteBooks;
        if (favoriteBookList.size() > 2) {
            limitedFavoriteBooks = new ArrayList<>(favoriteBookList.subList(0, 2));
        } else {
            limitedFavoriteBooks = new ArrayList<>(favoriteBookList);
        }

        favoriteBookAdapter = new FavoriteBookAdapter(this, limitedFavoriteBooks);
        favoriteBooksRecyclerView.setAdapter(favoriteBookAdapter);
    }
}
