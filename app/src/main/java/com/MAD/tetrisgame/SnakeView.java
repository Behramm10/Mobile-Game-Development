package com.MAD.tetrisgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class SnakeView extends View {
    
    private Paint snakePaint;
    private Paint foodPaint;
    private Paint gridPaint;
    private Paint gameOverPaint;
    private SnakeEngine snakeEngine;
    private int cellSize;
    private int xOffset;
    private int yOffset;
    private boolean showGameOver = false;
    
    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        snakePaint = new Paint();
        snakePaint.setColor(Color.parseColor("#4CAF50"));
        snakePaint.setStyle(Paint.Style.FILL);
        
        foodPaint = new Paint();
        foodPaint.setColor(Color.parseColor("#F44336"));
        foodPaint.setStyle(Paint.Style.FILL);
        
        gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#E0E0E0"));
        gridPaint.setStrokeWidth(1f);
        
        gameOverPaint = new Paint();
        gameOverPaint.setColor(Color.RED);
        gameOverPaint.setTextSize(64);
        gameOverPaint.setTextAlign(Paint.Align.CENTER);
        gameOverPaint.setFakeBoldText(true);
    }
    
    public void setSnakeEngine(SnakeEngine engine) {
        this.snakeEngine = engine;
    }
    
    public void setGameOver(boolean gameOver) {
        this.showGameOver = gameOver;
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (snakeEngine != null) {
            // Calculate cell size to fit the board
            int boardWidth = snakeEngine.getBoardWidth();
            int boardHeight = snakeEngine.getBoardHeight();
            
            cellSize = Math.min(w / boardWidth, h / boardHeight);
            
            // Center the board
            xOffset = (w - boardWidth * cellSize) / 2;
            yOffset = (h - boardHeight * cellSize) / 2;
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (snakeEngine == null) return;
        
        drawGrid(canvas);
        drawSnake(canvas);
        drawFood(canvas);
        
        if (showGameOver) {
            drawGameOver(canvas);
        }
    }
    
    private void drawGrid(Canvas canvas) {
        int boardWidth = snakeEngine.getBoardWidth();
        int boardHeight = snakeEngine.getBoardHeight();
        
        // Draw vertical lines
        for (int i = 0; i <= boardWidth; i++) {
            float x = xOffset + i * cellSize;
            canvas.drawLine(x, yOffset, x, yOffset + boardHeight * cellSize, gridPaint);
        }
        
        // Draw horizontal lines
        for (int i = 0; i <= boardHeight; i++) {
            float y = yOffset + i * cellSize;
            canvas.drawLine(xOffset, y, xOffset + boardWidth * cellSize, y, gridPaint);
        }
    }
    
    private void drawSnake(Canvas canvas) {
        for (int i = 0; i < snakeEngine.getSnake().size(); i++) {
            SnakeEngine.Point segment = snakeEngine.getSnake().get(i);
            
            // Head is darker green
            if (i == 0) {
                snakePaint.setColor(Color.parseColor("#2E7D32"));
            } else {
                snakePaint.setColor(Color.parseColor("#4CAF50"));
            }
            
            float left = xOffset + segment.x * cellSize + 2;
            float top = yOffset + segment.y * cellSize + 2;
            float right = left + cellSize - 4;
            float bottom = top + cellSize - 4;
            
            RectF rect = new RectF(left, top, right, bottom);
            canvas.drawRoundRect(rect, 8, 8, snakePaint);
        }
    }
    
    private void drawFood(Canvas canvas) {
        SnakeEngine.Point food = snakeEngine.getFood();
        if (food != null) {
            float left = xOffset + food.x * cellSize + 4;
            float top = yOffset + food.y * cellSize + 4;
            float right = left + cellSize - 8;
            float bottom = top + cellSize - 8;
            
            RectF rect = new RectF(left, top, right, bottom);
            canvas.drawOval(rect, foodPaint);
        }
    }
    
    private void drawGameOver(Canvas canvas) {
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        canvas.drawText("Game Over", centerX, centerY, gameOverPaint);
    }
}
