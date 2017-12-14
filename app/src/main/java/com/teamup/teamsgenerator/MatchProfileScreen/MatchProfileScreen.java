package com.teamup.teamsgenerator.MatchProfileScreen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.teamup.teamsgenerator.Beans.Player;
import com.teamup.teamsgenerator.Database.DBSQLiteHelper;
import com.teamup.teamsgenerator.MainScreen.ListsScreen;
import com.teamup.teamsgenerator.MatchRegistrationScreen.MatchRegScreen;
import com.teamup.teamsgenerator.R;
import com.teamup.teamsgenerator.TeamsListScreen.TeamComparator;
import com.teamup.teamsgenerator.TeamsListScreen.TeamListScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchProfileScreen extends AppCompatActivity {

    private DBSQLiteHelper db;

    private TextView matchNameTxtView;
    private TextView playersQtyTxtView;

    private Spinner playersPerTeamSpinner;

    private Switch balancedTeamsSwitch;

    private List<Player> playersList;
    private ListView playersListView;

    private ImageButton removeBtn;
    private ImageButton editBtn;
    private Button backBtn;
    private Button generateBtn;

    private String matchName;

    private Intent it;

    private List<Integer> spinnerElements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_profile_screen);

        it = getIntent();
        matchName = it.getStringExtra("MATCH_NAME");

        db = new DBSQLiteHelper(getApplicationContext(), null, null, 1);

        matchNameTxtView = (TextView) findViewById(R.id.match_profile_name_text_view);
        playersQtyTxtView = (TextView) findViewById(R.id.match_profile_players_qty_text_view);

        playersPerTeamSpinner = (Spinner) findViewById(R.id.match_profile_players_per_team_spinner);

        balancedTeamsSwitch = (Switch) findViewById(R.id.match_profile_balanced_switch);

        playersListView = (ListView) findViewById(R.id.match_profile_players_listview);

        removeBtn = (ImageButton) findViewById(R.id.sec_toolbar_remove_button);
        editBtn = (ImageButton) findViewById(R.id.sec_toolbar_edit_button);
        backBtn = (Button) findViewById(R.id.match_profile_goback_button);
        generateBtn = (Button) findViewById(R.id.match_profile_generate_button);

        assemblyList();
        syncInfos();
        backBtnClickHandler();
        removeBtnClickHandler();
        editBtnClickHandler();
        balanceSwitchClickHandler();
        generateBtnClickHandler();
    }

    private void assemblyList(){
        playersList = db.matchPlayers(matchName);

        ListAdapter listAdapter = new MatchPlayersListAdapter(getApplicationContext(), playersList);

        playersListView.setAdapter(listAdapter);
    }

    private void backBtnClickHandler(){

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MatchProfileScreen.this, ListsScreen.class);
                startActivity(it);

                finish();
            }
        });

    }

    @Override
    public void onBackPressed(){
        Intent it = new Intent(MatchProfileScreen.this, ListsScreen.class);
        startActivity(it);

        finish();
    }

    private void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MatchProfileScreen.this, R.style.MyDialogTheme);
        builder.setMessage("Remover partida?");
        builder.setCancelable(false);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteMatch(matchName, playersList);

                Intent it = new Intent(MatchProfileScreen.this, ListsScreen.class);
                startActivity(it);

                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void removeBtnClickHandler(){
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    private void editBtnClickHandler(){

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MatchProfileScreen.this, MatchRegScreen.class);
                it.putExtra("MATCH_NAME", matchName);
                startActivity(it);

                finish();
            }
        });

    }

    private void balanceSwitchClickHandler(){
        balancedTeamsSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(balancedTeamsSwitch.isChecked()){
                    Toast.makeText(getApplicationContext(), "Times balanceados", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Times não balanceados", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void generateBtnClickHandler(){

        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playersList.size() < 4){
                    Toast.makeText(getApplicationContext(), "Partida com menos de 4 jogadores!", Toast.LENGTH_LONG).show();
                }
                else{
                    boolean balance = balancedTeamsSwitch.isChecked();
                    int playersPerTeam = spinnerElements.get(playersPerTeamSpinner.getSelectedItemPosition());

                    TeamComparator tc = new TeamComparator();
                    Collections.sort(playersList, tc);
                    Collections.reverse(playersList);

                    ArrayList<String> players = new ArrayList<String>();
                    for(int i = 0; i < playersList.size(); i++){
                        players.add(playersList.get(i).getPlayerName());
                    }

                    Intent it = new Intent(MatchProfileScreen.this, TeamListScreen.class);
                    it.putExtra("MATCH_NAME", matchName);
                    it.putStringArrayListExtra("MATCH_PLAYERS", players);
                    it.putExtra("MATCH_BALANCE", balance);
                    it.putExtra("PLAYERS_PER_TEAM", playersPerTeam);
                    startActivity(it);

                    finish();
                }
            }
        });

    }

    private void syncInfos(){
        //Nome do local da partida
        matchNameTxtView.setText(matchName);
        //Quantidade de jogadores na partida
        playersQtyTxtView.setText(String.valueOf(playersList.size()));

        //Opções de quantidade de jogadores em cada time
        spinnerElements = new ArrayList<Integer>();
        int spinnerLimit = playersList.size() / 2;
        for(int i = 2; i <= spinnerLimit; i++){
            spinnerElements.add(i);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.players_per_team_text, spinnerElements);
        playersPerTeamSpinner.setAdapter(adapter);
        playersPerTeamSpinner.setSelection(spinnerElements.size() - 1);

        //Ativa o balanceamento, é o estado padrão
        balancedTeamsSwitch.setChecked(true);
    }
}
