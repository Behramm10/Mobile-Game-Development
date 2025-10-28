package com.MAD.tetrisgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class NextPieceView extends View {

    private Paint piecePaint;
    private Paint gridPaint;
    private Piece nextPiece;
    private int cellSize;

    public NextPieceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        piecePaint = new Paint();
        gridPaint = new Paint();
        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStrokeWidth(1f);
    }

    public void setNextPiece(Piece piece) {
        this.nextPiece = piece;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Calculate cell size to fit the piece in the view
        cellSize = Math.min(w, h) / 4; // Assume max 4x4 piece
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        if (nextPiece == null || canvas == null) return;

        int[][] pieceShape = nextPiece.getShape();
        int pieceWidth = pieceShape[0].length;
        int pieceHeight = pieceShape.length;

        // Calculate offset to center the piece
        int offsetX = (getWidth() - pieceWidth * cellSize) / 2;
        int offsetY = (getHeight() - pieceHeight * cellSize) / 2;

        // Draw the piece
        piecePaint.setColor(nextPiece.getColor());
        for (int row = 0; row < pieceHeight; row++) {
            for (int col = 0; col < pieceWidth; col++) {
                if (pieceShape[row][col] == 1) {
                    float left = offsetX + col * cellSize;
                    float top = offsetY + row * cellSize;
                    float right = left + cellSize;
                    float bottom = top + cellSize;
                    canvas.drawRect(left, top, right, bottom, piecePaint);
                }
            }
        }
    }
}
