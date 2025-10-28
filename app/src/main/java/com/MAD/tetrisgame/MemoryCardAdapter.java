package com.MAD.tetrisgame;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class MemoryCardAdapter extends RecyclerView.Adapter<MemoryCardAdapter.CardViewHolder> {
    
    private List<MemoryCard> cards;
    private OnCardClickListener listener;
    
    public interface OnCardClickListener {
        void onCardClick(int position);
    }
    
    public MemoryCardAdapter(List<MemoryCard> cards, OnCardClickListener listener) {
        this.cards = cards;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memory_card_item, parent, false);
        return new CardViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        MemoryCard card = cards.get(position);
        
        if (card.isMatched()) {
            holder.cardView.setAlpha(0.5f);
            holder.cardView.setEnabled(false);
        } else {
            holder.cardView.setAlpha(1.0f);
            holder.cardView.setEnabled(true);
        }
        
        if (card.isFlipped()) {
            holder.cardImage.setImageResource(card.getImageResource());
            holder.cardImage.setVisibility(View.VISIBLE);
        } else {
            holder.cardImage.setVisibility(View.GONE);
        }
        
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!card.isFlipped() && !card.isMatched()) {
                    listener.onCardClick(position);
                }
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return cards.size();
    }
    
    public static class CardViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView cardImage;
        
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            cardImage = itemView.findViewById(R.id.cardImage);
        }
    }
}
