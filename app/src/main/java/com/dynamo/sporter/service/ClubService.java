package com.dynamo.sporter.service;

import com.dynamo.sporter.model.Club;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public interface ClubService {


    void createClub(Club club);

    void deleteClub();
}
