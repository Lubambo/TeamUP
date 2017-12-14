package com.teamup.teamsgenerator.TeamsListScreen;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teamup.teamsgenerator.Beans.Team;
import com.teamup.teamsgenerator.Beans.TeamDuo;
import com.teamup.teamsgenerator.R;

import java.util.List;

/**
 * Created by Felipe on 05/06/2017.
 */
public class TeamListAdapter extends ArrayAdapter<TeamDuo> {

    private TextView teamANameTxtView;
    private TextView teamBNameTxtView;
    float textSize;

    private LinearLayout teamAPlayersListView;
    private LinearLayout teamBPlayersListView;

    private TeamDuo teamDuo;
    private Team teamA;
    private Team teamB;

    public TeamListAdapter(Context context, List<TeamDuo> teams) {
        super(context, R.layout.player_list_layout, teams);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater teamListInflater = LayoutInflater.from(getContext());
        View teamListRowView = teamListInflater.inflate(R.layout.team_list_layout, parent, false);

        teamANameTxtView = (TextView) teamListRowView.findViewById(R.id.team_a_text_view);
        teamBNameTxtView = (TextView) teamListRowView.findViewById(R.id.team_b_text_view);
        textSize = 16.0f;

        teamAPlayersListView = (LinearLayout) teamListRowView.findViewById(R.id.team_a_list_view);
        teamBPlayersListView = (LinearLayout) teamListRowView.findViewById(R.id.team_b_list_view);

        teamDuo = getItem(position);
        teamA = teamDuo.getTeamA();
        teamB = teamDuo.getTeamB();

        assemblyTeamA(parent);
        assemblyTeamB(parent);

        return teamListRowView;

    }

    private void assemblyTeamA(ViewGroup parent){
        if(teamA != null){
            teamANameTxtView.setText(teamA.getTeamName());

            for(int i = 0; i < teamA.getPlayers().size(); i++){
                TextView tv = new TextView(parent.getContext());
                tv.setText(teamA.getPlayers().get(i));
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                teamAPlayersListView.addView(tv);
            }
        }
    }

    private void assemblyTeamB(ViewGroup parent){
        if(teamB != null){
            teamBNameTxtView.setText(teamB.getTeamName());

            for(int i = 0; i < teamB.getPlayers().size(); i++){
                TextView tv = new TextView(parent.getContext());
                tv.setGravity(Gravity.RIGHT);
                tv.setText(teamB.getPlayers().get(i));
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                teamBPlayersListView.addView(tv);
            }
        }
    }

}
