package com.teamup.teamsgenerator.TeamsListScreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.teamup.teamsgenerator.Beans.Team;
import com.teamup.teamsgenerator.Beans.TeamDuo;
import com.teamup.teamsgenerator.MatchProfileScreen.MatchProfileScreen;
import com.teamup.teamsgenerator.R;

import java.util.ArrayList;
import java.util.List;

public class TeamListScreen extends AppCompatActivity {

    private List<String> players;
    private List<Team> teamList;
    private ListView teamsListView;

    private Button backBtn;
    private Button sendBtn;

    private Intent it;

    private String matchName;

    private boolean balance;

    private int playersPerTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list_screen);

        teamsListView = (ListView) findViewById(R.id.team_list_screen_list_view);

        backBtn = (Button) findViewById(R.id.team_list_screen_goback_button);
        sendBtn = (Button) findViewById(R.id.team_list_screen_send_button);

        it = getIntent();
        matchName = it.getStringExtra("MATCH_NAME");
        players = it.getStringArrayListExtra("MATCH_PLAYERS");
        balance = it.getBooleanExtra("MATCH_BALANCE", true);
        playersPerTeam = it.getIntExtra("PLAYERS_PER_TEAM", (players.size() / 2));

        assemblyList();
        backBtnClickHandler();
        sendBtnClickHandler();
    }

    private void assemblyListBalanced(){

        List<Team> teamsBag = new ArrayList<Team>();

        int index = 0;														//Controlador do loop
        int counter = 0;													//Verifica o time que o loop se encontra
        int teamNumeration = 0;                                             //Insere a numeração dos times

        int teamQty = (players.size() / playersPerTeam);	                //Quantidade de times;

        int convokedPlayers = teamQty * playersPerTeam;						//Quantidade máximas de jogadores que serão convocados
        int remainingPlayers = players.size() % playersPerTeam;             //Quantidade de jogadores que sobram na divisão

        System.out.println("Inicial Convoked Players = " + String.valueOf(convokedPlayers));
        System.out.println("Inicial Remaining Players = " + String.valueOf(remainingPlayers));

        boolean forward = true;												//Controla o fluxo da variável counter

        String teamName = null;												//Recebe o nome genérico do time

        do{
            //adiciona time no array de times (teamsBag)
            teamName = "Time " + String.valueOf(teamNumeration + 1);
            Team newTeam = new Team(teamName);
            teamsBag.add(newTeam);
            teamNumeration++;
        }while(teamNumeration < teamQty);

        //Distribui os jogadores nos times
        do{
            //Se o time não possui uma quantidade de jogadores == playersPerTeam, o jogador é adicionado OLHA ESSE -1 AEEEE
            if(teamsBag.get(counter).getPlayers().size() < playersPerTeam){
                teamsBag.get(counter).getPlayers().add(players.get(index));

                index++;
                convokedPlayers--;

                if( ( counter == (teamQty - 1) && (teamsBag.get(counter).getPlayers().size() < playersPerTeam) ) ){
                    teamsBag.get(counter).getPlayers().add(players.get(index));

                    index++;
                    convokedPlayers--;
                }
            }

            //Alterna o sentido que o array de times é percorrido.
            //Possibilita uma distribuição satisfatória (não ótima) dos jogadores
            //(teamQty - 1) == índice do último time no array de times
            if(counter == 0){
                forward = true;
            }
            if(counter == (teamQty - 1)){
                forward = false;
            }

            if(forward){
                counter++;
            }
            else{
                counter--;
            }

        }while( (index < players.size()) && (convokedPlayers > 0) );

        //Se houver jogadores sobrando, será criado um time com estes
        if(remainingPlayers > 0){
            teamName = "Time " + String.valueOf(teamNumeration + 1);
            Team newTeam = new Team(teamName);
            teamsBag.add(newTeam);

            do{
                teamsBag.get(teamsBag.size() - 1).getPlayers().add(players.get(index));
                index++;
                remainingPlayers--;
            }while((index < players.size()) && remainingPlayers > 0);
        }

        List<TeamDuo> teamDuos = makeTeamDuoList(teamsBag);

        ListAdapter adapter = new TeamListAdapter(getApplicationContext(), teamDuos);
        teamsListView.setAdapter(adapter);

    }
    private void assemblyListUnbalanced(){

        List<Team> teamsBag = new ArrayList<Team>();

        int index = 0;														//Controlador do loop
        int counter = 0;													//Verifica o time que o loop se encontra
        int teamNumeration = 0;                                             //Insere a numeração dos times

        int teamQty = (players.size() / playersPerTeam);	                //Quantidade de times;

        int convokedPlayers = teamQty * playersPerTeam;						//Quantidade máximas de jogadores que serão convocados
        int remainingPlayers = players.size() % playersPerTeam;             //Quantidade de jogadores que sobram na divisão

        String teamName = null;												//Recebe o nome genérico do time

        do{
            //adiciona time no array de times (teamsBag)
            teamName = "Time " + String.valueOf(teamNumeration + 1);
            Team newTeam = new Team(teamName);
            teamsBag.add(newTeam);
            teamNumeration++;
        }while(teamNumeration < teamQty);

        //Embaralha o array de jogadores, para garantir aleatoriedade
        for(int i = 0; i < players.size(); i++){
            int seed = i + (int)(Math.random() * (players.size() - i));

            String temp = players.get(seed);
            players.set(seed, players.get(i));
            players.set(i, temp);
        }

        do{
            //Se o time não possui uma quantidade de jogadores == playersPerTeam
            if(teamsBag.get(counter).getPlayers().size() < playersPerTeam){
                teamsBag.get(counter).getPlayers().add(players.get(index));

                index++;
                convokedPlayers--;

                if(teamsBag.get(counter).getPlayers().size() == playersPerTeam){
                    counter++;
                }
            }
        }while( (index < players.size()) && (convokedPlayers > 0) );

        //Se houver jogadores sobrando, será criado um time com estes
        if(remainingPlayers > 0){
            teamName = "Time " + String.valueOf(teamNumeration + 1);
            Team newTeam = new Team(teamName);
            teamsBag.add(newTeam);

            do{
                teamsBag.get(teamsBag.size() - 1).getPlayers().add(players.get(index));
                index++;
            }while((index < players.size()) && remainingPlayers > 0);
        }

        List<TeamDuo> teamDuos = makeTeamDuoList(teamsBag);

        ListAdapter adapter = new TeamListAdapter(getApplicationContext(), teamDuos);
        teamsListView.setAdapter(adapter);

    }
    private void assemblyList(){
        if(balance){
            assemblyListBalanced();
        }
        else{
            assemblyListUnbalanced();
        }
    }

    private List<TeamDuo> makeTeamDuoList(List<Team> teams){

        teamList = teams;
        List<TeamDuo> teamDuos = new ArrayList<TeamDuo>();

        for(int i = 0; i < teams.size(); i+=2){
            TeamDuo td;
            if(i + 1 < teams.size()){
                td = new TeamDuo(teams.get(i), teams.get(i + 1));
            }
            else{
                td = new TeamDuo(teams.get(i), null);
            }
            teamDuos.add(td);
        }
        return teamDuos;
    }

    private void backBtnClickHandler(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(TeamListScreen.this, MatchProfileScreen.class);
                it.putExtra("MATCH_NAME", matchName);
                startActivity(it);

                finish();
            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent it = new Intent(TeamListScreen.this, MatchProfileScreen.class);
        it.putExtra("MATCH_NAME", matchName);
        startActivity(it);

        finish();
    }

    private void sendBtnClickHandler(){
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTeamsViaText(teamList);
            }
        });
    }

    private void sendTeamsViaText(List<Team> teams) {
        String teamsText = " -- TIMES -- \n";

        for(int i = 0; i < teams.size(); i++){
            teamsText = teamsText + "-------\n";
            teamsText = teamsText + teams.get(i).getTeamName() + "\n";
            teamsText = teamsText + "-------\n";
            for(int j = 0; j < teams.get(i).getPlayers().size(); j++){
                teamsText = teamsText + " " + teams.get(i).getPlayers().get(j) + "\n";
            }
            teamsText = teamsText + "\n";
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, teamsText);
        sendIntent.setType("text/plain");
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(sendIntent);
        }
    }

}
