package com.dynamo.sporter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dynamo.sporter.createfragments.CreateClubFragment;
import com.dynamo.sporter.profilesfragments.UserProfileFragment;
import com.dynamo.sporter.service.PlayerService;
import com.dynamo.sporter.service.PlayerServiceImpl;
import com.dynamo.sporter.shared.SharedPrefManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlayerHomeFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    FirebaseFirestore db;
    PlayerService playerService;
    Context context;
    private Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sptr_player_home_fragment, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.sptr_player_home_toolbar);
        playerService = new PlayerServiceImpl();
        if (playerService.isClubMember(SharedPrefManager.getInstance(context).getUserId(), context))
            ((MainActivity) context).navigateTo(new ClubHomeFragment(), false);

        ((TextView) view.findViewById(R.id.create_club_link)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).navigateTo(new CreateClubFragment(), true);
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void setUpToolbar(View view) {
        final MaterialToolbar toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.inflateMenu(R.menu.sptr_menu);
        toolbar.setOnMenuItemClickListener(this);

        toolbar.getMenu().findItem(R.id.sptr_mark_as_admin).setVisible(false);
        toolbar.getMenu().findItem(R.id.sptr_unmark_as_admin).setVisible(false);

        toolbar.getMenu().findItem(R.id.sptr_create_challenge_menu_item).setVisible(false);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sptr_logout:
                SharedPrefManager.getInstance(context).logout();
                ((MainActivity) context).navigateTo(new LoginFragment(), false);
                break;
            case R.id.sptr_user_profile:
                ((MainActivity) context).navigateTo(new UserProfileFragment(), true);
                break;
        }

        return true;
    }
}