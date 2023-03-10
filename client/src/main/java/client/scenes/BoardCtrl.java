package client.scenes;

import client.lib.CollisionChecking;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BoardCtrl implements Initializable {

    @FXML
    private AnchorPane card1Container;
    @FXML
    private AnchorPane card2Container;
    @FXML
    private AnchorPane card3Container;
    @FXML
    private AnchorPane list1Container;
    @FXML
    private AnchorPane list2Container;
    @FXML
    private AnchorPane list3Container;

    List<AnchorPane> listContainers;

    List<AnchorPane> listCards;

    private double originalX;
    private double originalY;


    /**
     * The method adds the cardContainers and the listContainers into arrayLists in order to access
     * them easier in the following methods
     * @param url            The location used to resolve relative paths for the root object, or
     *                       {@code null} if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or {@code null} if
     *                       the root object was not localized.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listContainers = new ArrayList<>();
        listContainers.add(list1Container);
        listContainers.add(list2Container);
        listContainers.add(list3Container);
        listCards = new ArrayList<>();
        listCards.add(card2Container);
        listCards.add(card3Container);

    }


    /**
     * This method allows the dragged card to be rendered above all the other nodes
     * @param mouseEvent an object containing information about the mouse event
     */
    public void dragDetected(MouseEvent mouseEvent) {
        card1Container.getParent().toFront();
        card1Container.toFront();
    }

    /**
     * The method initializes where the mouse has been pressed relative to the top-left corner of the card
     * before the card being dragged
     *
     * @param mouseEvent an object containing information about the mouse event
     */
    public void mousePressed(MouseEvent mouseEvent) {
        originalX = mouseEvent.getX();
        originalY = mouseEvent.getY();
    }

    /**
     * The method changes the cardContainer's position based on the cursor's current position by
     * checking what the difference between where the mouse clicked the card (based on originalX/Y) and where it
     * currently is (mouseEvent.getX/Y()) and adding it to the card's coordinates (card1Container.getLayoutX/Y())
     *
     * @param mouseEvent an object containing information about the mouse event
     */

    public void mouseDragged(MouseEvent mouseEvent) {
        card1Container.setLayoutX(card1Container.getLayoutX() + mouseEvent.getX() - originalX);
        card1Container.setLayoutY(card1Container.getLayoutY() + mouseEvent.getY() - originalY);
    }

    /**
     * The method finalises drag-and-drop
     */
    public void mouseReleased() {

        // bound1 is the boundaries of card1Container
        Bounds bound1 = card1Container.localToScene(card1Container.getBoundsInLocal());

        // check for potential drop targets
        for(AnchorPane listContainer : listContainers) {
            // bound2 is the boundaries of the listContainer
            Bounds bound2 = listContainer.localToScene(listContainer.getBoundsInLocal());

            if(CollisionChecking.collide(bound1, bound2)) {
                if(!listContainer.equals(card1Container.getParent())) {
                    // if the card is dropped into a new list, then the cards in the old list are moved up
                    realign((AnchorPane) card1Container.getParent(), -1);
                }
                dropCard(listContainer);
            }
        }
    }

    /**
     * The method realigns the cards from a list once a card has been dragged
     * from / dropped into the list
     * @param initialList the parent list of the dragged card
     * @param CONSTANT has the value 1 for moving the remaining cards down
     *                 has the value -1 for moving the remaining cards up
     */
    public void realign(AnchorPane initialList, int CONSTANT){
        for (Node cardElement : listCards) {
            if(cardElement.getParent().equals(initialList))
                cardElement.setLayoutY(cardElement.getLayoutY() + CONSTANT * 25);
        }

    }

    /**
     * The method places the dragged card into the first position of the list which it has been dragged into
     * by removing it from its parent list and adding it to the list it has been dragged to
     * and positioning it to the top of the list using coordinates,
     * as well as realigns the cards from its parent list
     * @param listContainer
     */
    public void dropCard(AnchorPane listContainer) {
        if(!(card1Container.getParent().equals(listContainer))) {
            realign(listContainer, 1);
        }

        ((AnchorPane)card1Container.getParent()).getChildren().remove(card1Container);
        listContainer.getChildren().add(card1Container);
        card1Container.setLayoutX(11.5);
        card1Container.setLayoutY(25);


    }
}
