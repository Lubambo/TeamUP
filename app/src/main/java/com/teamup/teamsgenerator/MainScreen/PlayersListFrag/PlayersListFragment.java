package com.teamup.teamsgenerator.MainScreen.PlayersListFrag;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.teamup.teamsgenerator.Database.DBSQLiteHelper;
import com.teamup.teamsgenerator.Beans.Player;
import com.teamup.teamsgenerator.PlayerProfileScreen.PlayerProfileScreen;
import com.teamup.teamsgenerator.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayersListFragment extends Fragment {

    private DBSQLiteHelper db;
    private List<Player> playersList;
    private ListView playersListView;

    public PlayersListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_players_list, container, false);

        playersList = new ArrayList<Player>();
        playersListView = (ListView) view.findViewById(R.id.player_listview);

        assemblyList();
        listClickHandler();

        return view;

    }

    private void assemblyList(){

        db = new DBSQLiteHelper(getActivity(), null, null, 1);
        playersList = db.readPlayers();

        ListAdapter listAdapter = new PlayerListAdapter(getActivity(), playersList);

        playersListView.setAdapter(listAdapter);

    }

    private void listClickHandler(){

        playersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Player clickedPlayer = (Player) playersListView.getItemAtPosition(position);

                Intent it = new Intent(getActivity(), PlayerProfileScreen.class);
                it.putExtra("PLAYER_NAME", clickedPlayer.getPlayerName());
                it.putExtra("PLAYER_CONTACT", clickedPlayer.getPlayerContact());
                it.putExtra("PLAYER_SKILL", clickedPlayer.getSkillValue());
                startActivity(it);

                getActivity().finish();
            }
        });

    }

}
