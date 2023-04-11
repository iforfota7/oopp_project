package client.scenes;

import client.scenes.config.Draggable;
import client.scenes.config.Shortcuts;
import client.utils.ServerUtils;
import commons.*;
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
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import javax.inject.Inject;

public class BoardCtrl {
    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private final CardDetailsCtrl cardDetailsCtrl;

    @FXML
    private HBox firstRow;
    @FXML
    private Label boardName;
    @FXML
    public ScrollPane scrollPane;

    private Boards board;

    List<VBox> listContainers;
    List<AnchorPane> listCards;

    private VBox currentList;

    private Cards currentCard;

    private final Draggable drag;
    private Shortcuts shortcuts;

    private List<String> serverURLS;

    private Font font;

    private String currentCardColor;


    /**
     * The method adds the cardContainers and the listContainers into arrayLists in order to access
     * them easier in the following methods
     * @param board - sets variable board from class to specific board
     */
    public void initialize(Boards board) {
        font = Font.font("Bell MT", FontWeight.NORMAL,
                FontPosture.REGULAR, 12);
        listContainers = new ArrayList<>();
        listCards = new ArrayList<>();
        this.board = board;
        if(board != null)
            boardName.setText(board.name);

        if(!serverURLS.contains(server.getServer())) {
            serverURLS.add(server.getServer());
            webSocketLists();
            webSocketCards();
            webSocketsBoard();
        }
        refresh();
        firstRow.requestFocus();
        server.registerForUpdates(b->{

            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    if(mainCtrl.isBoard(b)){


                        Alert e = new Alert(Alert.AlertType.WARNING,
                                "This board has been deleted by admin");
                        e.show();

                        mainCtrl.closeSecondaryStage();
                        mainCtrl.closeThirdStage();
                        mainCtrl.showBoardOverview();


                    }


                }
            });
        });
    }


    /**
     * Calls method for stopping Thread Executor service from server.
     */
    public void stop(){
        server.stop();
    }

    /**
     * This method configures websockets for lists
     */
    private void webSocketLists() {
        server.registerForMessages("/topic/lists", Lists.class, l->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    initialize(board);
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
                    initialize(board);
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

        server.registerForMessages("/topic/boards/rename", Boards.class, b -> {
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
                    initialize(board);
                }
            });
        });

        server.registerForMessages("/topic/cards/rename", Cards.class, c->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    initialize(board);
                }
            });
        });

        server.registerForMessages("/topic/cards/add", Cards.class, c->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    initialize(board);
                }
            });
        });
    }

    /**
     * This method configures websockets for the board
     */
    public void webSocketsBoard() {
        server.registerForMessages("/topic/boards/setCss", Boards.class, board->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    server.revertPreset(board);
                    initialize(board);
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
        board.lists = server.getListsByBoard(board.id);

        Cards currentCardObject = null;
        if(shortcuts != null)
            currentCardObject = shortcuts.getCurrentCardObject();
        shortcuts=new Shortcuts(mainCtrl, server, this, currentCardObject);
        mainCtrl.getBoard().setOnKeyPressed(shortcuts::activateShortcut);

        for (Lists list : board.lists) {
            addNewList(list);
        }

        refreshCustomization();
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
    public BoardCtrl(MainCtrl mainCtrl, ServerUtils server,
                     CardDetailsCtrl cardDetailsCtrl){
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.drag = new Draggable(this.server);
        this.cardDetailsCtrl = cardDetailsCtrl;

        serverURLS = new ArrayList<>();

    }

    /**
     * Default constructor of BoardCtrl
     */
    public BoardCtrl() {

        mainCtrl=new MainCtrl();
        server=new ServerUtils();
        cardDetailsCtrl=new CardDetailsCtrl(server,mainCtrl);

        drag = new Draggable(server);
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
     * Method closes the secondary scene, cancelling the deletion
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
        firstRow.getChildren().add(newList);
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
        VBox headerList = new VBox(7);
        HBox footerList = new HBox(30);

        headerList.setId("header");

        headerList.setMinSize(200, 360);
        footerList.setMinSize(200, 40);
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
        Font fontList = Font.font("Bell MT", FontWeight.NORMAL,
                FontPosture.REGULAR, 17);
        listName.setFont(fontList);

        headerList.getChildren().addAll(listName, listSeparator);
        listContainers.add(headerList);

        list.getChildren().addAll(headerList, footerList);
        list.setId("list"+Long.toString(l.id));
        list.getProperties().put("list", l);
        listName.setStyle("-fx-font-size: 16px; -fx-content-display: CENTER; " +
                "-fx-padding: 7 10 0 10; -fx-text-fill: " + board.listFtColor + ";");
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
        refactorButtonList.setText("Edit List");
        refactorButtonList.setPrefWidth(97);
        refactorButtonList.setPrefHeight(25);
        refactorButtonList.setStyle("-fx-background-color: #f08080;");
        refactorButtonList.setFont(font);

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
                "-fx-font-size: 15px;");
        addButton.setPadding(new Insets(-1, 0, 0, 0));
        addButton.setPrefWidth(28);
        addButton.setPrefHeight(26.4);
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
        listName.setStyle("-fx-content-display: " +
                "CENTER; -fx-padding: 7 10 0 10;");
        listName.setAlignment(Pos.CENTER);
        return listName;
    }

    /**
     * Creates the body of the list
     * @return a Vbox, which represents the body of the list, designed accordingly
     */
    public VBox createListBody(){
        VBox vbox = new VBox();
        vbox.setPrefWidth(200);
        vbox.setPrefHeight(400);
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
     * Deletes a card from the database, after the user confirmed
     * the deletion
     */
    public void deleteCard() {
        server.removeCard(currentCard);
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Closes the scene which asks for confirmation of deleting a card
     */
    public void undeleteCard() {
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Opens the Help Scene of a board when the button 'H' from
     * bottom right is pushed
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
            AnchorPane currentCard = (AnchorPane) event.getSource();
            Cards openedCard = (Cards) ((AnchorPane)currentCard.getParent())
                    .getChildren().get(1).getProperties().get("card");

            cardDetailsCtrl.setBoard(board);
            cardDetailsCtrl.setOpenedCard(openedCard);
            cardDetailsCtrl.colors = board.colorPreset.get(openedCard.colorStyle);
            mainCtrl.showCardDetail();
            cardDetailsCtrl.refreshOpenedCard();
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
        c.colorStyle = board.defaultColor;
        server.addCard(c);
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Adds a list of name text to a board
     * @param text the name of the list
     * @param position the position of the list
     */
    public void addListToBoard(String text, int position){
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
        AnchorPane blanket = newAnchorPane();
        blanket.setPrefWidth(162.4);
        blanket.setPrefHeight(40);
        blanket.setLayoutX(30);
        blanket.setOnDragDetected(drag::dragDetected);
        blanket.setOnDragExited(drag::dragExited);
        blanket.setOnDragEntered(drag::dragEntered);
        blanket.setOnDragDropped(drag::dragDropped);
        blanket.setOnMouseClicked(this::cardDetail);

        blanket.setId("card"+Long.toString(c.id));
        blanket.setOnMouseEntered(shortcuts::onMouseHover);
        blanket.getProperties().put("card", c);

        if(c.equals(shortcuts.getCurrentCardObject())) {
            shortcuts.setCurrentCard(blanket);
            shortcuts.highlightCurrentCard();
        }

        card.getProperties().put("card", c);
        card.setId("card"+Long.toString(c.id));

        // add text and the delete button for the card
        newCard.getChildren().addAll(deleteCard, card, blanket);
        newCard.getProperties().put("card", c);
        newCard.setId("card"+Long.toString(c.id));

        if(shortcuts.getCurrentCard()!=null &&
                newCard.getId().equals(shortcuts.getCurrentCard().getId())) {
            blanket.setStyle("-fx-border-color: red;  " +
                    "-fx-border-radius: 4;");
            shortcuts.setCurrentCard(blanket);
        }

        if(c.positionInsideList > 5){
            Double height = anchor.getMinHeight();
            anchor.setMinHeight(height + 47);
        }
        anchor.getChildren().add(c.positionInsideList+ 2, newCard);
    }

    /**
     * Create the body of a card, well customised and aligned accordingly
     * @param c the card for which the card body is created
     * @return the new card's body, a vbox
     */
    public VBox newCardBody(Cards c){
        VBox cardBody = new VBox();
        //layout settings
        cardBody.setPrefWidth(162.4);
        cardBody.setPrefHeight(40);
        cardBody.setFillWidth(true);
        cardBody.setLayoutX(30);

        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setColor(Color.rgb(173, 139, 223));
        innerShadow.setRadius(8.83);
        innerShadow.setWidth(18.66);
        innerShadow.setHeight(18.66);

        if(c.colorStyle == null || !(board.colorPreset.containsKey(c.colorStyle))) {
            currentCardColor = board.colorPreset.get(board.defaultColor);
            c.colorStyle = null; // this will be setup later
        }
        else
            currentCardColor = board.colorPreset.get(c.colorStyle);

        String[] colors = currentCardColor.split(" ");
        cardBody.setStyle("-fx-background-color: " +
                colors[0] + ";-fx-background-radius: 4;");
        cardBody.setEffect(innerShadow);

        HBox cardOverviewInfo = newCardOverviewBody(c);
        HBox cardTags = newCardTagsBody(c);
        cardTags.setStyle("-fx-background-color: " + colors[0] +
                "; -fx-background-radius: 4;");
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

        cardOverviewBody.setPrefWidth(122);
        cardOverviewBody.setPrefHeight(31);

        Label cardTitle = new Label(c.title);
        VBox cardDetailsOverview = newCardDetailsOverview(c);

        cardTitle.setPrefWidth(96.8);
        cardTitle.setPrefHeight(31.2);
        cardTitle.setPadding(new Insets(0, 0, -2, 12));
        cardTitle.setFont(font);

        String[] colors = currentCardColor.split(" ");

        cardOverviewBody.setStyle("-fx-background-color: " +
                colors[0] + "; -fx-background-radius: 4;");
        cardDetailsOverview.setStyle("-fx-background-color: " + colors[0] + ";"
                + "-fx-text-fill: " + colors[1] + "; " +
                "-fx-background-radius: 4;");
        cardTitle.setStyle("-fx-background-color: " + colors[0] + ";"
                + "-fx-text-fill: " + colors[1] + "; " +
                "-fx-background-radius: 4;");
        cardOverviewBody.getChildren().addAll(cardTitle, cardDetailsOverview);

        return cardOverviewBody;
    }

    /**
     * Creates the part of the body of a card where are displayed the process
     * regarding the number of subtasks and whether the card also has a
     * description or not
     *
     * @param card Object containing information about the card
     * @return the 'details' part of the body of the given card
     */
    public VBox newCardDetailsOverview(Cards card){
        VBox cardDetailsOverview = new VBox();
        cardDetailsOverview.setPrefWidth(66.4);
        cardDetailsOverview.setPrefHeight(31.2);

        String[] colors = currentCardColor.split(" ");

        Label subtasksCount = createSubtasksCountLabel(card);
        subtasksCount.setStyle("-fx-font-size: 7; -fx-text-fill: " + colors[1]);
        ProgressBar subtasksProgressBar = createSubtasksProgressBar(card);
        String labelText = "Description: no";
        if(!card.description.equals(""))
            labelText = "Description: yes";
        Label descriptionExistence = new Label(labelText);
        descriptionExistence.setStyle("-fx-font-size: 8; -fx-text-fill: " + colors[1]);
        descriptionExistence.setAlignment(Pos.CENTER_LEFT);
        descriptionExistence.setPrefWidth(50.4);
        descriptionExistence.setPrefHeight(7);
        descriptionExistence.setPadding(new Insets(0, -7, -8, 1));

        cardDetailsOverview.setAlignment(Pos.TOP_LEFT);
        cardDetailsOverview.getChildren().addAll(descriptionExistence, subtasksCount,
                subtasksProgressBar);

        return cardDetailsOverview;
    }

    /**
     * regarding the number of subtasks and whether the card also has a
     * description or not
     * @param card Object containing information about the card
     * @return the 'title' part of the body of the given card
     */
    private Label createSubtasksCountLabel(Cards card) {
        String subtasksLabelText = "no subtasks";
        if(card.subtasks != null && card.subtasks.size() > 0) {
            int total = card.subtasks.size();
            int done = 0;
            for(Subtask subtask : card.subtasks)
                if(subtask.checked)
                    done++;
            subtasksLabelText = done + "/" + total + " subtasks";
        }
        Label subtasksCount = new Label(subtasksLabelText);

        String descriptionLabelText = "Description: no";
        if(!card.description.equals("")) {
            descriptionLabelText = "Description: yes";
        }
        Label descriptionExistence = new Label(descriptionLabelText);

        subtasksCount.setAlignment(Pos.CENTER_RIGHT);
        subtasksCount.setPrefWidth(65.6);
        subtasksCount.setPrefHeight(16);
        subtasksCount.setPadding(new Insets(-3, 10, -8, 0));
        return subtasksCount;
    }

    /**
     *To create a progress bar for a task:
     * @param card Object containing information about the card
     * @return the 'progress bar' part of the body of the given card
     */
    private ProgressBar createSubtasksProgressBar(Cards card) {
        ProgressBar subtasksProgressBar = new ProgressBar();
        subtasksProgressBar.setPrefWidth(40);
        subtasksProgressBar.setPrefHeight(12);
        double progress = 0;
        if (card.subtasks != null && card.subtasks.size() > 0) {
            int total = card.subtasks.size();
            int done = 0;
            for (Subtask subtask : card.subtasks)
                if (subtask.checked)
                    done++;
            progress = (double) done / total;
            subtasksProgressBar.setProgress(progress);
            if (progress == 1.0) {
                subtasksProgressBar.setStyle("-fx-accent: green;");
            } else if (progress > 0) {
                subtasksProgressBar.setStyle("-fx-accent: orange;");
            }
        }
        subtasksProgressBar.setPadding(new Insets(0, -16, 0, 13));
        return subtasksProgressBar;
    }

    /**
     * Creates the part of the body of a card where are displayed the tags assessed
     * to the card
     * @param card The card whose tags we render
     * @return the part where are displayed the tags assessed to the card
     */
    public HBox newCardTagsBody(Cards card){
        HBox cardTagsBody = new HBox(11.5);

        cardTagsBody.setPrefWidth(162.4);
        cardTagsBody.setPrefHeight(9.6);
        cardTagsBody.setPadding(new Insets(0, 0, 0, 8));
        cardTagsBody.setStyle("-fx-background-color: #e6e6fa; -fx-background-radius: 4;");

        for(Tags tag : card.tags) {
            Circle circle = new Circle();
            circle.setRadius(2);
            circle.setFill(Color.web(tag.backgroundColor));
            cardTagsBody.getChildren().add(circle);
        }

        return cardTagsBody;
    }

    /**
     * Creates an empty anchor pane for a card
     * @return the created anchor pane
     */
    public AnchorPane newAnchorPane(){
        AnchorPane anchor = new AnchorPane();
        anchor.setPrefWidth(200);
        anchor.setPrefHeight(40);
        anchor.setOnDragDetected(drag::dragDetected);

        return anchor;
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
        button.setLayoutY(11);
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
    public void addBoardToUser(Boards board){
        server.addBoardToUser(board);
    }

    /**
     * Exits the specific board to show board overview
     */
    public void exitBoard() {
        shortcuts.setCurrentCardObject(null);
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
     * Gets the board object of the current board
     * @return this board's board object
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
        scrollPane.setStyle("-fx-background: " + board.boardBgColor + ";");
        boardName.setStyle("-fx-text-fill: " + board.boardFtColor  + ";");
    }

    /**
     * Opens the scene which shows the tags the current board has
     */
    public void openTag(){
        mainCtrl.showTagControl(board);
    }

    /**
     * Sets the currentCard reference to the provided card
     * @param card the card to be assigned to currentCard
     */
    public void setCurrentCard(Cards card) {
        this.currentCard = card;
    }

    /**
     * Gets the card details controller object of this board
     * @return the card details controller object
     */
    public CardDetailsCtrl getCardDetailsCtrl() {
        return cardDetailsCtrl;
    }
}

