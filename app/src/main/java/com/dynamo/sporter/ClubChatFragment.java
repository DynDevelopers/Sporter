package com.dynamo.sporter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

import com.dynamo.sporter.model.ChatMessage;
import com.dynamo.sporter.model.Player;
import com.dynamo.sporter.shared.SharedPrefManager;
import com.dynamo.sporter.util.Utility;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ClubChatFragment extends Fragment implements View.OnClickListener {

    private ListView listOfMessages;
    private FirebaseFirestore db;
    private FirebaseDatabase msgDB;
    private EditText messageEdittext;
    private ImageButton sendButton;
    private FirebaseListAdapter<MyClubMessage> adapter;
    private Context context;
    private String clubID;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sptr_chat_fragment, container, false);
        msgDB = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();

        listOfMessages = (ListView) view.findViewById(R.id.sptr_list_of_messages);
        toolbar = (Toolbar) view.findViewById(R.id.sptr_chat_toolbar);

        messageEdittext = (EditText) view.findViewById(R.id.sptr_message);
        sendButton = (ImageButton) view.findViewById(R.id.sptr_send);
        clubID = SharedPrefManager.getInstance(context).getClubID().trim();

        toolbar.setTitle("Club Chats");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        adapter = new FirebaseListAdapter<MyClubMessage>((Activity) context, MyClubMessage.class, R.layout.club_chat_layout, FirebaseDatabase.getInstance().getReference().child("club").child("messages").child(clubID)) {
            @Override
            protected void populateView(View v, MyClubMessage model, int position) {
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);
                db.collection("players").document(model.getSenderID()).get().addOnCompleteListener(task -> {
                    if (task.isComplete() && task.getResult() != null) {

                        Player player = task.getResult().toObject(Player.class);
                        messageText.setText(model.getMessage());
                        String senderName = Utility.getFullName(player.getFirstname(), player.getLastname());
                        messageUser.setText(senderName);

                        messageTime.setText(model.getDatetime());
                    }

                });
            }
        };
        listOfMessages.setAdapter(adapter);

        sendButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void uploadMessage() {
        if (messageEdittext.getText().toString().isEmpty()) {
            Log.i("Message", "Empty Message");
            return;
        }
        DatabaseReference reference = msgDB.getReference();

        String senderID = SharedPrefManager.getInstance(context).getUserId().trim();
        MyClubMessage message = new MyClubMessage(messageEdittext.getText().toString(), senderID);
        reference.child("club").child("messages").child(clubID).push().setValue(message);
        messageEdittext.setText("");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sptr_send) {
            uploadMessage();
        }
    }

    static class MyClubMessage {
        private String message;
        private String senderID;
        private String datetime;

        public MyClubMessage() {}
        @SuppressLint("SimpleDateFormat")
        public MyClubMessage(String message, String senderID) {
            this.message = message;
            this.senderID = senderID;
            this.datetime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        }

        public String getMessage() {
            return message;
        }

        public String getSenderID() {
            return senderID;
        }

        public String getDatetime() {
            return datetime;
        }
    }
}