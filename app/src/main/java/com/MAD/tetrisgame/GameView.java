package com.MAD.tetrisgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {

    private Paint piecePaint; // Renamed for clarity
    private Paint gridPaint; // New Paint for grid lines
    private Paint borderPaint; // New Paint for the border
    private GameEngine gameEngine;
    private int cellSize;
    private int xOffset;
    private Paint textPaint;
    private boolean isGameOver = false;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        piecePaint = new Paint();
        piecePaint.setAntiAlias(true);
        
        gridPaint = new Paint();
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStrokeWidth(2f); // Thicker, more visible grid lines
        gridPaint.setAntiAlias(true);
        gridPaint.setAlpha(150); // Semi-transparent for subtle effect

        borderPaint = new Paint();
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStrokeWidth(8f); // Thick border
        borderPaint.setStyle(Paint.Style.STROKE); // Set to stroke for border
        borderPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(64); // Adjust size as needed
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(true);
        textPaint.setAntiAlias(true);
    }
    public void setGameOver(boolean isOver) {
        this.isGameOver = isOver;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (gameEngine != null) {
            // Account for thick border (8px on each side = 16px total)
            int borderPadding = 16;
            int availableWidth = w - borderPadding;
            int availableHeight = h - borderPadding;
            
            // Calculate cell size to fit the board with some padding
            int cellSizeByWidth = availableWidth / gameEngine.getBoardWidth();
            int cellSizeByHeight = availableHeight / gameEngine.getBoardHeight();
            
            // Use the smaller dimension to ensure the board fits completely
            cellSize = Math.min(cellSizeByWidth, cellSizeByHeight);
            
            // Ensure minimum cell size for visibility
            if (cellSize < 20) {
                cellSize = 20;
            }

            // Calculate the horizontal offset to center the board
            int boardWidthInPixels = gameEngine.getBoardWidth() * cellSize;
            xOffset = (w - boardWidthInPixels) / 2;
        }
    }

    public void setGameEngine(GameEngine engine) {
        this.gameEngine = engine;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (gameEngine == null) return;

        drawBackground(canvas);
        drawBorders(canvas);
        drawGrid(canvas);
        drawGameBoard(canvas);
        if (gameEngine.getCurrentPiece() != null) {
            drawCurrentPiece(canvas);
        }

        // Check if the game is over and draw the text
        if (isGameOver) {
            float centerX = canvas.getWidth() / 2f;
            float centerY = canvas.getHeight() / 2f;
            canvas.drawText("Game Over", centerX, centerY, textPaint);
        }
    }

    // drawBackground
    private void drawBackground(Canvas canvas) {
        float boardWidth = gameEngine.getBoardWidth() * cellSize;
        float boardHeight = gameEngine.getBoardHeight() * cellSize;
        
        // Draw dark background for the game area
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Paint.Style.FILL);
        
        canvas.drawRect(xOffset, 0, xOffset + boardWidth, boardHeight, backgroundPaint);
    }

    // drawBorders
    private void drawBorders(Canvas canvas) {
        float boardWidth = gameEngine.getBoardWidth() * cellSize;
        float boardHeight = gameEngine.getBoardHeight() * cellSize;
        
        // Draw thick border around the entire game board
        float borderOffset = 4f; // Half of border width to center it
        canvas.drawRect(
            xOffset - borderOffset, 
            -borderOffset, 
            xOffset + boardWidth + borderOffset, 
            boardHeight + borderOffset, 
            borderPaint
        );
        
        // Draw inner border for extra definition
        Paint innerBorderPaint = new Paint();
        innerBorderPaint.setColor(Color.BLACK);
        innerBorderPaint.setStrokeWidth(2f);
        innerBorderPaint.setStyle(Paint.Style.STROKE);
        innerBorderPaint.setAntiAlias(true);
        
        canvas.drawRect(
            xOffset, 
            0, 
            xOffset + boardWidth, 
            boardHeight, 
            innerBorderPaint
        );
    }

    // drawGrid
    private void drawGrid(Canvas canvas) {
        // Draw vertical lines
        for (int i = 0; i <= gameEngine.getBoardWidth(); i++) {
            float x = xOffset + i * cellSize;
            canvas.drawLine(x, 0, x, gameEngine.getBoardHeight() * cellSize, gridPaint);
        }

        // Draw horizontal lines
        for (int i = 0; i <= gameEngine.getBoardHeight(); i++) {
            float y = i * cellSize;
            canvas.drawLine(xOffset, y, xOffset + gameEngine.getBoardWidth() * cellSize, y, gridPaint);
        }
        
        // Draw additional subtle grid lines for better definition
        Paint subtleGridPaint = new Paint();
        subtleGridPaint.setColor(Color.LTGRAY);
        subtleGridPaint.setStrokeWidth(1f);
        subtleGridPaint.setAntiAlias(true);
        subtleGridPaint.setAlpha(80);
        
        // Draw half-cell grid lines for better visual definition
        for (int i = 0; i < gameEngine.getBoardWidth(); i++) {
            for (int j = 0; j < gameEngine.getBoardHeight(); j++) {
                float left = xOffset + i * cellSize;
                float top = j * cellSize;
                float right = left + cellSize;
                float bottom = top + cellSize;
                
                // Draw cell outline for better definition
                canvas.drawRect(left, top, right, bottom, subtleGridPaint);
            }
        }
    }

    // drawGameBoard
    private void drawGameBoard(Canvas canvas) {
        for (int row = 0; row < gameEngine.getBoardHeight(); row++) {
            for (int col = 0; col < gameEngine.getBoardWidth(); col++) {
                int cellColor = gameEngine.getGameBoard()[row][col];
                if (cellColor != 0) {
                    piecePaint.setColor(cellColor);
                    // Add xOffset to the left and right coordinates
                    float left = xOffset + col * cellSize;
                    float top = row * cellSize;
                    float right = left + cellSize;
                    float bottom = top + cellSize;
                    canvas.drawRect(left, top, right, bottom, piecePaint);
                }
            }
        }
    }

    // drawCurrentPiece
    private void drawCurrentPiece(Canvas canvas) {
        Piece piece = gameEngine.getCurrentPiece();
        piecePaint.setColor(piece.getColor());

        int[][] pieceShape = piece.getShape();
        for (int row = 0; row < pieceShape.length; row++) {
            for (int col = 0; col < pieceShape[row].length; col++) {
                if (pieceShape[row][col] == 1) {
                    // Add xOffset to the left and right coordinates
                    float left = xOffset + (piece.getX() + col) * cellSize;
                    float top = (piece.getY() + row) * cellSize;
                    float right = left + cellSize;
                    float bottom = top + cellSize;
                    canvas.drawRect(left, top, right, bottom, piecePaint);
                }
            }
        }
    }
}