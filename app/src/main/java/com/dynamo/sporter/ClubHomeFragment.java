package com.dynamo.sporter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dynamo.sporter.createfragments.CreateChallengeFragment;
import com.dynamo.sporter.editfragments.EditClubProfileFragment;
import com.dynamo.sporter.profilesfragments.ClubProfileFragment;
import com.dynamo.sporter.profilesfragments.UserProfileFragment;
import com.dynamo.sporter.shared.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ClubHomeFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener, Toolbar.OnMenuItemClickListener {

    private Toolbar toolbar;
    private Context context;
    private View view;
    private BottomNavigationView navigationView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sptr_club_home_fragment, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.club_home_tool_bar);
        navigationView = (BottomNavigationView) view.findViewById(R.id.club_home_bottom_navigation_bar);
        setHasOptionsMenu(true);
        toolbar.setElevation(0);
        toolbar.setBackgroundColor(Color.WHITE);
        navigationView.setOnNavigationItemSelectedListener(this);
        setNavigationMenuItems(false, true, true);

        if (savedInstanceState != null) {
            getFragmentManager().getFragment(savedInstanceState, "currentFragment");
        } else {
            navigateTo(new ChallengePostListFragment(), false);
        }

        return view;
    }

    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.navigator_container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_home:
                navigateTo(new ChallengePostListFragment(), false);
                setNavigationMenuItems(false, true, true);
                break;
            case R.id.menu_item_myChallenges:
                navigateTo(new MyChallengeFragment(), false);
                setNavigationMenuItems(true, false, true);
                break;
            case R.id.menu_item_club_profile:
                setNavigationMenuItems(true, true, false);
                navigateTo(new ClubProfileFragment(SharedPrefManager.getInstance(context).getClubID().trim()), false);
                break;
            default:
                navigateTo(new ChallengePostListFragment(), false);
        }
        return true;
    }

    private void setNavigationMenuItems(boolean home, boolean myChallenges, boolean clubProfile) {
        navigationView.findViewById(R.id.menu_item_home).setEnabled(home);
        navigationView.findViewById(R.id.menu_item_myChallenges).setEnabled(myChallenges);
        navigationView.findViewById(R.id.menu_item_club_profile).setEnabled(clubProfile);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.inflateMenu(R.menu.sptr_menu);
        toolbar.setOnMenuItemClickListener(this);

        toolbar.getMenu().findItem(R.id.sptr_mark_as_admin).setVisible(false);
        toolbar.getMenu().findItem(R.id.sptr_unmark_as_admin).setVisible(false);

        if (!SharedPrefManager.getInstance(context).isClubAdmin()) {
            toolbar.getMenu().findItem(R.id.sptr_create_challenge_menu_item).setVisible(false);
            toolbar.getMenu().findItem(R.id.sptr_edit_club_profile).setVisible(false);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.sptr_logout:
                SharedPrefManager.getInstance(context).logout();
                ((MainActivity) context).navigateTo(new LoginFragment(), false);
                break;
            case R.id.sptr_create_challenge_menu_item:
                ((MainActivity) context).navigateTo(new CreateChallengeFragment(), true);
                break;
            case R.id.sptr_user_profile:
                ((MainActivity) context).navigateTo(new UserProfileFragment(), true);
                break;
            case R.id.sptr_edit_club_profile:
                ((MainActivity) context).navigateTo(new EditClubProfileFragment(SharedPrefManager.getInstance(context).getClubID().trim()), true);
                break;
            case R.id.my_chats:
                ((MainActivity) context).navigateTo(new ClubChatFragment(), true);
                break;
            default:
                Log.i("Menu Item", "Under Construction");
        }
        return true;
    }
}