package it.polimi.sw2019.controller;
import it.polimi.sw2019.model.*;


public class TurnManager {

    /**
     * Default Constructor
     */
    public TurnManager() {

    }

    /* Attributes */

    private Match match;

    private Cell selectedCell;

    private Powerup selectedPowerup;

    private TypeOfAction typeOfAction;

    private int powerupPaid;

    private ShootingChoices shootingChoices;

    /* Methods */

    public void setMatch(Match match) {
        this.match = match;
    }

    public Match getMatch() {
        return match;
    }

    public void setSelectedCell(Cell selectedCell) {
        this.selectedCell = selectedCell;
    }

    public Cell getSelectedCell() {
        return selectedCell;
    }

    public void setSelectedPowerup(Powerup selectedPowerup) {
        this.selectedPowerup = selectedPowerup;
    }

    public Powerup getSelectedPowerup() {
        return selectedPowerup;
    }

    public void setTypeOfAction(TypeOfAction typeOfAction) {
        this.typeOfAction = typeOfAction;
    }

    public TypeOfAction getTypeOfAction() {
        return typeOfAction;
    }

    public void setPowerupPaid(int powerupPaid) {
        this.powerupPaid = powerupPaid;
    }

    public int getPowerupPaid() {
        return powerupPaid;
    }

    public void setShootingChoices(ShootingChoices shootingChoices) {
        this.shootingChoices = shootingChoices;
    }

    public ShootingChoices getShootingChoices() {
        return shootingChoices;
    }

    public void doAction() {

        //TODO implement
        return;
    }

    public void endTur() {

        //TODO implement
        return;
    }

    public void timer()  {

        //TODO implement
        return;
    }

    public void reset()  {

        //TODO implement
        return;
    }

}
