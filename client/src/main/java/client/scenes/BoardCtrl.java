package client.scenes;

import client.scenes.config.Draggable;
import client.utils.ServerUtils;
import commons.Lists;
import commons.Cards;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


import javafx.event.ActionEvent;

import javax.inject.Inject;

public class BoardCtrl implements Initializable {
    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    @FXML
    private AnchorPane rootContainer;

    @FXML
    private HBox firstRow;
    @FXML
    private Label boardName;


    List<VBox> listContainers;
    List<AnchorPane> listCards;

    private VBox currentList;
    private Hyperlink currentCard;
    private long mousePressedTime;

    private List<Lists> lists;

    private Draggable drag;


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
        listCards = new ArrayList<>();
        refresh();
    }

    private void webSocketLists() {
        server.registerForMessages("/topic/lists", Lists.class, l->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Added a new list");
                    addNewList(l);
                    refreshData();
                }
            });
        });

        server.registerForMessages("/topic/lists/rename", Lists.class, l->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Label title = (Label) rootContainer.lookup("#list_title_"+l.id);
                    title.setText(l.title);
                    refreshData();
                }
            });
        });

        server.registerForMessages("/topic/lists/remove", Lists.class, l->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    VBox list = (VBox)rootContainer.lookup("#list"+l.id);
                    firstRow.getChildren().removeAll(list);
                    refreshData();
                }
            });
        });
    }

    private void webSocketCards() {
        server.registerForMessages("/topic/cards/remove", Cards.class, c->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    VBox l = (VBox) rootContainer.lookup("#list"+c.list.id);
                    AnchorPane card = (AnchorPane) rootContainer.lookup("#card"+c.id);
                    ((VBox) l.getChildren().get(0)).getChildren().remove(card);
                    refreshData();

                }
            });
        });

        server.registerForMessages("/topic/cards/rename", Cards.class, c->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ((Hyperlink)((AnchorPane) rootContainer.lookup("#card"+c.id)).
                            getChildren().get(0)).setText(c.title);
                    refreshData();
                }
            });
        });

        server.registerForMessages("/topic/cards/add", Cards.class, c->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    VBox l = (VBox) rootContainer.lookup("#list"+c.list.id);
                    addNewCard((VBox) l.getChildren().get(0), c);
                    refreshData();
                }
            });
        });
    }

    public void refresh(){
        firstRow.getChildren().clear();
        lists = server.getLists();
        for(int i = 0; i<lists.size(); i++){
            addNewList(lists.get(i));

        }
    }

    public void refreshData(){
        lists = server.getLists();
        refreshLists(lists);
    }

    public void refreshCards(VBox listContainer, List<Cards> c){
        int j = 0;
        for(Node i : listContainer.getChildren()){

            Cards card = (Cards) i.getProperties().get("card");

            if(card!=null){
              i.getProperties().remove("card");
              i.getProperties().put("card", c.get(j));
              j++;
            }
        }
    }

    public void refreshLists(List<Lists> l){
        int j = 0;
        for(Node i : firstRow.getChildren()){
            Lists list = (Lists) i.getProperties().get("list");
            if(list!=null){
                i.getProperties().put("list", l.get(j));
                refreshCards((VBox) ((VBox) i).getChildren().get(0), l.get(j).cards);
                j++;
            }
        }
    }

    /**
     * Auxiliary call to mainCtrl Inject function
     *
     * @param mainCtrl         The master controller, which will later be replaced
     *                         by a class of window controllers
     * @param server           Used for connection to backend and websockets to function
     */
    @Inject
    public BoardCtrl(MainCtrl mainCtrl, ServerUtils server){
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.drag = new Draggable(this.server);

        webSocketLists();
        webSocketCards();
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

    void rnList(String name) {
        Lists l = (Lists) this.currentList.getProperties().get("list");
        l.title = name;
        server.renameList(l);
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
          mainCtrl.closeDEList();
        server.removeList((Lists) currentList.getProperties().get("list"));
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
     * Adds a new list to the board by creating all of its elements
     * and aligning them correspondingly in the listView
     * @param l list to be added
     */
    public void addNewList(Lists l) {
       VBox newList = createNewList(l);
       mainCtrl.addNewList(newList, firstRow);
       for(int i = 0; i<l.cards.size(); i++){
           Cards c = l.cards.get(i);
           addNewCard((VBox)newList.getChildren().get(0), c);
        }
    }

    /**
     * Creates a new list with all its elements
     * @param l list to be created
     * @return and VBox with the new list, aligned correspondingly
     */
    public VBox createNewList(Lists l){
        // creating the listView element
        VBox list = createListBody();
        VBox headerList = new VBox(6);
        HBox footerList = new HBox(30);

        headerList.setId("header");

        headerList.setMinSize(150, 235);
        footerList.setMinSize(150, 25);
        headerList.setAlignment(Pos.TOP_CENTER);
        footerList.setAlignment(Pos.TOP_CENTER);
        footerList.setStyle("-fx-padding: 0 7 0 7");
        list.setOnDragExited(drag::dragExited);
        list.setOnDragEntered(drag::dragEntered);
        list.setOnDragDropped(drag::dragDropped);
        list.setOnDragOver(drag::dragOver);
        // creating the adding card button, aligning and customising it
        Button addCardButton = createAddCardButton();

        // creating the Delete List button, aligning and customising it
        MenuButton refactorButtonList = createRefactorButton();

        footerList.getChildren().addAll(addCardButton, refactorButtonList);

        // creating the separator under the title, aligning and customising it
        Separator listSeparator = createSeparator();

        // creating the label for the name of the list, aligning and customising it
        Label listName = createListTitle(l.title);
        listName.setId("list_title_"+l.id);

        headerList.getChildren().addAll(listName, listSeparator);
        listContainers.add(headerList);

        list.getChildren().addAll(headerList, footerList);
        list.setId("list"+Long.toString(l.id));
        list.getProperties().put("list", l);
        return list;
    }

    /**
     * Creates a new button on the list, which when pressed,
     * shows a menu of two options: renaming or deleting the list;
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
        addButton.setStyle("-fx-border-radius: 50; -fx-background-radius: 70; " +
                "-fx-background-color: #c8a5d9; -fx-border-color: #8d78a6; " +
                "-fx-font-size: 10px;");
        addButton.setPrefWidth(24);
        addButton.setPrefHeight(23);
       addButton.setOnAction(this::openAddNewCard);
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
        listName.setStyle("-fx-font-size: 13px; -fx-content-display: " +
                "CENTER; -fx-padding: 5 10 0 10;");
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

    /**
     * Delete Card function
     * @param event Card delete process
     */
    @FXML
    public void deleteCard(ActionEvent event) {
        Button deleteCard = (Button) event.getTarget();
       // ((VBox)deleteCard.getParent().getParent()).getChildren().remove(deleteCard.getParent());
        Cards c = (Cards) deleteCard.getParent().getProperties().get("card");
        server.removeCard(c);
    }

    /**
     * open the Card Detail scene and modify all information about the card, including its name.....
     * In order to prevent it from opening while dragging,
     * the code here sets a time delay between pressing and releasing the left mouse button.
     * If the time delay is greater than a certain value,
     * the click option will not be triggered, so the cardDetail won't open during dragging.
     *
     * @param event a button (Hyperlink)
     */
    @FXML
    void cardDetail(ActionEvent event) {

            this.currentCard = (Hyperlink) event.getTarget();
            mainCtrl.showCardDetail();

    }

    /**
     * Save new card details to board scene
     * When the function returns from mainCtrl,
     * it will update the card name displayed on the board and refresh the pointer to currentCard.
     */
    void refreshCard(String text) {
        Cards c  = (Cards) this.currentCard.getParent().getProperties().get("card");
        c.title = text;
        server.renameCard(c);
        mainCtrl.closeCardDetails();
    }

    void openAddNewCard(ActionEvent event){
        this.currentList = (VBox)((Node)event.getSource()).getParent().getParent();
        mainCtrl.showAddCard();
    }


    public void addCardToList(String text){
        Lists l = (Lists) this.currentList.getProperties().get("list");
        Cards c = new Cards(text, l.cards.size(), l);
        c.list = l;
        server.addCard(c);
        mainCtrl.closeNewCard();
        //Cards
    }



    public void addNewCard(VBox anchor, Cards c){


        // create a new anchor pane for the card
        AnchorPane newCard = newAnchorPane();

        // add text and the delete button for the card
        newCard.getChildren().addAll(newHyperlink(), newDeleteCardButton());


        // append the card to the list

        this.currentCard = (Hyperlink) newCard.getChildren().get(0);
        newCard.getProperties().put("card", c);
        newCard.setId("card"+Long.toString(c.id));
        currentCard.setOnDragExited(drag::dragExited);
        currentCard.setOnDragEntered(drag::dragEntered);
        currentCard.setOnDragDropped(drag::dragDropped);
        currentCard.setOnDragOver(drag::dragOver);
        this.currentCard.setText(c.title);

        anchor.getChildren().add(c.positionInsideList+ 2, newCard);

        // show card detail scene to be able to set details of card

    }


    /**
     * Creates an empty anchor pane for a card
     * @return the created anchor pane
     */
    public AnchorPane newAnchorPane(){
        AnchorPane anchor = new AnchorPane();
        anchor.setLayoutX(0);
        anchor.setLayoutY(0);

        return anchor;
    }

    /**
     * Creates a new hyperlink for a card
     * @return the created hyperlink
     */
    public Hyperlink newHyperlink(){
        Hyperlink card = new Hyperlink();

        // set positioning, sizing, text alignment, and background color of the hyperlink
        card.setLayoutX(41);
        card.setLayoutY(1);
        card.setPrefSize(95, 23);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color:  #E6E6FA");

        card.setOnDragDetected(drag::dragDetected);

        card.setOnDragDone(drag::dragDone);
        // set the card to execute cardDetail on action
        card.setOnAction(this::cardDetail);
        return card;
    }

    /**
     * Create a new delete card button for a card
     * @return a new button
     */
    public Button newDeleteCardButton(){
        Button button = new Button();

        // set the text, positioning, mnemonic parsing, and style of the button
        button.setText("X");
        button.setLayoutX(11);
        button.setLayoutY(3);
        button.setMnemonicParsing(false);
        button.setStyle("-fx-background-color: #f08080; -fx-font-size: 9.0");

        // set the button to delete the card it is a part of when clicked
        button.setOnAction(this::deleteCard);
        return button;
    }

    public HBox getFirstRow() {
        return firstRow;
    }

    /**
     * Sets the name of the board that will be displayed to the user
     *
     * @param boardName The string containing the name of the board
     */
    public void setBoardName(String boardName) {
        this.boardName.setText(boardName);
    }

    public void exitBoard() {
        mainCtrl.showBoardOverview();
    }

    /**
     *The method here implements the functionality of the admin button,
     *  allowing users to enter a password to verify their admin privileges.
     *  If the password is correct, the corresponding button functionality will be displayed,
     *  allowing the user to upgrade to an admin and perform renaming and deletion functions.
     */
    @FXML
    private Button lockBtu;
    private BooleanProperty adminLock = new SimpleBooleanProperty(false);
    private String adminPassword = "6464";
    @FXML
    private Button removeBoard;
    @FXML
    void adminLogin() {
        if (adminLock.getValue()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Admin!");
            alert.setHeaderText(null);
            alert.setContentText("Admin has been unlocked!");
            alert.showAndWait();
        } else {
            mainCtrl.showConfirmAdmin();
        }
    }
    public String getAdminPassword(){return adminPassword;}
    public void openAdminFeatures() {
        adminLock.set(true);
        removeBoard.setVisible(true);
        mainCtrl.closeConfirmAdmin();
        lockBtu.setStyle("-fx-border-color: green");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login successful!");
        alert.setHeaderText(null);
        alert.setContentText("Welcome admin!");
        alert.showAndWait();
    }
    /**
     *The functionality of the delete current board button will be displayed
     * after obtaining admin privileges.
     *  It returns to the board overview interface and deletes the current board.
     */
    @FXML
    void removeBoard() {
        mainCtrl.removeCurrentBoard();
    }


}
