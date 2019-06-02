package it.polimi.sw2019.controller;

import it.polimi.sw2019.model.*;
import it.polimi.sw2019.model.Character;
import it.polimi.sw2019.network.messages.BoardCoord;
import it.polimi.sw2019.network.messages.GrabWeapon;
import it.polimi.sw2019.network.messages.IndexMessage;
import it.polimi.sw2019.network.messages.Message;
import it.polimi.sw2019.network.server.VirtualView;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles a single action
 */
public class SingleActionManager {

    /**
     * Default Constructor
     */
    public SingleActionManager(Match match, VirtualView view, TurnManager turnManager) {
        this.match = match;
        this.view = view;
        this.turnManager = turnManager;
        this.atomicActions = new AtomicActions(match);
        this.payment = new Payment(match, view, this);
        this.choices = new Choices(match, view, payment, atomicActions, this);

    }

    /* Attributes */

    private Match match;

    private VirtualView view;

    private AtomicActions atomicActions;

    private TurnManager turnManager;

    private Choices choices;

    private Payment payment;

    /* Methods */

    public Match getMatch() {
        return match;
    }

    public AtomicActions getAtomicActions() {
        return atomicActions;
    }

    public Choices getChoices() {
        return choices;
    }

    public Payment getPayment() {
        return payment;
    }

    public TurnManager getTurnManager() {
        return turnManager;
    }

    public void singleActionHandler(Message message){

        switch (message.getTypeOfAction()){
            case MOVE:
                moveHandler(message);
                break;
            case GRAB:
                grabHandler(message);
                break;
            case GRABWEAPON:
            case RELOAD:
                payment.paymentStarter(message);
                break;
            case MOVEBEFORESHOOT:
                moveBeforeShootHandler(message);
                break;
            case MOVEAFTERSHOOT:
                moveAfterShootHandler(message);
                break;
            case USEPOWERUP:
                usePowerupHandler(message);
                break;
            case ENDTURN:
                turnManager.endTurn();
                break;
            default:
                System.console().printf("TYPE OF ACTION UNKNOWN");
                break;
        }
    }

    /**
     * Handles the move and reduces the number of the player actions (handles the message too)
     * @param message
     */
    public void moveHandler(Message message){

        Player player = match.getPlayerByUsername(message.getUsername());
        BoardCoord selection = message.deserializeBoardCoord();
        atomicActions.move(player, match.getBoard().getCell(selection));

        reducePlayerNumberOfActions();
    }

    public void grabHandler(Message message){

        Player player = match.getPlayerByUsername(message.getUsername());
        BoardCoord selection = message.deserializeBoardCoord();
        Cell selectedCell = match.getBoard().getCell(selection);
        atomicActions.grab(player, selectedCell);

        //Add the cell to a list of empty cells (for the end of the turn)
        turnManager.getEmptyCommonCells().add(selectedCell);

        reducePlayerNumberOfActions();
    }

    public void grabWeaponHandler(Message message){

        Player player = match.getPlayerByUsername(message.getUsername());
        GrabWeapon grabWeapon = message.deserializeGrabWeapon();
        Cell spawnCell = match.getBoard().getCell(grabWeapon.getSpawnCell());


        //If there's a weapon to discard
        if(grabWeapon.getDiscardedWeapon()>=0){
            atomicActions.grabWeaponAndReplace(player,spawnCell, grabWeapon.getGrabbedWeapon(), grabWeapon.getDiscardedWeapon());
        }
        else {
            atomicActions.grabWeapon(player, spawnCell, grabWeapon.getGrabbedWeapon());
        }

        //Add the cell to a list of empty cells (for the end of the turn)
        turnManager.getEmptySpawnCells().add(spawnCell);

        reducePlayerNumberOfActions();

    }

    public void moveBeforeShootHandler(Message message){

        Message answer = new Message(message.getUsername());

        //First I deserialize the message
        Player player = match.getPlayerByUsername(message.getUsername());
        BoardCoord boardCoord = message.deserializeBoardCoord();
        Cell selectedCell = match.getBoard().getCell(boardCoord);

        //Check if is a Move&Shoot
        if (choices.getSelectedWeapon()==null){
            atomicActions.move(player, selectedCell);
            List<IndexMessage> indexMessageList = createShootMessage(player);
            answer.createAvailableCardsMessage(TypeOfAction.SHOOT, indexMessageList, true);

            view.display(answer);
        }

        // check if I have to move myself
        else if (choices.getCurrentEffect().getMove().isMoveYouBefore()){
            atomicActions.move(player, selectedCell);
            choices.effectHandler();
        }

        // check if I have to move other players
        else if (choices.getCurrentEffect().getMove().isMoveTargetBefore()){
            //Sets the moveCell in Choices
            choices.setMoveCell(selectedCell);

            //Moves the selectedPlayer
            atomicActions.move(choices.getSelectedPlayer(), selectedCell);

            //The player than is added to the movedPlayers
            choices.getMovedPlayers().add(choices.getSelectedPlayer());

            //Than calculates the player that can be moved
            if (choices.getMovedPlayers().size() < choices.getCurrentEffect().getMove().getMaxTargets()){
                List<Character> characterList = choices.availablePlayersToMove();
                answer.createAvailablePlayers(TypeOfAction.MOVEBEFORESHOOT, characterList);
                view.display(answer);
            }
            else {

                choices.effectHandler();
            }
        }

    }

