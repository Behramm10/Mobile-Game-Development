package com.MAD.tetrisgame;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ScoreViewHolder> {

    private List<QueryDocumentSnapshot> scoreList;

    public LeaderboardAdapter(List<QueryDocumentSnapshot> scoreList) {
        this.scoreList = scoreList;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        QueryDocumentSnapshot scoreDoc = scoreList.get(position);
        holder.positionTextView.setText((position + 1) + ".");
        holder.userTextView.setText(scoreDoc.getString("userEmail"));
        holder.gameTextView.setText(scoreDoc.getString("gameName"));
        holder.scoreTextView.setText(String.valueOf(scoreDoc.getLong("score")));
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        public TextView positionTextView;
        public TextView userTextView;
        public TextView gameTextView;
        public TextView scoreTextView;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            positionTextView = itemView.findViewById(R.id.positionTextView);
            userTextView = itemView.findViewById(R.id.userTextView);
            gameTextView = itemView.findViewById(R.id.gameTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
        }
    }
}