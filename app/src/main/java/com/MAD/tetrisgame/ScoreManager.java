package com.MAD.tetrisgame;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.HashMap;
import java.util.Map;

public class ScoreManager {

    public static void saveScore(String gameName, int score) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            
            // Check if user already has a score for this game
            db.collection("userScores")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> data = documentSnapshot.getData();
                            if (data != null && data.containsKey(gameName)) {
                                int currentBest = ((Long) data.get(gameName)).intValue();
                                if (score > currentBest) {
                                    // Update only if new score is better
                                    updateUserBestScore(db, user, gameName, score);
                                }
                            } else {
                                // First score for this game
                                updateUserBestScore(db, user, gameName, score);
                            }
                        } else {
                            // First time user, create document
                            updateUserBestScore(db, user, gameName, score);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Fallback: save to general scores collection
                        saveToGeneralScores(db, user, gameName, score);
                    });
        }
    }
    
    private static void updateUserBestScore(FirebaseFirestore db, FirebaseUser user, String gameName, int score) {
        Map<String, Object> updateData = new HashMap<>();
        updateData.put(gameName, score);
        updateData.put("lastUpdated", com.google.firebase.firestore.FieldValue.serverTimestamp());
        
        db.collection("userScores")
                .document(user.getUid())
                .set(updateData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    // Also save to general leaderboard for display
                    saveToGeneralScores(db, user, gameName, score);
                })
                .addOnFailureListener(e -> {
                    // Fallback: save to general scores
                    saveToGeneralScores(db, user, gameName, score);
                });
    }
    
    private static void saveToGeneralScores(FirebaseFirestore db, FirebaseUser user, String gameName, int score) {
        Map<String, Object> userScore = new HashMap<>();
        userScore.put("userId", user.getUid());
        userScore.put("userEmail", user.getEmail());
        userScore.put("gameName", gameName);
        userScore.put("score", score);
        userScore.put("timestamp", com.google.firebase.firestore.FieldValue.serverTimestamp());

        // Add to general scores collection for leaderboard
        db.collection("scores")
                .add(userScore)
                .addOnSuccessListener(documentReference -> {
                    // Score saved successfully
                })
                .addOnFailureListener(e -> {
                    // Error saving score
                });
    }
}