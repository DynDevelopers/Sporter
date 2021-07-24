package com.dynamo.sporter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dynamo.sporter.model.Challenge;
import com.dynamo.sporter.model.Club;
import com.dynamo.sporter.shared.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChallengeChatListFragment extends Fragment {

    private Challenge challenge;
    private ListView clubsChatListView;
    private Context context;
    private List<String> clubIDs;
    private Toolbar toolbar;

    public ChallengeChatListFragment(Challenge challenge) {
        this.challenge = challenge;
    }
    FirebaseDatabase messageDB;
    FirebaseFirestore db;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sptr_club_chat_list_fragment, container, false);
        messageDB = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();

        toolbar = (Toolbar) view.findViewById(R.id.clubs_chat_tool_bar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        clubsChatListView = (ListView) view.findViewById(R.id.sptr_clubs_chat_list_view);
        clubsChatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity) context).navigateTo(new ChatFragment(challenge, clubIDs.get(i)), true);
            }
        });
        setChats();
        return view;
    }

    private void setChats() {
        clubIDs = new ArrayList<>();
        messageDB.getReference().child(challenge.getID().trim()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isComplete() && task.getResult() != null) {
                    Iterator<DataSnapshot> dataItr = task.getResult().getChildren().iterator();
                    Log.i("Challenge ID", task.getResult().getKey());
                    while (dataItr.hasNext()) {
                        DataSnapshot data = dataItr.next();
                        clubIDs.add(data.getKey().trim());
                    }
                    if (clubIDs.size() > 0)
                        setClubs(clubIDs);
                    else
                        ((TextView) view.findViewById(R.id.no_messages)).setVisibility(View.VISIBLE);
                } else {
                    Log.i("Club IDs", "Null Result");
                }
            }
        });
    }

    private void setClubs(List<String> clubIDs) {
        List<String> clubsName = new ArrayList<>();
        Log.i("setClubs()", "Inside setClubs");

        db.collection("clubs").whereIn(FieldPath.documentId(), clubIDs).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isComplete() || task.getResult() == null)
                    return;

                for (DocumentSnapshot doc : task.getResult()) {
                    Club club = doc.toObject(Club.class);
                    clubsName.add(club.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_activated_1, clubsName);
                clubsChatListView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}