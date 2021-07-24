package com.dynamo.sporter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.dynamo.sporter.model.Player;
import com.dynamo.sporter.util.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowAvailablePlayersFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private List<Player> players;
    FirebaseFirestore db;
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sptr_show_available_players_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.sptr_available_players_list_view);
        db = FirebaseFirestore.getInstance();
        players = new ArrayList<>();
        populateAvailablePlayers(players);
        listView.setOnItemClickListener(this);
        return view;
    }

    private void populateAvailablePlayers(List<Player> players) {
        db.collection("players").whereEqualTo("clubid", null).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        Player player = doc.toObject(Player.class);
                        player.setId(doc.getId());
                        players.add(player);
                    }
                    if (players.size() > 0)
                        listView.setAdapter(new AvailablePlayersListAdapter(players, context));
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ((MainActivity) context).navigateTo(new AvailablePlayerDetailFragment(players.get(i)), true);
    }

    class AvailablePlayersListAdapter implements ListAdapter {

        private List<Player> players;
        private Context context;

        public AvailablePlayersListAdapter(List<Player> players, Context context) {
            this.players = players;
            this.context = context;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int i) {
            return true;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) { }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) { }

        @Override
        public int getCount() {
            return players.size();
        }

        @Override
        public Object getItem(int i) {
            return players.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @SuppressLint({"InflateParams", "SetTextI18n"})
        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                Log.i("Add Challenge", "getView: Add Challenge");
                convertView = LayoutInflater.from(context).inflate(R.layout.players_list_view, null);
                String fullname = Utility.getFullName(players.get(position).getFirstname(), players.get(position).getFirstname());
                ((TextView) convertView.findViewById(R.id.sptr_player_full_name)).setText(fullname);
                ((TextView) convertView.findViewById(R.id.sptr_player_preferred_sport)).setText("Preferred Sports: "+players.get(position).getPreferredSports());
                ((TextView) convertView.findViewById(R.id.sptr_player_age)).setText("Age: " + players.get(position).getAge());
            }
            return convertView;
        }

        @Override
        public int getItemViewType(int i) {
            return i;
        }

        @Override
        public int getViewTypeCount() {
            return players.size();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }
}