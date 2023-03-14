package client.scenes;

import client.lib.CollisionChecking;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.input.MouseEvent;


import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class BoardCtrl implements Initializable {
    private final MainCtrl mainCtrl;

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

    @FXML
    private VBox vBoard;
    @FXML
    private HBox firstRow;
    //private static HBox actualRow;


    List<AnchorPane> listContainers;
    List<AnchorPane> listCards;

    private double originalX;
    private double originalY;
    private VBox currentList;
    private Hyperlink currentCard;
    private long mousePressedTime;
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
        mousePressedTime = System.currentTimeMillis();
        mouseEvent.consume();
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
        mouseEvent.consume();
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
     * @param listContainer the list in which a card is dropped
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
        MenuItem menuItem = (MenuItem) event.getSource();
        ContextMenu popup = menuItem.getParentPopup();
        this.currentList = (VBox) popup.getOwnerNode().getParent().getParent();
        mainCtrl.showRenameList();
    }
    void RNList(String name) {
            ObservableList<Node> children = currentList.getChildren();
            for (Node node : children) {
                if (node instanceof Label ) {
                    Label label = (Label) node;
                    label.setText(name);
                    break;
                }
            }
        mainCtrl.closeRNList();
    }

    /**
     *Trigger function for deleting List option in the drop-down options button
     * @param event List delete process
     */
    @FXML
    void deleteList(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        ContextMenu popup = menuItem.getParentPopup();
        this.currentList = (VBox) popup.getOwnerNode().getParent().getParent();
        mainCtrl.showDeleteList();
    }
    void deleteL() {
        ((HBox)currentList.getParent()).getChildren().remove(currentList);
        mainCtrl.closeDEList();
    }
    void undeleteL() {
        mainCtrl.closeDEList();
    }

    /**
     *Trigger function for adding a List with a button //ActionEvent event
     */
    @FXML
    void addList(){
        mainCtrl.showAddList();
    }

    /**
     * Adds a new list to the board by creating all of its elements and aligning them correspondingly in the listView
     * @param newListName the name of the new list
     */
    public void addNewList(String newListName) {
        // closes the scene of adding a new list
        mainCtrl.closeADList();

        VBox newList = createNewList(newListName);
        mainCtrl.addNewList(newList, firstRow);
    }

    /**
     * Creates a new list with all its elements
     * @param newListName the name of the new list to be created
     * @return and VBox with the new list, aligned correspondingly
     */
    public VBox createNewList(String newListName){
        // creating the listView element
        VBox list = createListBody();
        VBox headerList = new VBox(7);
        HBox footerList = new HBox(30);

        headerList.setMinSize(150, 235);
        footerList.setMinSize(150, 25);
        headerList.setAlignment(Pos.TOP_CENTER);
        footerList.setAlignment(Pos.TOP_CENTER);
        footerList.setStyle("-fx-padding: 0 7 0 7");


        // creating the adding card button, aligning and customising it
        Button addCardButton = createAddCardButton();

        // creating the Delete List button, aligning and customising it
        MenuButton refactorButtonList = createRefactorButton();

        footerList.getChildren().addAll(addCardButton, refactorButtonList);

        // creating the separator under the title, aligning and customising it
        Separator listSeparator = createSeparator();

        // creating the label for the name of the list, aligning and customising it
        Label listName = createListTitle(newListName);

        headerList.getChildren().addAll(listName, listSeparator);

        list.getChildren().addAll(headerList, footerList);
        return list;
    }

    /**
     * Creates a new button on the list, which when pressed, shows a menu of two options: renaming or
     * deleting the list;
     * @return a button to refactor a list
     */
    public MenuButton createRefactorButton(){
        MenuButton refactorButtonList = new MenuButton();
        refactorButtonList.setText("Refactor List");
        refactorButtonList.setPrefWidth(75.2);
        refactorButtonList.setPrefHeight(22);
        refactorButtonList.setStyle("-fx-background-color: #f08080; -fx-font-size: 9px;");

        MenuItem renameOption = new MenuItem();
        renameOption.setText("Rename List");
        renameOption.setOnAction(this::renameList);

        MenuItem deleteOption = new MenuItem();
        deleteOption.setText("Delete List");
        deleteOption.setOnAction(this::deleteList);

        refactorButtonList.getItems().add(renameOption);
        refactorButtonList.getItems().add(deleteOption);

        return refactorButtonList;
    }

    /**
     * Creates a separator for a list, separating visually the title of the list from its body
     * @return a separator, aligned correspondingly
     */
    public Separator createSeparator(){
        Separator listSeparator = new Separator();
        listSeparator.setPrefWidth(150);
        listSeparator.setPrefHeight(4);
        listSeparator.setStyle("-fx-padding: -10 0 0 0;");
        return listSeparator;
    }

    /**
     * Creates a button which when pressed, creates a new card in the list
     * @return a button to create a new card, properly customised
     */
    public Button createAddCardButton(){
        Button addButton = new Button();
        addButton.setText("+");
        addButton.setStyle("-fx-border-radius: 50; -fx-background-radius: 70; -fx-background-color: #c8a5d9; " +
                "-fx-border-color: #8d78a6; -fx-font-size: 10px;");
        addButton.setPrefWidth(24);
        addButton.setPrefHeight(23);
        return addButton;
    }

    /**
     * Creates a label which shows the list's title
     * @param newListName the name the list should have
     * @return a Label with the name of the list
     */
    public Label createListTitle(String newListName){
        Label listName = new Label();
        listName.setText(newListName);
        listName.setStyle("-fx-font-size: 13px; -fx-content-display: CENTER; -fx-padding: 5 10 0 10;");
        listName.setAlignment(Pos.CENTER);
        return listName;
    }

    /**
     * Creates the body of the list
     * @return a Vbox, which represents the body of the list, designed accordingly
     */
    public VBox createListBody(){
        VBox vbox = new VBox();
        vbox.setPrefWidth(150);
        vbox.setPrefHeight(260);
        vbox.setStyle("-fx-background-color: #ffffff;");
        return vbox;
    }
}
