package client.scenes;

import client.scenes.config.Draggable;
import client.utils.ServerUtils;
import commons.Boards;
import commons.Lists;
import commons.Cards;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;


import javafx.event.ActionEvent;
import javafx.scene.paint.Color;

import javax.inject.Inject;

public class BoardCtrl {
    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private final CardDetailsCtrl cardDetailsCtrl;

    @FXML
    private Button tags;
    @FXML
    private AnchorPane rootContainer;
    @FXML
    private HBox firstRow;
    @FXML
    private Label boardName;

    private Boards board;

    List<VBox> listContainers;
    List<AnchorPane> listCards;

    private VBox currentList;

    private Cards currentCard;

    private List<Lists> lists;

    private final Draggable drag;
    private List<String> serverURLS;


    /**
     * The method adds the cardContainers and the listContainers into arrayLists in order to access
     * them easier in the following methods
     * @param board - sets variable board from class to specific board
     */
    public void initialize(Boards board) {
        listContainers = new ArrayList<>();
        listCards = new ArrayList<>();
        this.board = board;

        if(!serverURLS.contains(server.getServer())) {
            serverURLS.add(server.getServer());
            webSocketLists();
            webSocketCards();
        }
        refresh();
    }

    /**
     * This method configures websockets for lists
     */
    private void webSocketLists() {
        server.registerForMessages("/topic/lists", Lists.class, l->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(l.board.name.equals(boardName.getText())) {
                        addNewList(l);
                        refreshData();
                    }
                }
            });
        });

        server.registerForMessages("/topic/lists/rename", Lists.class, l->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                        initialize(board);
                }
            });
        });

        server.registerForMessages("/topic/lists/remove", Lists.class, l->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(l.board.name.equals(boardName.getText())) {
                        VBox list = (VBox)rootContainer.lookup("#list"+l.id);
                        firstRow.getChildren().removeAll(list);
                        refreshData();
                    }
                }
            });
        });

        server.registerForMessages("/topic/boards/update", Boards.class, b -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    initialize(b);
                }
            });
        });
    }

    /**
     * This method configures websockets for cards
     */
    private void webSocketCards() {
        server.registerForMessages("/topic/cards/remove", Cards.class, c->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(c.list.board.name.equals(boardName.getText())) {
                        VBox l = (VBox) rootContainer.lookup("#list"+c.list.id);
                        AnchorPane card = (AnchorPane) rootContainer.lookup("#card"+c.id);
                        ((VBox) l.getChildren().get(0)).getChildren().remove(card);
                        refreshData();
                    }

                }
            });
        });

        server.registerForMessages("/topic/cards/rename", Cards.class, c->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(c.list.board.name.equals(boardName.getText())) {
                        ((Label)((HBox)((VBox)rootContainer.lookup("#card"+c.id))
                                .getChildren().get(0))
                                .getChildren().get(0)).setText(c.title);
                        refreshData();
                    }
                }
            });
        });

        server.registerForMessages("/topic/cards/add", Cards.class, c->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(c.list.board.name.equals(boardName.getText())) {
                        VBox l = (VBox) rootContainer.lookup("#list"+c.list.id);
                        addNewCard((VBox) l.getChildren().get(0), c);
                        refreshData();
                    }
                }
            });
        });
    }

    /**
     * Method that refreshes the board by getting all lists from the
     * server and displaying them
     */
    public void refresh(){
        firstRow.getChildren().clear();
        lists = server.getListsByBoard(board.id);
        for (Lists list : lists) {
            addNewList(list);
        }
    }

    /**
     * Method that gets lists for a specific board
     */
    public void refreshData(){
        lists = server.getListsByBoard(board.id);
        refreshLists(lists);
    }

    /**
     * Method that refreshes all the cards in a list
     * @param listContainer the container of the list
     * @param c the list of cards
     */
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

    /**
     * Method that refreshes all the lists in a board
     * @param l a list of lists to be redrawn
     */
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
     * @param cardDetailsCtrl  Used for calling methods that have to do with opening
     *                         the card details scene for a card
     */
    @Inject
    public BoardCtrl(MainCtrl mainCtrl, ServerUtils server, CardDetailsCtrl cardDetailsCtrl){
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.drag = new Draggable(this.server);
        this.cardDetailsCtrl = cardDetailsCtrl;

        serverURLS = new ArrayList<>();
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

    /**
     * Method that renames a list to a given name and saves it to the database
     * @param name the new name of the list
     */
    void rnList(String name) {
        Lists l = (Lists) this.currentList.getProperties().get("list");
        l.title = name;
        server.renameList(l);
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Trigger function for deleting List option in the drop-down options button
     * @param event List delete process
     */
    @FXML
    void deleteList(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        ContextMenu popup = menuItem.getParentPopup();
        this.currentList = (VBox) popup.getOwnerNode().getParent().getParent();
        mainCtrl.showDeleteList();

    }

    /**
     * Closes delete card scene and deletes card from database
     */
    void deleteL() {
        mainCtrl.closeSecondaryStage();
        server.removeList((Lists) currentList.getProperties().get("list"));
    }

    /**
     * Method closes the secondary scene, cancelling the delete
     */
    void undeleteL() {
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Trigger function for adding a List with a button //ActionEvent event
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
        refactorButtonList.setText("Edit List");
        refactorButtonList.setPrefWidth(60);
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
        Cards c = (Cards) ((AnchorPane)deleteCard.getParent()).getChildren()
                .get(1).getProperties().get("card");
        currentCard = c;
        mainCtrl.showDeleteCard();
    }

    /**
     * Method tha deletes the card from the database and closes the secondary scene
     */
    void deleteCard() {
        server.removeCard(currentCard);
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Method that cancels the deletion and closes the secondary scene
     */
    void undeleteCard() {
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Method that shows the help scene
     */
    public void showHelpScene(){
        mainCtrl.showHelpScene();
    }

    /**
     * Opens the Card Detail scene and modify all information about the card
     * Event is triggered by double-clicking on a card
     *
     * @param event Object containing information about the mouse event
     */
    @FXML
    void cardDetail(MouseEvent event) {
        if(event.getClickCount() == 2) {
            VBox currentCard = (VBox) ((AnchorPane)(
                    (AnchorPane)event.getSource()).getParent()).getChildren().get(1);
            Cards openedCard = (Cards) currentCard.getProperties().get("card");
            cardDetailsCtrl.setBoard(board);
            cardDetailsCtrl.setOpenedCard(openedCard);
            mainCtrl.showCardDetail();
        }
    }

    /**
     * Method opens the secondary scene for adding a new card
     * @param event button click indicating new card should be added
     */
    void openAddNewCard(ActionEvent event){
        this.currentList = (VBox)((Node)event.getSource()).getParent().getParent();
        mainCtrl.showAddCard();
    }

    /**
     * Adds a card of name text to a list
     * @param text the name of the new card
     */
    public void addCardToList(String text){
        Lists l = (Lists) this.currentList.getProperties().get("list");
        Cards c = new Cards(text, l.cards.size(), l, "", new ArrayList<>());
        c.list = l;
        server.addCard(c);
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Adds a list of name text to a board
     * @param text the name of the list
     * @param position the position of the list
     */
    public void addListToBoard(String text, int position){
        // the following two lines causes a stack overflow
        Lists list = new Lists(text, position, board);

        try {
            server.addList(list);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    /**
     * Method that creates a new card and adds it
     * @param anchor the anchor to which the card should be added
     * @param c the card to be added
     */
    public void addNewCard(VBox anchor, Cards c){
        // create a new anchor pane for the card
        AnchorPane newCard = newAnchorPane();

        // create the button to delete a card and the card's body
        Button deleteCard = newDeleteCardButton();
        VBox card = newCardBody(c);
        AnchorPane over = newAnchorPane();
        over.setPrefWidth(114.4);
        over.setPrefHeight(34.4);
        over.setLayoutX(30);
        over.setOnDragDetected(drag::dragDetected);
        over.setOnMouseClicked(this::cardDetail);

        card.getProperties().put("card", c);
        card.setId("card"+Long.toString(c.id));
        card.setOnDragExited(drag::dragExited);
        card.setOnDragEntered(drag::dragEntered);
        card.setOnDragDropped(drag::dragDropped);
        card.setOnDragOver(drag::dragOver);
        card.setOnMouseClicked(this::cardDetail);

        newCard.getChildren().addAll(deleteCard, card, over);
        //newCard.setOnDragDetected(drag::dragDetected);
        /*/ append the card to the list
        Hyperlink currentCard = (Hyperlink) newCard.getChildren().get(0);
        newCard.getProperties().put("card", c);
        newCard.setId("card"+Long.toString(c.id));
        currentCard.setOnDragExited(drag::dragExited);
        currentCard.setOnDragEntered(drag::dragEntered);
        currentCard.setOnDragDropped(drag::dragDropped);
        currentCard.setOnDragOver(drag::dragOver);
        currentCard.setText(c.title);*/

        anchor.getChildren().add(c.positionInsideList+ 2, newCard);

        // show card detail scene to be able to set details of card

    }

    /**
     * Create the body of a card, well customised and aligned accordingly
     * @param c the card for which the card body is created
     * @return the new card's body, a vbox
     */
    public VBox newCardBody(Cards c){
        VBox cardBody = new VBox();
        //layout settings
        cardBody.setPrefWidth(114.4);
        cardBody.setPrefHeight(34.4);
        cardBody.setFillWidth(true);
        cardBody.setLayoutX(30);

        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setColor(Color.rgb(173, 139, 223));
        innerShadow.setRadius(8.83);
        innerShadow.setWidth(18.66);
        innerShadow.setHeight(18.66);

        //properties settings
        cardBody.setStyle("-fx-background-color: #e6e6fa; -fx-background-radius: 4;");
        cardBody.setEffect(innerShadow);

        cardBody.setOnDragDetected(drag::dragDetected);

        HBox cardOverviewInfo = newCardOverviewBody(c);
        HBox cardTags = newCardTagsBody();

        cardBody.getChildren().addAll(cardOverviewInfo, cardTags);
        return cardBody;
    }

    /**
     * Creates the overview part of the body of a card, which contains
     * its title, a progress state regarding the number of subtasks checked
     * and whether the card also has a description or not
     * @param c the card for which the body in frontend is created
     * @return the overview part of the body of the given card
     */
    public HBox newCardOverviewBody(Cards c){
        HBox cardOverviewBody = new HBox();

        cardOverviewBody.setPrefWidth(114.4);
        cardOverviewBody.setPrefHeight(25.6);
        cardOverviewBody.setStyle("-fx-background-color: #e6e6fa; -fx-background-radius: 4;");

        Label cardTitle = new Label(c.title);
        VBox cardDetailsOverview = newCardDetailsOverview();

        cardTitle.setPrefWidth(54.4);
        cardTitle.setPrefHeight(25.6);
        cardTitle.setPadding(new Insets(0, 0, -2, 10));
        cardTitle.setStyle("-fx-font-size: 11;");

        //cardTitle.setOnDragDetected(drag::dragDetected);
        cardDetailsOverview.setOnDragDetected(drag::dragDetected);

        cardOverviewBody.getChildren().addAll(cardTitle, cardDetailsOverview);
        return cardOverviewBody;
    }

    /**
     * Creates the part of the body of a card where are displayed the process
     * regarding the number of subtasks and whether the card also has a
     * description or not
     * @return the 'details' part of the body of the given card
     */
    public VBox newCardDetailsOverview(){
        VBox cardDetailsOverview = new VBox();
        cardDetailsOverview.setPrefWidth(61);
        cardDetailsOverview.setPrefHeight(25.6);

        Label subtasksCount = new Label("0/0 subtasks");
        Label descriptionExistence = new Label("Description: no");

        subtasksCount.setStyle("-fx-font-size: 7;");
        subtasksCount.setAlignment(Pos.CENTER_RIGHT);
        subtasksCount.setPrefWidth(61);
        subtasksCount.setPrefHeight(13);
        subtasksCount.setPadding(new Insets(0, 10, -5, 0));

        descriptionExistence.setStyle("-fx-font-size: 7;");
        descriptionExistence.setAlignment(Pos.CENTER_RIGHT);
        descriptionExistence.setPrefWidth(61);
        descriptionExistence.setPrefHeight(13);
        descriptionExistence.setPadding(new Insets(-1, 10, 1, 0));

        subtasksCount.setOnDragDetected(drag::dragDetected);
        descriptionExistence.setOnDragDetected(drag::dragDetected);

        cardDetailsOverview.getChildren().addAll(subtasksCount, descriptionExistence);
        return cardDetailsOverview;
    }

    /**
     * Creates the part of the body of a card where are displayed the tags assessed
     * to the card
     * @return the part where are displayed the tags assessed to the card
     */
    public HBox newCardTagsBody(){
        HBox cardTagsBody = new HBox(6);

        cardTagsBody.setPrefWidth(114.4);
        cardTagsBody.setPrefHeight(6.4);
        cardTagsBody.setPadding(new Insets(0, 0, 0, 8));
        cardTagsBody.setStyle("-fx-background-color: #e6e6fa; -fx-background-radius: 4;");

        return cardTagsBody;
    }

    /**
     * Creates an empty anchor pane for a card
     * @return the created anchor pane
     */
    public AnchorPane newAnchorPane(){
        AnchorPane anchor = new AnchorPane();
        anchor.setPrefWidth(150.4);
        anchor.setPrefHeight(36);
        anchor.setOnDragDetected(drag::dragDetected);

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

        // set the card to execute cardDetail on action
//        card.setOnAction(this::cardDetail);
        card.setOnMouseClicked(this::cardDetail);
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
        button.setLayoutX(6);
        button.setLayoutY(7);
        button.setMnemonicParsing(false);
        button.setStyle("-fx-background-color: #f08080; -fx-font-size: 9.0");
        button.setPadding(new Insets(3, 6, 1.5, 6));

        // set the button to delete the card it is a part of when clicked
        button.setOnAction(this::deleteCard);
        return button;
    }

    /**
     * Method that returns the first row of lists
     * @return the first row of lists
     */
    public HBox getFirstRow() {
        return firstRow;
    }

    /**
     * Sets the name of the board that will be displayed to the user
     *
     * @param b The string containing the name of the board
     */
    public void setBoardName(Boards b) {
        this.boardName.setText(b.name);

        this.board = b;
    }

    /**
     * Method that adds board to users visited boards
     * @param board the board to be added
     */
    public void addBoardToList(Boards board){
        server.addBoardToUser(board);
    }

    /**
     * Exits the specific board to show board overview
     */
    public void exitBoard() {
        mainCtrl.showBoardOverview();
    }

    /**
     * Opens the scene which shows the tags the current board has
     */
    public void openTag(){
        mainCtrl.showTagControl(board);
    }
}
