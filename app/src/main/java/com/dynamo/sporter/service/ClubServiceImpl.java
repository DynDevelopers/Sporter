package com.dynamo.sporter.service;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dynamo.sporter.ClubHomeFragment;
import com.dynamo.sporter.MainActivity;
import com.dynamo.sporter.model.Club;
import com.dynamo.sporter.shared.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClubServiceImpl implements ClubService {

    FirebaseFirestore mFirestore;
    private Context context;

    public ClubServiceImpl(Context context) {
        this.context = context;
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void createClub(Club club) {
        mFirestore.collection("clubs").add(club).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    Log.i("Create Club", "Club Added Successfully");
                    if (task.getResult() != null)
                        mapPlayerAndClub(club.getOwnerid(), task.getResult().getId());
                } else
                    Log.i("Create Club", "Cannot Add this Club");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Create Club", Objects.requireNonNull(e.getMessage()));
            }
        });
    }

    @Override
    public void deleteClub() { }

    void mapPlayerAndClub(String ownerid, String clubid) {
        Map<String, String> playerClubMapper = new HashMap<>();
        playerClubMapper.put("playerid", ownerid);
        playerClubMapper.put("clubid", clubid);
        DocumentReference documentReference = mFirestore.collection("players").document(ownerid);
        mFirestore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(documentReference);

                transaction.update(documentReference, "clubid", clubid).update(documentReference, "clubAdmin", true);
                return null;
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    mFirestore.collection("playersclubs").add(playerClubMapper);
                    SharedPrefManager.getInstance(context).updateClubAdmin(true);
                    SharedPrefManager.getInstance(context).updateClubID(clubid);
                    ((MainActivity) context).navigateTo(new ClubHomeFragment(), false);
                }
            }
        });
    }
}
