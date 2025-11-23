package com.example.librarymanagementsystem_users;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

public class MyBooksDashActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnHistory, btnBorrowed, btnReturned;
    ScrollView historyContent, borrowedContent, returnedContent;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybooks_dash);

        btnHistory = findViewById(R.id.btnHistory);
        btnBorrowed = findViewById(R.id.btnBorrowed);
        btnReturned = findViewById(R.id.btnReturned);
        backButton = findViewById(R.id.backButton);

        historyContent = findViewById(R.id.history_content);
        borrowedContent = findViewById(R.id.borrowed_content);
        returnedContent = findViewById(R.id.returned_content);

        btnHistory.setOnClickListener(this);
        btnBorrowed.setOnClickListener(this);
        btnReturned.setOnClickListener(this);
        backButton.setOnClickListener(this);

        // Show borrowed content by default
        showContent(borrowedContent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnHistory) {
            showContent(historyContent);
        } else if (id == R.id.btnBorrowed) {
            showContent(borrowedContent);
        } else if (id == R.id.btnReturned) {
            showContent(returnedContent);
        } else if (id == R.id.backButton) {
            finish();
        }
    }

    private void showContent(View contentToShow) {
        historyContent.setVisibility(View.GONE);
        borrowedContent.setVisibility(View.GONE);
        returnedContent.setVisibility(View.GONE);
        contentToShow.setVisibility(View.VISIBLE);
    }
}
