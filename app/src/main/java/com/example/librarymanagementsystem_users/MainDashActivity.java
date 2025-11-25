package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.librarymanagementsystem_users.functions.Book;
import com.example.librarymanagementsystem_users.functions.BookData;
import com.google.android.material.card.MaterialCardView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;
import java.util.stream.Collectors;

public class MainDashActivity extends AppCompatActivity {

    Button btnAll, btnAction, btnRomance, btnComedy, btnHorror, btnThriller, btScan, btHome, btMyBooks, btnSearch;
    MaterialCardView profileButton;
    TextView bookGenre;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dash);

        btnAll = findViewById(R.id.btnAll);
        btnAction = findViewById(R.id.btnAction);
        btnRomance = findViewById(R.id.btnRomance);
        btnComedy = findViewById(R.id.btnComedy);
        btnHorror = findViewById(R.id.btnHorror);
        btnThriller = findViewById(R.id.btnThriller);
        profileButton = findViewById(R.id.profileButton);
        btScan = findViewById(R.id.btScan);
        btHome = findViewById(R.id.btHome);
        btMyBooks = findViewById(R.id.btMyBooks);
        bookGenre = findViewById(R.id.bookGenre);
        searchView = findViewById(R.id.searchView);
        btnSearch = findViewById(R.id.button);

        Intent intent = getIntent();
        String query = intent.getStringExtra("SEARCH_QUERY");

        if (query != null && !query.isEmpty()) {
            searchView.setQuery(query, true);
            filterBooks("All", query);
        } else {
            filterBooks("All", null);
        }

        btnSearch.setOnClickListener(v -> {
            String searchQuery = searchView.getQuery().toString();
            filterBooks("All", searchQuery);
        });

        btMyBooks.setOnClickListener(v -> {
            Intent myBooksIntent = new Intent(MainDashActivity.this, MyBooksDashActivity.class);
            startActivity(myBooksIntent);
        });

        profileButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(MainDashActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        });

        btScan.setOnClickListener(v -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(MainDashActivity.this);
            intentIntegrator.setPrompt("Scan a barcode or QR Code");
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.initiateScan();
        });

        btHome.setOnClickListener(v -> {
            Intent homeIntent = new Intent(MainDashActivity.this, HomeActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(homeIntent);
        });

        View.OnClickListener genreClickListener = v -> {
            resetButtons();
            v.setSelected(true);
            Button selectedButton = (Button) v;
            String genre = selectedButton.getText().toString();
            bookGenre.setText(genre + " Books");
            filterBooks(genre, null);
        };

        btnAll.setOnClickListener(genreClickListener);
        btnAction.setOnClickListener(genreClickListener);
        btnRomance.setOnClickListener(genreClickListener);
        btnComedy.setOnClickListener(genreClickListener);
        btnHorror.setOnClickListener(genreClickListener);
        btnThriller.setOnClickListener(genreClickListener);

        btnAll.setSelected(true);
        bookGenre.setText(btnAll.getText().toString() + " Books");
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
            filteredBooks = filteredBooks.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                            book.getAuthor().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }

        LinearLayout booksContainer = findViewById(R.id.books_container);
        booksContainer.removeAllViews(); // Clear existing views
        LayoutInflater inflater = LayoutInflater.from(this);
        final int booksPerRow = 3;

        LinearLayout.LayoutParams bookLayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        bookLayoutParams.setMarginEnd(16);

        LinearLayout rowLayout = null;

        for (int i = 0; i < filteredBooks.size(); i++) {
            final Book book = filteredBooks.get(i);

            if (i % booksPerRow == 0) {
                rowLayout = new LinearLayout(this);
                LinearLayout.LayoutParams rowLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                rowLayoutParams.setMargins(0, 0, 0, 16); // Add bottom margin to each row
                rowLayout.setLayoutParams(rowLayoutParams);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.setWeightSum(booksPerRow);
                booksContainer.addView(rowLayout);
            }

            View bookView = inflater.inflate(R.layout.item_book, rowLayout, false);

            ImageView cover = bookView.findViewById(R.id.imageBook);
            TextView title = bookView.findViewById(R.id.textTitle);
            TextView author = bookView.findViewById(R.id.textAuthor);
            TextView genreView = bookView.findViewById(R.id.textGenre);
            TextView status = bookView.findViewById(R.id.textStatus);

            cover.setImageResource(book.getCoverResourceId());
            title.setText(book.getTitle());
            author.setText(book.getAuthor());
            genreView.setText(book.getGenre());

            if (book.isAvailable()) {
                status.setText("Available");
                status.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green)));
            } else {
                status.setText("Unavailable");
                status.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange)));
            }

            bookView.setLayoutParams(bookLayoutParams);
            if (rowLayout != null) {
                rowLayout.addView(bookView);
            }

            bookView.setOnClickListener(v -> {
                Intent viewBookIntent = new Intent(MainDashActivity.this, ViewBookActivity.class);
                viewBookIntent.putExtra("book", book);
                startActivity(viewBookIntent);
            });
        }
    }

    private void resetButtons() {
        Button[] buttons = {btnAll, btnAction, btnRomance, btnComedy, btnHorror, btnThriller};
        for (Button b : buttons) {
            b.setSelected(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), intentResult.getContents(), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
