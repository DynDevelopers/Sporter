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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dynamo.sporter.GalleryFragment;
import com.dynamo.sporter.LoginFragment;
import com.dynamo.sporter.MainActivity;
import com.dynamo.sporter.R;
import com.dynamo.sporter.model.Player;
import com.dynamo.sporter.service.PlayerService;
import com.dynamo.sporter.service.PlayerServiceImpl;
import com.dynamo.sporter.shared.SharedPrefManager;
import com.dynamo.sporter.util.Utility;

public class ClubPlayerProfileFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private final Player player;
    private PlayerService playerService;
    private Context context;
    private Toolbar toolbar;
    private TextView name, age,gender, email, location, description, preferredSports;
    public ClubPlayerProfileFragment(Player player) {
        this.player = player;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sptr_club_player_profile_fragment, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.sptr_club_player_details_toolbar);
        String fullname = Utility.getFullName(player.getFirstname(), player.getLastname());
        String fulllocation = player.getCountry() + ", " + player.getState() + ", " + player.getCity() + ", " + player.getPincode();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        name = ((TextView) view.findViewById(R.id.sptr_club_player_name));
        age = ((TextView) view.findViewById(R.id.sptr_club_player_age));
        gender = ((TextView) view.findViewById(R.id.sptr_club_player_gender));
        email = ((TextView) view.findViewById(R.id.sptr_club_player_email));
        location = ((TextView) view.findViewById(R.id.sptr_club_player_location));
        description = ((TextView) view.findViewById(R.id.sptr_club_player_description));
        preferredSports = ((TextView) view.findViewById(R.id.sptr_club_player_preferred_sports));

        if (savedInstanceState == null) {
            name.setText(fullname);
            age.setText(player.getAge());
            gender.setText(player.getGender());
            email.setText(player.getEmail());
            location.setText(fulllocation);
            description.setText(((player.getDescription() == null || player.getDescription().equals(""))?"No Description":player.getDescription()));
            preferredSports.setText(player.getPreferredSports());
        }


        ((Button) view.findViewById(R.id.sptr_club_player_gallery_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).navigateTo(new GalleryFragment(player.getId()), true);
            }
        });
        Button removePlayerButton = ((Button) view.findViewById(R.id.sptr_club_player_remove_button));
        if (!player.getClubid().equals(SharedPrefManager.getInstance(context).getClubID().trim()))
            removePlayerButton.setVisibility(View.GONE);

        removePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!playerService.isAdmin(context))
                    Log.i("Remove Player", "You dont have admin permissions");
                else
                    Log.i("Remove Player", "Under Construction");
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("Selected Player ID", player.getId());
        Log.i("Selected Player IsAdmin", String.valueOf(player.isClubAdmin()));
        toolbar.inflateMenu(R.menu.sptr_menu);
        toolbar.getMenu().findItem(R.id.sptr_create_challenge_menu_item).setVisible(false);
        toolbar.getMenu().findItem(R.id.sptr_edit_club_profile).setVisible(false);
        toolbar.getMenu().findItem(R.id.sptr_user_profile).setVisible(false);

        if (!SharedPrefManager.getInstance(context).isClubAdmin()) {
            toolbar.getMenu().findItem(R.id.sptr_mark_as_admin).setVisible(false);
            toolbar.getMenu().findItem(R.id.sptr_unmark_as_admin).setVisible(false);
        } else {
            Log.i("Is Admin", String.valueOf(player.isClubAdmin()));
            if (!player.isClubAdmin()) {
                toolbar.getMenu().findItem(R.id.sptr_mark_as_admin).setVisible(true);
                toolbar.getMenu().findItem(R.id.sptr_unmark_as_admin).setVisible(false);
            } else {
                toolbar.getMenu().findItem(R.id.sptr_mark_as_admin).setVisible(false);
                toolbar.getMenu().findItem(R.id.sptr_unmark_as_admin).setVisible(true);
            }
        }

        toolbar.getMenu().findItem(R.id.my_chats).setVisible(false);
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.sptr_logout:
                SharedPrefManager.getInstance(context).logout();
                ((MainActivity) context).navigateTo(new LoginFragment(), false);
                break;
            case R.id.sptr_mark_as_admin:
                Log.i("Mark as Admin", "Under Construction");
                break;
            case R.id.sptr_unmark_as_admin:
                Log.i("Unmark as Admin", "Under Construction");
                break;
            default:
                Log.i("Menu Item", "Under Construction");
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("name", name.getText().toString());
        outState.putString("age", age.getText().toString());
        outState.putString("gender", gender.getText().toString());
        outState.putString("email", email.getText().toString());
        outState.putString("location", location.getText().toString());
        outState.putString("description", description.getText().toString());
        outState.putString("preferredSports", preferredSports.getText().toString());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            name.setText(savedInstanceState.getString("name"));
            age.setText(savedInstanceState.getString("age"));
            gender.setText(savedInstanceState.getString("gender"));
            email.setText(savedInstanceState.getString("email"));
            location.setText(savedInstanceState.getString("location"));
            description.setText(savedInstanceState.getString("description"));
            preferredSports.setText(savedInstanceState.getString("preferredSports"));
        }
    }
}