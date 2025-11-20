package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_info);

        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainDashActivity.class);
            startActivity(intent);
            finish(); // Optional: finish ProfileActivity so it's not in the back stack
        });
    }
}
