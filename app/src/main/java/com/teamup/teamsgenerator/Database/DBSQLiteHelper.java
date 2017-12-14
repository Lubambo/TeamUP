package com.teamup.teamsgenerator.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.teamup.teamsgenerator.Beans.Match;
import com.teamup.teamsgenerator.Beans.Player;

import java.util.List;

//FAZER OS 'DAOs' das classes player e match, separá-las daqui.


/**
 * Created by Felipe on 31/05/2017.
 */
public class DBSQLiteHelper extends SQLiteOpenHelper {

    //Database constants
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TeamsGenDB.db";

    //Database Players table constants
    private static final String TABLE_PLAYERS = "player";
    private static final String COLUMN_PLAYERS_ID = "playerID";
    private static final String COLUMN_PLAYERS_NAME = "playerName";
    private static final String COLUMN_PLAYERS_SKILL = "skill";
    private static final String COLUMN_PLAYERS_CONTACT = "contact";

    //Database Matches table constants
    private static final String TABLE_MATCHES = "match";
    private static final String COLUMN_MATCHES_ID = "matchID";
    private static final String COLUMN_MATCHES_NAME = "matchName";

    //Database Player_Match table constants
    private static final String TABLE_PLMA = "player_match";
    private static final String COLUMN_PLMA_PLID = "playerID";
    private static final String COLUMN_PLMA_MAID = "matchID";

    private PlayerDAO playerDAO;
    private MatchDAO matchDAO;

    //private static final String[] COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_STATE};

    public DBSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

        playerDAO = new PlayerDAO();
        matchDAO = new MatchDAO();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create Players Table
        String CREATE_PLAYERS_TABLE = "CREATE TABLE " + TABLE_PLAYERS + "( " +
                COLUMN_PLAYERS_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_PLAYERS_NAME + " VARCHAR(30) NOT NULL, " +
                COLUMN_PLAYERS_SKILL + " TINYINT UNSIGNED NOT NULL, " +
                COLUMN_PLAYERS_CONTACT + " VARCHAR(30)" +
                ");";

        //Create Matches Table
        String CREATE_MATCHES_TABLE = "CREATE TABLE " + TABLE_MATCHES + "( " +
                COLUMN_MATCHES_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_MATCHES_NAME + " VARCHAR(30) NOT NULL" +
                ");";

        //Create Player_Match Table
        String CREATE_PLMA_TABLE = "CREATE TABLE " + TABLE_PLMA + "( " +
                COLUMN_PLMA_PLID + " INTEGER, " +
                COLUMN_PLMA_MAID + " INTEGER, " +
                "PRIMARY KEY (" + COLUMN_PLMA_PLID + ", " + COLUMN_PLMA_MAID + "), " +
                "FOREIGN KEY (" + COLUMN_PLMA_PLID + ") REFERENCES " + TABLE_PLAYERS + "(" + COLUMN_PLAYERS_ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY (" + COLUMN_PLMA_MAID + ") REFERENCES " + TABLE_MATCHES + "(" + COLUMN_MATCHES_ID + ") ON DELETE CASCADE" +
                ");";

