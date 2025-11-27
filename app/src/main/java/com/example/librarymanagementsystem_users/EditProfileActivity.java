package com.example.librarymanagementsystem_users;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagementsystem_users.models.UserResponseDto;
import com.example.librarymanagementsystem_users.models.UserProfileDto;
import com.example.librarymanagementsystem_users.reotrfit.RetrofitService;
import com.example.librarymanagementsystem_users.reotrfit.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etFirstName, etLastName, etPhone, etAddress;
    private Button btnUpdate;

    private UserApi userApi;
    private Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_overlay);

        // Initialize Views
        etUsername = findViewById(R.id.editUsername);
        etEmail = findViewById(R.id.editEmail);
        etFirstName = findViewById(R.id.editFirstName);
        etLastName = findViewById(R.id.editLastName);
        etPhone = findViewById(R.id.editPhoneNumber);
        etAddress = findViewById(R.id.editAddress);
        btnUpdate = findViewById(R.id.saveButton);

        // Get userid sa Intent
        userId = getIntent().getLongExtra("USER_ID", 0L);
        if (userId == 0L) {
            Toast.makeText(this, "User ID not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Use RetrofitService
        userApi = RetrofitService.getUserApi();

        loadUserProfile(); // load ki current user

        btnUpdate.setOnClickListener(v -> updateUser());
    }

    private void loadUserProfile() {
        userApi.getUserById(userId).enqueue(new Callback<UserResponseDto>() {
            @Override
            public void onResponse(@NonNull Call<UserResponseDto> call, @NonNull Response<UserResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponseDto user = response.body();
                    etUsername.setText(user.getUsername());
                    etEmail.setText(user.getEmail());
                    etFirstName.setText(user.getFirstName());
                    etLastName.setText(user.getLastName());
                    etPhone.setText(user.getPhoneNumber());
                    etAddress.setText(user.getAddress());
                } else {
                    Toast.makeText(EditProfileActivity.this, "Failed to load user info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponseDto> call, @NonNull Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUser() {
        UserProfileDto dto = new UserProfileDto();
        dto.setUsername(etUsername.getText().toString().trim());
        dto.setEmail(etEmail.getText().toString().trim());
        dto.setFirstName(etFirstName.getText().toString().trim());
        dto.setLastName(etLastName.getText().toString().trim());
        dto.setPhoneNumber(etPhone.getText().toString().trim());
        dto.setAddress(etAddress.getText().toString().trim());

        userApi.updateUserProfile(userId, dto).enqueue(new Callback<UserResponseDto>() {
            @Override
            public void onResponse(@NonNull Call<UserResponseDto> call, @NonNull Response<UserResponseDto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Profile updated!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Update failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponseDto> call, @NonNull Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
