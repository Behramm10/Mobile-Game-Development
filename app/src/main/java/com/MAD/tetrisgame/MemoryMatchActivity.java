package com.MAD.tetrisgame;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryMatchActivity extends AppCompatActivity implements MemoryCardAdapter.OnCardClickListener {
    
    private RecyclerView recyclerView;
    private MemoryCardAdapter adapter;
    private List<MemoryCard> cards;
    private List<MemoryCard> flippedCards;
    
    private TextView scoreText;
    private TextView timeText;
    private TextView movesText;
    private MaterialButton resetButton;
    private MaterialButton difficultyButton;
    
    private int score = 0;
    private int moves = 0;
    private int matchedPairs = 0;
    private int totalPairs;
    private String currentDifficulty = "Easy";
    private int gridSize = 4; // 4x4 = 16 cards = 8 pairs
    private CountDownTimer timer;
    private long timeLeft = 0;
    
    // Simple emoji/icon resources for cards
    private int[] cardImages = {
        android.R.drawable.ic_menu_edit,
        android.R.drawable.ic_menu_agenda,
        android.R.drawable.ic_menu_camera,
        android.R.drawable.ic_menu_gallery,
        android.R.drawable.ic_menu_manage,
        android.R.drawable.ic_menu_myplaces,
        android.R.drawable.ic_menu_preferences,
        android.R.drawable.ic_menu_send,
        android.R.drawable.ic_menu_share,
        android.R.drawable.ic_menu_slideshow,
        android.R.drawable.ic_menu_sort_by_size,
        android.R.drawable.ic_menu_upload
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_match);
        
        recyclerView = findViewById(R.id.recyclerView);
        scoreText = findViewById(R.id.scoreText);
        timeText = findViewById(R.id.timeText);
        movesText = findViewById(R.id.movesText);
        resetButton = findViewById(R.id.resetButton);
        difficultyButton = findViewById(R.id.difficultyButton);
        
        flippedCards = new ArrayList<>();
        setupDifficulty();
        setupRecyclerView();
        setupButtons();
        startNewGame();
    }
    
    private void setupDifficulty() {
        difficultyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentDifficulty) {
                    case "Easy":
                        currentDifficulty = "Medium";
                        gridSize = 5; // 5x5 = 25 cards = 12 pairs + 1 extra
                        difficultyButton.setText("Medium");
                        break;
                    case "Medium":
                        currentDifficulty = "Hard";
                        gridSize = 6; // 6x6 = 36 cards = 18 pairs
                        difficultyButton.setText("Hard");
                        break;
                    case "Hard":
                        currentDifficulty = "Easy";
                        gridSize = 4; // 4x4 = 16 cards = 8 pairs
                        difficultyButton.setText("Easy");
                        break;
                }
                startNewGame();
            }
        });
    }
    
    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, gridSize);
        recyclerView.setLayoutManager(layoutManager);
    }
    
    private void setupButtons() {
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });
    }
    
    private void startNewGame() {
        if (timer != null) {
            timer.cancel();
        }
        
        score = 0;
        moves = 0;
        matchedPairs = 0;
        flippedCards.clear();
        
        // Set time based on difficulty
        switch (currentDifficulty) {
            case "Easy":
                timeLeft = 120000; // 2 minutes
                break;
            case "Medium":
                timeLeft = 180000; // 3 minutes
                break;
            case "Hard":
                timeLeft = 240000; // 4 minutes
                break;
        }
        
        createCards();
        setupRecyclerView();
        adapter = new MemoryCardAdapter(cards, this);
        recyclerView.setAdapter(adapter);
        
        updateDisplay();
        startTimer();
    }
    
    private void createCards() {
        cards = new ArrayList<>();
        totalPairs = (gridSize * gridSize) / 2;
        
        // Create pairs
        for (int i = 0; i < totalPairs; i++) {
            int imageIndex = i % cardImages.length;
            cards.add(new MemoryCard(i, cardImages[imageIndex], i * 2));
            cards.add(new MemoryCard(i, cardImages[imageIndex], i * 2 + 1));
        }
        
        // Shuffle cards
        Collections.shuffle(cards);
        
        // Update positions after shuffling
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setPosition(i);
        }
    }
    
    private void startTimer() {
        timer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateDisplay();
            }
            
            @Override
            public void onFinish() {
                gameOver();
            }
        }.start();
    }
    
    @Override
    public void onCardClick(int position) {
        MemoryCard card = cards.get(position);
        
        if (card.isFlipped() || card.isMatched() || flippedCards.size() >= 2) {
            return;
        }
        
        card.setFlipped(true);
        flippedCards.add(card);
        adapter.notifyItemChanged(position);
        
        if (flippedCards.size() == 2) {
            moves++;
            checkForMatch();
        }
        
        updateDisplay();
    }
    
    private void checkForMatch() {
        MemoryCard card1 = flippedCards.get(0);
        MemoryCard card2 = flippedCards.get(1);
        
        if (card1.getId() == card2.getId()) {
            // Match found
            card1.setMatched(true);
            card2.setMatched(true);
            matchedPairs++;
            score += 100;
            
            if (matchedPairs == totalPairs) {
                gameWon();
            }
        } else {
            // No match, flip cards back after delay
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    card1.setFlipped(false);
                    card2.setFlipped(false);
                    adapter.notifyItemChanged(card1.getPosition());
                    adapter.notifyItemChanged(card2.getPosition());
                }
            }, 1000);
        }
        
        flippedCards.clear();
    }
    
    private void gameWon() {
        if (timer != null) {
            timer.cancel();
        }
        
        int timeBonus = (int) (timeLeft / 1000) * 10;
        score += timeBonus;
        
        Toast.makeText(this, "Congratulations! You won!", Toast.LENGTH_LONG).show();
        ScoreManager.saveScore("MemoryMatch", score);
    }
    
    private void gameOver() {
        Toast.makeText(this, "Time's up! Game Over!", Toast.LENGTH_LONG).show();
        ScoreManager.saveScore("MemoryMatch", score);
    }
    
    private void updateDisplay() {
        scoreText.setText("Score: " + score);
        movesText.setText("Moves: " + moves);
        
        long minutes = timeLeft / 60000;
        long seconds = (timeLeft % 60000) / 1000;
        timeText.setText(String.format("Time: %02d:%02d", minutes, seconds));
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
