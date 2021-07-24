package com.dynamo.sporter;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.appbar.MaterialToolbar;

public class SearchClubsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sptr_search_clubs_fragment, container, false);
        MaterialToolbar toolbar = (MaterialToolbar) view.findViewById(R.id.app_bar);
        setUpToolbar(toolbar);
        return view;
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
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();
            }
        });
    }
}