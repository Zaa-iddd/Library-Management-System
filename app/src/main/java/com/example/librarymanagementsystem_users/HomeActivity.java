package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementsystem_users.functions.Book;
import com.example.librarymanagementsystem_users.functions.BookData;
import com.google.android.material.card.MaterialCardView;
import com.journeyapps.barcodescanner.CaptureActivity;

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
    private long userId; // logged-in user ID

    private ScrollView homeScrollView;
    private RelativeLayout mainDashContainer;

    // Views from main_dash
    private Button btnAll, btnAction, btnRomance, btnComedy, btnHorror, btnThriller, btnSearch;
    private TextView bookGenre;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        sharedPreferences = getSharedPreferences("favorites", MODE_PRIVATE);

        homeScrollView = findViewById(R.id.home_scroll_view);
        mainDashContainer = findViewById(R.id.main_dash_container);

        // Get the user ID passed from LoginActivity first, or from SharedPreferences
        userId = getIntent().getLongExtra("USER_ID", 0);
        if (userId == 0) {
            userId = sharedPreferences.getLong("USER_ID", 0);
        } else {
            // Save the userId to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("USER_ID", userId);
            editor.apply();
        }

        // --- Logic for main_dash inside HomeActivity ---
        // Bind Views from main_dash
        btnAll = mainDashContainer.findViewById(R.id.btnAll);
        btnAction = mainDashContainer.findViewById(R.id.btnAction);
        btnRomance = mainDashContainer.findViewById(R.id.btnRomance);
        btnComedy = mainDashContainer.findViewById(R.id.btnComedy);
        btnHorror = mainDashContainer.findViewById(R.id.btnHorror);
        btnThriller = mainDashContainer.findViewById(R.id.btnThriller);
        bookGenre = mainDashContainer.findViewById(R.id.bookGenre);
        searchView = mainDashContainer.findViewById(R.id.searchView);
        btnSearch = mainDashContainer.findViewById(R.id.button);

        // Genre filter buttons
        View.OnClickListener genreClickListener = v -> {
            resetButtons();
            v.setSelected(true);
            Button selectedButton = (Button) v;
            String genre = selectedButton.getText().toString();
            if (genre.equals("All")) {
                bookGenre.setText("All Books");
            } else {
                bookGenre.setText(genre + " Books");
            }
            filterBooks(genre, null);
        };

        btnAll.setOnClickListener(genreClickListener);
        btnAction.setOnClickListener(genreClickListener);
        btnRomance.setOnClickListener(genreClickListener);
        btnComedy.setOnClickListener(genreClickListener);
        btnHorror.setOnClickListener(genreClickListener);
        btnThriller.setOnClickListener(genreClickListener);

        btnSearch.setOnClickListener(v -> {
            String searchQuery = searchView.getQuery().toString();
            filterBooks("All", searchQuery);
        });
        // --- End of logic for main_dash ---

        // Show main_dash on "View All" click
        TextView viewBooks = findViewById(R.id.viewBooks);
        viewBooks.setOnClickListener(v -> {
            homeScrollView.setVisibility(View.GONE);
            mainDashContainer.setVisibility(View.VISIBLE);
            resetButtons();
            showTrendingBooksInDash();
        });

        // Navigate to MyBooksDashActivity
        TextView viewMyBook = findViewById(R.id.viewMyBook);
        viewMyBook.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MyBooksDashActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        // Trending Books RecyclerView
        trendingBooksRecyclerView = findViewById(R.id.trendingBooksRecyclerView);
        trendingBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        trendingBookList = new BookData().getTrendingBooks();
        trendingBookAdapter = new TrendingBookAdapter(this, trendingBookList);
        trendingBooksRecyclerView.setAdapter(trendingBookAdapter);

        // Favorite Books RecyclerView
        favoriteBooksRecyclerView = findViewById(R.id.favoriteBooksRecyclerView);
        favoriteBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadFavoriteBooks();

        // Search on home screen
        SearchView homeSearchView = homeScrollView.findViewById(R.id.searchView);
        Button homeSearchButton = homeScrollView.findViewById(R.id.button);
        homeSearchButton.setOnClickListener(v -> {
            String query = homeSearchView.getQuery().toString();
            Intent intent = new Intent(HomeActivity.this, MainDashActivity.class);
            intent.putExtra("SEARCH_QUERY", query);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        // Bottom Navigation
        Button btHome = findViewById(R.id.btHome);
        btHome.setOnClickListener(v -> {
            if (mainDashContainer.getVisibility() == View.VISIBLE) {
                mainDashContainer.setVisibility(View.GONE);
                homeScrollView.setVisibility(View.VISIBLE);
            }
        });

        Button btScan = findViewById(R.id.btScan);
        btScan.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CaptureActivity.class);
            startActivity(intent);
        });

        Button btMyBooks = findViewById(R.id.btMyBooks);
        btMyBooks.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MyBooksDashActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        // Profile card
        MaterialCardView profileCard = findViewById(R.id.btEditProfile);
        profileCard.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
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

    @Override
    public void onBackPressed() {
        if (mainDashContainer.getVisibility() == View.VISIBLE) {
            mainDashContainer.setVisibility(View.GONE);
            homeScrollView.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    // --- Methods for main_dash ---
    private void showTrendingBooksInDash() {
        if(bookGenre != null) bookGenre.setText("Trending Books");
        List<Book> trendingBooks = new BookData().getTrendingBooks();
        LinearLayout booksContainer = mainDashContainer.findViewById(R.id.books_container);
        BookViewHelper.populateBooks(this, booksContainer, trendingBooks);
    }

    private void filterBooks(String genre, String query) {
        List<Book> allBooks = BookData.getBooks();
        List<Book> filteredBooks;

        if (genre.equals("All")) {
            filteredBooks = allBooks;
        } else {
            filteredBooks = allBooks.stream()
                    .filter(book -> book.getGenre().equalsIgnoreCase(genre))
                    .collect(Collectors.toList());
        }

        if (query != null && !query.isEmpty()) {
            String lowerCaseQuery = query.toLowerCase();
            filteredBooks = filteredBooks.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                            book.getAuthor().toLowerCase().contains(lowerCaseQuery))
                    .collect(Collectors.toList());
        }
        LinearLayout booksContainer = mainDashContainer.findViewById(R.id.books_container);
        BookViewHelper.populateBooks(this, booksContainer, filteredBooks);
    }

    private void resetButtons() {
        Button[] buttons = {btnAll, btnAction, btnRomance, btnComedy, btnHorror, btnThriller};
        for (Button b : buttons) {
            if (b != null) b.setSelected(false);
        }
    }
}
