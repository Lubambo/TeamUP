package com.teamup.teamsgenerator.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.teamup.teamsgenerator.Beans.Match;
import com.teamup.teamsgenerator.Beans.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felipe on 01/06/2017.
 */
class MatchDAO {

    public MatchDAO(){

    }

    //Adiciona novo player ao banco de dados
    public void addMatch(SQLiteOpenHelper sqLiteOpenHelper, Match match,
                         String TABLE_MATCHES,
                         String COLUMN_MATCHES_NAME){

        //Abre conexão com o banco de dados
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        //Objeto que reune todos os elementos que serão inseridos
        ContentValues values = new ContentValues();
        values.put(COLUMN_MATCHES_NAME, match.getMatchName());

        //Insere na tabela TABLE_MATCHES(matches) do banco de dados um novo registro(values)
        db.insert(TABLE_MATCHES, null, values);

        //Encerra a conexão com o banco de dados
        db.close();
    }

    //Remove match do banco de dados
    public void deleteMatch(SQLiteOpenHelper sqLiteOpenHelper, String matchName,
                            String TABLE_MATCHES,
                            String COLUMN_MATCHES_NAME  ){

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_MATCHES + " WHERE " + COLUMN_MATCHES_NAME + " = \"" + matchName + "\";");

        db.close();
    }

    //Pesquisa nome da match na tabela matches
    public boolean searchMatch(SQLiteOpenHelper sqLiteOpenHelper, String matchName,
                               String TABLE_MATCHES,
                               String COLUMN_MATCHES_NAME){

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        String QUERY = "SELECT * FROM " + TABLE_MATCHES + " WHERE " + COLUMN_MATCHES_NAME + " = \"" + matchName + "\" COLLATE NOCASE;";

        Cursor c = db.rawQuery(QUERY, null);

        c.moveToFirst();

        if(c.getCount() > 0){
            String dbName = c.getString(c.getColumnIndex(COLUMN_MATCHES_NAME));

            c.close();
            db.close();

            if(dbName.equalsIgnoreCase(matchName)){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            c.close();
            db.close();
            return false;
        }

    }

    //Retorna Lista com as matches no banco de dados
    public List<Match> readMatches(SQLiteOpenHelper sqLiteOpenHelper,
                                   String TABLE_PLAYERS, String COLUMN_PLAYERS_ID, String COLUMN_PLAYERS_NAME, String COLUMN_PLAYERS_SKILL, String COLUMN_PLAYERS_CONTACT,
                                   String TABLE_MATCHES, String COLUMN_MATCHES_ID, String COLUMN_MATCHES_NAME,
                                   String TABLE_PLMA, String COLUMN_PLMA_PLID, String COLUMN_PLMA_MAID){

        List<Match> matches = new ArrayList<Match>();

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        //Pesquisa todas as matches na tabela match
        String GET_MATCHES_QUERY = "SELECT * FROM " + TABLE_MATCHES + " ORDER BY " + COLUMN_MATCHES_NAME + " COLLATE NOCASE ASC;";

        Cursor c = db.rawQuery(GET_MATCHES_QUERY, null);
        c.moveToFirst();

        //Percorre a query que contém as matches
        while(!c.isAfterLast()){

            if(c.getString(c.getColumnIndex(COLUMN_MATCHES_NAME)) != null){

                String matchName = c.getString(c.getColumnIndex(COLUMN_MATCHES_NAME));

                List<Player> players = matchPlayers(db, matchName,
                                                    TABLE_PLAYERS, COLUMN_PLAYERS_ID, COLUMN_PLAYERS_NAME, COLUMN_PLAYERS_SKILL, COLUMN_PLAYERS_CONTACT,
                                                    TABLE_MATCHES, COLUMN_MATCHES_ID, COLUMN_MATCHES_NAME,
                                                    TABLE_PLMA, COLUMN_PLMA_PLID, COLUMN_PLMA_MAID);

                Match match = new Match(c.getString(c.getColumnIndex(COLUMN_MATCHES_NAME)), players);

                matches.add(match);
            }
            c.moveToNext();
        }

        c.close();
        db.close();

        return matches;
    }

    //Retorna os jogadores da match informada no parâmetro
    public List<Player> matchPlayers(SQLiteDatabase db, String matchName,
                                     String TABLE_PLAYERS, String COLUMN_PLAYERS_ID, String COLUMN_PLAYERS_NAME, String COLUMN_PLAYERS_SKILL, String COLUMN_PLAYERS_CONTACT,
                                     String TABLE_MATCHES, String COLUMN_MATCHES_ID, String COLUMN_MATCHES_NAME,
                                     String TABLE_PLMA, String COLUMN_PLMA_PLID, String COLUMN_PLMA_MAID){

        //Pesquisa todos os players correspondentes a match percorrida no loop
        String GET_MATCH_PLAYERS_QUERY = "SELECT DISTINCT " +
                TABLE_PLAYERS + "." + COLUMN_PLAYERS_NAME + ", " + TABLE_PLAYERS + "." + COLUMN_PLAYERS_SKILL + ", " + TABLE_PLAYERS + "." + COLUMN_PLAYERS_CONTACT +
                " FROM " + TABLE_PLAYERS +
                " INNER JOIN " + TABLE_PLMA + " ON " + TABLE_PLAYERS + "." + COLUMN_PLAYERS_ID + " = " + TABLE_PLMA + "." + COLUMN_PLMA_PLID +
                " INNER JOIN " + TABLE_MATCHES + " ON " + TABLE_PLMA + "." + COLUMN_PLMA_MAID + " =" +
                " (SELECT " + COLUMN_MATCHES_ID + " FROM " + TABLE_MATCHES + " WHERE " + COLUMN_MATCHES_NAME + " = \"" + matchName + "\")" +
                " ORDER BY " + TABLE_PLAYERS + "." + COLUMN_PLAYERS_NAME + " COLLATE NOCASE ASC;"; //se der pau olha esse COLLATE NOCASE ASC

        List<Player> players = new ArrayList<Player>();

        String QUERY = GET_MATCH_PLAYERS_QUERY;

        Cursor thisMatchPlayers = db.rawQuery(QUERY ,null);
        thisMatchPlayers.moveToFirst();

        //Percorre a query que contém os identificadores dos jogadores desta match
        while(!thisMatchPlayers.isAfterLast()){

            if(thisMatchPlayers.getString(thisMatchPlayers.getColumnIndex(COLUMN_PLAYERS_NAME)) != null){

                String playerName = thisMatchPlayers.getString(thisMatchPlayers.getColumnIndex(COLUMN_PLAYERS_NAME));
                int skillValue = thisMatchPlayers.getInt(thisMatchPlayers.getColumnIndex(COLUMN_PLAYERS_SKILL));
                String playerContact = thisMatchPlayers.getString(thisMatchPlayers.getColumnIndex(COLUMN_PLAYERS_NAME));

                Player matchPlayer = new Player(playerName, skillValue, playerContact);

                players.add(matchPlayer);
            }
            thisMatchPlayers.moveToNext();
        }

        thisMatchPlayers.close();

        return players;

    }

    //Edita match
    public void editMatch(SQLiteOpenHelper sqLiteOpenHelper, String oldMatchName, String newMatchName,
                          String TABLE_MATCHES, String COLUMN_MATCHES_NAME){

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        String QUERY = "UPDATE " + TABLE_MATCHES + " SET " + COLUMN_MATCHES_NAME + " = \"" + newMatchName + "\" " +
                "WHERE " + COLUMN_MATCHES_NAME + " = \"" + oldMatchName + "\";";

        db.execSQL(QUERY);

        db.close();

    }

    //Remove jogadores, na tabela player_match, relacionados a partida dada no parâmetro
    public void clearMatchPlayerlist(SQLiteDatabase db, String matchName, List<Player> players,
                                     String TABLE_PLAYERS, String COLUMN_PLAYERS_ID, String COLUMN_PLAYERS_NAME,
                                     String TABLE_MATCHES, String COLUMN_MATCHES_ID, String COLUMN_MATCHES_NAME,
                                     String TABLE_PLMA, String COLUMN_PLMA_PLID, String COLUMN_PLMA_MAID){

        for(int i = 0; i < players.size(); i++){

            String DELETE_QUERY = "DELETE FROM " + TABLE_PLMA + " WHERE " +
                    TABLE_PLMA + "." + COLUMN_PLMA_PLID + " = " +
                    "(SELECT " + COLUMN_PLAYERS_ID + " FROM " + TABLE_PLAYERS + " WHERE " + COLUMN_PLAYERS_NAME + " = \"" + players.get(i).getPlayerName() + "\") AND " +
                    TABLE_PLMA + "." + COLUMN_PLMA_MAID + " = " +
                    "(SELECT " + COLUMN_MATCHES_ID + " FROM " + TABLE_MATCHES + " WHERE " + COLUMN_MATCHES_NAME + " = \"" + matchName + "\");";

            db.execSQL(DELETE_QUERY);

        }

    }

}
