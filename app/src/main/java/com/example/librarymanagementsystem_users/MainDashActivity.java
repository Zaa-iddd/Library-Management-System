package com.example.librarymanagementsystem_users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainDashActivity extends AppCompatActivity {
    Button btnAction, btnRomance, btnComedy, btnHorror, btnThriller, btnAll;
    CardView profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dash);

        btnAll = findViewById(R.id.btnAll);
        btnAction = findViewById(R.id.btnAction);
        btnRomance = findViewById(R.id.btnRomance);
        btnComedy = findViewById(R.id.btnComedy);
        btnHorror = findViewById(R.id.btnHorror);
        btnThriller = findViewById(R.id.btnThriller);
        profileButton = findViewById(R.id.profileButton);

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainDashActivity.this, ProfileActivity.class);
            startActivity(intent);
        });


        View.OnClickListener genreClickListener = v -> {
            resetButtons();

            Button clicked = (Button) v;
            clicked.setBackgroundResource(R.drawable.button_selected);
        };

        btnAll.setOnClickListener(genreClickListener);
        btnAction.setOnClickListener(genreClickListener);
        btnRomance.setOnClickListener(genreClickListener);
        btnComedy.setOnClickListener(genreClickListener);
        btnHorror.setOnClickListener(genreClickListener);
        btnThriller.setOnClickListener(genreClickListener);
        btnAll.setOnClickListener(genreClickListener);
    }

    private void resetButtons() {
        Button[] buttons = {btnAll,btnAction, btnRomance, btnComedy, btnHorror, btnThriller};
        for (Button b : buttons) {
            b.setBackgroundResource(R.drawable.button);
        }
    }
}
