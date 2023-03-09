package client.scenes;

import client.lib.CollisionChecking;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.input.MouseEvent;


import javafx.event.ActionEvent;

import javax.inject.Inject;

public class BoardCtrl implements Initializable {
    private MainCtrl mainCtrl;

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
    //@FXML
    //private AnchorPane listContainerNew;
    @FXML
    private Group boardGroup;
    //@FXML
    //private AnchorPane listsPane;

    @FXML
    private Label listName1;
    //@FXML
    //private Label listNameNew;
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

    /**
     * Auxiliary call to mainCtrl Inject function
     * @param mainCtrl The master controller, which will later be replaced by a class of window controllers
     */
    @Inject
    public BoardCtrl(MainCtrl mainCtrl){
        this.mainCtrl = mainCtrl;
    }

    /**
     *Trigger function for the change List name option in the drop-down options button
     * @param event List name change process
     */
    @FXML
    void renameList(ActionEvent event) {
        mainCtrl.showRenameList();
    }
    void RNList(String name) {
        listName1.setText(name);
        mainCtrl.closeRNList();
    }

    /**
     *Trigger function for deleting List option in the drop-down options button
     * @param event List delete process
     */
    @FXML
    void deleteList(ActionEvent event) {
        mainCtrl.showDeleteList();
    }
    void deleteL() {

        list1Container.setVisible(false);
        mainCtrl.closeDEList();
    }
    void undeleteL() {
        mainCtrl.closeDEList();
    }

    /**
     *Trigger function for adding a List with a button
     * @param event List add process
     */
    @FXML
    void addLIst(ActionEvent event) {
        mainCtrl.showAddList();
    }

    /**
     * Adds a new list to the board by creating all of its elements and aligning them correspondingly in the listView
     * @param newListName the name of the new list
     */
    public void addNewList(String newListName) {
        // closes the scene of adding a new list
        mainCtrl.closeADList();

        // creating the listView element
        ListView<Objects> listView = new ListView<>();
        listView.setPrefWidth(150);
        listView.setPrefHeight(260);

        // creating the Delete List button, aligning and customising it
        Button deleteButtonList = new Button();
        deleteButtonList.setText("Delete List");
        deleteButtonList.setLayoutX(89);
        deleteButtonList.setLayoutY(231);
        deleteButtonList.setPrefWidth(54.4);
        deleteButtonList.setPrefHeight(20);
        deleteButtonList.setStyle("-fx-background-color: #f08080; -fx-font-size: 9px;");

        // creating the separator under the title, aligning and customising it
        Separator listSeparator = new Separator();
        listSeparator.setLayoutX(1);
        listSeparator.setLayoutY(19);
        listSeparator.setPrefWidth(150);
        listSeparator.setPrefHeight(4);

        // creating the adding card button, aligning and customising it
        Button addButton = new Button();
        addButton.setText("+");
        addButton.setStyle("-fx-border-radius: 50; -fx-background-radius: 70; -fx-background-color: #c8a5d9; " +
                            "-fx-border-color: #8d78a6; -fx-font-size: 10px;");
        addButton.setLayoutX(9);
        addButton.setLayoutY(230);
        addButton.setPrefWidth(24);
        addButton.setPrefHeight(23);

        // creating the label for the name of the list, aligning and customising it
        Label listName = new Label();
        listName.setLayoutX(56);
        listName.setLayoutY(2);
        listName.setText(newListName);
        listName.setStyle("-fx-font-size: 13px;");

        // the anchor pane which contains the whole list
        AnchorPane newList = new AnchorPane();

        newList.getChildren().addAll(listView, deleteButtonList, listSeparator, addButton, listName);

        mainCtrl.addNewList(newList, boardGroup);
    }
}
