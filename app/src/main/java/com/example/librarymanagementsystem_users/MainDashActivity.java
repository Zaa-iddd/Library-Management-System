package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainDashActivity extends AppCompatActivity {

    Button btnAll, btnAction, btnRomance, btnComedy, btnHorror, btnThriller, btScan, btHome,btMyBooks;
    MaterialCardView profileButton;
    TextView bookGenre;

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


        addBookViews();

        btMyBooks.setOnClickListener(v ->{
            Intent intent = new Intent(MainDashActivity.this, MyBooksDashActivity.class);
            startActivity(intent);
        });

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainDashActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        btScan.setOnClickListener(v -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(MainDashActivity.this);
            intentIntegrator.setPrompt("Scan a barcode or QR Code");
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.initiateScan();
        });

        btHome.setOnClickListener(v -> {
            Intent intent = new Intent(MainDashActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        View.OnClickListener genreClickListener = v -> {
            resetButtons();
            v.setSelected(true);
            Button selectedButton = (Button) v;
            bookGenre.setText(selectedButton.getText().toString() + " Books");
        };

        btnAll.setOnClickListener(genreClickListener);
        btnAction.setOnClickListener(genreClickListener);
        btnRomance.setOnClickListener(genreClickListener);
        btnComedy.setOnClickListener(genreClickListener);
        btnHorror.setOnClickListener(genreClickListener);
        btnThriller.setOnClickListener(genreClickListener);

        // Select All by default
        btnAll.setSelected(true);
        bookGenre.setText(btnAll.getText().toString() + " Books");
    }

    private void addBookViews() {
        LinearLayout booksContainer = findViewById(R.id.books_container);
        LayoutInflater inflater = LayoutInflater.from(this);
        final int booksPerRow = 3;
        final int numBooks = 5; // NUMBER OF BOOKS TO DISPLAY

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
                Intent intent = new Intent(MainDashActivity.this, ViewBookActivity.class);
                startActivity(intent);
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
