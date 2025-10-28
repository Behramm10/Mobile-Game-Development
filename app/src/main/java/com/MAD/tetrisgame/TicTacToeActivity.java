package com.MAD.tetrisgame;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TicTacToeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int roundCount;
    private boolean isAIMode = false;
    private String difficulty = "Easy";
    private int player1Wins = 0;
    private int player2Wins = 0;
    private int draws = 0;

    private TextView playerTurnTextView;
    private TextView scoreTextView;
    private com.google.android.material.button.MaterialButton modeButton;
    private com.google.android.material.button.MaterialButton difficultyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        playerTurnTextView = findViewById(R.id.playerTurnTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        modeButton = findViewById(R.id.modeButton);
        difficultyButton = findViewById(R.id.difficultyButton);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        // Mode button (Player vs Player / Player vs AI)
        modeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAIMode = !isAIMode;
                modeButton.setText(isAIMode ? "vs AI" : "vs Player");
                resetGame();
            }
        });

        // Difficulty button (only visible in AI mode)
        difficultyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (difficulty) {
                    case "Easy":
                        difficulty = "Medium";
                        difficultyButton.setText("Medium");
                        break;
                    case "Medium":
                        difficulty = "Hard";
                        difficultyButton.setText("Hard");
                        break;
                    case "Hard":
                        difficulty = "Easy";
                        difficultyButton.setText("Easy");
                        break;
                }
            }
        });

        updateScoreDisplay();
    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (player1Turn) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
        }

        roundCount++;

        if (checkForWin()) {
            if (player1Turn) {
                player1Wins++;
                Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
                ScoreManager.saveScore("TicTacToe", 1);
            } else {
                if (isAIMode) {
                    Toast.makeText(this, "AI wins!", Toast.LENGTH_SHORT).show();
                } else {
                    player2Wins++;
                    Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
                }
                ScoreManager.saveScore("TicTacToe", 1);
            }
            disableButtons();
            updateScoreDisplay();
        } else if (roundCount == 9) {
            draws++;
            Toast.makeText(this, "It's a draw!", Toast.LENGTH_SHORT).show();
            updateScoreDisplay();
        } else {
            player1Turn = !player1Turn;
            updateTurnText();
            
            // AI move if it's AI mode and AI's turn
            if (isAIMode && !player1Turn && !checkForWin() && roundCount < 9) {
                makeAIMove();
            }
        }
    }
    private boolean checkForWin() {

        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")) {
                return true;
            }
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")) {
            return true;
        }
        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void disableButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    private void resetGame() {
        roundCount = 0;
        player1Turn = true;
        updateTurnText();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
    }

    private void updateTurnText() {
        if (player1Turn) {
            playerTurnTextView.setText("Player 1's Turn");
        } else {
            if (isAIMode) {
                playerTurnTextView.setText("AI's Turn");
            } else {
                playerTurnTextView.setText("Player 2's Turn");
            }
        }
    }
    
    private void updateScoreDisplay() {
        scoreTextView.setText("Player 1: " + player1Wins + " | " + 
                            (isAIMode ? "AI" : "Player 2") + ": " + player2Wins + " | Draws: " + draws);
    }
    
    private void makeAIMove() {
        // Simple AI implementation with different difficulty levels
        int[] move = getBestMove();
        if (move[0] != -1 && move[1] != -1) {
            buttons[move[0]][move[1]].performClick();
        }
    }
    
    private int[] getBestMove() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        
        // Try to win first
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j].equals("")) {
                    field[i][j] = "O";
                    if (checkWinForPlayer(field, "O")) {
                        field[i][j] = "";
                        return new int[]{i, j};
                    }
                    field[i][j] = "";
                }
            }
        }
        
        // Try to block player
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j].equals("")) {
                    field[i][j] = "X";
                    if (checkWinForPlayer(field, "X")) {
                        field[i][j] = "";
                        return new int[]{i, j};
                    }
                    field[i][j] = "";
                }
            }
        }
        
        // Use minimax for hard difficulty
        if (difficulty.equals("Hard")) {
            return minimax(field, true);
        }
        
        // Random move for easy/medium
        return getRandomMove(field);
    }
    
    private int[] minimax(String[][] field, boolean isMaximizing) {
        String winner = checkWinner(field);
        if (winner != null) {
            if (winner.equals("O")) return new int[]{0, 0, 1}; // AI wins
            if (winner.equals("X")) return new int[]{0, 0, -1}; // Player wins
            return new int[]{0, 0, 0}; // Draw
        }
        
        int[] bestMove = new int[]{-1, -1, isMaximizing ? -1000 : 1000};
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j].equals("")) {
                    field[i][j] = isMaximizing ? "O" : "X";
                    int[] score = minimax(field, !isMaximizing);
                    field[i][j] = "";
                    
                    if (isMaximizing && score[2] > bestMove[2]) {
                        bestMove = new int[]{i, j, score[2]};
                    } else if (!isMaximizing && score[2] < bestMove[2]) {
                        bestMove = new int[]{i, j, score[2]};
                    }
                }
            }
        }
        
        return bestMove;
    }
    
    private int[] getRandomMove(String[][] field) {
        java.util.List<int[]> emptyCells = new java.util.ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j].equals("")) {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }
        
        if (!emptyCells.isEmpty()) {
            java.util.Random random = new java.util.Random();
            return emptyCells.get(random.nextInt(emptyCells.size()));
        }
        
        return new int[]{-1, -1};
    }
    
    private String checkWinner(String[][] field) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (!field[i][0].equals("") && field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2])) {
                return field[i][0];
            }
        }
        
        // Check columns
        for (int j = 0; j < 3; j++) {
            if (!field[0][j].equals("") && field[0][j].equals(field[1][j]) && field[0][j].equals(field[2][j])) {
                return field[0][j];
            }
        }
        
        // Check diagonals
        if (!field[0][0].equals("") && field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2])) {
            return field[0][0];
        }
        if (!field[0][2].equals("") && field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0])) {
            return field[0][2];
        }
        
        return null;
    }
    
    private boolean checkWinForPlayer(String[][] field, String player) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(player) && field[i][1].equals(player) && field[i][2].equals(player)) {
                return true;
            }
        }
        
        // Check columns
        for (int j = 0; j < 3; j++) {
            if (field[0][j].equals(player) && field[1][j].equals(player) && field[2][j].equals(player)) {
                return true;
            }
        }
        
        // Check diagonals
        if (field[0][0].equals(player) && field[1][1].equals(player) && field[2][2].equals(player)) {
            return true;
        }
        if (field[0][2].equals(player) && field[1][1].equals(player) && field[2][0].equals(player)) {
            return true;
        }
        
        return false;
    }
}