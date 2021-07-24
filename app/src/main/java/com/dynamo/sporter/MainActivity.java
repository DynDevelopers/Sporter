package com.dynamo.sporter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;

import com.dynamo.sporter.createfragments.CreatePlayerFragment;
import com.dynamo.sporter.shared.SharedPrefManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");
        } else {
//        SharedPrefManager.getInstance(this).logout();
            if (SharedPrefManager.getInstance(this).isLoggedIn())
                navigateTo(new PlayerHomeFragment(), false);
            else
                navigateTo(new LoginFragment(), false);

//        navigateTo(new ClubHomeFragment(), false);
//        navigateTo(new GalleryFragment(""), false);
        }
    }

    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    public Activity getContext() {
        return MainActivity.this;
    }
}