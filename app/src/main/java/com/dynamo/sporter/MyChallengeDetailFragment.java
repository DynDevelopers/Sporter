package com.dynamo.sporter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dynamo.sporter.editfragments.EditChallengeFragment;
import com.dynamo.sporter.model.Challenge;
import com.dynamo.sporter.shared.SharedPrefManager;
import com.dynamo.sporter.util.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

import io.grpc.okhttp.internal.Util;

public class MyChallengeDetailFragment extends Fragment implements View.OnClickListener {

    Toolbar toolbar;
    private Challenge challenge;
    private Context context;
    private TextView sportName, status, challengeDate, challengeTime, timeNegotiable, location, locationNegotiable, teamSize, dateNegotiable, playersAgeGroup, challengeDescription;
    private Button disable, delete;
    private TextView createdBy;
    public MyChallengeDetailFragment() {  }
    public MyChallengeDetailFragment(Challenge challenge) {
        this.challenge = challenge;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.sptr_my_challenge_detail_fragment, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.my_challenge_detail_toolbar);

        sportName = ((TextView) view.findViewById(R.id.details_sport_name));
        status = ((TextView) view.findViewById(R.id.details_challengeStatus));
        challengeDate = ((TextView) view.findViewById(R.id.details_cdate));
        challengeTime = ((TextView) view.findViewById(R.id.details_ctime));
        timeNegotiable = ((TextView) view.findViewById(R.id.details_tnegotiable));
        playersAgeGroup = ((TextView) view.findViewById(R.id.details_playersAgeGroup));
        location = ((TextView) view.findViewById(R.id.details_clocation));
        locationNegotiable = ((TextView) view.findViewById(R.id.details_lnegotiable));
        dateNegotiable = ((TextView) view.findViewById(R.id.details_dnegotiable));
        teamSize = ((TextView) view.findViewById(R.id.details_teamSize));

        challengeDescription = ((TextView) view.findViewById(R.id.details_cdescription));

        delete = view.findViewById(R.id.sptr_delete_challenge_button);
        disable = view.findViewById(R.id.sptr_disbale_challenge_button);

        setUpToolbar(toolbar);
        delete.setOnClickListener(this);
        disable.setOnClickListener(this);

        if (!SharedPrefManager.getInstance(context).isClubAdmin()) {
            delete.setVisibility(View.GONE);
            disable.setVisibility(View.GONE);
        }

        if (savedInstanceState == null) {
            sportName.setText(challenge.getSportname());
            status.setText((challenge.getStatus().equals("A")?"Active":"Inactive"));
            challengeDate.setText(challenge.getDate());
            challengeTime.setText(challenge.getTime());
            timeNegotiable.setText(((challenge.isTnegotiable())?"Yes":"No"));
            playersAgeGroup.setText(challenge.getAgeGroup());
            location.setText(challenge.getLocation());
            locationNegotiable.setText(((challenge.islNegotiable()?"Yes":"No")));
            dateNegotiable.setText(((challenge.isDateNegotiable()?"Yes":"No")));
            teamSize.setText(challenge.getTeamSize());
            challengeDescription.setText(challenge.getDescription());
            createdBy = ((TextView) view.findViewById(R.id.details_created_by));

            FirebaseFirestore.getInstance().collection("players").document(challenge.getCreatedByID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isComplete() && task.getResult() != null) {
                        String firstname = task.getResult().get("firstname").toString();
                        String lastname = task.getResult().get("lastname").toString();
                        createdBy.setText(" "+Utility.getFullName(firstname, lastname));
                    }
                }
            });
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sptr_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.edit:
                ((MainActivity) context).navigateTo(new EditChallengeFragment(challenge.getID()), true);
                break;
            case R.id.chats:
                ((MainActivity) context).navigateTo(new ChallengeChatListFragment(challenge), true);
                break;
        }
        return true;
    }

    private void setUpToolbar(View view) {
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("sportname", challenge.getSportname());
        outState.putString("status", (challenge.getStatus().equals("A")?"Active":"Inactive"));
        outState.putString("challengeDate", challenge.getDate());
        outState.putString("teamSize", challenge.getTeamSize());
        outState.putString("time", challenge.getTime());
        outState.putString("timeNegotiable", (challenge.isTnegotiable())?"Yes":"No");
        outState.putString("dateNegotiable", challenge.isDateNegotiable()?"Yes":"No");
        outState.putString("locationNegotiable", (challenge.islNegotiable()?"Yes":"No"));
        outState.putString("playersAgeGroup", challenge.getAgeGroup());
        outState.putString("location", challenge.getLocation());
        outState.putString("teamSize", challenge.getTeamSize());
        outState.putString("challengeDescription", challenge.getDescription());
        outState.putString("createdBy", createdBy.getText().toString());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {

            sportName.setText(savedInstanceState.getString("sportname"));
            status.setText(savedInstanceState.getString("status"));
            challengeDate.setText(savedInstanceState.getString("challengeDate"));
            challengeTime.setText(savedInstanceState.getString("teamSize"));
            timeNegotiable.setText(savedInstanceState.getString("timeNegotiable"));
            playersAgeGroup.setText(savedInstanceState.getString("playersAgeGroup"));
            location.setText(savedInstanceState.getString("location"));
            locationNegotiable.setText(savedInstanceState.getString("locationNegotiable"));
            dateNegotiable.setText(savedInstanceState.getString("dateNegotiable"));
            teamSize.setText(savedInstanceState.getString("teamSize"));
            challengeDescription.setText(savedInstanceState.getString("challengeDescription"));
            createdBy.setText(" " + savedInstanceState.getString("createdBy"));
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.sptr_disbale_challenge_button:
                break;
            case R.id.sptr_delete_challenge_button:
                break;
        }
    }
}