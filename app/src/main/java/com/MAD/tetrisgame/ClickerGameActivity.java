package com.MAD.tetrisgame;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ClickerGameActivity extends AppCompatActivity {

    private TextView scoreTextView, timerTextView, comboTextView, cpsTextView;
    private com.google.android.material.button.MaterialButton clickButton;
    private com.google.android.material.button.MaterialButton difficultyButton;
    private int score = 0;
    private int combo = 0;
    private int maxCombo = 0;
    private int clicks = 0;
    private long startTime = 0;
    private boolean isGameActive = false;
    private String currentDifficulty = "Easy";
    private int gameTimeSeconds = 10;
    private long lastClickTime = 0;
    private static final long COMBO_TIMEOUT = 1000; // 1 second to maintain combo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicker_game);

        scoreTextView = findViewById(R.id.scoreTextView);
        timerTextView = findViewById(R.id.timerTextView);
        comboTextView = findViewById(R.id.comboTextView);
        cpsTextView = findViewById(R.id.cpsTextView);
        clickButton = findViewById(R.id.clickButton);
        difficultyButton = findViewById(R.id.difficultyButton);

        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGameActive) {
                    startGame();
                }
                handleClick();
            }
        });

        difficultyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentDifficulty) {
                    case "Easy":
                        currentDifficulty = "Medium";
                        gameTimeSeconds = 15;
                        difficultyButton.setText("Medium");
                        break;
                    case "Medium":
                        currentDifficulty = "Hard";
                        gameTimeSeconds = 20;
                        difficultyButton.setText("Hard");
                        break;
                    case "Hard":
                        currentDifficulty = "Easy";
                        gameTimeSeconds = 10;
                        difficultyButton.setText("Easy");
                        break;
                }
            }
        });
    }

    private void startGame() {
        isGameActive = true;
        score = 0;
        combo = 0;
        maxCombo = 0;
        clicks = 0;
        startTime = System.currentTimeMillis();
        lastClickTime = 0;
        
        updateDisplay();
        
        new CountDownTimer(gameTimeSeconds * 1000, 100) {
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Time: " + millisUntilFinished / 1000);
                
                // Check combo timeout
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastClickTime > COMBO_TIMEOUT && combo > 0) {
                    combo = 0;
                    updateDisplay();
                }
            }

            public void onFinish() {
                isGameActive = false;
                ScoreManager.saveScore("ClickerGame", score);
                timerTextView.setText("Time's Up!");
                clickButton.setEnabled(false);
                
                // Calculate final CPS
                long totalTime = System.currentTimeMillis() - startTime;
                double cps = (double) clicks / (totalTime / 1000.0);
                
                Toast.makeText(ClickerGameActivity.this, 
                    "Game Over!\nScore: " + score + 
                    "\nMax Combo: " + maxCombo + 
                    "\nCPS: " + String.format("%.1f", cps), 
                    Toast.LENGTH_LONG).show();
            }
        }.start();
    }
    
    private void handleClick() {
        if (!isGameActive) return;
        
        long currentTime = System.currentTimeMillis();
        clicks++;
        
        // Check if within combo timeout
        if (currentTime - lastClickTime <= COMBO_TIMEOUT) {
            combo++;
        } else {
            combo = 1;
        }
        
        lastClickTime = currentTime;
        maxCombo = Math.max(maxCombo, combo);
        
        // Calculate score with combo multiplier
        int points = 1 + (combo / 5); // Bonus points for every 5 combo
        score += points;
        
        updateDisplay();
        
        // Animate button for visual feedback
        clickButton.animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .setDuration(50)
            .withEndAction(() -> {
                clickButton.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(50);
            });
    }
    
    private void updateDisplay() {
        scoreTextView.setText("Score: " + score);
        comboTextView.setText("Combo: " + combo);
        
        if (isGameActive && clicks > 0) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;
            double cps = (double) clicks / (elapsedTime / 1000.0);
            cpsTextView.setText("CPS: " + String.format("%.1f", cps));
        }
    }
}