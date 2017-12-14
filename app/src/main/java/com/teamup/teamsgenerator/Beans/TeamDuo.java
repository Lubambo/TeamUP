package com.teamup.teamsgenerator.Beans;

/**
 * Created by Felipe on 06/06/2017.
 */
public class TeamDuo {

    private Team teamA;
    private Team teamB;

    public TeamDuo(Team teamA, Team teamB){
        setTeamA(teamA);
        setTeamB(teamB);
    }

    public Team getTeamA() {
        return teamA;
    }

    public void setTeamA(Team teamA) {
        this.teamA = teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public void setTeamB(Team teamB) {
        this.teamB = teamB;
    }
}
