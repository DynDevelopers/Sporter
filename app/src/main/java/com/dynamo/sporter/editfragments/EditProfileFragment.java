package com.dynamo.sporter.editfragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.dynamo.sporter.R;
import com.dynamo.sporter.model.Player;
import com.dynamo.sporter.service.SpinnerService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EditProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private TextInputEditText firstname, lastname, email, phoneno, description;
    private Spinner gender, age, country, state, city, pincode, preferredSport;
    private Button signUpButton;
    private String strGender, strCountry, strState, strCity, strPincode, strAge, strPreferredSport;
    private Context context;
    private FirebaseFirestore db;
    private String ID;
    private TextView signupText;

    public EditProfileFragment() {

    }
    public EditProfileFragment(String ID) {
        this.ID = ID;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sptr_create_players_fragment, container, false);

        db= FirebaseFirestore.getInstance();

        signupText = (TextView) view.findViewById(R.id.signup_text);

        signupText.setText("Update Profile");

        firstname = (TextInputEditText) view.findViewById(R.id.sptr_usr_firstname);
        lastname = (TextInputEditText) view.findViewById(R.id.sptr_usr_lastname);
        email = (TextInputEditText) view.findViewById(R.id.sptr_usr_email);

        phoneno = (TextInputEditText) view.findViewById(R.id.sptr_usr_phoneno);
        description = (TextInputEditText) view.findViewById(R.id.sptr_usr_description);

        ((TextInputLayout) view.findViewById(R.id.sptr_usr_pass_til)).setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.sptr_signin_textview)).setVisibility(View.GONE);
        email.setVisibility(View.GONE);

        preferredSport = (Spinner) view.findViewById(R.id.sptr_usr_preferred_sport);
        gender = (Spinner) view.findViewById(R.id.sptr_usr_gender_spinner);
        age = (Spinner) view.findViewById(R.id.sptr_usr_age_spinner);
        country = (Spinner) view.findViewById(R.id.sptr_usr_country_spinner);
        state = (Spinner) view.findViewById(R.id.sptr_usr_state_spinner);
        city = (Spinner) view.findViewById(R.id.sptr_usr_city_spinner);
        pincode = (Spinner) view.findViewById(R.id.sptr_usr_pincode_spinner);

        signUpButton = (Button) view.findViewById(R.id.sptr_signup_button);

        preferredSport.setOnItemSelectedListener(this);
        gender.setOnItemSelectedListener(this);
        age.setOnItemSelectedListener(this);
        country.setOnItemSelectedListener(this);
        state.setOnItemSelectedListener(this);
        city.setOnItemSelectedListener(this);
        pincode.setOnItemSelectedListener(this);

        signUpButton.setText(R.string.sptr_submit);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
        if (savedInstanceState == null)
            init();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()) {
            case R.id.sptr_usr_gender_spinner:
                strGender = gender.getSelectedItem().toString();
                break;
            case R.id.sptr_usr_age_spinner:
                strAge = age.getSelectedItem().toString();
                break;
            case R.id.sptr_usr_preferred_sport:
                strPreferredSport = preferredSport.getSelectedItem().toString();
                break;
            case R.id.sptr_usr_country_spinner:
                strCountry = country.getSelectedItem().toString();
                break;
            case R.id.sptr_usr_state_spinner:
                strState = state.getSelectedItem().toString();
                break;
            case R.id.sptr_usr_city_spinner:
                strCity = city.getSelectedItem().toString();
                break;
            case R.id.sptr_usr_pincode_spinner:
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
        db.collection("players").document(ID).get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isComplete() && task.getResult() != null) {
                    Player player = task.getResult().toObject(Player.class);
                    setDefaults(player);
                }
            }
        });
    }

    private void setDefaults(Player player) {
        firstname.setText(player.getFirstname());
        lastname.setText(player.getLastname());

        phoneno.setText(player.getPhoneNo());
        description.setText(player.getDescription());

        int pI = Arrays.asList(getResources().getStringArray(R.array.sports)).indexOf(player.getPreferredSports());
        int gI = Arrays.asList(getResources().getStringArray(R.array.gender)).indexOf(player.getGender());
        int aI = Arrays.asList(getResources().getStringArray(R.array.age)).indexOf(player.getAge());

        preferredSport.setSelection(pI);
        gender.setSelection(gI);
        age.setSelection(aI);

        strCountry = player.getCountry().trim();
        strState = player.getState();
        strCity = player.getCity().trim();
        strPincode = player.getPincode().trim();

        strAge = player.getAge();
        strGender = player.getGender();
        strPreferredSport = player.getPreferredSports();

        SpinnerService.initSpinner(context, country, state, city, pincode, strCountry, strState, strCity, strPincode);
    }

    private void updateProfile() {
        Map<String, Object> data = new HashMap<>();
        data.put("firstname", firstname.getText().toString());
        data.put("lastname", lastname.getText().toString());
        data.put("phoneNo", phoneno.getText().toString());
        data.put("preferredSports", strPreferredSport);
        data.put("age", strAge);
        data.put("country", strCountry);
        data.put("state", strState);
        data.put("city", strCity);
        data.put("gender", strGender);
        data.put("pincode", strPincode);

        db.collection("players").document(ID).update(data);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("ID", this.ID);
        outState.putString("firstname", firstname.getText().toString());
        outState.putString("lastname", lastname.getText().toString());
        outState.putString("phoneno", phoneno.getText().toString());
        outState.putString("description", description.getText().toString());
        outState.putString("country", strCountry);
        outState.putString("state", strState);
        outState.putString("city", strCity);
        outState.putString("pincode", strPincode);
        outState.putString("age", strAge);
        outState.putString("gender", strGender);
        outState.putString("preferredsport", strPreferredSport);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            firstname.setText(savedInstanceState.getString("firstname"));
            lastname.setText(savedInstanceState.getString("lastname"));
            phoneno.setText(savedInstanceState.getString("phoneno"));
            description.setText(savedInstanceState.getString("description"));

            strCountry = savedInstanceState.getString("country");
            strState = savedInstanceState.getString("state");
            strCity = savedInstanceState.getString("city");
            strPincode = savedInstanceState.getString("pincode");

            strAge = savedInstanceState.getString("age");
            strGender = savedInstanceState.getString("gender");
            strPreferredSport = savedInstanceState.getString("preferredsport");

            int pI = Arrays.asList(getResources().getStringArray(R.array.sports)).indexOf(strPreferredSport);
            int gI = Arrays.asList(getResources().getStringArray(R.array.gender)).indexOf(strGender);
            int aI = Arrays.asList(getResources().getStringArray(R.array.age)).indexOf(strAge);

            preferredSport.setSelection(pI);
            gender.setSelection(gI);
            age.setSelection(aI);

            this.ID = savedInstanceState.getString("ID");
            SpinnerService.initSpinner(context, country, state, city, pincode, strCountry, strState, strCity, strPincode);
        }
    }
}