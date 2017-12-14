package com.teamup.teamsgenerator.MatchRegistrationScreen;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.teamup.teamsgenerator.Database.DBSQLiteHelper;
import com.teamup.teamsgenerator.MainScreen.ListsScreen;
import com.teamup.teamsgenerator.Beans.Match;
import com.teamup.teamsgenerator.Beans.Player;
import com.teamup.teamsgenerator.R;

import java.util.ArrayList;
import java.util.List;

public class MatchRegScreen extends AppCompatActivity {

    private DBSQLiteHelper db;

    private EditText matchNameEditTxt;

    private TextView playersQtyTxtView;

    private List<Player> playersList;
    private ListView playersListView;

    private Button saveBtn;
    private Button cancelBtn;

    private Intent it;
    private Bundle check;

    private List<Player> originalMatchPlayers;
    private String originalName;
    private String matchName;
    private int playersQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_reg_screen);

        db = new DBSQLiteHelper(getApplicationContext(), null, null, 1);

        matchNameEditTxt = (EditText) findViewById(R.id.match_name_edit_text);

        playersQtyTxtView = (TextView) findViewById(R.id.match_profile_players_qty_text_view);

        playersList = new ArrayList<Player>();
        playersListView = (ListView) findViewById(R.id.match_players_listview);

        saveBtn = (Button) findViewById(R.id.save_button);
        cancelBtn = (Button) findViewById(R.id.cancel_button);

        playersQty = 0;

        it = getIntent();
        check = it.getExtras();

        syncEditionPlayerInfos();
        assemblyList();
        playerListClickHandler();
        saveBtnClickHandler();
        cancelBtnClickHandler();
        screenTouch();

    }

    private void syncEditionPlayerInfos(){
        if(check != null){
            originalName = it.getStringExtra("MATCH_NAME");

            matchNameEditTxt.setText(originalName);
        }
    }

    private void assemblyList(){
        playersList = db.readPlayers();
        if(check != null) {
            playersList = db.readPlayers();
            originalMatchPlayers = db.matchPlayers(originalName);

            playersQty = originalMatchPlayers.size();
            playersQtyTxtView.setText(String.valueOf(originalMatchPlayers.size()));

            for(int i = 0; i < playersList.size(); i++){
                for(int j = 0; j < originalMatchPlayers.size(); j++){
                    if(originalMatchPlayers.get(j).getPlayerName().equalsIgnoreCase(playersList.get(i).getPlayerName())){
                        playersList.get(i).setSelected(true);
                    }
                }
            }
        }

        ListAdapter listAdapter = new MatchPlayersListAdapter(getApplicationContext(), playersList);
        playersListView.setAdapter(listAdapter);
    }

    private void playerListClickHandler(){

        playersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Player clickedPlayer = (Player) playersListView.getItemAtPosition(position);

                ImageView selectionIcon = (ImageView) view.findViewById(R.id.player_skill);

                if(clickedPlayer.isSelected()){
                    clickedPlayer.setSelected(false);
                    selectionIcon.setImageResource(R.mipmap.player_is_not_selected);

                    playersQty--;
                    playersQtyTxtView.setText(String.valueOf(playersQty));
                }
                else{
                    clickedPlayer.setSelected(true);
                    selectionIcon.setImageResource(R.mipmap.player_is_selected);

                    playersQty++;
                    playersQtyTxtView.setText(String.valueOf(playersQty));
                }

            }
        });

    }

    private void saveBtnClickHandlerRegistration(){

        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                matchName = matchNameEditTxt.getText().toString();
                matchName = matchName.replaceAll("\\s+", " ");   //transforma excesso de espaços em branco por um único espaço em branco;
                matchName = matchName.replaceAll("^\\s+", "");   //remove espaços em branco antes do nome;
                matchName = matchName.replaceAll("\\s+$", "");   //remove espaços em branco depois do nome;

                //Verifica se o textview do nome do local da partida não está vazio
                if(matchName.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Partida sem nome do local", Toast.LENGTH_LONG).show();
                }
                else{

                    boolean alreadyExists = db.searchMatch(matchName);

                    //Verifica se o nome inserido para o local da partida já exista no banco de dados
                    if(alreadyExists){
                        Toast.makeText(getApplicationContext(), "Já existe partida neste local", Toast.LENGTH_LONG).show();
                    }
                    else{
                        if(playersQty < 4){
                            Toast.makeText(getApplicationContext(), "Deve haver no mínimo quatro jogadores na partida", Toast.LENGTH_LONG).show();
                        }
                        else{
                            List<Player> matchPlayers = mountMatchPlayersList();

                            Match match = new Match(matchName, matchPlayers);
                            db.addMatch(match);
                            db.addPlayer_Match(match);

                            Intent it = new Intent(MatchRegScreen.this, ListsScreen.class);
                            startActivity(it);
                            finish();
                        }

                    }

                }

            }
        });

    }
    private void saveBtnClickHandlerEdition(){
        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                matchName = matchNameEditTxt.getText().toString();
                matchName = matchName.replaceAll("\\s+", " ");   //transforma excesso de espaços em branco por um único espaço em branco;
                matchName = matchName.replaceAll("^\\s+", "");   //remove espaços em branco antes do nome;
                matchName = matchName.replaceAll("\\s+$", "");   //remove espaços em branco depois do nome;

                //Verifica se o textview do nome do local da partida não está vazio
                if(matchName.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Partida sem nome do local", Toast.LENGTH_LONG).show();
                }
                else{
                    //Se o usuário não mudou o nome do local da partida, não é preciso verificar no banco de dados
                    if(matchName.equalsIgnoreCase(originalName)){
                        if(playersQty < 4){
                            Toast.makeText(getApplicationContext(), "Deve haver no mínimo quatro jogadores na partida", Toast.LENGTH_LONG).show();
                        }
                        else{
                            List<Player> matchPlayers = mountMatchPlayersList();
                            db.editMatch(originalName, originalMatchPlayers, matchName, matchPlayers);

                            Intent it = new Intent(MatchRegScreen.this, ListsScreen.class);
                            startActivity(it);
                            finish();
                        }
                    }
                    else{
                        boolean alreadyExists = db.searchMatch(matchName);

                        //Verifica se o nome inserido para o jogador já exista no banco de dados
                        if(alreadyExists){
                            Toast.makeText(getApplicationContext(), "Já existe partida neste local", Toast.LENGTH_LONG).show();
                        }
                        else{
                            if(playersQty < 4){
                                Toast.makeText(getApplicationContext(), "Deve haver no mínimo quatro jogadores na partida", Toast.LENGTH_LONG).show();
                            }
                            else{
                                List<Player> matchPlayers = mountMatchPlayersList();
                                db.editMatch(originalName, originalMatchPlayers, matchName, matchPlayers);

                                Intent it = new Intent(MatchRegScreen.this, ListsScreen.class);
                                startActivity(it);
                                finish();
                            }
                        }
                    }
                }
            }
        });
    }
    private void saveBtnClickHandler(){
        if(check == null){
            saveBtnClickHandlerRegistration();
        }
        else{
            saveBtnClickHandlerEdition();
        }
    }

    private void cancelBtnClickHandler(){

        cancelBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                Intent it = new Intent(MatchRegScreen.this, ListsScreen.class);
                startActivity(it);
                finish();

            }

        });

    }

    @Override
    public void onBackPressed(){
        Intent it = new Intent(MatchRegScreen.this, ListsScreen.class);
        startActivity(it);
        finish();
    }

    private List<Player> mountMatchPlayersList(){
        List<Player> matchPlayers = new ArrayList<Player>();

        for(int i = 0; i < playersList.size(); i++){

            if(playersList.get(i).isSelected()){
                matchPlayers.add(playersList.get(i));
            }

        }

        return matchPlayers;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void screenTouch(){
        matchNameEditTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }
}
