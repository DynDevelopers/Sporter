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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dynamo.sporter.MainActivity;
import com.dynamo.sporter.R;
import com.dynamo.sporter.editfragments.EditProfileFragment;
import com.dynamo.sporter.shared.SharedPrefManager;
import com.dynamo.sporter.util.Utility;

public class UserProfileFragment extends Fragment implements MenuItem.OnMenuItemClickListener {

    private Context context;
    private Toolbar toolbar;
    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sptr_user_profile_fragment, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.sptr_usr_profile_toolbar);

        String firstname = SharedPrefManager.getInstance(context).getFirstname();
        String lastname = SharedPrefManager.getInstance(context).getLastname();
        String email = SharedPrefManager.getInstance(context).getEmail();
        String phoneno = SharedPrefManager.getInstance(context).getPhoneNo();
        String gender = SharedPrefManager.getInstance(context).getGender();
        String country = SharedPrefManager.getInstance(context).getCountry();
        String state = SharedPrefManager.getInstance(context).getState();
        String city = SharedPrefManager.getInstance(context).getCity();
        String pincode = SharedPrefManager.getInstance(context).getPincode();
        String preferredSport = SharedPrefManager.getInstance(context).getPreferredSport();
        String age = SharedPrefManager.getInstance(context).getAge();
        String description = SharedPrefManager.getInstance(context).getDescription();

        ((TextView) view.findViewById(R.id.sptr_usr_fullname)).setText(Utility.getFullName(firstname, lastname));
        ((TextView) view.findViewById(R.id.sptr_usr_email)).setText(email);
        ((TextView) view.findViewById(R.id.sptr_usr_phoneno)).setText(phoneno);
        ((TextView) view.findViewById(R.id.sptr_usr_gender)).setText(": "+gender);
        ((TextView) view.findViewById(R.id.sptr_usr_location)).setText(country + ", " + state + ", " + city + ", " + pincode);
        ((TextView) view.findViewById(R.id.sptr_usr_preferred_sport)).setText(preferredSport);
        ((TextView) view.findViewById(R.id.sptr_usr_age)).setText(": "+age);
        ((TextView) view.findViewById(R.id.sptr_usr_description)).setText(description);

        TextView createClubLink = ((TextView) view.findViewById(R.id.create_club_link));

        if (SharedPrefManager.getInstance(context).getClubID() == null) {
            ((TextView) view.findViewById(R.id.sptr_usr_not_added_to_club_text)).setVisibility(View.VISIBLE);
            createClubLink.setVisibility(View.VISIBLE);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.inflateMenu(R.menu.sptr_toolbar_menu);
        toolbar.getMenu().findItem(R.id.chats).setVisible(false);

        toolbar.getMenu().findItem(R.id.edit).setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.edit:
                ((MainActivity) context).navigateTo(new EditProfileFragment(SharedPrefManager.getInstance(context).getUserId().trim()), true);
                break;
        }
        return false;
    }
}