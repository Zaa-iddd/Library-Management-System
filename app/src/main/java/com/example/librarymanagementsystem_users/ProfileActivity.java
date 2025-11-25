package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.CaptureActivity;

public class ProfileActivity extends AppCompatActivity {

    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_info);

        logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        Button btHome = findViewById(R.id.btHome);
        btHome.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            finish();
        });

        Button btScan = findViewById(R.id.btScan);
        btScan.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, CaptureActivity.class));
            finish();
        });

        Button btMyBooks = findViewById(R.id.btMyBooks);
        btMyBooks.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, MyBooksDashActivity.class));
            finish();
        });
    }
}
