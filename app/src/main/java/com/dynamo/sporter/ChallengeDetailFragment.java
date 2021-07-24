package com.dynamo.sporter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dynamo.sporter.model.Challenge;
import com.dynamo.sporter.profilesfragments.ClubProfileFragment;
import com.dynamo.sporter.shared.SharedPrefManager;
import com.dynamo.sporter.util.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChallengeDetailFragment extends Fragment {

    private final Challenge challenge;
    private Context context;
    public ChallengeDetailFragment(Challenge challenge) {
        this.challenge = challenge;
    }
    TextView createdBy, clubName;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sptr_challenge_detail_fragment, container, false);
        setUpToolbar(view);
        clubName = ((TextView) view.findViewById(R.id.clubName));
        ((TextView) view.findViewById(R.id.cdate)).setText(challenge.getDate());
        ((TextView) view.findViewById(R.id.ctime)).setText(challenge.getTime());
        ((TextView) view.findViewById(R.id.cdateNegotiable)).setText(((challenge.isDateNegotiable())?"Yes":"No"));
        ((TextView) view.findViewById(R.id.cTimeNegotiable)).setText(((challenge.isTnegotiable())?"Yes":"No"));
        ((TextView) view.findViewById(R.id.playersAgeGroup)).setText(challenge.getAgeGroup());
        ((TextView) view.findViewById(R.id.clocation)).setText(challenge.getLocation());
        ((TextView) view.findViewById(R.id.lnegotiable)).setText(((challenge.islNegotiable())?"Yes":"No"));
        ((TextView) view.findViewById(R.id.cdescription)).setText(challenge.getDescription());
        ((TextView) view.findViewById(R.id.teamSize)).setText(challenge.getTeamSize());
        ((TextView) view.findViewById(R.id.challenge_sport_name)).setText(challenge.getSportname());
        createdBy = ((TextView) view.findViewById(R.id.createdBy));

        setClubName(challenge.getClubID());
        setCreatedByName(challenge.getCreatedByID());
        TextView viewClubDetail = ((TextView) view.findViewById(R.id.sptr_view_club_detail));
        viewClubDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).navigateTo(new ClubProfileFragment(challenge.getClubID()), true);
            }
        });

        Button chatButton = ((Button) view.findViewById(R.id.sptr_challenge_chat));

        if (SharedPrefManager.getInstance(context).getClubID().trim().equals(challenge.getClubID().trim())) {
            chatButton.setEnabled(false);
            viewClubDetail.setEnabled(false);
        }

        chatButton.setOnClickListener(view1 -> ((MainActivity) context).navigateTo(new ChatFragment(challenge, SharedPrefManager.getInstance(context).getClubID().trim()), true));

        return view;
    }

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.challenge_detail_app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
    }

    private void setClubName(String ID) {
        FirebaseFirestore.getInstance().collection("clubs").document(ID.trim()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult() != null && task.isComplete()) {
                    String name = task.getResult().get("name").toString();
                    clubName.setText(name);
                }
            }
        });
    }

    private void setCreatedByName(String ID) {
        FirebaseFirestore.getInstance().collection("players").document(ID.trim()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult() != null && task.isComplete()) {
                    String firstname = task.getResult().get("firstname").toString();
                    String lastname = task.getResult().get("lastname").toString();
                    createdBy.setText(" " + Utility.getFullName(firstname, lastname));
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}