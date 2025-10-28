package com.MAD.tetrisgame;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;

public class SnakeGameActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    
    private SnakeView snakeView;
    private SnakeEngine snakeEngine;
    private Handler handler = new Handler();
    private static final long UPDATE_DELAY = 200; // Snake moves every 200ms
    private GestureDetector gestureDetector;
    
    private TextView scoreText;
    private TextView highScoreText;
    private MaterialButton startButton;
    private MaterialButton resetButton;
    private MaterialButton pauseButton;
    
    private boolean isPaused = false;
    private static final String PREF_SNAKE_HIGH_SCORE = "snake_high_score";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake_game);
        
        snakeView = findViewById(R.id.snakeView);
        scoreText = findViewById(R.id.scoreText);
        highScoreText = findViewById(R.id.highScoreText);
        startButton = findViewById(R.id.startButton);
        resetButton = findViewById(R.id.resetButton);
        pauseButton = findViewById(R.id.pauseButton);
        
        snakeEngine = new SnakeEngine();
        snakeView.setSnakeEngine(snakeEngine);
        
        gestureDetector = new GestureDetector(this, this);
        
        loadHighScore();
        setupButtons();
    }
    
    private void setupButtons() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!snakeEngine.isGameStarted()) {
                    snakeEngine.startGame();
                    startButton.setVisibility(View.GONE);
                    pauseButton.setVisibility(View.VISIBLE);
                    handler.post(updateGame);
                }
            }
        });
        
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
        
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (snakeEngine.isGameStarted() && !snakeEngine.isGameOver()) {
                    isPaused = !isPaused;
                    if (isPaused) {
                        pauseButton.setText("Resume");
                        handler.removeCallbacks(updateGame);
                    } else {
                        pauseButton.setText("Pause");
                        handler.post(updateGame);
                    }
                }
            }
        });
    }
    
    private Runnable updateGame = new Runnable() {
        @Override
        public void run() {
            if (snakeEngine.isGameStarted() && !snakeEngine.isGameOver() && !isPaused) {
                snakeEngine.update();
                updateScoreDisplay();
                snakeView.invalidate();
                
                if (snakeEngine.isGameOver()) {
                    gameOver();
                } else {
                    handler.postDelayed(this, UPDATE_DELAY);
                }
            }
        }
    };
    
    private void updateScoreDisplay() {
        scoreText.setText("Score: " + snakeEngine.getScore());
    }
    
    private void gameOver() {
        snakeView.setGameOver(true);
        snakeView.invalidate();
        pauseButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);
        startButton.setText("Play Again");
        
        // Save high score locally
        saveHighScore(snakeEngine.getScore());
        
        // Save score to Firebase leaderboard
        ScoreManager.saveScore("Snake", snakeEngine.getScore());
        
        Toast.makeText(this, "Game Over! Score: " + snakeEngine.getScore(), Toast.LENGTH_SHORT).show();
    }
    
    private void resetGame() {
        snakeEngine.resetGame();
        snakeView.setGameOver(false);
        snakeView.invalidate();
        startButton.setVisibility(View.VISIBLE);
        startButton.setText("Start Game");
        pauseButton.setVisibility(View.GONE);
        pauseButton.setText("Pause");
        isPaused = false;
        handler.removeCallbacks(updateGame);
        updateScoreDisplay();
    }
    
    private void loadHighScore() {
        int highScore = getSharedPreferences(PREF_SNAKE_HIGH_SCORE, MODE_PRIVATE)
                .getInt(PREF_SNAKE_HIGH_SCORE, 0);
        highScoreText.setText("Best: " + highScore);
    }
    
    private void saveHighScore(int currentScore) {
        int currentHighScore = getSharedPreferences(PREF_SNAKE_HIGH_SCORE, MODE_PRIVATE)
                .getInt(PREF_SNAKE_HIGH_SCORE, 0);
        
        if (currentScore > currentHighScore) {
            getSharedPreferences(PREF_SNAKE_HIGH_SCORE, MODE_PRIVATE)
                    .edit()
                    .putInt(PREF_SNAKE_HIGH_SCORE, currentScore)
                    .apply();
            highScoreText.setText("Best: " + currentScore);
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
    
    // GestureDetector methods
    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }
    
    @Override
    public void onShowPress(MotionEvent e) {}
    
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }
    
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }
    
    @Override
    public void onLongPress(MotionEvent e) {}
    
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (snakeEngine.isGameStarted() && !snakeEngine.isGameOver() && !isPaused) {
            float absVelocityX = Math.abs(velocityX);
            float absVelocityY = Math.abs(velocityY);
            
            if (absVelocityX > absVelocityY) {
                // Horizontal swipe
                if (velocityX > 0) {
                    snakeEngine.setDirection(SnakeEngine.Direction.RIGHT);
                } else {
                    snakeEngine.setDirection(SnakeEngine.Direction.LEFT);
                }
            } else {
                // Vertical swipe
                if (velocityY > 0) {
                    snakeEngine.setDirection(SnakeEngine.Direction.DOWN);
                } else {
                    snakeEngine.setDirection(SnakeEngine.Direction.UP);
                }
            }
        }
        return true;
    }
}
