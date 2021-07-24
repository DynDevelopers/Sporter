package com.dynamo.sporter.service;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.firebase.firestore.FirebaseFirestore;

public interface PlayerService {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    boolean isClubMember(String playerId, Context context);
    void addPlayerToClub(Context context, String playerid, String clubid);
    boolean isAdmin(Context context);
}
