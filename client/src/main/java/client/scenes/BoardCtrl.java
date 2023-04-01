package client.scenes;

import client.scenes.config.Draggable;
import client.utils.ServerUtils;
import commons.Boards;
import commons.Lists;
import commons.Cards;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;


import javafx.event.ActionEvent;

import javax.inject.Inject;

public class BoardCtrl {
    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private final CardDetailsCtrl cardDetailsCtrl;
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

    private List<Lists> lists;

    private final Draggable drag;


    /**
     * The method adds the cardContainers and the listContainers into arrayLists in order to access
     * them easier in the following methods
     * @param board - sets variable board from class to specific board
     */
    public void initialize(Boards board) {
        listContainers = new ArrayList<>();
        listCards = new ArrayList<>();
        this.board = board;
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
                        ((Hyperlink)((AnchorPane) rootContainer.lookup("#card"+c.id)).
                                getChildren().get(0)).setText(c.title);
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
        this.board = server.getBoardByID(boardName.getText());
        firstRow.getChildren().clear();
        lists = server.getListsByBoard(board.id);
        for (Lists list : lists) {
            addNewList(list);
        }
        refreshCustomization();
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
     * @param mainCtrl          The master controller, which will later be replaced
     *                          by a class of window controllers
     * @param server            Used for connection to backend and websockets to function
     * @param cardDetailsCtrl   Used for calling methods that have to do with opening
     *                          the card details scene for a card
     */
    @Inject
    public BoardCtrl(MainCtrl mainCtrl, ServerUtils server, CardDetailsCtrl
            cardDetailsCtrl){
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.drag = new Draggable(this.server);
        this.cardDetailsCtrl = cardDetailsCtrl;
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
     * Method closes the secondary scene, cancelling to delete
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

   // @FXML
    //void renameBoard(){mainCtrl.showRenameBoard();}

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
        listName.setStyle("-fx-text-fill: " + board.listFtColor + ";");
        list.setStyle("-fx-background-color: " + board.listBgColor + ";");
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
        Cards c = (Cards) deleteCard.getParent().getProperties().get("card");
        server.removeCard(c);
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
            Hyperlink currentCard = (Hyperlink) event.getSource();
            Cards openedCard = (Cards) currentCard.getParent().getProperties().get("card");
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
        Cards c = new Cards(text, l.cards.size(), l, "", null);
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

        // add text and the delete button for the card
        newCard.getChildren().addAll(newHyperlink(), newDeleteCardButton());


        // append the card to the list

        Hyperlink currentCard = (Hyperlink) newCard.getChildren().get(0);
        newCard.getProperties().put("card", c);
        newCard.setId("card"+Long.toString(c.id));
        currentCard.setOnDragExited(drag::dragExited);
        currentCard.setOnDragEntered(drag::dragEntered);
        currentCard.setOnDragDropped(drag::dragDropped);
        currentCard.setOnDragOver(drag::dragOver);
        currentCard.setText(c.title);
        currentCard.setStyle("-fx-background-color: " + board.cardBgColor + ";"
                + "-fx-text-fill: " + board.cardFtColor + ";");
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
        button.setLayoutX(11);
        button.setLayoutY(3);
        button.setMnemonicParsing(false);
        button.setStyle("-fx-background-color: #f08080; -fx-font-size: 9.0");

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
     * Exits the specific board to show board overview
     */
    public void exitBoard() {
        mainCtrl.showBoardOverview();
    }

    /**
     * Open a Customization window to modify the color and font of this board.
     */
    @FXML
    void openCustomization() {
        mainCtrl.showCustomization(boardName.getText());
    }

    /**
     * confirm the board elements.
     * @return current board
     */
    public Boards getCurrentBoard() {
        return board;
    }

    /**
     * Update board elements
     * @param currentBoard board after set color
     */
    public void setCurrentBoard(Boards currentBoard) {
        this.board = currentBoard;
    }
    /**
     *Reset the corresponding colors of the current board
     *  based on the color information stored in the board.
     */
    public void refreshCustomization() {
        //boards color CSS setting
        boardName.getScene().getRoot().lookup("#firstRow").
                setStyle("-fx-background-color: " + board.boardBgColor + ";");
        boardName.getScene().getRoot()
                .setStyle("-fx-background-color: " + board.boardBgColor + ";");
        boardName.setStyle("-fx-text-fill: " + board.boardFtColor  + ";");
    }
}
