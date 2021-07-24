package com.dynamo.sporter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dynamo.sporter.adapter.PlayersListAdapter;
import com.dynamo.sporter.model.Player;
import com.dynamo.sporter.profilesfragments.ClubPlayerProfileFragment;
import com.dynamo.sporter.service.PlayerService;
import com.dynamo.sporter.service.PlayerServiceImpl;
import com.dynamo.sporter.shared.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PlayersListFragment extends Fragment {

    ListView clubPlayers;
    Toolbar toolbar;
    FirebaseFirestore db;
    private Context context;
    PlayerService playerService;
    private LinearLayout progressBarLayout;
    private String clubID;

    public PlayersListFragment(String clubID) {
        this.clubID = clubID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sptr_players_list_fragment, container, false);
        setHasOptionsMenu(true);
        progressBarLayout = (LinearLayout) view.findViewById(R.id.sptr_progress_bar_layout);

        clubPlayers = view.findViewById(R.id.sptr_club_players_listview);
        toolbar = view.findViewById(R.id.sptr_players_list_toolbar);
        db = FirebaseFirestore.getInstance();

        playerService = new PlayerServiceImpl();

        AppCompatActivity activity = ((AppCompatActivity)getActivity());
        if (activity != null)
            activity.setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();
            }
        });
        List<Player> players = new ArrayList<>();
        setClubPlayers(players);
        clubPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity) getActivity()).navigateTo(new ClubPlayerProfileFragment(players.get(i)), true);
            }
        });
        return view;
    }

    private void setClubPlayers(List<Player> players) {

        db.collection("players").whereEqualTo("clubid", this.clubID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        Player player = new Player(doc.get("firstname").toString(),doc.get("lastname").toString(),doc.get("age").toString(),doc.get("email").toString(),doc.get("phoneNo").toString(),doc.get("preferredSports").toString(),doc.get("gender").toString(),doc.get("country").toString(),doc.get("state").toString(),doc.get("city").toString(),doc.get("pincode").toString(), doc.get("description").toString(),doc.get("clubid").toString(),(Boolean)doc.get("clubAdmin"));
                        player.setId(doc.getId());
                        if(player.getId().equals(SharedPrefManager.getInstance(context).getUserId()))
                            players.add(0, player);
                        else
                            players.add(player);
                    }
                    if (players.size() > 0) {
                        PlayersListAdapter playersListAdapter = new PlayersListAdapter(context, players);
                        clubPlayers.setAdapter(playersListAdapter);
                    }
                }
                progressBarLayout.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.sptr_add:
                if (!playerService.isAdmin(context)) {
                    Log.i("Delete Club", "You dont have admin permissions");
                } else {
                    ((MainActivity) context).navigateTo(new ShowAvailablePlayersFragment(), true);
                }
        }
        return true;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}