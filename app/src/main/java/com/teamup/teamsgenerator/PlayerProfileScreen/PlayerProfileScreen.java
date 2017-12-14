package com.teamup.teamsgenerator.PlayerProfileScreen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamup.teamsgenerator.Beans.Player;
import com.teamup.teamsgenerator.Database.DBSQLiteHelper;
import com.teamup.teamsgenerator.MainScreen.ListsScreen;
import com.teamup.teamsgenerator.PlayerRegistrationScreen.PlayerRegScreen;
import com.teamup.teamsgenerator.R;

public class PlayerProfileScreen extends AppCompatActivity {

    private DBSQLiteHelper db;

    private TextView playerNameTxtView;
    //private TextView playerContactTxtView;

    private ImageView playerSkillImgView;

    private ImageButton removeBtn;
    private ImageButton editBtn;
    private Button backBtn;

    private String playerName;
    private String playerContact;
    private int playerSkill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile_screen);

        db = new DBSQLiteHelper(getApplicationContext(), null, null, 1);

        playerNameTxtView = (TextView) findViewById(R.id.player_name_text_view);
        //playerContactTxtView = (TextView) findViewById(R.id.player_contact_text_view);

        playerSkillImgView = (ImageView) findViewById(R.id.player_skill_image_view);

        removeBtn = (ImageButton) findViewById(R.id.sec_toolbar_remove_button);
        editBtn = (ImageButton) findViewById(R.id.sec_toolbar_edit_button);
        backBtn = (Button) findViewById(R.id.player_profile_goback_button);

        playerName = getIntent().getStringExtra("PLAYER_NAME");
        playerContact = getIntent().getStringExtra("PLAYER_CONTACT");
        playerSkill = getIntent().getIntExtra("PLAYER_SKILL", 1);

        syncPlayerInfos();
        removeBtnClickHandler();
        editBtnClickHandler();
        backBtnClickHandler();
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

    private void syncPlayerInfos(){
        playerNameTxtView.setText(playerName);
        //playerContactTxtView.setText(playerContact);
        assemblySkillImage(playerSkill, playerSkillImgView);
    }

    private void backBtnClickHandler(){

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(PlayerProfileScreen.this, ListsScreen.class);
                startActivity(it);

                finish();
            }
        });

    }

    @Override
    public void onBackPressed(){
        Intent it = new Intent(PlayerProfileScreen.this, ListsScreen.class);
        startActivity(it);

        finish();
    }

    private void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerProfileScreen.this, R.style.MyDialogTheme);
        builder.setMessage("Remover partida?");
        builder.setCancelable(false);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Player player = new Player(playerName, playerSkill, playerContact);
                db.deletePlayer(player);

                Intent it = new Intent(PlayerProfileScreen.this, ListsScreen.class);
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
                Intent it = new Intent(PlayerProfileScreen.this, PlayerRegScreen.class);
                it.putExtra("PLAYER_NAME", playerName);
                it.putExtra("PLAYER_CONTACT", playerContact);
                it.putExtra("PLAYER_SKILL", playerSkill);
                startActivity(it);

                finish();
            }
        });

    }

}
