package com.MAD.tetrisgame;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LeaderboardAdapter adapter;
    private FirebaseFirestore db;
    private List<QueryDocumentSnapshot> scoreList = new ArrayList<>();
    private String currentGame = "Tetris";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.leaderboardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Find the new buttons
        com.google.android.material.button.MaterialButton tetrisButton = findViewById(R.id.tetrisButton);
        com.google.android.material.button.MaterialButton ticTacToeButton = findViewById(R.id.ticTacToeButton);
        com.google.android.material.button.MaterialButton clickerGameButton = findViewById(R.id.clickerGameButton);
        com.google.android.material.button.MaterialButton snakeButton = findViewById(R.id.snakeButton);
        com.google.android.material.button.MaterialButton memoryButton = findViewById(R.id.memoryButton);
        
        // Setup SwipeRefreshLayout
        androidx.swiperefreshlayout.widget.SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchScores(currentGame);
        });

        // Add listeners to the buttons
        tetrisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentGame = "Tetris";
                updateButtonSelection(tetrisButton);
                fetchScores("Tetris");
            }
        });

        ticTacToeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentGame = "TicTacToe";
                updateButtonSelection(ticTacToeButton);
                fetchScores("TicTacToe");
            }
        });

        clickerGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentGame = "ClickerGame";
                updateButtonSelection(clickerGameButton);
                fetchScores("ClickerGame");
            }
        });

        snakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentGame = "Snake";
                updateButtonSelection(snakeButton);
                fetchScores("Snake");
            }
        });

        memoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentGame = "MemoryMatch";
                updateButtonSelection(memoryButton);
                fetchScores("MemoryMatch");
            }
        });

        // Load the initial scores for the Tetris leaderboard
        currentGame = "Tetris";
        updateButtonSelection(tetrisButton);
        fetchScores("Tetris");
    }

    private void fetchScores(String gameName) {
        db.collection("scores")
                .whereEqualTo("gameName", gameName)
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        scoreList.clear();
                        if (task.getResult() != null) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                scoreList.add(document);
                            }
                        }
                        adapter = new LeaderboardAdapter(scoreList);
                        recyclerView.setAdapter(adapter);
                        
                        // Stop refresh animation
                        androidx.swiperefreshlayout.widget.SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        Toast.makeText(this, "Error getting scores.", Toast.LENGTH_SHORT).show();
                        androidx.swiperefreshlayout.widget.SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }
    
    private void updateButtonSelection(com.google.android.material.button.MaterialButton selectedButton) {
        // Reset all buttons to outlined style
        com.google.android.material.button.MaterialButton tetrisButton = findViewById(R.id.tetrisButton);
        com.google.android.material.button.MaterialButton ticTacToeButton = findViewById(R.id.ticTacToeButton);
        com.google.android.material.button.MaterialButton clickerGameButton = findViewById(R.id.clickerGameButton);
        com.google.android.material.button.MaterialButton snakeButton = findViewById(R.id.snakeButton);
        com.google.android.material.button.MaterialButton memoryButton = findViewById(R.id.memoryButton);
        
        // Reset all buttons to outlined style
        tetrisButton.setBackgroundTintList(getColorStateList(android.R.color.transparent));
        ticTacToeButton.setBackgroundTintList(getColorStateList(android.R.color.transparent));
        clickerGameButton.setBackgroundTintList(getColorStateList(android.R.color.transparent));
        snakeButton.setBackgroundTintList(getColorStateList(android.R.color.transparent));
        memoryButton.setBackgroundTintList(getColorStateList(android.R.color.transparent));
        
        // Set selected button to filled style
        selectedButton.setBackgroundTintList(getColorStateList(R.color.primary));
    }
}