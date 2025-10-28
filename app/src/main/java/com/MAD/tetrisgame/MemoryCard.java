package com.MAD.tetrisgame;

public class MemoryCard {
    private int id;
    private int imageResource;
    private boolean isFlipped;
    private boolean isMatched;
    private int position;
    
    public MemoryCard(int id, int imageResource, int position) {
        this.id = id;
        this.imageResource = imageResource;
        this.position = position;
        this.isFlipped = false;
        this.isMatched = false;
    }
    
    public int getId() {
        return id;
    }
    
    public int getImageResource() {
        return imageResource;
    }
    
    public boolean isFlipped() {
        return isFlipped;
    }
    
    public void setFlipped(boolean flipped) {
        this.isFlipped = flipped;
    }
    
    public boolean isMatched() {
        return isMatched;
    }
    
    public void setMatched(boolean matched) {
        this.isMatched = matched;
    }
    
    public int getPosition() {
        return position;
    }
    
    public void setPosition(int position) {
        this.position = position;
    }
}
