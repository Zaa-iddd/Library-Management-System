package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    Animation topAnim, middleAnim, bottomAnim;
    View wave;
    TextView appTitle, welcomeText, noAccountText;
    EditText etUsername, etPassword;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // load animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        middleAnim = AnimationUtils.loadAnimation(this, R.anim.middle_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        // Connect views
        wave = findViewById(R.id.view);
        appTitle = findViewById(R.id.appTitle);
        welcomeText = findViewById(R.id.welcomeText);
        noAccountText = findViewById(R.id.noAccountText);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.loginButton);

        //  animations
        wave.setAnimation(topAnim);
        appTitle.setAnimation(topAnim);
        welcomeText.setAnimation(middleAnim);
        etUsername.setAnimation(middleAnim);
        etPassword.setAnimation(middleAnim);
        loginButton.setAnimation(bottomAnim);
        noAccountText.setAnimation(bottomAnim);

        // ogin button click
        loginButton.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: add pa ki fb pang navi lang

            startActivity(new Intent(LoginActivity.this, MainDashActivity.class));
        });

        // para diresto ki sigup
        noAccountText.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }
}
