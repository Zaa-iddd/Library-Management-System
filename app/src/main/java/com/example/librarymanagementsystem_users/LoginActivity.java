package com.example.librarymanagementsystem_users;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagementsystem_users.api.RetrofitClient;
import com.example.librarymanagementsystem_users.models.LoginRequestDto;
import com.example.librarymanagementsystem_users.models.LoginResponseDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView welcomeText, noAccountText;
    EditText etUsername, etPassword;
    Button loginButton;

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

        // login button click
        loginButton.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(username, password);
        });

        // para diresto ki sigup
        noAccountText.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }

    private void loginUser(String username, String password) {
        LoginRequestDto loginRequest = new LoginRequestDto(username, password);

        RetrofitClient.getApiService().login(loginRequest).enqueue(new Callback<LoginResponseDto>() {
            @Override
            public void onResponse(Call<LoginResponseDto> call, Response<LoginResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Login successful
                    LoginResponseDto loginResponse = response.body();
                    saveAuthToken(loginResponse.getToken());

                    Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                    // Navigate to the main dashboard
                    Intent intent = new Intent(LoginActivity.this, MainDashActivity.class);
                    startActivity(intent);
                    finish(); // Finish LoginActivity so the user can't go back to it
                } else {
                    // Login failed
                    Toast.makeText(LoginActivity.this, "Login Failed: Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponseDto> call, Throwable t) {
                // Network error
                Toast.makeText(LoginActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAuthToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("auth_token", token);
        editor.apply();
    }
}
