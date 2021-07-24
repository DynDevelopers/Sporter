package com.dynamo.sporter.createfragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.dynamo.sporter.R;
import com.dynamo.sporter.model.Club;
import com.dynamo.sporter.service.ClubService;
import com.dynamo.sporter.service.ClubServiceImpl;
import com.dynamo.sporter.shared.SharedPrefManager;
import com.dynamo.sporter.util.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CreateClubFragment extends Fragment {

    private EditText clubName, description;
    private Spinner country, state, city, pincode;
    private FirebaseFirestore db;
    private String strCountry = "", strState = "", strCity = "", strPincode = "";
    private ClubService clubService;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sptr_create_club_fragment, container, false);
        db = FirebaseFirestore.getInstance();

        clubName = (EditText) view.findViewById(R.id.sptr_club_name);
        description = (EditText) view.findViewById(R.id.sptr_club_description);

        country = (Spinner) view.findViewById(R.id.sptr_club_country_spinner);
        state = (Spinner) view.findViewById(R.id.sptr_club_state_spinner);
        city = (Spinner) view.findViewById(R.id.sptr_club_city_spinner);
        pincode = (Spinner) view.findViewById(R.id.sptr_club_pincode_spinner);

        setCountrySpinner();

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strCountry = adapterView.getItemAtPosition(i).toString();
                setStateSpinner(strCountry);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strState = adapterView.getItemAtPosition(i).toString();
                setCitySpinner(strState);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strCity = adapterView.getItemAtPosition(i).toString();
                setPincodeSpinner(strCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pincode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strPincode = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        clubService = new ClubServiceImpl(context);

        ((Button) view.findViewById(R.id.sptr_create_club_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createClub();
            }
        });
        return view;
    }

    private void setCountrySpinner() {
        List<String> countries = new ArrayList();
        db.collection("location").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult())
                        countries.add(doc.getId());
                    country.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, countries));
                }
            }
        });
    }

    private void setStateSpinner(String country) {
        List<String> states = new ArrayList<>();
        db.collection("location").document(country).collection("States").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc : task.getResult())
                    states.add(doc.getId());
                state.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, states));
            }
        });
    }

    private void setCitySpinner(String state) {
        List<String> cities = new ArrayList<>();
        db.collection("location").document(strCountry).collection("States").document(state).collection("Cities").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc : task.getResult())
                    cities.add(doc.getId());
                city.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cities));
            }
        });
    }

    private void setPincodeSpinner(String city) {
        Log.i("Selected City", city);
        List<String> pincodes = new ArrayList<>();
        db.collection("location").document(strCountry).collection("States").document(strState).collection("Cities").document(city).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (String pincode : (List<String>) task.getResult().get("pincodes"))
                        pincodes.add(pincode);
                    pincode.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, pincodes));
                }
            }
        });
    }

    private void createClub() {
        clubService.createClub(new Club(
                clubName.getText().toString(),
                SharedPrefManager.getInstance(getContext()).getUserId(),
                Utility.getTimestamp(),
                strCountry,
                strState,
                strCity,
                strPincode,
                description.getText().toString()
            )
        );
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}