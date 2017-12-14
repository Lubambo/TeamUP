package com.teamup.teamsgenerator.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felipe on 05/06/2017.
 */
public class Team {

    private String teamName;
    private List<String> players;

    public Team(){
        players = new ArrayList<String>();
    }
    public Team(String teamName){
        setTeamName(teamName);
        players = new ArrayList<String>();
    }
    public Team(String teamName, List<String> players){
        setTeamName(teamName);
        setPlayers(players);
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }
}
