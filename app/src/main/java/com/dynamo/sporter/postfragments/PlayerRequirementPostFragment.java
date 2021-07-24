package com.dynamo.sporter.postfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dynamo.sporter.R;

public class PlayerRequirementPostFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sptr_player_requirement_post_fragment, container, false);

        return view;
    }
}