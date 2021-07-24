package com.dynamo.sporter.createfragments;

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
import com.dynamo.sporter.service.SpinnerService;
import com.dynamo.sporter.shared.SharedPrefManager;
import com.dynamo.sporter.util.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Calendar;

import io.grpc.okhttp.internal.Util;

public class CreateChallengeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    TextInputEditText date, timePicker, location, description;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;
    private Toolbar toolbar;
    private Button button;
    private CheckBox timeN, dateN, locationN;
    private Spinner age, sport, teamSize;
    private String ageSelected, sportSelected, teamSizeSelected;
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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        date.setOnClickListener(this);
        timePicker.setOnClickListener(this);

        age.setOnItemSelectedListener(this);
        sport.setOnItemSelectedListener(this);
        teamSize.setOnItemSelectedListener(this);

        button.setOnClickListener(this);

        date.setFocusable(false);
        timePicker.setFocusable(false);
        return view;
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
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
                submitChallenge();
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

    private void submitChallenge() {
        final String[] clubName = {null};
        String clubID = SharedPrefManager.getInstance(context).getClubID().trim();
        db.collection("clubs").document(clubID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    clubName[0] = task.getResult().get("name").toString();
                    Challenge challenge = getChallenge();
                    submit(challenge);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Fetch ClubInfo", "Unable to fetch club info: " + e.getMessage());
            }
        });
    }

    private Challenge getChallenge() {
        String cDate = date.getText().toString();
        String cTime = timePicker.getText().toString();
        String cLocation = location.getText().toString();
        String createdByID = SharedPrefManager.getInstance(context).getUserId();
        String clubID = SharedPrefManager.getInstance(context).getClubID();
        String cDescription = description.getText().toString();
        final String[] clubName = {null};
        boolean isDateN = dateN.isChecked();
        boolean isTimeN = timeN.isChecked();
        boolean isLocationN = locationN.isChecked();

        return new Challenge(clubID, teamSizeSelected, ageSelected, cDate, isDateN, cTime, isTimeN, cLocation, isLocationN, sportSelected, cDescription, "A", createdByID);
    }

    private void submit(Challenge challenge) {
        db.collection("challenges").add(challenge).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful())
                    Log.i("Submit Challenge", "Challenge Added Successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Submit Challenge", e.getMessage());
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putString("ID", this.ID);
//        outState.putString("firstname", firstname.getText().toString());
//        outState.putString("lastname", lastname.getText().toString());
//        outState.putString("phoneno", phoneno.getText().toString());
//        outState.putString("description", description.getText().toString());
//        outState.putString("country", strCountry);
//        outState.putString("state", strState);
//        outState.putString("city", strCity);
//        outState.putString("pincode", strPincode);
//        outState.putString("age", strAge);
//        outState.putString("gender", strGender);
//        outState.putString("preferredsport", strPreferredSport);
//    }
//
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        if (savedInstanceState != null) {
//            firstname.setText(savedInstanceState.getString("firstname"));
//            lastname.setText(savedInstanceState.getString("lastname"));
//            phoneno.setText(savedInstanceState.getString("phoneno"));
//            description.setText(savedInstanceState.getString("description"));
//
//            strCountry = savedInstanceState.getString("country");
//            strState = savedInstanceState.getString("state");
//            strCity = savedInstanceState.getString("city");
//            strPincode = savedInstanceState.getString("pincode");
//
//            strAge = savedInstanceState.getString("age");
//            strGender = savedInstanceState.getString("gender");
//            strPreferredSport = savedInstanceState.getString("preferredsport");
//
//            int pI = Arrays.asList(getResources().getStringArray(R.array.sports)).indexOf(strPreferredSport);
//            int gI = Arrays.asList(getResources().getStringArray(R.array.gender)).indexOf(strGender);
//            int aI = Arrays.asList(getResources().getStringArray(R.array.age)).indexOf(strAge);
//
//            preferredSport.setSelection(pI);
//            gender.setSelection(gI);
//            age.setSelection(aI);
//
//            this.ID = savedInstanceState.getString("ID");
//            SpinnerService.initSpinner(context, country, state, city, pincode, strCountry, strState, strCity, strPincode);
//        }
//    }

}