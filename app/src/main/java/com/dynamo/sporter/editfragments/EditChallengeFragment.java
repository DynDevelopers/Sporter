package com.dynamo.sporter.editfragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.dynamo.sporter.R;
import com.dynamo.sporter.model.Challenge;
import com.dynamo.sporter.util.Utility;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditChallengeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    TextInputEditText date, timePicker, location, description;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;
    private Toolbar toolbar;
    private Button button;
    private CheckBox timeN, dateN, locationN;
    private Spinner age, sport, teamSize;
    private String ageSelected, sportSelected, teamSizeSelected;
    private String ID;

    public EditChallengeFragment() {

    }
    public EditChallengeFragment(String ID) {
        this.ID = ID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sptr_create_challenge_fragment, container, false);

        date = (TextInputEditText) view.findViewById(R.id.sptr_challenge_date);
        timePicker = (TextInputEditText) view.findViewById(R.id.sptr_challenge_time);
        location = (TextInputEditText) view.findViewById(R.id.sptr_challenge_address);
        description = (TextInputEditText) view.findViewById(R.id.sptr_challenge_description);

        timeN = (CheckBox) view.findViewById(R.id.sptr_timen_checkbox);
        dateN = (CheckBox) view.findViewById(R.id.sptr_daten_checkbox);
        locationN = (CheckBox) view.findViewById(R.id.sptr_locationn_checkbox);

        age = (Spinner) view.findViewById(R.id.sptr_create_challenge_players_age);
        sport = (Spinner) view.findViewById(R.id.sptr_create_challenge_sport);
        teamSize = (Spinner) view.findViewById(R.id.sptr_create_challenge_team_size);

        button = (Button) view.findViewById(R.id.sptr_submit_challenge);
        toolbar = (Toolbar) view.findViewById(R.id.sptr_create_challenge_toolbar);

        date.setOnClickListener(this);
        timePicker.setOnClickListener(this);
        button.setOnClickListener(this);

        age.setOnItemSelectedListener(this);

        date.setFocusable(false);
        timePicker.setFocusable(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        if (savedInstanceState == null)
            init();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.sptr_challenge_date:
                setDate();
                break;
            case R.id.sptr_challenge_time:
                setTime();
                break;
            case R.id.sptr_submit_challenge:
                updateChallenge();
                break;
        }
    }

    private void setDate() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        date.setText(day + "/" + (month+1) + "/" +year);
                        Log.i("Selected Date", day + "/" + (month+1) + "/" +year);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void setTime() {
        Calendar c = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timePicker.setText(Utility.formatTime(hourOfDay, minute));
                        Log.i("Selected Time", Utility.formatTime(hourOfDay, minute));
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()) {
            case R.id.sptr_create_challenge_players_age:
                ageSelected = age.getSelectedItem().toString();
                break;
            case R.id.sptr_create_challenge_sport:
                sportSelected = sport.getSelectedItem().toString();
                break;
            case R.id.sptr_create_challenge_team_size:
                teamSizeSelected = teamSize.getSelectedItem().toString();
                break;
            default:
                Log.i("Spinner onItemSelected", "Spinner onItemSelected");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void init() {
        db.collection("challenges").document(ID).get().addOnCompleteListener(task -> {
            if (task.isComplete() && task.getResult() != null) {
                DocumentSnapshot doc = task.getResult();
//                Challenge challenge = new Challenge(doc.get("clubName").toString(), doc.get("clubID").toString(), doc.get("teamSize").toString(), doc.get("ageGroup").toString(), doc.get("date").toString(), (boolean) doc.get("dateNegotiable"), doc.get("time").toString(), (boolean) doc.get("tnegotiable"), doc.get("location").toString(), (boolean) doc.get("lNegotiable"), doc.get("sportname").toString(), doc.get("description").toString(), doc.get("status").toString(), doc.get("createdBy").toString());
                Challenge challenge = doc.toObject(Challenge.class);
                challenge.setID(doc.getId());
                setDefaults(challenge);
            }
        });
    }

    private void setDefaults(Challenge challenge) {
        date.setText(challenge.getDate());
        timePicker.setText(challenge.getTime());
        location.setText(challenge.getLocation());
        description.setText(challenge.getDescription());

        timeN.setChecked(challenge.isTnegotiable());
        dateN.setChecked(challenge.isDateNegotiable());
        locationN.setChecked(challenge.islNegotiable());

        sportSelected = challenge.getSportname();
        teamSizeSelected = challenge.getTeamSize();
        ageSelected = challenge.getAgeGroup();

        int sI = Arrays.asList(getResources().getStringArray(R.array.sports)).indexOf(sportSelected);
        int aI = Arrays.asList(getResources().getStringArray(R.array.gender)).indexOf(ageSelected);
        int tI = Arrays.asList(getResources().getStringArray(R.array.age)).indexOf(teamSizeSelected);

        sport.setSelection(sI);
        teamSize.setSelection(tI);
        age.setSelection(aI);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void updateChallenge() {
        Map<String, Object> data = new HashMap<>();
        data.put("date", date.getText().toString());
        data.put("time", timePicker.getText().toString());
        data.put("location", location.getText().toString());
        data.put("description", description.getText().toString());
        data.put("tnegotiable", timeN.isChecked());
        data.put("dateNegotiable", dateN.isChecked());
        data.put("lNegotiable", locationN.isChecked());
        data.put("sportname", sportSelected);
        data.put("teamSize", teamSizeSelected);
        data.put("ageGroup", ageSelected);

        db.collection("challenges").document(ID).update(data);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("date", date.getText().toString());
        outState.putString("time", timePicker.getText().toString());
        outState.putString("location", location.getText().toString());
        outState.putString("description", description.getText().toString());
        outState.putBoolean("tnegotiable", timeN.isChecked());
        outState.putBoolean("dateNegotiable", dateN.isChecked());
        outState.putBoolean("lNegotiable", locationN.isChecked());
        outState.putString("sportname", sportSelected);
        outState.putString("teamSize", teamSizeSelected);
        outState.putString("ageGroup", ageSelected);

        outState.putString("ID", this.ID);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            date.setText(savedInstanceState.getString("date"));
            timePicker.setText(savedInstanceState.getString("time"));
            location.setText(savedInstanceState.getString("location"));
            description.setText(savedInstanceState.getString("description"));

            timeN.setChecked(savedInstanceState.getBoolean("tnegotiable"));
            dateN.setChecked(savedInstanceState.getBoolean("dateNegotiable"));
            locationN.setChecked(savedInstanceState.getBoolean("lNegotiable"));

            sportSelected = savedInstanceState.getString("sportname");
            teamSizeSelected = savedInstanceState.getString("teamSize");
            ageSelected = savedInstanceState.getString("ageGroup");

            int sI = Arrays.asList(getResources().getStringArray(R.array.sports)).indexOf(sportSelected);
            int aI = Arrays.asList(getResources().getStringArray(R.array.gender)).indexOf(ageSelected);
            int tI = Arrays.asList(getResources().getStringArray(R.array.age)).indexOf(teamSizeSelected);

            sport.setSelection(sI);
            teamSize.setSelection(tI);
            age.setSelection(aI);

            this.ID = savedInstanceState.getString("ID");
        }
    }
}