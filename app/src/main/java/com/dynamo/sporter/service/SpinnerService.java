package com.dynamo.sporter.service;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SpinnerService {
    private SpinnerService() { }

    public static void initSpinner(Context context, Spinner country, Spinner state, Spinner city, Spinner pincodes, String defaultCountry, String defaultState, String defaultCity, String defaultPincode) {
        setCountrySpinner(context, country, defaultCountry);
        setStateSpinner(context, state, "India", defaultState);
        setCitySpinner(context, city, "India", "Maharashtra", defaultCity);
        setPincodeSpinner(context, pincodes, "India", "Maharashtra", "Mumbai", defaultPincode);
    }

    public static void initSpinner(Context context, Spinner country, Spinner state, Spinner city, Spinner pincodes) {
        setCountrySpinner(context, country, null);
        setStateSpinner(context, state, "India", null);
        setCitySpinner(context, city, "India", "Maharashtra", null);
        setPincodeSpinner(context, pincodes, "India", "Maharashtra", "Mumbai", null);
    }

    private static void setCountrySpinner(Context context, Spinner spinner, String defaultValue) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<String> countries = new ArrayList();
        db.collection("location").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    for (QueryDocumentSnapshot doc : task.getResult())
                        countries.add(doc.getId());
                    spinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, countries));
                    if (defaultValue != null)
                        spinner.setSelection(countries.indexOf(defaultValue));
                }
            }
        });
    }

    private static void setStateSpinner(Context context, Spinner spinner, String country, String defaultState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<String> states = new ArrayList<>();
        db.collection("location").document(country).collection("States").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc : task.getResult())
                    states.add(doc.getId());
                spinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, states));
                if (defaultState != null)
                    spinner.setSelection(states.indexOf(defaultState));
            }
        });
    }

    private static void setCitySpinner(Context context, Spinner spinner, String country, String state, String defaultCity) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<String> cities = new ArrayList<>();
        db.collection("location").document(country).collection("States").document(state).collection("Cities").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc : task.getResult())
                    cities.add(doc.getId());
                spinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, cities));
                if (defaultCity != null)
                    spinner.setSelection(cities.indexOf(defaultCity));
            }
        });
    }

    private static void setPincodeSpinner(Context context, Spinner spinner, String country, String state, String city, String defaultPincode) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<String> pincodes = new ArrayList<>();
        db.collection("location").document(country).collection("States").document(state).collection("Cities").document(city).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (String pincode : (List<String>) task.getResult().get("pincodes"))
                        pincodes.add(pincode);
                    spinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, pincodes));
                    if (defaultPincode != null)
                        spinner.setSelection(pincodes.indexOf(defaultPincode));
                }
            }
        });
    }
}
