package com.dynamo.sporter.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dynamo.sporter.R;
import com.dynamo.sporter.model.Challenge;
import com.dynamo.sporter.util.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyChallengeListViewAdapter implements ListAdapter {
    private Context context;
    private List<Challenge> challenges;
    public MyChallengeListViewAdapter(@NonNull Context context, List<Challenge> challenges) {
        this.context =context;
        this.challenges = challenges;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return this.challenges.size();
    }

    @Override
    public Object getItem(int i) { return this.challenges.get(i); }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.my_challenge_listview_layout, null);


            View finalView = view;
            FirebaseFirestore.getInstance().collection("players").document(challenges.get(i).getCreatedByID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.getResult() != null && task.isComplete()) {
                        ((TextView) finalView.findViewById(R.id.sptr_sport_name)).setText(challenges.get(i).getSportname());
                        ((TextView) finalView.findViewById(R.id.sptr_challenge_created)).setText(challenges.get(i).getDate());
                        String firstname = task.getResult().get("firstname").toString();
                        String lastname = task.getResult().get("lastname").toString();
                        ((TextView) finalView.findViewById(R.id.sptr_created_by)).setText(Utility.getFullName(firstname, lastname));
                    }
                }
            });

        }
        return view;
    }

    @Override
    public int getItemViewType(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return this.challenges.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
