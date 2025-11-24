package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagementsystem_users.api.RetrofitClient;
import com.example.librarymanagementsystem_users.models.SignupRequestDto;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    TextView alreadyAccountText;
    EditText etUsername, etPassword, etConfirmPassword, etEmail;
    Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // Connect views
        alreadyAccountText = findViewById(R.id.alreadyAccountText);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        signupButton = findViewById(R.id.signupButton);

        // signup button click
        signupButton.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirm = etConfirmPassword.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirm)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            signupUser(username, email, password);
        });

        alreadyAccountText.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });
    }

    private void signupUser(String username, String email, String password) {
        SignupRequestDto signupRequest = new SignupRequestDto(username, email, password);

        RetrofitClient.getApiService().signup(signupRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Signup successful
                    Toast.makeText(SignupActivity.this, "Signup Successful! Please login.", Toast.LENGTH_SHORT).show();

                    // Navigate to the Login screen
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Finish SignupActivity so the user can't go back to it
                } else {
                    // Signup failed. Log the error and show a more detailed message.
                    String errorMessage = "Signup Failed. Please try again.";
                    if (response.errorBody() != null) {
                        try {
                            // Read the error body from the server response.
                            errorMessage = response.errorBody().string();
                            Log.e(TAG, "Signup failed with error body: " + errorMessage);
                        } catch (IOException e) {
                            Log.e(TAG, "Error parsing error body", e);
                        }
                    }
                    Toast.makeText(SignupActivity.this, "Signup Failed: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Network error
                Log.e(TAG, "Network Error: " + t.getMessage(), t);
                Toast.makeText(SignupActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
