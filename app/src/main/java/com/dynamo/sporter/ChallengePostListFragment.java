package com.dynamo.sporter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dynamo.sporter.adapter.ChallengePostListViewAdapter;
import com.dynamo.sporter.model.Challenge;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChallengePostListFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView listView;
    List<Challenge> challenges;
    FirebaseFirestore db;
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sptr_challenge_post_list_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.challenge_post_listview);

        challenges = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        db.collection("challenges").orderBy("date").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete() && task.getResult() != null) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        Challenge challenge = doc.toObject(Challenge.class);
                        challenge.setID(doc.getId());
                        challenges.add(challenge);
                    }
                    if (challenges.size() > 0) {
                        ChallengePostListViewAdapter adapter = new ChallengePostListViewAdapter(context, challenges);
                        listView.setAdapter(adapter);
                    }
                }
            }
        });

        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ((MainActivity) getActivity()).navigateTo(new ChallengeDetailFragment(this.challenges.get(i)), true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}