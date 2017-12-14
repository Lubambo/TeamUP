package com.teamup.teamsgenerator.MainScreen.MatchListFrag;


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
import com.teamup.teamsgenerator.Beans.Match;
import com.teamup.teamsgenerator.MatchProfileScreen.MatchProfileScreen;
import com.teamup.teamsgenerator.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatchesListFragment extends Fragment {

    private DBSQLiteHelper db;
    private List<Match> matchesList;
    private ListView matchesListView;

    public MatchesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_matches_list, container, false);

        matchesList = new ArrayList<Match>();
        matchesListView = (ListView) view.findViewById(R.id.match_listview);

        assemblyList();
        listClickHandler();

        return view;
    }

    private void assemblyList(){

        db = new DBSQLiteHelper(getActivity(), null, null, 1);
        matchesList = db.readMatches();

        ListAdapter listAdapter = new MatchListAdapter(getActivity(), matchesList);

        matchesListView.setAdapter(listAdapter);

    }


    private void listClickHandler(){

        matchesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Match clickedMatch = (Match) matchesListView.getItemAtPosition(position);

                Intent it = new Intent(getActivity(), MatchProfileScreen.class);
                it.putExtra("MATCH_NAME", clickedMatch.getMatchName());
                startActivity(it);

                getActivity().finish();
            }
        });

    }

}
