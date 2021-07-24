package com.dynamo.sporter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dynamo.sporter.model.Challenge;
import com.dynamo.sporter.model.ChatMessage;
import com.dynamo.sporter.model.Club;
import com.dynamo.sporter.model.Player;
import com.dynamo.sporter.shared.SharedPrefManager;
import com.dynamo.sporter.util.Utility;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.grpc.okhttp.internal.Util;

public class ChatFragment extends Fragment {

    private Challenge challenge;
    private Toolbar toolbar;
    private FirebaseListAdapter<ChatMessage> adapter;
    private Context context;

    private ListView listOfMessages;
    private FirebaseFirestore db;
    private FirebaseDatabase msgDB;
    private EditText messageEdittext;
    private ImageButton sendButton;
    private String clubID;

    public ChatFragment(Challenge challenge, String clubID) {
        this.clubID = clubID;
        this.challenge = challenge;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        msgDB = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();

        View view = inflater.inflate(R.layout.sptr_chat_fragment, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.sptr_chat_toolbar);


        String ID = this.clubID;
        if (!SharedPrefManager.getInstance(context).getClubID().equals(challenge.getClubID()))
            ID = challenge.getClubID().trim();

        db.collection("clubs").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isComplete() && task.getResult() != null)
                    toolbar.setTitle(task.getResult().toObject(Club.class).getName());
            }
        });

        messageEdittext = (EditText) view.findViewById(R.id.sptr_message);
        sendButton = (ImageButton) view.findViewById(R.id.sptr_send);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        listOfMessages = (ListView) view.findViewById(R.id.sptr_list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>((Activity) context, ChatMessage.class, R.layout.sptr_message_layout, FirebaseDatabase.getInstance().getReference().child(challenge.getID().trim()).child(clubID).child("messages")) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                TextView messageClubName = (TextView)v.findViewById(R.id.message_club_name);
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);
                db.collection("players").document(model.getMessageUserID()).get().addOnCompleteListener(task -> {
                    if (task.isComplete() && task.getResult() != null) {
                        Player player = task.getResult().toObject(Player.class);
                        messageClubName.setText(model.getMessageClubName().trim());
                        messageText.setText(model.getMessageText());
                        assert player != null;
                        String senderName = Utility.getFullName(player.getFirstname(), player.getLastname());
                        messageUser.setText(senderName);

                        messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm)",
                                model.getMessageTime()));
                    }
                });

            }
        };

        listOfMessages.setAdapter(adapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadMessage();
            }
        });

        if (!SharedPrefManager.getInstance(context).isClubAdmin()) {
            Log.i("Club Admin", String.valueOf(SharedPrefManager.getInstance(context).isClubAdmin()));
            messageEdittext.setHint("You don't have admin access");
            messageEdittext.setEnabled(false);
            sendButton.setEnabled(false);
        }
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void uploadMessage() {
        if (messageEdittext.getText().toString().isEmpty()) {
            Log.i("Message", "Empty Message");
            return;
        }
        DatabaseReference reference = msgDB.getReference();
        String firstname = SharedPrefManager.getInstance(context).getFirstname();
        String lastname = SharedPrefManager.getInstance(context).getLastname();
        Log.i("ID", clubID);
        db.collection("clubs").document(SharedPrefManager.getInstance(context).getClubID().trim()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isComplete() && task.getResult() != null) {
                    String senderID = SharedPrefManager.getInstance(context).getUserId().trim();
                    String senderClubName = task.getResult().get("name").toString();
                    ChatMessage message = new ChatMessage(messageEdittext.getText().toString().trim(), senderID, senderClubName);

                    reference.child(challenge.getID().trim()).child(clubID).child("messages").push().setValue(message);
                    reference.child(challenge.getID().trim()).child(clubID).child("updatetime").setValue(Utility.getTimestamp());
                    messageEdittext.setText("");
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}