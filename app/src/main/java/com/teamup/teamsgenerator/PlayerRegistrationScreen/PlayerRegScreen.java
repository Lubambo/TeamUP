package com.teamup.teamsgenerator.PlayerRegistrationScreen;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.teamup.teamsgenerator.Database.DBSQLiteHelper;
import com.teamup.teamsgenerator.MainScreen.ListsScreen;
import com.teamup.teamsgenerator.Beans.Player;
import com.teamup.teamsgenerator.R;

public class PlayerRegScreen extends AppCompatActivity {

    private DBSQLiteHelper db;

    private EditText playerNameEditTxt;
    //private EditText playerContactEditTxt;

    private RadioGroup skillRadioGrp;

    private Button saveBtn;
    private Button cancelBtn;

    private Intent it;
    private Bundle check;

    private String originalName;
    private String playerName;
    private String playerContact;
    private int skillValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_reg_screen);

        db = new DBSQLiteHelper(this, null, null, 1);

        playerNameEditTxt = (EditText) findViewById(R.id.player_name_edit_text);
        //playerContactEditTxt = (EditText)findViewById(R.id.player_contact_edit_text);

        skillRadioGrp = (RadioGroup) findViewById(R.id.skills_radio_group);

        saveBtn = (Button) findViewById(R.id.save_button);
        cancelBtn = (Button) findViewById(R.id.cancel_button);

        it = getIntent();
        check = it.getExtras();

        syncEditionPlayerInfos();
        saveBtnClickHandler();
        cancelBtnClickHandler();
        screenTouch();

    }

    private void syncEditionPlayerInfos(){
        if(check != null){
            originalName = it.getStringExtra("PLAYER_NAME");

            playerNameEditTxt.setText(originalName);
            //playerContactEditTxt.setText(it.getStringExtra("PLAYER_CONTACT"));

            switch(it.getIntExtra("PLAYER_SKILL", 1)){
                case 1:
                    skillRadioGrp.check(R.id.radio_skill_one);
                    break;

                case 2:
                    skillRadioGrp.check(R.id.radio_skill_two);
                    break;

                case 3:
                    skillRadioGrp.check(R.id.radio_skill_three);
                    break;

                case 4:
                    skillRadioGrp.check(R.id.radio_skill_four);
                    break;

                case 5:
                    skillRadioGrp.check(R.id.radio_skill_five);
                    break;
            }
        }
    }

    private void skillRadioGrpSwitch(){

        int checkedID = skillRadioGrp.getCheckedRadioButtonId();

        switch(checkedID){
            case R.id.radio_skill_one:
                skillValue = 1;
                break;

            case R.id.radio_skill_two:
                skillValue = 2;
                break;

            case R.id.radio_skill_three:
                skillValue = 3;
                break;

            case R.id.radio_skill_four:
                skillValue = 4;
                break;

            case R.id.radio_skill_five:
                skillValue = 5;
                break;
        }

    }

    private void saveBtnClickHandlerRegistration(){

        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                playerName = playerNameEditTxt.getText().toString();
                playerName = playerName.replaceAll("\\s+", " ");   //transforma excesso de espaços em branco por um único espaço em branco;
                playerName = playerName.replaceAll("^\\s+", "");   //remove espaços em branco antes do nome;
                playerName = playerName.replaceAll("\\s+$", "");   //remove espaços em branco depois do nome;

                //Verifica se o textview do nome do jogador não está vazio
                if(playerName.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Jogador sem nome", Toast.LENGTH_LONG).show();
                }
                else{
                    boolean alreadyExists = db.searchPlayer(playerName);

                    //Verifica se o nome inserido para o jogador já exista no banco de dados
                    if(alreadyExists){
                        Toast.makeText(getApplicationContext(), "Já existe jogador com este nome", Toast.LENGTH_LONG).show();
                    }
                    else{
                        skillRadioGrpSwitch();

                        //playerContact = playerContactEditTxt.getText().toString().toLowerCase();
                        playerContact = "-";

                        Player player = new Player(playerName, skillValue, playerContact);
                        db.addPlayer(player);

                        Intent it = new Intent(PlayerRegScreen.this, ListsScreen.class);
                        startActivity(it);
                        finish();
                    }
                }
            }
        });

    }
    private void saveBtnClickHandlerEdition(){

        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                playerName = playerNameEditTxt.getText().toString();
                playerName = playerName.replaceAll("\\s+", " ");   //transforma excesso de espaços em branco por um único espaço em branco;
                playerName = playerName.replaceAll("^\\s+", "");   //remove espaços em branco antes do nome;
                playerName = playerName.replaceAll("\\s+$", "");   //remove espaços em branco depois do nome;

                //Verifica se o textview do nome do jogador não está vazio
                if(playerName.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Jogador sem nome", Toast.LENGTH_LONG).show();
                }
                else{
                    //Se o usuário não mudou o nome do jogador, não é preciso verificar no banco de dados
                    if(playerName.equalsIgnoreCase(originalName)){
                        skillRadioGrpSwitch();

                        //playerContact = playerContactEditTxt.getText().toString().toLowerCase();
                        playerContact = "-";

                        db.editPlayer(originalName, playerName, playerContact, skillValue);

                        Intent it = new Intent(PlayerRegScreen.this, ListsScreen.class);
                        startActivity(it);
                        finish();
                    }
                    else{
                        boolean alreadyExists = db.searchPlayer(playerName);

                        //Verifica se o nome inserido para o jogador já exista no banco de dados
                        if(alreadyExists){
                            Toast.makeText(getApplicationContext(), "Já existe jogador com este nome", Toast.LENGTH_LONG).show();
                        }
                        else{
                            skillRadioGrpSwitch();

                            //playerContact = playerContactEditTxt.getText().toString().toLowerCase();
                            playerContact = "-";

                            db.editPlayer(originalName, playerName, playerContact, skillValue);

                            Intent it = new Intent(PlayerRegScreen.this, ListsScreen.class);
                            startActivity(it);
                            finish();
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

                Intent it = new Intent(PlayerRegScreen.this, ListsScreen.class);
                startActivity(it);
                finish();

            }

        });

    }

    @Override
    public void onBackPressed(){
        Intent it = new Intent(PlayerRegScreen.this, ListsScreen.class);
        startActivity(it);
        finish();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void screenTouch(){
        playerNameEditTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }
}
