package com.dynamo.sporter.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.dynamo.sporter.ClubHomeFragment;
import com.dynamo.sporter.MainActivity;
import com.dynamo.sporter.model.Player;
import com.dynamo.sporter.shared.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class PlayerServiceImpl implements PlayerService {

    public PlayerServiceImpl() { }

    @Override
    public boolean isClubMember(String playerId, Context context) {
        System.out.println("Player Id: " + playerId);
        String clubID = SharedPrefManager.getInstance(context).getClubID();
        return clubID != null;
    }

    @Override
    public void addPlayerToClub(Context context, String playerid, String clubid) {
        db.collection("players").document(playerid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isComplete() && task.getResult() != null) {
                    if (task.getResult().toObject(Player.class).getClubid() != null)
                        Log.i("Add player to club", "Cannot add player because player already belongs to some club");
                    else {
                        db.collection("players").document(playerid).update("clubid", clubid).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Map<String, String> playerclubMap = new HashMap<>();
                                playerclubMap.put("playerid", playerid);
                                playerclubMap.put("clubid", clubid);
                                db.collection("playersclubs").add(playerclubMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        Log.i("Player & Club Mapping", "Player & Club Mapping Added Successfully");
                                        ((MainActivity) context).navigateTo(new ClubHomeFragment(), false);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i("Player & Club Mapping", "Player & Club Mapping Failed");
                                    }
                                });
                                Log.i("Add player to club", "Player ID" + playerid + " successfully added to club ID " + clubid);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("Add player to club", e.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public boolean isAdmin(Context context) {
        return SharedPrefManager.getInstance(context).isClubAdmin();
    }
}
