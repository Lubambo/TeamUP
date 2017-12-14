package com.teamup.teamsgenerator.TeamsListScreen;

import com.teamup.teamsgenerator.Beans.Player;

import java.util.Comparator;

/**
 * Created by Felipe on 05/06/2017.
 */
public class TeamComparator implements Comparator<Player> {

    public int compare(Player p1, Player p2){
        if(p1.getSkillValue() < p2.getSkillValue()){
            return -1;
        }
        if(p1.getSkillValue() > p2.getSkillValue()){
            return 1;
        }
        return 0;
    }
}