    public void moveAfterShootHandler(Message message){

        Cell selectedCell = match.getBoard().getCell(message.deserializeBoardCoord());

        //moving first player shooted to the selected cell
        atomicActions.move(choices.getShootedPlayers().get(0), selectedCell);

        choices.powerupsAfterShoot();
    }

    public void usePowerupHandler(Message message){

        Cell selectedCell = match.getBoard().getCell(message.deserializeBoardCoord());

        // case I'm using newton
        if (choices.getSelectedPlayer() != null){

            atomicActions.move(choices.getSelectedPlayer(), selectedCell);
        }

        // case where I use teleporter
        else {

            atomicActions.move(match.getCurrentPlayer(), selectedCell);
        }

        //Discarding the powerup from the player hand
        match.getPlayerByUsername(message.getUsername()).usePoweup(choices.getSelectedPowerup());
        match.getBoard().discardPowerup(choices.getSelectedPowerup());
        match.notifyPrivateHand(match.getPlayerByUsername(message.getUsername()));

        //resetting the powerup choices
        choices.resetEverything();

        Message next = new Message(match.getCurrentPlayer().getName());
        next.createMessageCanIShoot(choices.canIshoot());
    }

    /**
     * This method reduces the current player left actions and display if the player can shoot
     */
    public void reducePlayerNumberOfActions(){

        Player player = match.getCurrentPlayer();

        match.setCurrentPlayerLeftActions(match.getCurrentPlayerLeftActions()-1);

        Message message = new Message(player.getName());

        if(match.getCurrentPlayerLeftActions()>0){
            message.createMessageCanIShoot(player.canIshootBeforeComplexAction());
        }
        else {
            message.createMessageCanIShoot(false);
        }

        view.display(message);
    }

    public List<IndexMessage> createReloadMessage(Player currentPlayer){
        List<Weapon> reloadableWeapons = currentPlayer.reloadableWeapons();
        List<IndexMessage> indexMessageList = new ArrayList<>();

        for (Weapon weapon : reloadableWeapons){
            indexMessageList.add(new IndexMessage(currentPlayer.getWeaponIndex(weapon)));
        }

        return indexMessageList;
    }

    public List<IndexMessage> createShootMessage(Player currentPlayer){
        List<Weapon> usableWeapon = currentPlayer.availableWeapons();
        List<IndexMessage> indexMessageList = new ArrayList<>();

        for (Weapon weapon : usableWeapon){
            indexMessageList.add(new IndexMessage(currentPlayer.getWeaponIndex(weapon)));
        }

        return indexMessageList;
    }

    public void reloadHandler(Message message){
        IndexMessage indexMessage = message.deserializeIndexMessage();
        Player player = match.getCurrentPlayer();
        Weapon weapon = player.getWeaponFromIndex(indexMessage.getSelectionIndex());

        atomicActions.reload(player, weapon);

        List<IndexMessage> indexMessageList = createReloadMessage(player);

        Message answer = new Message(player.getName());

        //The view checks if the indexMessageList is empty
        answer.createAvailableCardsMessage(TypeOfAction.RELOAD, indexMessageList, true);

        view.display(answer);
    }

    /**
     * this method see if the player can use another effect, if he can it shows him the options, otherwise
     * continue the turn
     */
    public void endShootingAction(){

        List<Effect> usableEffects = choices.getSelectedWeapon().usableEffects(match.getPlayers());

        // removing the already executed effects
        usableEffects.removeAll(choices.getUsedEffect());

        // if I just used an additional effect then I can't use any other effect except for move type effects
        if ( choices.getCurrentEffect().isAdditionalEffect() ){

            for (Effect effect: usableEffects){

                if (effect.getType() != EffectsKind.MOVE){

                    usableEffects.remove(effect);
                }
            }
        }

        // this if is to make sure that I don't give the possibility to the player to choose again an effect that he has already chosen
        // I may have this problem for every weapon with a move effect (except for cyberblade, the only weapon with hasAnOrder)
        if (choices.getCurrentEffect().getType() == EffectsKind.MOVE && !choices.getSelectedWeapon().hasAnOrder() && !usableEffects.isEmpty()){

            usableEffects.clear();
        }

        // I don't have any effect to execute
        if ( usableEffects.isEmpty() ){

            //setting the weapon to null after having unloaded it
            choices.getSelectedWeapon().unloadWeapon();
            match.notifyPrivateHand(match.getCurrentPlayer());
            choices.setSelectedWeapon(null);
            choices.getUsedEffect().clear();
            reducePlayerNumberOfActions();
        }

        // showing him the possible effects
        else {

            List<IndexMessage> effects = new ArrayList<>();

            for (Effect effect: usableEffects){

                effects.add(new IndexMessage(choices.getSelectedWeapon().getIndexByEffect(effect)));
            }

            Message options = new Message(match.getCurrentPlayer().getName());
            options.createAvailableEffects(effects);
            view.display(options);
        }
    }
}
