package com.dynamo.sporter.editfragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dynamo.sporter.R;
import com.dynamo.sporter.model.Club;
import com.dynamo.sporter.service.ClubService;
import com.dynamo.sporter.service.SpinnerService;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditClubProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private EditText clubName, description;
    private Spinner country, state, city, pincode;
    private String strCountry = "", strState = "", strCity = "", strPincode = "";
    private ClubService clubService;
    private Context context;
    private String ID;
    private FirebaseFirestore db;

    public EditClubProfileFragment() { }
    public EditClubProfileFragment(String ID) {
        this.ID = ID;
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sptr_create_club_fragment, container, false);

        clubName = (EditText) view.findViewById(R.id.sptr_club_name);
        description = (EditText) view.findViewById(R.id.sptr_club_description);

        country = (Spinner) view.findViewById(R.id.sptr_club_country_spinner);
        state = (Spinner) view.findViewById(R.id.sptr_club_state_spinner);
        city = (Spinner) view.findViewById(R.id.sptr_club_city_spinner);
        pincode = (Spinner) view.findViewById(R.id.sptr_club_pincode_spinner);

        country.setOnItemSelectedListener(this);
        state.setOnItemSelectedListener(this);
        city.setOnItemSelectedListener(this);
        pincode.setOnItemSelectedListener(this);

        ((TextView) view.findViewById(R.id.sptr_create_club)).setText("UPDATE ClUB INFO");
        Button updateClubInfo = ((Button) view.findViewById(R.id.sptr_create_club_button));
        updateClubInfo.setText("Update");
        updateClubInfo.setOnClickListener(view1 -> updateClubInfo());

        db = FirebaseFirestore.getInstance();

        if (savedInstanceState == null)
            init();

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()) {
            case R.id.sptr_club_country_spinner:
                strCountry = country.getSelectedItem().toString();
                break;
            case R.id.sptr_club_state_spinner:
                strState = state.getSelectedItem().toString();
                break;
            case R.id.sptr_club_city_spinner:
                strCity = city.getSelectedItem().toString();
                break;
            case R.id.sptr_club_pincode_spinner:
                strPincode = pincode.getSelectedItem().toString();
                break;
            default:
                Log.i("Spinner onItemSelected", "Spinner onItemSelected");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void init() {
        db.collection("clubs").document(ID).get().addOnCompleteListener(task -> {
            if (task.isComplete() && task.getResult() != null) {
                Club club = task.getResult().toObject(Club.class);
                Log.i("Club", club.toString());
                setDefaults(club);
            }
        });
    }

    private void setDefaults(Club club) {
        clubName.setText(club.getName());
        description.setText(club.getDescription());

        strCountry = club.getCountry().trim();
        strState = club.getState().trim();
        strCity = club.getCity().trim();
        strPincode = club.getPincode().trim();

        SpinnerService.initSpinner(context, country, state, city, pincode, strCountry, strState, strCity, strPincode);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void updateClubInfo() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", clubName.getText().toString());
        data.put("description", description.getText().toString());
        data.put("country", strCountry);
        data.put("state", strState);
        data.put("city", strCity);
        data.put("pincode", strPincode);

        db.collection("clubs").document(ID).update(data);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("ID", this.ID);
        outState.putString("name", clubName.getText().toString());
        outState.putString("description", description.getText().toString());
        outState.putString("country", strCountry);
        outState.putString("state", strState);
        outState.putString("city", strCity);
        outState.putString("pincode", strPincode);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            clubName.setText(savedInstanceState.getString("name"));
            description.setText(savedInstanceState.getString("description"));

            strCountry = savedInstanceState.getString("country");
            strState = savedInstanceState.getString("state");
            strCity = savedInstanceState.getString("city");
            strPincode = savedInstanceState.getString("pincode");
            this.ID = savedInstanceState.getString("ID");
            SpinnerService.initSpinner(context, country, state, city, pincode, strCountry, strState, strCity, strPincode);
        }
    }
}