        db.execSQL(CREATE_PLAYERS_TABLE);
        db.execSQL(CREATE_MATCHES_TABLE);
        db.execSQL(CREATE_PLMA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_TABLE = "DROP TABLE IF EXISTS ";

        db.execSQL(DROP_TABLE + TABLE_PLMA);
        db.execSQL(DROP_TABLE + TABLE_PLAYERS);
        db.execSQL(DROP_TABLE + TABLE_MATCHES);

        this.onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    //Adiciona novo player ao banco de dados
    public void addPlayer(Player player){

        playerDAO.addPlayer(this, player, TABLE_PLAYERS, COLUMN_PLAYERS_NAME, COLUMN_PLAYERS_SKILL, COLUMN_PLAYERS_CONTACT);

    }

    //Adiciona novo player ao banco de dados
    public void addMatch(Match match){

        matchDAO.addMatch(this, match, TABLE_MATCHES, COLUMN_MATCHES_NAME);
    }

    //Adiciona os players da match na tabela player_match
    public void addPlayer_Match(Match match){

        String INSERT_VALUE = "INSERT INTO " + TABLE_PLMA + " (" + COLUMN_PLMA_PLID + ", " + COLUMN_PLMA_MAID + ") VALUES (";

        String GET_PLAYER = "(SELECT " + COLUMN_PLAYERS_ID + " FROM " + TABLE_PLAYERS + " WHERE " + COLUMN_PLAYERS_NAME + " = \"";

        String GET_MATCH = "\"), (SELECT " + COLUMN_MATCHES_ID + " FROM " + TABLE_MATCHES +
                           " WHERE " + COLUMN_MATCHES_NAME + " = \"" + match.getMatchName() + "\"));";

        SQLiteDatabase db = this.getWritableDatabase();

        for(int i = 0; i < match.getPlayers().size(); i++){

            String QUERY = INSERT_VALUE + GET_PLAYER + match.getPlayers().get(i).getPlayerName() + GET_MATCH;

            db.execSQL(QUERY);
        }

        db.close();
    }

    //Adiciona os players da match na tabela player_match
    public void addPlayer_Match(String matchName, List<Player> matchPlayers){

        String INSERT_VALUE = "INSERT INTO " + TABLE_PLMA + " (" + COLUMN_PLMA_PLID + ", " + COLUMN_PLMA_MAID + ") VALUES (";

        String GET_PLAYER = "(SELECT " + COLUMN_PLAYERS_ID + " FROM " + TABLE_PLAYERS + " WHERE " + COLUMN_PLAYERS_NAME + " = \"";

        String GET_MATCH = "\"), (SELECT " + COLUMN_MATCHES_ID + " FROM " + TABLE_MATCHES +
                " WHERE " + COLUMN_MATCHES_NAME + " = \"" + matchName + "\"));";

        SQLiteDatabase db = this.getWritableDatabase();

        for(int i = 0; i < matchPlayers.size(); i++){

            String QUERY = INSERT_VALUE + GET_PLAYER + matchPlayers.get(i).getPlayerName() + GET_MATCH;

            db.execSQL(QUERY);
        }

        db.close();
    }

    //Remove player do banco de dados
    public void deletePlayer(Player player){
        playerDAO.deletePlayer(this, player, TABLE_PLAYERS, COLUMN_PLAYERS_NAME);
    }

    //Remove match do banco de dados
    public void deleteMatch(String matchName, List<Player> matchPlayers){
        matchDAO.deleteMatch(this, matchName, TABLE_MATCHES, COLUMN_MATCHES_NAME);
    }

    //Retorna Lista com os players no banco de dados
    public List<Player> readPlayers(){

        return playerDAO.readPlayers(this, TABLE_PLAYERS, COLUMN_PLAYERS_NAME, COLUMN_PLAYERS_SKILL, COLUMN_PLAYERS_CONTACT);
    }

    //Retorna Lista com as matches no banco de dados
    public List<Match> readMatches(){

        return matchDAO.readMatches(this,
                                    TABLE_PLAYERS, COLUMN_PLAYERS_ID, COLUMN_PLAYERS_NAME, COLUMN_PLAYERS_SKILL, COLUMN_PLAYERS_CONTACT,
                                    TABLE_MATCHES, COLUMN_MATCHES_ID, COLUMN_MATCHES_NAME,
                                    TABLE_PLMA, COLUMN_PLMA_PLID, COLUMN_PLMA_MAID);
    }

    //Retorna os jogadores da match informada no parâmetro
    public List<Player> matchPlayers(String matchName){

        SQLiteDatabase db = this.getWritableDatabase();

        List<Player> players = matchDAO.matchPlayers(db, matchName,
                                                     TABLE_PLAYERS, COLUMN_PLAYERS_ID, COLUMN_PLAYERS_NAME, COLUMN_PLAYERS_SKILL, COLUMN_PLAYERS_CONTACT,
                                                     TABLE_MATCHES, COLUMN_MATCHES_ID, COLUMN_MATCHES_NAME,
                                                     TABLE_PLMA, COLUMN_PLMA_PLID, COLUMN_PLMA_MAID);

        db.close();

        return players;

    }

    //Edita player
    public void editPlayer(String oldPlayerName, String newPlayerName, String newPlayerContact, int skillValue){

        playerDAO.editPlayer(this, oldPlayerName, newPlayerName, newPlayerContact, skillValue,
                             TABLE_PLAYERS, COLUMN_PLAYERS_NAME, COLUMN_PLAYERS_CONTACT, COLUMN_PLAYERS_SKILL);

    }

    //Edita match
    public void editMatch(String oldMatchName, List<Player> oldMatchPlayers, String newMatchName, List<Player> newMatchPlayers){
        clearMatchPlayerlist(oldMatchName, oldMatchPlayers);

        matchDAO.editMatch(this, oldMatchName, newMatchName,
                           TABLE_MATCHES, COLUMN_MATCHES_NAME);

        addPlayer_Match(newMatchName, newMatchPlayers);
    }

    //Remove jogadores, na tabela player_match, relacionados a partida dada no parâmetro
    public void clearMatchPlayerlist(String matchName, List<Player> players){

        SQLiteDatabase db = this.getWritableDatabase();

        matchDAO.clearMatchPlayerlist(db, matchName, players,
                                      TABLE_PLAYERS, COLUMN_PLAYERS_ID, COLUMN_PLAYERS_NAME,
                                      TABLE_MATCHES, COLUMN_MATCHES_ID, COLUMN_MATCHES_NAME,
                                      TABLE_PLMA, COLUMN_PLMA_PLID, COLUMN_PLMA_MAID);

        db.close();

    }

    public boolean searchPlayer(String playerName){

        return playerDAO.searchPlayer(this, playerName, TABLE_PLAYERS, COLUMN_PLAYERS_NAME);

    }

    public boolean searchMatch(String matchName){

        return matchDAO.searchMatch(this, matchName, TABLE_MATCHES, COLUMN_MATCHES_NAME);

    }

}
