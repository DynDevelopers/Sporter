package com.dynamo.sporter.profilesfragments;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dynamo.sporter.GalleryFragment;
import com.dynamo.sporter.MainActivity;
import com.dynamo.sporter.PlayersListFragment;
import com.dynamo.sporter.R;
import com.dynamo.sporter.model.Club;
import com.dynamo.sporter.service.ClubService;
import com.dynamo.sporter.service.ClubServiceImpl;
import com.dynamo.sporter.service.PlayerService;
import com.dynamo.sporter.service.PlayerServiceImpl;
import com.dynamo.sporter.shared.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class ClubProfileFragment extends Fragment implements View.OnClickListener {

    private TextView fullName, dateCreated, owner, location, description;
    private Context context;
    private FirebaseFirestore db;
    private String clubID;
    private LinearLayout progressBarLayout;
    private Toolbar toolbar;
    private AppBarLayout appbar;
    private Button deleteClub;

    public ClubProfileFragment() { }

    public ClubProfileFragment(String clubID) {
        this.clubID = clubID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sptr_club_profile_fragment, container, false);

        appbar = (AppBarLayout) view.findViewById(R.id.sptr_club_profile_app_bar);
        toolbar = (Toolbar) view.findViewById(R.id.sptr_club_profile_tool_bar);

        progressBarLayout = (LinearLayout) view.findViewById(R.id.sptr_progress_bar_layout);
        PlayerService playerService = new PlayerServiceImpl();


        fullName = (TextView) view.findViewById(R.id.sptr_club_full_name);
        dateCreated = (TextView) view.findViewById(R.id.sptr_club_date_created);
        owner = (TextView) view.findViewById(R.id.sptr_club_owner);
        location = (TextView) view.findViewById(R.id.sptr_club_location);
        description = (TextView) view.findViewById(R.id.sptr_club_full_descritpion);

        Button viewPlayers = view.findViewById(R.id.sptr_button_view_club_players);
        Button viewGallery = view.findViewById(R.id.sptr_button_view_club_gallery);

        viewPlayers.setOnClickListener(this);
        viewGallery.setOnClickListener(this);

        deleteClub = view.findViewById(R.id.sptr_button_delete_club);

        db = FirebaseFirestore.getInstance();

        if (SharedPrefManager.getInstance(context).getClubID().trim().equals(this.clubID)) {
            appbar.setVisibility(View.GONE);
        } else {
            deleteClub.setVisibility(View.GONE);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        deleteClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!playerService.isAdmin(context))
                    Log.i("Delete Club", "You dont have admin permissions");
                else
                    Log.i("Delete Club", "Under Construction");
            }
        });

        if (savedInstanceState == null) {
            if (clubID != null) {
                setData(this.clubID);
            }
        }


        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setData(String clubID) {
        db.collection("clubs").document(clubID.trim()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isComplete() && task.getResult() != null) {
                    Log.i("club info", task.getResult().toString());
                    Map<String, Object> doc = task.getResult().getData();

                    Club club = task.getResult().toObject(Club.class);
                    Log.i("Club toString", club.toString());
                    fullName.setText(": " + club.getName());
                    dateCreated.setText(": " + club.getDateCreated());

                    FirebaseFirestore.getInstance().collection("players").document(club.getOwnerid().trim()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                String fname = task.getResult().get("firstname").toString();
                                String lname = task.getResult().get("lastname").toString();
                                owner.setText(": " + (fname.charAt(0)+"").toUpperCase()+fname.substring(1) + " " + (lname.charAt(0)+"").toUpperCase()+lname.substring(1));
                                progressBarLayout.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

                    location.setText(": " + club.getCountry() + ", " + club.getState() + ", " + club.getCity() +", " + club.getPincode());
                    description.setText(": " + club.getDescription());
                    toolbar.setTitle(club.getName());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sptr_button_view_club_players:
                ((MainActivity) getActivity()).navigateTo(new PlayersListFragment(this.clubID), true);
                break;
            case R.id.sptr_button_view_club_gallery:
                ((MainActivity) getActivity()).navigateTo(new GalleryFragment(clubID.trim()), true);
                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("fullname", fullName.getText().toString());
        outState.putString("dateCreated", dateCreated.getText().toString());
        outState.putString("owner", owner.getText().toString());
        outState.putString("location", location.getText().toString());
        outState.putString("description", description.getText().toString());
        outState.putString("ID", this.clubID);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            fullName.setText(savedInstanceState.getString("fullname"));
            dateCreated.setText(savedInstanceState.getString("dateCreated"));
            owner.setText(savedInstanceState.getString("owner"));
            location.setText(savedInstanceState.getString("location"));
            description.setText(savedInstanceState.getString("description"));
            this.clubID = savedInstanceState.getString("ID");

            if (SharedPrefManager.getInstance(context).getClubID().trim().equals(this.clubID)) {
                appbar.setVisibility(View.GONE);
            } else {
                deleteClub.setVisibility(View.GONE);
            }
        }
    }
}