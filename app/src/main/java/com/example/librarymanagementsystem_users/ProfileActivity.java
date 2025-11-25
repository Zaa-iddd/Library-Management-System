package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.CaptureActivity;

public class ProfileActivity extends AppCompatActivity {

    Button logoutButton;
    Button editProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_info);

        logoutButton = findViewById(R.id.logoutButton);
        editProfileButton = findViewById(R.id.editProfileButton);

        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        editProfileButton.setOnClickListener(this::showEditProfileOverlay);

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

    private void showEditProfileOverlay(View v) {
        // Inflate the overlay layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.edit_profile_overlay, null);

        // Create a new popup window
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // Show the popup window
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        // Get references to the buttons in the overlay
        Button saveButton = popupView.findViewById(R.id.saveButton);
        Button cancelButton = popupView.findViewById(R.id.cancelButton);

        // Set click listeners for the overlay buttons
        saveButton.setOnClickListener(view -> {
            popupWindow.dismiss();
            showPasswordConfirmationOverlay(v);
        });

        cancelButton.setOnClickListener(view -> {
            // Handle cancel button click
            popupWindow.dismiss();
        });
    }

    private void showPasswordConfirmationOverlay(View v) {
        // Inflate the overlay layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.password_confirmation_overlay, null);

        // Create a new popup window
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // Show the popup window
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        // Get references to the buttons in the overlay
        Button confirmButton = popupView.findViewById(R.id.confirmButton);
        Button returnButton = popupView.findViewById(R.id.returnButton);

        // Set click listeners for the overlay buttons
        confirmButton.setOnClickListener(view -> {
            // Handle save button click
            popupWindow.dismiss();
        });

        returnButton.setOnClickListener(view -> {
            // Handle cancel button click
            popupWindow.dismiss();
            showEditProfileOverlay(v);
        });
    }
}
