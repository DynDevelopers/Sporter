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
import android.widget.TextView;

import com.dynamo.sporter.LoginFragment;
import com.dynamo.sporter.MainActivity;
import com.dynamo.sporter.R;
import com.dynamo.sporter.model.Player;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CreatePlayerFragment extends Fragment implements View.OnClickListener {

    private TextInputEditText firstname, lastname, email, password, phoneno, description;
    private Spinner gender, age, country, state, city, pincode, preferredSport;
    private FirebaseAuth mAuth;
    private Button signUpButton;
    private FirebaseFirestore db;
    String strGender = "", strCountry = "", strState = "", strCity = "", strPincode = "", strAge = "", strPreferredSport = "";
    private Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sptr_create_players_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        firstname = (TextInputEditText) view.findViewById(R.id.sptr_usr_firstname);
        lastname = (TextInputEditText) view.findViewById(R.id.sptr_usr_lastname);
        email = (TextInputEditText) view.findViewById(R.id.sptr_usr_email);
        password = (TextInputEditText) view.findViewById(R.id.sptr_usr_pass);
        phoneno = (TextInputEditText) view.findViewById(R.id.sptr_usr_phoneno);
        preferredSport = (Spinner) view.findViewById(R.id.sptr_usr_preferred_sport);
        gender = (Spinner) view.findViewById(R.id.sptr_usr_gender_spinner);
        age = (Spinner) view.findViewById(R.id.sptr_usr_age_spinner);
        country = (Spinner) view.findViewById(R.id.sptr_usr_country_spinner);
        state = (Spinner) view.findViewById(R.id.sptr_usr_state_spinner);
        city = (Spinner) view.findViewById(R.id.sptr_usr_city_spinner);
        pincode = (Spinner) view.findViewById(R.id.sptr_usr_pincode_spinner);
        description = (TextInputEditText) view.findViewById(R.id.sptr_usr_description);
        signUpButton = (Button) view.findViewById(R.id.sptr_signup_button);

        setCountrySpinner();
        signUpButton.setOnClickListener(this);

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

        age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strAge = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strGender = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        preferredSport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strPreferredSport = preferredSport.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ((TextView) view.findViewById(R.id.sptr_signin_textview)).setOnClickListener(this);
        return view;
    }

    private boolean isFirstnameEmpty() {
        if (firstname.getText().toString().isEmpty()) {
            firstname.setError("* required");
            return true;
        }
        return false;
    }

    private boolean isLastnameEmpty() {
        if (lastname.getText().toString().isEmpty()) {
            lastname.setError("* required");
            return true;
        }
        return false;
    }

    private boolean isEmailEmpty() {
        if (email.getText().toString().isEmpty()) {
            email.setError("* required");
            return true;
        }
        return false;
    }

    private boolean isPasswordEmpty() {
        if (password.getText().toString().isEmpty()) {
            password.setError("* required");
            return true;
        }
        return false;
    }

    private boolean isPhoneNoEmpty() {
        if (phoneno.getText().toString().isEmpty()) {
            phoneno.setError("* required");
            return true;
        }
        return false;
    }

    private boolean isDescriptionEmpty() {
        if (description.getText().toString().isEmpty()) {
            description.setError("* required");
            return true;
        }
        return false;
    }

    private boolean isInputValid() {
        return !(isFirstnameEmpty() || isLastnameEmpty() || isEmailEmpty() || isPasswordEmpty() || isPhoneNoEmpty() || isDescriptionEmpty());
    }

    private void userSignUp() {
        if (!isInputValid())
            return;
        Player player = new Player(firstname.getText().toString().toLowerCase(),
                lastname.getText().toString().toLowerCase(),
                strAge,
                email.getText().toString(),
                phoneno.getText().toString(),
                strPreferredSport,
                strGender,
                strCountry,
                strState,
                strCity,
                strPincode,
                description.getText().toString(),
                null,
                false);
        db.collection("players")
                .add(player)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                            Log.d("SignUp Success", "DocumentSnapshot added with ID: " + documentReference.getId());
                             addUser(documentReference.getId());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("SignUp Failure", "Error adding document", e);
                        }
                    });
    }

    private void addUser(String id) {
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("SignUp", "Successfull");
                            ((MainActivity) getActivity()).navigateTo(new LoginFragment(), false);
                        } else {
                            Log.w("SignUp", "signInWithEmail:failure", task.getException());
                            db.collection("players").document(id).delete().isSuccessful();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sptr_signup_button)
            userSignUp();

        if (view.getId() == R.id.sptr_signin_textview)
            ((MainActivity) getActivity()).navigateTo(new LoginFragment(), false);
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
}