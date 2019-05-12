package it.polimi.sw2019.network.messages;

public class GrabWeapon {

    /**
     * Default constructor
     */
    public GrabWeapon(){}

    /* Attributes */

    private int grabbedWeapon;
    private int discardedWeapon;

    /* Methods */

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