package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagementsystem_users.models.LoginRequestDto;
import com.example.librarymanagementsystem_users.models.UserResponseDto;
import com.example.librarymanagementsystem_users.reotrfit.RetrofitService;
import com.example.librarymanagementsystem_users.reotrfit.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView welcomeText, noAccountText;
    EditText etUsername, etPassword;
    Button loginButton, skipLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Connect views
        welcomeText = findViewById(R.id.welcomeText);
        noAccountText = findViewById(R.id.noAccountText);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.loginButton);
        skipLoginButton = findViewById(R.id.skipLoginButton);

        // Login button click
        loginButton.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create login request using the constructor
            LoginRequestDto loginRequest = new LoginRequestDto(username, password);

            // Call API
            UserApi userApi = RetrofitService.getUserApi();
            userApi.login(loginRequest).enqueue(new Callback<UserResponseDto>() {
                @Override
                public void onResponse(Call<UserResponseDto> call, Response<UserResponseDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserResponseDto user = response.body();
                        Toast.makeText(LoginActivity.this, "Welcome " + user.getUsername(), Toast.LENGTH_SHORT).show();

                        // Pass USER_ID to HomeActivity
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("USER_ID", user.getId());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserResponseDto> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        // Click "No account? Sign up"
        noAccountText.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

        // Skip login
        skipLoginButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("USER_ID", 0L); // guest user
            startActivity(intent);
            finish();
        });
    }
}
