package com.dynamo.sporter.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.ListAdapter;

import com.dynamo.sporter.R;
import com.dynamo.sporter.model.Challenge;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ChallengePostListViewAdapter implements ListAdapter {

    private final Context context;
    private final List<Challenge> challenges;

    public ChallengePostListViewAdapter(@NonNull Context context, List<Challenge> challenges) {
        this.context = context;
        this.challenges = challenges;
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
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            Log.i("Add Challenge", "getView: Add Challenge");
            convertView = LayoutInflater.from(context).inflate(R.layout.challenges_list_view, null);

            View finalConvertView = convertView;
            FirebaseFirestore.getInstance().collection("clubs").document(challenges.get(position).getClubID().trim()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    ((TextView) finalConvertView.findViewById(R.id.challenge_date)).setText(challenges.get(position).getDate());
                    ((TextView) finalConvertView.findViewById(R.id.club_name)).setText(task.getResult().get("name").toString());
                    ((TextView) finalConvertView.findViewById(R.id.sport_name)).setText(challenges.get(position).getSportname());
                }
            });

        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return this.challenges.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }
}
