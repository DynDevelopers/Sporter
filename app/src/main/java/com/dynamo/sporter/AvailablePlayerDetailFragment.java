package com.dynamo.sporter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.dynamo.sporter.model.Player;
import com.dynamo.sporter.service.PlayerService;
import com.dynamo.sporter.service.PlayerServiceImpl;
import com.dynamo.sporter.shared.SharedPrefManager;

import java.util.List;

public class AvailablePlayerDetailFragment extends Fragment {

    private Player player;
    private Toolbar toolbar;
    private TextView name, age, gender, email, preferredSports, location, description;
    private PlayerService playerService;
    private Context context;
    AvailablePlayerDetailFragment(Player player) {
        this.player = player;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sptr_available_player_detail_fragment, container, false);
        playerService = new PlayerServiceImpl();

        toolbar = (Toolbar) view.findViewById(R.id.sptr_available_player_details_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();
            }
        });

        String name = (player.getFirstname().charAt(0)+"").toUpperCase() + player.getFirstname().substring(1) + " " + (player.getLastname().charAt(0)+"").toUpperCase() + player.getLastname().substring(1);
        String location = player.getCountry() + ", " + player.getState() + ", " + player.getCity() + ", " + player.getPincode();

        ((TextView) view.findViewById(R.id.sptr_available_player_name)).setText(": " + name);
        ((TextView) view.findViewById(R.id.sptr_available_player_age)).setText(": " + player.getAge());
        ((TextView) view.findViewById(R.id.sptr_available_player_gender)).setText(": " + player.getGender());
        ((TextView) view.findViewById(R.id.sptr_available_player_email)).setText(": " + player.getEmail());
        ((TextView) view.findViewById(R.id.sptr_available_player_location)).setText(": " + location);
        ((TextView) view.findViewById(R.id.sptr_available_player_description)).setText(": " + ((player.getDescription() == null || player.getDescription().equals(""))?"No Description":player.getDescription()));
        ((TextView) view.findViewById(R.id.sptr_available_player_preferred_sports)).setText(": " + player.getPreferredSports());


        ((Button) view.findViewById(R.id.sptr_add_player_to_club_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerService.addPlayerToClub(context, player.getId(), SharedPrefManager.getInstance(context).getClubID());
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}