package com.example.librarymanagementsystem_users;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton createButton;
    private MaterialButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Set the initial layout
        setAndSetupLoginLayout();
    }

    private void setAndSetupLoginLayout() {
        setContentView(R.layout.login_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginButton = findViewById(R.id.loginButton); // Corrected ID from your layout
        createButton = findViewById(R.id.create_button); // Corrected to the create button ID

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle login logic here
                Toast.makeText(MainActivity.this, "Login Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to the create account layout
                setAndSetupCreateAccountLayout();
            }
        });
    }

    private void setAndSetupCreateAccountLayout() {
        setContentView(R.layout.create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Make sure your create_account.xml has a button with the id "loginButton" to go back
        MaterialButton loginFromCreateButton = findViewById(R.id.loginButton); 

        loginFromCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch back to the login layout
                setAndSetupLoginLayout();
            }
        });
    }
}
