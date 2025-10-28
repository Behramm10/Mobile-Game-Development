package com.MAD.tetrisgame;
import java.util.Random;
public class GameEngine {

    // These constants define the size of our game board
    private static final int BOARD_WIDTH = 8;
    private static final int BOARD_HEIGHT = 19;

    // This 2D array represents our game board
    // A value of 0 means the cell is empty
    private int[][] gameBoard;

    private Piece currentPiece;
    private Piece nextPiece;
    private Random random = new Random();
    private int score = 0;
    private boolean isGameOver = false;
    public GameEngine() {
        // Initialize the game board with empty cells
        gameBoard = new int[BOARD_HEIGHT][BOARD_WIDTH];
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                gameBoard[row][col] = 0;
            }
        }
        spawnNextPiece();
        spawnNewPiece();
    }

    private void spawnNewPiece() {
        // Use the next piece as current piece
        if (nextPiece != null) {
            currentPiece = nextPiece;
            
            // Check if the new piece can fit at the starting position
            if (!canMoveTo(currentPiece.getX(), currentPiece.getY())) {
                isGameOver = true;
            }
        } else {
            // Fallback: create a new piece if nextPiece is null
            int pieceIndex = random.nextInt(Piece.SHAPES.length);
            currentPiece = new Piece(pieceIndex, Piece.COLORS[pieceIndex]);
        }
        
        // Generate new next piece
        spawnNextPiece();
    }
    
    private void spawnNextPiece() {
        int pieceIndex = random.nextInt(Piece.SHAPES.length);
        nextPiece = new Piece(pieceIndex, Piece.COLORS[pieceIndex]);
    }
    public Piece getCurrentPiece() {
        return currentPiece;
    }
    
    public Piece getNextPiece() {
        return nextPiece;
    }
    public boolean isGameOver() {
        return isGameOver;
    }
    // This is the core method for moving the piece down
    public int clearLines() {
        int linesCleared = 0;
        for (int row = BOARD_HEIGHT - 1; row >= 0; row--) {
            if (isRowFull(row)) {
                linesCleared++;
                clearRow(row);
                shiftRowsDown(row);
            }
        }
        score += calculateScore(linesCleared);
        return linesCleared;
    }

    private boolean isRowFull(int row) {
        for (int col = 0; col < BOARD_WIDTH; col++) {
            if (gameBoard[row][col] == 0) {
                return false;
            }
        }
        return true;
    }

    private void clearRow(int row) {
        for (int col = 0; col < BOARD_WIDTH; col++) {
            gameBoard[row][col] = 0;
        }
    }

    private void shiftRowsDown(int clearedRow) {
        for (int row = clearedRow - 1; row >= 0; row--) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                gameBoard[row + 1][col] = gameBoard[row][col];
                gameBoard[row][col] = 0;
            }
        }
    }

    private int calculateScore(int lines) {
        switch (lines) {
            case 1:
                return 100;
            case 2:
                return 300;
            case 3:
                return 500;
            case 4:
                return 800; // Tetris!
            default:
                return 0;
        }
    }

    public int getScore() {
        return score;
    }

    // You need to modify the movePieceDown method
    public boolean movePieceDown() {
        if (canMoveTo(currentPiece.getX(), currentPiece.getY() + 1)) {
            currentPiece.setY(currentPiece.getY() + 1);
            return true;
        } else {
            mergePieceToBoard();
            // Call the clearLines method after a piece lands
            clearLines();
            spawnNewPiece();
            return false;
        }
    }
    public void movePieceLeft() {
        if (currentPiece != null && canMoveTo(currentPiece.getX() - 1, currentPiece.getY())) {
            currentPiece.setX(currentPiece.getX() - 1);
        }
    }

    public void movePieceRight() {
        if (currentPiece != null && canMoveTo(currentPiece.getX() + 1, currentPiece.getY())) {
            currentPiece.setX(currentPiece.getX() + 1);
        }
    }

    public void rotatePiece() {
        if (currentPiece == null) return;
        
        int[][] currentShape = currentPiece.getShape();
        int rows = currentShape.length;
        int cols = currentShape[0].length;

        // Create a new array for the rotated shape
        int[][] newShape = new int[cols][rows];

        // Rotate the shape matrix
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                newShape[c][rows - 1 - r] = currentShape[r][c];
            }
        }

        // Check if the rotated shape is valid before applying it
        if (canMoveToWithShape(currentPiece.getX(), currentPiece.getY(), newShape)) {
            currentPiece.setShape(newShape);
        }
    }

    // A helper method for canMoveTo that takes a specific shape
    public boolean canMoveToWithShape(int newX, int newY, int[][] shape) {
        if (shape == null || shape.length == 0) return false;
        
        int pieceWidth = shape[0].length;
        int pieceHeight = shape.length;

        for (int row = 0; row < pieceHeight; row++) {
            for (int col = 0; col < pieceWidth; col++) {
                if (shape[row][col] == 1) {
                    int boardX = newX + col;
                    int boardY = newY + row;

                    // Strict boundary enforcement - pieces cannot go outside the 8x19 grid
                    if (boardX < 0 || boardX >= BOARD_WIDTH || boardY < 0 || boardY >= BOARD_HEIGHT) {
                        return false;
                    }
                    if (gameBoard[boardY][boardX] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    // Checks if the piece can move to a given position without collision
    // Enforces strict 8x19 boundary - pieces cannot go outside these limits
    public boolean canMoveTo(int newX, int newY) {
        if (currentPiece == null) return false;
        
        int[][] pieceShape = currentPiece.getShape();
        int pieceWidth = pieceShape[0].length;
        int pieceHeight = pieceShape.length;

        for (int row = 0; row < pieceHeight; row++) {
            for (int col = 0; col < pieceWidth; col++) {
                if (pieceShape[row][col] == 1) {
                    int boardX = newX + col;
                    int boardY = newY + row;

                    // Strict boundary enforcement - pieces cannot go outside the 8x19 grid
                    if (boardX < 0 || boardX >= BOARD_WIDTH || boardY < 0 || boardY >= BOARD_HEIGHT) {
                        return false;
                    }
                    // Check for collision with an already placed block on the board
                    if (gameBoard[boardY][boardX] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // Merges the current piece's blocks into the game board
    private void mergePieceToBoard() {
        int[][] pieceShape = currentPiece.getShape();
        for (int row = 0; row < pieceShape.length; row++) {
            for (int col = 0; col < pieceShape[row].length; col++) {
                if (pieceShape[row][col] == 1) {
                    gameBoard[currentPiece.getY() + row][currentPiece.getX() + col] = currentPiece.getColor();
                }
            }
        }
    }

    // This method gives other parts of the app access to the game board

    public int[][] getGameBoard() {
        return gameBoard;
    }

    // Getter methods for the board dimensions
    public int getBoardWidth() {
        return BOARD_WIDTH;
    }

    public int getBoardHeight() {
        return BOARD_HEIGHT;
    }
}