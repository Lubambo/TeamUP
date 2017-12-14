package com.teamup.teamsgenerator.MainScreen.MatchListFrag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.teamup.teamsgenerator.Beans.Match;
import com.teamup.teamsgenerator.R;

import java.util.List;

/**
 * Created by Felipe on 03/06/2017.
 */
public class MatchListAdapter extends ArrayAdapter<Match> {

    public MatchListAdapter(Context context, List<Match> matches){
        super(context, R.layout.match_list_layout, matches);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater matchListInflater = LayoutInflater.from(getContext());
        View matchRowListView = matchListInflater.inflate(R.layout.match_list_layout, parent, false);

        TextView matchName = (TextView) matchRowListView.findViewById(R.id.match_name);
        TextView playerQty = (TextView) matchRowListView.findViewById(R.id.players_quantity);

        Match match = getItem(position);

        matchName.setText(match.getMatchName());
        playerQty.setText(String.valueOf(match.getPlayers().size()));

        return matchRowListView;

    }

}
