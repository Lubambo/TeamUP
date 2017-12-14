package com.teamup.teamsgenerator.Beans;

/**
 * Created by Felipe on 31/05/2017.
 */
public class Player {

    private String playerName;
    private int skillValue;
    private String playerContact;
    private boolean selected;

    public Player(){
        selected = false;
    }

    public Player(String playerName, int skillValue){
        setPlayerName(playerName);
        setSkillValue(skillValue);
        selected = false;
    }

    public Player(String playerName, int skillValue, String playerContact){
        setPlayerName(playerName);
        setSkillValue(skillValue);
        setPlayerContact(playerContact);
        selected = false;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getSkillValue() {
        return skillValue;
    }

    public void setSkillValue(int skillValue) {
        this.skillValue = skillValue;
    }

    public String getPlayerContact() {
        return playerContact;
    }

    public void setPlayerContact(String playerContact) {
        this.playerContact = playerContact;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
