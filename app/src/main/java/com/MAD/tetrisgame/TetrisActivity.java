package com.MAD.tetrisgame;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.content.Context;

public class TetrisActivity extends AppCompatActivity {

    private GameView gameView;
    private GameEngine gameEngine;
    private Handler handler = new Handler();
    private static final long UPDATE_DELAY = 500;
    private TextView scoreText;
    private TextView levelText;
    private boolean isPaused = false;
    private com.google.android.material.button.MaterialButton buttonPause;
    private com.google.android.material.button.MaterialButton buttonHardDrop;
    private com.google.android.material.button.MaterialButton difficultyButton;
    private TextView gameOverText;
    private TextView highScoreText;
    private NextPieceView nextPieceView;
    private static final String PREF_HIGH_SCORE = "high_score";
    private int currentLevel = 1;
    private String currentDifficulty = "Easy";
    private long currentUpdateDelay = 500; // Base delay for Easy difficulty

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            gameView = findViewById(R.id.gameView);
            gameEngine = new GameEngine();
            gameView.setGameEngine(gameEngine);
            scoreText = findViewById(R.id.scoreText);
            levelText = findViewById(R.id.levelText);
            highScoreText = findViewById(R.id.highScoreText);
            nextPieceView = findViewById(R.id.nextPieceView);

            loadHighScore();
            updateNextPiece(); // Show initial next piece
        } catch (Exception e) {
            e.printStackTrace();
            // Handle initialization error gracefully
        }

        com.google.android.material.button.MaterialButton buttonLeft = findViewById(R.id.button_left);
        com.google.android.material.button.MaterialButton buttonRotate = findViewById(R.id.button_rotate);
        com.google.android.material.button.MaterialButton buttonRight = findViewById(R.id.button_right);
        com.google.android.material.button.MaterialButton buttonReset = findViewById(R.id.button_reset);
        buttonPause = findViewById(R.id.button_pause);
        buttonHardDrop = findViewById(R.id.button_hard_drop);
        difficultyButton = findViewById(R.id.difficultyButton);

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameEngine.movePieceLeft();
                gameView.invalidate();
            }
        });

        buttonRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameEngine.rotatePiece();
                gameView.invalidate();
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameEngine.movePieceRight();
                gameView.invalidate();
            }
        });

        // Add the click listener for the reset button
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Restart the activity to reset the game
                finish();
                startActivity(getIntent());
            }
        });

        // Hard drop button
        buttonHardDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPaused && !gameEngine.isGameOver()) {
                    // Drop piece to bottom instantly
                    while (gameEngine.movePieceDown()) {
                        // Keep dropping until it can't move further
                    }
                    gameView.invalidate();
                }
            }
        });

        // Difficulty button
        difficultyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentDifficulty) {
                    case "Easy":
                        currentDifficulty = "Medium";
                        currentUpdateDelay = 300;
                        difficultyButton.setText("Medium");
                        break;
                    case "Medium":
                        currentDifficulty = "Hard";
                        currentUpdateDelay = 150;
                        difficultyButton.setText("Hard");
                        break;
                    case "Hard":
                        currentDifficulty = "Easy";
                        currentUpdateDelay = 500;
                        difficultyButton.setText("Easy");
                        break;
                }
            }
        });

        handler.post(updateGame);

        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = !isPaused; // Toggle the pause state
                if (isPaused) {
                    buttonPause.setText("Play");
                    handler.removeCallbacks(updateGame); // Stop the game loop
                } else {
                    buttonPause.setText("Pause");
                    handler.post(updateGame); // Resume the game loop
                }
            }
        });

        handler.post(updateGame);
    }

    private Runnable updateGame = new Runnable() {
        @Override
        public void run() {
            try {
                if (gameEngine != null && !gameEngine.isGameOver()) {
                    gameEngine.movePieceDown();
                    updateScoreDisplay();
                    updateLevel();
                    updateNextPiece();
                    if (gameView != null) {
                        gameView.invalidate();
                    }
                    handler.postDelayed(this, currentUpdateDelay);
                } else if (gameEngine != null) {
                    if (gameView != null) {
                        gameView.setGameOver(true);
                        gameView.invalidate();
                    }
                    ScoreManager.saveScore("Tetris", gameEngine.getScore());
                    // Check and save the new high score
                    saveHighScore(gameEngine.getScore());
                    handler.removeCallbacks(this);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Handle game loop error gracefully
                handler.removeCallbacks(this);
            }
        }
    };
    
    private void updateScoreDisplay() {
        scoreText.setText(String.valueOf(gameEngine.getScore()));
    }
    
    private void updateLevel() {
        int score = gameEngine.getScore();
        int newLevel = (score / 1000) + 1; // Level up every 1000 points
        if (newLevel > currentLevel) {
            currentLevel = newLevel;
            levelText.setText(String.valueOf(currentLevel));
            // Increase speed as level increases
            currentUpdateDelay = Math.max(50, 500 - (currentLevel * 30));
        }
    }
    
    private void updateNextPiece() {
        try {
            if (nextPieceView != null && gameEngine != null && gameEngine.getNextPiece() != null) {
                nextPieceView.setNextPiece(gameEngine.getNextPiece());
            }
        } catch (Exception e) {
            // Handle any potential errors gracefully
            e.printStackTrace();
        }
    }

    // --- New Methods for High Score ---
    private void loadHighScore() {
        SharedPreferences sharedPref = getSharedPreferences(PREF_HIGH_SCORE, Context.MODE_PRIVATE);
        int highScore = sharedPref.getInt(PREF_HIGH_SCORE, 0); // Default to 0 if no score is saved
        highScoreText.setText(String.valueOf(highScore));
    }

    private void saveHighScore(int currentScore) {
        SharedPreferences sharedPref = getSharedPreferences(PREF_HIGH_SCORE, Context.MODE_PRIVATE);
        int currentHighScore = sharedPref.getInt(PREF_HIGH_SCORE, 0);

        if (currentScore > currentHighScore) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(PREF_HIGH_SCORE, currentScore);
            editor.apply(); // Use apply() for a non-blocking save
            highScoreText.setText(String.valueOf(currentScore));
        }
    }
}