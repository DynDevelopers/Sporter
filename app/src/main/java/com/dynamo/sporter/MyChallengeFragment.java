package com.dynamo.sporter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dynamo.sporter.adapter.MyChallengeListViewAdapter;
import com.dynamo.sporter.model.Challenge;
import com.dynamo.sporter.shared.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyChallengeFragment extends Fragment implements AdapterView.OnItemClickListener {

    List<Challenge> challenges;
    ListView challengesListView;
    FirebaseFirestore db;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sptr_my_challenge_fragment, container, false);
        challenges = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        challengesListView = (ListView) view.findViewById(R.id.my_challenges_listview);
        challengesListView.setOnItemClickListener(this);
        Log.i("clubID", SharedPrefManager.getInstance(context).getClubID().trim());
        db.collection("challenges").whereEqualTo("clubID", SharedPrefManager.getInstance(context).getClubID()).get().addOnCompleteListener(task -> {
            if (task.isComplete() && task.getResult() != null) {
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    Challenge challenge = doc.toObject(Challenge.class);
                    challenge.setID(doc.getId());
                    challenges.add(challenge);
                }
                if (challenges.size() > 0) {
                    MyChallengeListViewAdapter adapter = new MyChallengeListViewAdapter(context, challenges);
                    challengesListView.setAdapter(adapter);
                }
                Log.i("Number of Challenges", String.valueOf(challenges.size()));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Error", e.getLocalizedMessage());
            }
        });
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ((MainActivity) getActivity()).navigateTo(new MyChallengeDetailFragment(this.challenges.get(i)), true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}