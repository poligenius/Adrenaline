package it.polimi.sw2019.view.gui;

import it.polimi.sw2019.model.TypeOfAction;
import it.polimi.sw2019.network.client.Client;
import it.polimi.sw2019.network.messages.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SelectCardController {

    /* Attributes */

    private Client client;

    @FXML
    private ImageView cardImage0;

    @FXML
    private ImageView cardImage1;

    @FXML
    private ImageView cardImage2;

    @FXML
    private Button closeButton;

    @FXML
    private Label mainLabel;

    private List<ImageView> cards = new ArrayList<>();

    private TypeOfAction currentTypeOfAction;

    private List<ImageView> myWeapons;

    private IndexMessage firstSelection;

    private BoardCoord lastSelectedCell;

    private static Logger logger = Logger.getLogger("SelectCardController");

    /* Methods */

    public void initialize(){
        cardImage0.setUserData(new IndexMessage(0));
        cardImage1.setUserData(new IndexMessage(1));
        cardImage2.setUserData(new IndexMessage(2));

        cards.add(cardImage0);
        cards.add(cardImage1);
        cards.add(cardImage2);
    }

    public void configure(Client client, AvailableCards availableCards, TypeOfAction typeOfAction, List<Image> images, BoardCoord lastSelectedCell, boolean noOption){
        this.client = client;
        currentTypeOfAction = typeOfAction;
        this.lastSelectedCell = lastSelectedCell;


        if (!availableCards.areWeapons() && typeOfAction != TypeOfAction.SPAWN){
            mainLabel.setText("CHOOSE A POWERUP");
        }

        if (typeOfAction == TypeOfAction.SPAWN){
            mainLabel.setText("YOU'RE DEAD: CHOOSE A POWERUP TO SPAWN");
        }

        List<IndexMessage> indexMessages = availableCards.getAvailableCards();

        showCards(indexMessages, images, true);

        closeButton.setVisible(noOption);
    }

    public void configure(Client client, AvailableCards availableCards, List<Image> images, BoardCoord lastSelectedCell, List<ImageView> myWeapons){
        this.myWeapons = myWeapons;
        configure(client, availableCards, TypeOfAction.GRAB, images, lastSelectedCell, true);
    }

    public void showCards(List<IndexMessage> indexMessages, List<Image> images, boolean disable){
        for (ImageView card : cards){
            card.setImage(images.get(cards.indexOf(card)));
        }

        for (IndexMessage indexMessage : indexMessages){
            ImageView card = cards.get(indexMessage.getSelectionIndex());
            CardController.setUnavailable(card, false);
            card.setDisable(disable);
        }
    }

    @FXML
    public void showSelection(ActionEvent actionEvent){

        DropShadow dropShadow = new DropShadow();

        dropShadow.setColor(Color.BLUE);
        dropShadow.setWidth(40.0);
        dropShadow.setHeight(40.0);
        dropShadow.setSpread(0.6);

        ImageView imageView = (ImageView) actionEvent.getSource();

        imageView.setEffect(dropShadow);
    }

    @FXML
    public void disableEffect(ActionEvent actionEvent){
        ImageView imageView = (ImageView) actionEvent.getSource();
        imageView.setEffect(null);
    }

    @FXML
    public void handleSelection(ActionEvent actionEvent){
        ImageView imageView = (ImageView) actionEvent.getSource();
        IndexMessage indexMessage = (IndexMessage) imageView.getUserData();

        if (currentTypeOfAction == TypeOfAction.GRAB){
            if (myWeapons != null && firstSelection==null){
                firstSelection = indexMessage;

                List<Image> weapons = new ArrayList<>();
                List<IndexMessage> areLoaded = new ArrayList<>();

                for (ImageView weapon : myWeapons){
                    weapons.add(weapon.getImage());

                    //If the opacity is 1 is loaded
                    if (weapon.getOpacity() == 1.0){
                        int index = myWeapons.indexOf(weapon);
                        areLoaded.add(new IndexMessage(index));
                    }
                }

                showCards(areLoaded, weapons, false);
                mainLabel.setText("CHOOSE A WEAPON TO DISCARD");
            }

            else{
                Message message = new Message(client.getUsername());
                if (firstSelection!=null){
                    message.createSingleActionGrabWeapon(new GrabWeapon(firstSelection.getSelectionIndex(), indexMessage.getSelectionIndex(), lastSelectedCell));
                }
                else {
                    message.createSingleActionGrabWeapon(new GrabWeapon(indexMessage.getSelectionIndex(), -1, lastSelectedCell));
                }
                client.send(message);
                closeWindow();
            }
        }


    }

    @FXML
    public void handleCloseButton(){
        if (currentTypeOfAction == TypeOfAction.RELOAD || currentTypeOfAction == TypeOfAction.USEPOWERUP){

            //If is a use powerup sends a message, if is a reload just closes the window
            if (currentTypeOfAction == TypeOfAction.USEPOWERUP){
                Message message = new Message(client.getUsername());
                message.createSelectionForUsePowerup(-1);
                client.send(message);
            }
            closeWindow();
        }
        else {
            logger.log(Level.SEVERE, "TypeOfAction not managed in the handleCloseButton method in SelectCardController");
        }
    }

    public void closeWindow(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

}