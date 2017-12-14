package com.teamup.teamsgenerator.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felipe on 31/05/2017.
 */
public class Match {

    private String matchName;
    private List<Player> players;

    public Match(){

    }

    public Match(String matchName){
        setMatchName(matchName);
        players = new ArrayList<Player>();
    }

    public Match(String matchName, List<Player> players){
        setMatchName(matchName);
        setPlayers(players);
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
