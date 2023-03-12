package client.scenes;

import client.lib.CollisionChecking;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
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
    private Group boardGroup;
    @FXML

    List<AnchorPane> listContainers;
    List<AnchorPane> listCards;

    private double originalX;
    private double originalY;
    private AnchorPane currentList;
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
        this.currentList = (AnchorPane) popup.getOwnerNode().getParent();
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
        this.currentList = (AnchorPane) popup.getOwnerNode().getParent();
        mainCtrl.showDeleteList();
    }
    void deleteL() {
        ((AnchorPane)currentList.getParent()).getChildren().remove(currentList);
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

        AnchorPane newList = createNewList(newListName);
        mainCtrl.addNewList(newList, boardGroup);
    }

    /**
     * Creates a new list with all its elements
     * @param newListName the name of the new list to be created
     * @return and AnchorPane with the new list, aligned correspondingly
     */
    public AnchorPane createNewList(String newListName){
        // creating the listView element
        ListView<Objects> listView = createListBody();

        // creating the Delete List button, aligning and customising it
        MenuButton refactorButtonList = createRefactorButton();

        // creating the separator under the title, aligning and customising it
        Separator listSeparator = createSeparator();

        // creating the adding card button, aligning and customising it
        Button addCardButton = createAddCardButton();

        // creating the label for the name of the list, aligning and customising it
        Label listName = createListTitle(newListName);

        // the anchor pane which contains the whole list
        AnchorPane newList = new AnchorPane();

        newList.getChildren().addAll(listView, refactorButtonList, listSeparator, addCardButton, listName);
        return newList;
    }

    /**
     * Creates a new button on the list, which when pressed, shows a menu of two options: renaming or
     * deleting the list;
     * @return a button to refactor a list, aligned correspondingly
     */
    public MenuButton createRefactorButton(){
        MenuButton refactorButtonList = new MenuButton();
        refactorButtonList.setText("Refactor List");
        refactorButtonList.setLayoutX(66);
        refactorButtonList.setLayoutY(230);
        refactorButtonList.setPrefWidth(75.2);
        refactorButtonList.setPrefHeight(20);
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
        listSeparator.setLayoutX(1);
        listSeparator.setLayoutY(19);
        listSeparator.setPrefWidth(150);
        listSeparator.setPrefHeight(4);
        return listSeparator;
    }

    /**
     * Creates a button which when pressed, creates a new card in the list
     * @return a button to create a new card, aligned correspondingly
     */
    public Button createAddCardButton(){
        Button addButton = new Button();
        addButton.setText("+");
        addButton.setStyle("-fx-border-radius: 50; -fx-background-radius: 70; -fx-background-color: #c8a5d9; " +
                "-fx-border-color: #8d78a6; -fx-font-size: 10px;");
        addButton.setLayoutX(9);
        addButton.setLayoutY(230);
        addButton.setPrefWidth(24);
        addButton.setPrefHeight(23);
        return addButton;
    }

    /**
     * Creates a label which shows the list's title
     * @param newListName the name the list should have
     * @return a label with the name of the list, aligned correspondingly
     */
    public Label createListTitle(String newListName){
        Label listName = new Label();
        listName.setLayoutX(56);
        listName.setLayoutY(2);
        listName.setText(newListName);
        listName.setStyle("-fx-font-size: 13px;");
        listName.setAlignment(Pos.CENTER);
        return listName;
    }

    /**
     * Creates the body of the list
     * @return a listView, which represents the body of the list, designed and aligned accordingly
     */
    public ListView<Objects> createListBody(){
        ListView<Objects> listView = new ListView<>();
        listView.setPrefWidth(150);
        listView.setPrefHeight(260);
        return listView;
    }
    /**
     * Delete Card function
     */
    @FXML
    public void deleteCard(ActionEvent event) {
        Button deleteCard = (Button) event.getTarget();
        ((AnchorPane)deleteCard.getParent().getParent()).getChildren().remove(deleteCard.getParent());
    }

    /**
     * open the Card Detail scene and modify all information about the card, including its name.....
     * In order to prevent it from opening while dragging, the code here sets a time delay between pressing and releasing the left mouse button.
     * If the time delay is greater than a certain value, the click option will not be triggered, so the cardDetail won't open during dragging.
     *
     * @param event an button (Hyperlink)
     */
    @FXML
    void cardDetail(ActionEvent event) {
        long mouseReleasedTime = System.currentTimeMillis();
        long mouseDuration = mouseReleasedTime - mousePressedTime;
        if(mouseDuration >= 2000) {
            this.currentCard = (Hyperlink) event.getTarget();
            mainCtrl.showCardDetail();
        }
    }

    /**
     * Save new card details to board scene
     * When the function returns from mainCtrl, it will update the card name displayed on the board and refresh the pointer to currentCard.
     */
    void RefreshCard(String text) {
        this.currentCard.setText(text);
        mainCtrl.closeCardDetails();
    }

    public void addCardToList(ActionEvent event){
        AnchorPane list = (AnchorPane) ((Button) event.getTarget()).getParent();
        addNewCard(list);
    }

    public void addNewCard(AnchorPane anchor){
        int count = 0;
        for(Node i : anchor.getChildren()){
            if(i.getClass().equals(AnchorPane.class)) count++;
        }

        AnchorPane newCard = newAnchorPane(count);
        newCard.getChildren().addAll(newHyperlink(), newDeleteCardButton());
        anchor.getChildren().add(count + 3, newCard);

        this.currentCard = (Hyperlink) newCard.getChildren().get(0);
        mainCtrl.showCardDetail();
    }

    public AnchorPane newAnchorPane(int position){
        AnchorPane anchor = new AnchorPane();
        anchor.setLayoutX(11.5);
        anchor.setLayoutY(25 + position*27);
        return anchor;
    }

    public Hyperlink newHyperlink(){
        Hyperlink card = new Hyperlink();
        card.setLayoutX(31);
        card.setLayoutY(0);
        card.setPrefHeight(23);
        card.setPrefWidth(95);
        card.setAlignment(Pos.CENTER);

        //need to set on click so can view card details!

        card.setStyle("-fx-background-color:  #E6E6FA");
        card.setOnAction(this::cardDetail); // Set the triggering event name to be the same as card link buttons, you can find the name in the fxml file (onAction="#cardDetail")
        return card;
    }

    public Button newDeleteCardButton(){
        Button button = new Button();
        button.setText("X");
        button.setLayoutX(0);
        button.setLayoutY(3);
        button.setMnemonicParsing(false);

        // need to set on action so can delete card!

        button.setStyle("-fx-background-color: #f08080; -fx-font-size: 9.0");
        button.setOnAction(this::deleteCard); //Set the triggering event name to be the same as card link buttons, you can find the name in the fxml file (onAction="#cardDetail")
        return button;
    }
}
