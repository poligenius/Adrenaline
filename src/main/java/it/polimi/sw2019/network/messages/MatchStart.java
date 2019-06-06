package it.polimi.sw2019.network.messages;

import it.polimi.sw2019.model.Character;
import it.polimi.sw2019.model.Match;

import java.util.ArrayList;
import java.util.List;

/**
 *  Class used to doSomething the info about the board and the game to the view
 */
public class MatchStart {

    /**
     * Default constructor
     */
    public MatchStart(){}

    /**
     * customize constructor
     */
    public MatchStart(Message matchSetUpMessage, List<String> usernames, List<Character> characters){

        MatchSetup matchSetup = matchSetUpMessage.deserializeMatchSetup();
        setBoardType(matchSetup.getBoardJsonName());
        setFrenzy(matchSetup.isFrenzy());
        setEightSkulls(matchSetup.isEightSkulls());
        setUsernames(usernames);
        setCharacters(characters);
    }

    /* Attributes */

    private String boardType;

    private List<String> usernames = new ArrayList<>();

    private List<Character> characters = new ArrayList<>();

    private boolean eightSkulls; /* true if we want to play with 8, false for 5 */

    private boolean frenzy; /* true if we want to play with frenzy */

    /* Methods */

    public void setBoardType(String boardType) {
        this.boardType = boardType;
    }

    public String getBoardType() {
        return boardType;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void setEightSkulls(boolean eightSkulls) {
        this.eightSkulls = eightSkulls;
    }

    public boolean isEightSkulls() {
        return eightSkulls;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setFrenzy(boolean frenzy) {
        this.frenzy = frenzy;
    }

    public boolean isFrenzy() {
        return frenzy;
    }
}
