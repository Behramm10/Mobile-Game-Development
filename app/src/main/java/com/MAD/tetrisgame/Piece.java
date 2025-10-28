package com.MAD.tetrisgame;
import android.graphics.Color;

public class Piece {

    private int[][] shape;
    private int color;
    private int x;
    private int y;

    public static final int[][][] SHAPES = {
            {{1, 1, 1, 1}},
            {{0, 1, 0}, {1, 1, 1}},
            {{1, 1}, {1, 1}},
            {{0, 1, 1}, {1, 1, 0}},
            {{1, 1, 0}, {0, 1, 1}},
            {{1, 0, 0}, {1, 1, 1}},
            {{0, 0, 1}, {1, 1, 1}}
    };
    public static final int[] COLORS = {
            Color.CYAN,   // I-shape
            Color.MAGENTA,  // T-shape
            Color.YELLOW,   // O-shape
            Color.RED,    // S-shape
            Color.GREEN,   // Z-shape
            Color.BLUE,   // J-shape
            Color.rgb(255, 127, 0) // L-shape (Orange)
    };
    public Piece(int shapeIndex, int color) {
        this.shape = SHAPES[shapeIndex];
        this.color = color;
        this.x = 3;
        this.y = 0;
    }

    public int[][] getShape() {
        return shape;
    }

    public int getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // --- NEW CODE: SETTER METHODS ---
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setShape(int[][] shape) {
        this.shape = shape;
    }
}