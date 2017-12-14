package com.teamup.teamsgenerator.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.teamup.teamsgenerator.Beans.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felipe on 01/06/2017.
 */
class PlayerDAO {

    public PlayerDAO(){

    }

    //Adiciona novo player ao banco de dados
    public void addPlayer(SQLiteOpenHelper sqLiteOpenHelper, Player player,
                          String TABLE_PLAYERS,
                          String COLUMN_PLAYERS_NAME,
                          String COLUMN_PLAYERS_SKILL,
                          String COLUMN_PLAYERS_CONTACT){

        //Abre conexão com o banco de dados
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        //Objeto que reune todos os elementos que serão inseridos
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYERS_NAME, player.getPlayerName());
        values.put(COLUMN_PLAYERS_SKILL, player.getSkillValue());
        values.put(COLUMN_PLAYERS_CONTACT, player.getPlayerContact());

        //Insere na tabela TABLE_PLAYERS(players) do banco de dados um novo registro(values)
        db.insert(TABLE_PLAYERS, null, values);

        //Encerra a conexão com o banco de dados
        db.close();
    }

    //Remove player do banco de dados
    public void deletePlayer(SQLiteOpenHelper sqLiteOpenHelper, Player player,
                             String TABLE_PLAYERS,
                             String COLUMN_PLAYERS_NAME){

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        db.execSQL("PRAGMA foreign_keys = ON");

        db.execSQL("DELETE FROM " + TABLE_PLAYERS + " WHERE " + COLUMN_PLAYERS_NAME + " = \"" + player.getPlayerName() + "\";");

        db.close();
    }

    //Pesquisa nome do player na tabela players
    public boolean searchPlayer(SQLiteOpenHelper sqLiteOpenHelper, String playerName,
                                String TABLE_PLAYERS,
                                String COLUMN_PLAYERS_NAME){

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        String QUERY = "SELECT * FROM " + TABLE_PLAYERS + " WHERE " + COLUMN_PLAYERS_NAME + " = \"" + playerName + "\" COLLATE NOCASE;";

        Cursor c = db.rawQuery(QUERY, null);

        c.moveToFirst();

        if(c.getCount() > 0){
            String dbName = c.getString(c.getColumnIndex(COLUMN_PLAYERS_NAME));

            System.out.println("playerName = " + playerName);
            System.out.println("dbName = " + dbName);

            c.close();
            db.close();

            if(dbName.equalsIgnoreCase(playerName)){
                System.out.println("true");
                return true;
            }
            else{
                System.out.println("false");
                return false;
            }
        }
        else{
            System.out.println("false");
            c.close();
            db.close();
            return false;
        }

    }

    //Retorna Lista com os players no banco de dados
    public List<Player> readPlayers(SQLiteOpenHelper sqLiteOpenHelper,
                                    String TABLE_PLAYERS,
                                    String COLUMN_PLAYERS_NAME,
                                    String COLUMN_PLAYERS_SKILL,
                                    String COLUMN_PLAYERS_CONTACT){

        List<Player> players = new ArrayList<Player>();

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        String QUERY = "SELECT * FROM " + TABLE_PLAYERS + " ORDER BY " + COLUMN_PLAYERS_NAME + " COLLATE NOCASE ASC;";

        Cursor c = db.rawQuery(QUERY, null);

        c.moveToFirst();

        while(!c.isAfterLast()){

            if(c.getString(c.getColumnIndex(COLUMN_PLAYERS_NAME)) != null){

                Player player = new Player(c.getString(c.getColumnIndex(COLUMN_PLAYERS_NAME)),
                        c.getInt(c.getColumnIndex(COLUMN_PLAYERS_SKILL)),
                        c.getString(c.getColumnIndex(COLUMN_PLAYERS_CONTACT)));

                players.add(player);
            }
            c.moveToNext();
        }

        c.close();
        db.close();
        return players;
    }

    //Edita player
    public void editPlayer(SQLiteOpenHelper sqLiteOpenHelper, String oldPlayerName, String newPlayerName, String newPlayerContact, int skillValue,
                           String TABLE_PLAYERS,
                           String COLUMN_PLAYERS_NAME,
                           String COLUMN_PLAYERS_CONTACT,
                           String COLUMN_PLAYERS_SKILL){

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        String QUERY = "UPDATE " + TABLE_PLAYERS + " SET " +
                       COLUMN_PLAYERS_NAME + " = \"" + newPlayerName + "\", " +
                       COLUMN_PLAYERS_CONTACT + " = \"" + newPlayerContact + "\", " +
                       COLUMN_PLAYERS_SKILL + " = " + skillValue +
                       " WHERE " + COLUMN_PLAYERS_NAME + " = \"" + oldPlayerName + "\";";

        db.execSQL(QUERY);

        db.close();

    }
}
