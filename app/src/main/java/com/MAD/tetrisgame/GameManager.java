package com.MAD.tetrisgame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.MAD.tetrisgame.LeaderboardActivity;

public class GameManager extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        if (currentUser != null) {
            welcomeTextView.setText("Welcome, " + currentUser.getEmail() + "!");
        }

        // Get card views instead of buttons
        com.google.android.material.card.MaterialCardView tetrisCard = findViewById(R.id.tetrisCard);
        com.google.android.material.card.MaterialCardView ticTacToeCard = findViewById(R.id.ticTacToeCard);
        com.google.android.material.card.MaterialCardView clickerCard = findViewById(R.id.clickerCard);
        com.google.android.material.card.MaterialCardView snakeCard = findViewById(R.id.snakeCard);
        com.google.android.material.card.MaterialCardView memoryCard = findViewById(R.id.memoryCard);
        com.google.android.material.card.MaterialCardView leaderboardCard = findViewById(R.id.leaderboardCard);
        com.google.android.material.button.MaterialButton logoutButton = findViewById(R.id.logoutButton);

        tetrisCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Tetris game activity
                startActivity(new Intent(GameManager.this, TetrisActivity.class));
            }
        });

        ticTacToeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Tic-Tac-Toe game activity
                startActivity(new Intent(GameManager.this, TicTacToeActivity.class));
            }
        });

        clickerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Clicker game activity
                startActivity(new Intent(GameManager.this, ClickerGameActivity.class));
            }
        });

        snakeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Snake game activity
                startActivity(new Intent(GameManager.this, SnakeGameActivity.class));
            }
        });

        memoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Memory Match game activity
                startActivity(new Intent(GameManager.this, MemoryMatchActivity.class));
            }
        });

        leaderboardCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Leaderboard activity
                startActivity(new Intent(GameManager.this, LeaderboardActivity.class));
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(GameManager.this, LoginActivity.class));
                finish();
            }
        });
    }
}