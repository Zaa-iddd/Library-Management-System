package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Animation topAnim, bottomAnim, middleAnim;

    View wave;
    TextView welcomeText, welcomeText2;
    Button loginButton, signupButton;
    ImageButton googleButton, facebookButton;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        // Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        middleAnim = AnimationUtils.loadAnimation(this, R.anim.middle_animation);

        // Views
        wave = findViewById(R.id.view);
        welcomeText = findViewById(R.id.welcomeText);
        welcomeText2 = findViewById(R.id.welcomeText2);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        googleButton = findViewById(R.id.googleButton);
        facebookButton = findViewById(R.id.facebookButton);

        // Set animations
        wave.setAnimation(topAnim);
        welcomeText.setAnimation(middleAnim);
        loginButton.setAnimation(bottomAnim);
        signupButton.setAnimation(bottomAnim);
        welcomeText2.setAnimation(bottomAnim);
        googleButton.setAnimation(bottomAnim);
        facebookButton.setAnimation(bottomAnim);

        // Button click listeners
        signupButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SignupActivity.class)));
        loginButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));


    }

}