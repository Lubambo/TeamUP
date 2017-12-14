package com.teamup.teamsgenerator.MatchProfileScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamup.teamsgenerator.Beans.Player;
import com.teamup.teamsgenerator.R;

import java.util.List;

/**
 * Created by Felipe on 16/06/2017.
 */

public class MatchPlayersListAdapter extends ArrayAdapter<Player> {

    public MatchPlayersListAdapter(Context context, List<Player> players){
        super(context, R.layout.match_players_list_layout, players);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater playerListInflater = LayoutInflater.from(getContext());
        View playerListRowView = playerListInflater.inflate(R.layout.match_players_list_layout, parent, false);

        TextView playerName = (TextView) playerListRowView.findViewById(R.id.player_name);
        ImageView skillImage = (ImageView) playerListRowView.findViewById(R.id.player_skill);

        Player player = getItem(position);

        playerName.setText(player.getPlayerName());
        assemblySkillImage(player.getSkillValue(), skillImage);

        return playerListRowView;

    }

    private void assemblySkillImage(int skillValue, ImageView imageView){

        switch(skillValue){
            case 1:
                imageView.setImageResource(R.mipmap.skill_value_one);
                break;

            case 2:
                imageView.setImageResource(R.mipmap.skill_value_two);
                break;

            case 3:
                imageView.setImageResource(R.mipmap.skill_value_three);
                break;

            case 4:
                imageView.setImageResource(R.mipmap.skill_value_four);
                break;

            case 5:
                imageView.setImageResource(R.mipmap.skill_value_five);
                break;
        }

    }

}
