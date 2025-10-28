package com.MAD.tetrisgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeEngine {
    
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
    
    private static final int BOARD_WIDTH = 20;
    private static final int BOARD_HEIGHT = 20;
    
    private List<Point> snake;
    private Point food;
    private Direction currentDirection;
    private Direction nextDirection;
    private Random random;
    private int score;
    private boolean gameOver;
    private boolean gameStarted;
    
    public SnakeEngine() {
        snake = new ArrayList<>();
        random = new Random();
        resetGame();
    }
    
    public void resetGame() {
        snake.clear();
        // Start with snake in the middle
        snake.add(new Point(BOARD_WIDTH / 2, BOARD_HEIGHT / 2));
        snake.add(new Point(BOARD_WIDTH / 2 - 1, BOARD_HEIGHT / 2));
        snake.add(new Point(BOARD_WIDTH / 2 - 2, BOARD_HEIGHT / 2));
        
        currentDirection = Direction.RIGHT;
        nextDirection = Direction.RIGHT;
        score = 0;
        gameOver = false;
        gameStarted = false;
        spawnFood();
    }
    
    public void startGame() {
        gameStarted = true;
    }
    
    public void setDirection(Direction direction) {
        // Prevent snake from going backwards into itself
        if (currentDirection == Direction.UP && direction != Direction.DOWN ||
            currentDirection == Direction.DOWN && direction != Direction.UP ||
            currentDirection == Direction.LEFT && direction != Direction.RIGHT ||
            currentDirection == Direction.RIGHT && direction != Direction.LEFT) {
            nextDirection = direction;
        }
    }
    
    public void update() {
        if (!gameStarted || gameOver) return;
        
        currentDirection = nextDirection;
        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);
        
        // Move head based on direction
        switch (currentDirection) {
            case UP:
                newHead.y--;
                break;
            case DOWN:
                newHead.y++;
                break;
            case LEFT:
                newHead.x--;
                break;
            case RIGHT:
                newHead.x++;
                break;
        }
        
        // Check wall collision
        if (newHead.x < 0 || newHead.x >= BOARD_WIDTH || 
            newHead.y < 0 || newHead.y >= BOARD_HEIGHT) {
            gameOver = true;
            return;
        }
        
        // Check self collision
        for (Point segment : snake) {
            if (newHead.x == segment.x && newHead.y == segment.y) {
                gameOver = true;
                return;
            }
        }
        
        snake.add(0, newHead);
        
        // Check if food is eaten
        if (newHead.x == food.x && newHead.y == food.y) {
            score += 10;
            spawnFood();
        } else {
            // Remove tail if no food eaten
            snake.remove(snake.size() - 1);
        }
    }
    
    private void spawnFood() {
        boolean validPosition = false;
        while (!validPosition) {
            food = new Point(random.nextInt(BOARD_WIDTH), random.nextInt(BOARD_HEIGHT));
            validPosition = true;
            
            // Make sure food doesn't spawn on snake
            for (Point segment : snake) {
                if (food.x == segment.x && food.y == segment.y) {
                    validPosition = false;
                    break;
                }
            }
        }
    }
    
    public List<Point> getSnake() {
        return snake;
    }
    
    public Point getFood() {
        return food;
    }
    
    public int getScore() {
        return score;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public boolean isGameStarted() {
        return gameStarted;
    }
    
    public int getBoardWidth() {
        return BOARD_WIDTH;
    }
    
    public int getBoardHeight() {
        return BOARD_HEIGHT;
    }
    
    public static class Point {
        public int x, y;
        
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
