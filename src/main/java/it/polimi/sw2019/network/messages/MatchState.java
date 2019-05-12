package it.polimi.sw2019.network.messages;

import it.polimi.sw2019.model.KillTokens;
import it.polimi.sw2019.model.PlayerBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * this class is send to the client to update the gui or the cli
 */
public class MatchState {

    /**
     * Default constructor
     */
    public MatchState(){}

    /* Attributes */

    private List<MessageCell> cells = new ArrayList<>();

    private List<PlayerBoard> playerBoards = new ArrayList<>();

    private List<PlayerHand> playerHands = new ArrayList<>();

    private KillTokens killTrack;

    private int weaponsDeckSize;

    private int powerupsDeckSize;

    /* Methods */

    public int getPowerupsDeckSize() {
        return powerupsDeckSize;
    }

    public void setCells(List<MessageCell> cells) {
        this.cells = cells;
    }

    public int getWeaponsDeckSize() {
        return weaponsDeckSize;
    }

    public void setKillTrack(KillTokens killTrack) {
        this.killTrack = killTrack;
    }

    public KillTokens getKillTrack() {
        return killTrack;
    }

    public void setPlayerBoards(List<PlayerBoard> playerBoards) {
        this.playerBoards = playerBoards;
    }

    public List<MessageCell> getCells() {
        return cells;
    }

    public void setPlayerHands(List<PlayerHand> playerHands) {
        this.playerHands = playerHands;
    }

    public List<PlayerBoard> getPlayerBoards() {
        return playerBoards;
    }

    public void setPowerupsDeckSize(int powerupsDeckSize) {
        this.powerupsDeckSize = powerupsDeckSize;
    }

    public List<PlayerHand> getPlayerHands() {
        return playerHands;
    }

    public void setWeaponsDeckSize(int weaponsDeckSize) {
        this.weaponsDeckSize = weaponsDeckSize;
    }

}
