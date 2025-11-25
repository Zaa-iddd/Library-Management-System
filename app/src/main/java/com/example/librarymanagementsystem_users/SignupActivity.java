package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagementsystem_users.models.UserRequestDto;
import com.example.librarymanagementsystem_users.models.UserResponseDto;
import com.example.librarymanagementsystem_users.reotrfit.RetrofitService;
import com.example.librarymanagementsystem_users.reotrfit.UserApi;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private EditText etUsername, etPassword, etConfirmPassword, etEmail;
    private Button signupButton;
    private TextView alreadyAccountText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        initializeComponents();
    }

    private void initializeComponents() {
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        signupButton = findViewById(R.id.signupButton);
        alreadyAccountText = findViewById(R.id.alreadyAccountText);
        progressBar = findViewById(R.id.progressBar);

        signupButton.setOnClickListener(v -> performSignup());

        alreadyAccountText.setOnClickListener(v -> 
            startActivity(new Intent(SignupActivity.this, LoginActivity.class)));
    }

    private void performSignup() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();



        // 1. Check if passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Check if any field is empty
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress and hide button
        signupButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        UserRequestDto userRequestDto = new UserRequestDto(username, email, password);
        UserApi userApi = RetrofitService.getUserApi();

        userApi.save(userRequestDto).enqueue(new Callback<UserResponseDto>() {
            @Override
            public void onResponse(Call<UserResponseDto> call, Response<UserResponseDto> response) {
                // Hide progress and show button
                signupButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "Signup Successful!  Please login.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    finish();
                } else {
                    String errorMessage = "Signup Failed.";
                    try {
                        if (response.errorBody() != null) {
                            errorMessage = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error parsing error body", e);
                    }
                    Toast.makeText(SignupActivity.this, "Signup Failed: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponseDto> call, Throwable t) {
                // Hide progress kina show button
                signupButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                Log.e(TAG, "Network Failure", t);
                Toast.makeText(SignupActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
