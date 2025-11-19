package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    Animation topAnim, middleAnim, bottomAnim;

    View wave;
    TextView appTitle, welcomeText, alreadyAccountText;
    EditText etUsername, etPassword, etConfirmPassword, etEmail;
    Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // load animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        middleAnim = AnimationUtils.loadAnimation(this, R.anim.middle_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        // Connect views
        wave = findViewById(R.id.view);
        appTitle = findViewById(R.id.appTitle);
        welcomeText = findViewById(R.id.welcomeText);
        alreadyAccountText = findViewById(R.id.alreadyAccountText);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        signupButton = findViewById(R.id.signupButton);

        //  animations
        wave.setAnimation(topAnim);
        appTitle.setAnimation(topAnim);
        welcomeText.setAnimation(middleAnim);
        etUsername.setAnimation(middleAnim);
        etEmail.setAnimation(middleAnim);
        etPassword.setAnimation(middleAnim);
        etConfirmPassword.setAnimation(middleAnim);
        signupButton.setAnimation(bottomAnim);
        alreadyAccountText.setAnimation(bottomAnim);

        // signup button click
        signupButton.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirm = etConfirmPassword.getText().toString().trim();

            if(username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                android.widget.Toast.makeText(this, "Fill all fields", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            if(!password.equals(confirm)) {
                android.widget.Toast.makeText(this, "Passwords do not match", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            android.widget.Toast.makeText(this, "Sign Up clicked!", android.widget.Toast.LENGTH_SHORT).show();
        });


        alreadyAccountText.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });
    }
}
