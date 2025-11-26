package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagementsystem_users.models.UserResponseDto;
import com.example.librarymanagementsystem_users.reotrfit.RetrofitService;
import com.example.librarymanagementsystem_users.reotrfit.UserApi;
import com.journeyapps.barcodescanner.CaptureActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private Button logoutButton;
    private Button editProfileButton;
    private Button viewBorrowedButton;
    private ImageView backButton;
    private TextView usernameText, emailText;

    private UserApi userApi;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_info);

        userId = getIntent().getLongExtra("USER_ID", 0L);

        logoutButton = findViewById(R.id.logoutButton);
        editProfileButton = findViewById(R.id.editProfileButton);
        viewBorrowedButton = findViewById(R.id.viewBorrowedButton);
        backButton = findViewById(R.id.backButton);
        usernameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);

        userApi = RetrofitService.getUserApi();

        loadUserProfile();

        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        editProfileButton.setOnClickListener(v -> {
            if (userId == 0L) {
                Toast.makeText(this, "Cannot edit profile. User ID not found.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        viewBorrowedButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MyBooksDashActivity.class);
            intent.putExtra("fragmentToLoad", "borrowed");
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> finish());

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

    private void loadUserProfile() {
        userApi.getUserById(userId).enqueue(new Callback<UserResponseDto>() {
            @Override
            public void onResponse(@NonNull Call<UserResponseDto> call, @NonNull Response<UserResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponseDto user = response.body();
                    usernameText.setText(user.getUsername());
                    emailText.setText(user.getEmail());
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to load user info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponseDto> call, @NonNull Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
