package com.dynamo.sporter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dynamo.sporter.createfragments.CreatePlayerFragment;
import com.dynamo.sporter.model.Player;
import com.dynamo.sporter.service.PlayerService;
import com.dynamo.sporter.service.PlayerServiceImpl;
import com.dynamo.sporter.shared.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class LoginFragment extends Fragment implements View.OnClickListener, OnCompleteListener<QuerySnapshot> {

    View view;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    private TextInputEditText emailEdittext, passEditText;
    String email, pass;
    Context context;
    private PlayerService playerService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.sptr_login_fragment, container, false);
        ((MaterialButton) view.findViewById(R.id.next_button)).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.sptr_navigateto_signup)).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        playerService = new PlayerServiceImpl();
        emailEdittext = (TextInputEditText) view.findViewById(R.id.sptr_signin_email);
        passEditText = (TextInputEditText) view.findViewById(R.id.sptr_signin_pass);
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sptr_navigateto_signup:
                ((MainActivity) getActivity()).navigateTo(new CreatePlayerFragment(), false);
                break;
            case R.id.next_button:
                usrSignIn();
                break;
        }
    }

    private boolean isEmailEmpty() {
        if (emailEdittext.getText().toString().isEmpty()) {
            emailEdittext.setError("* required");
            return true;
        }
        return false;
    }

    private boolean isPasswordEmpty() {
        if (passEditText.getText().toString().isEmpty()) {
            passEditText.setError("* required");
            return true;
        }
        return false;
    }

    private boolean isInputValid() {
        return !(isEmailEmpty() || isPasswordEmpty());
    }

    private void usrSignIn() {
        if (!isInputValid())
            return;
        email = emailEdittext.getText().toString();
        pass = passEditText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i("SignIn", "User Login Successfull");
                    getUserInfo(email);
                } else {
                    Log.i("SignIn", "Username or Password is incorrect");
                }
            }
        });
    }

    public void getUserInfo(String email) {
        FirebaseFirestore.getInstance().collection("players").whereEqualTo("email", email).get().addOnCompleteListener(this).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Get UserInfo", "Retrive user Data Failed");
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        List<DocumentSnapshot> snapshot = task.getResult().getDocuments();
        for (DocumentSnapshot doc : snapshot) {
            Player player = doc.toObject(Player.class);

            player.setId(doc.getId());

            SharedPrefManager.getInstance(this.context).userLogin(player);
            if (player.getClubid() == null)
                ((MainActivity) context).navigateTo(new PlayerHomeFragment(), false);
            else
                ((MainActivity) context).navigateTo(new ClubHomeFragment(), false);

            Log.i("Player Info", player.toString());
        }
    }
}