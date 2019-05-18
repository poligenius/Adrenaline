package it.polimi.sw2019.network.messages;

public class GrabWeapon {

    /**
     * Default constructor
     */
    public GrabWeapon(){}

    /**
     * customize constructor
     */
    public GrabWeapon(int grabbedWeapon, int discardedWeapon, BoardCoord spawnCell){

        setSpawnCell(spawnCell);
        setDiscardedWeapon(discardedWeapon);
        setGrabbedWeapon(grabbedWeapon);
    }

    /* Attributes */

    private BoardCoord spawnCell;
    private int grabbedWeapon;
    private int discardedWeapon;

    /* Methods */

    public BoardCoord getSpawnCell() {
        return spawnCell;
    }

    public void setSpawnCell(BoardCoord spawnCell) {
        this.spawnCell = spawnCell;
    }

    public int getDiscardedWeapon() {
        return discardedWeapon;
    }

    public int getGrabbedWeapon() {
        return grabbedWeapon;
    }

    public void setDiscardedWeapon(int discardedWeapon) {
        this.discardedWeapon = discardedWeapon;
    }

    public void setGrabbedWeapon(int grabbedWeapon) {
        this.grabbedWeapon = grabbedWeapon;
    }
}
