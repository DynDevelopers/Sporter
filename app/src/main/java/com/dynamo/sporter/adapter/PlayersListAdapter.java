package com.dynamo.sporter.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dynamo.sporter.R;
import com.dynamo.sporter.model.Player;
import com.dynamo.sporter.util.Utility;

import java.util.List;

public class PlayersListAdapter implements ListAdapter {

    List<Player> players;
    Context context;
    public PlayersListAdapter(@NonNull Context context, List<Player> players) {
        this.context = context;
        this.players = players;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) { return true; }

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
    public long getItemId(int i) { return i; }

    @Override
    public boolean hasStableIds() { return false; }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            Log.i("Add Challenge", "getView: Add Challenge");
            convertView = LayoutInflater.from(context).inflate(R.layout.players_list_view, null);
            String firstname = players.get(position).getFirstname();
            String lastname = players.get(position).getLastname();
            ((TextView) convertView.findViewById(R.id.sptr_player_full_name)).setText(Utility.getFullName(firstname, lastname));
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
