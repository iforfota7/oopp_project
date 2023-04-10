package client.scenes;

import client.scenes.config.TagsCardDetails;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


public class CardDetailsCtrl {
    @FXML
    private TextField cardTitleInput;
    @FXML
    private Text warning;
    @FXML
    private TextArea description;
    @FXML
    private VBox taskList;
    @FXML
    private GridPane tagList;
    @FXML
    private HBox inputSubtask;
    @FXML
    private TextField subtaskName;
    @FXML
    private Text warningSubtask;
    @FXML
    private Button saveButton;
    @FXML
    private Button closeButton;
    private int inputsOpen = 0;
    private boolean rename = false;

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private TagsCardDetails tagsCardDetails;

    private Subtask toRename;
    public Cards openedCard;
    public Boards board;
    private boolean sceneOpened = false;
    public boolean changesFromTags;
    private boolean changesFromSubtasks;
    private boolean changesInTitleOrDescription;
    private boolean changesInCustomization;
    private List<String> serverURLS;
    public String colors;
    private List<Tags> initialTags;
    private List<Subtask> initialSubtasks;
    private String initialCardColors;

    /**
     * Initializes the card details controller object
     * An instance of TagsCardDetails is initialized
     * in order to separate tag logic from the rest
     *
     * @param server Used for sending requests to the server
     * @param mainCtrl Used for navigating through different scenes
     */
    @Inject
    public CardDetailsCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
        serverURLS = new ArrayList<>();
    }

    /**
     * Method used to initialize the websockets
     * If websockets have already been initialized for this server address
     * the method does nothing
     *
     */
    public void init() {
        if(!serverURLS.contains(server.getServer())) {
            serverURLS.add(server.getServer());
            websocketConfig();
        }

        changesFromTags = false;
        changesFromSubtasks = false;
        changesInTitleOrDescription = false;
        changesInCustomization = false;

        initialTags = new ArrayList<>();
        initialSubtasks = new ArrayList<>();

        if(openedCard != null){
            if(openedCard.tags != null) {
                for(Tags t : openedCard.tags){
                    initialTags.add(t);
                }
            }
            if(openedCard.subtasks != null){
                for(Subtask s : openedCard.subtasks){
                    Subtask subtask = new Subtask(s.title, s.checked, s.position);
                    subtask.id = s.id;
                    initialSubtasks.add(subtask);
                }
            }
            initialCardColors = openedCard.colorStyle;
        }
        warning.setVisible(false);
    }

    /**
     * Configures websockets for the Card Details scene
     *
     */
    public void websocketConfig() {

        // When a client modifies card details, this scene gets modified
        // so that clients that see this scene will see the details changing
        server.registerForMessages("/topic/cards/rename", Cards.class, c->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(openedCard != null && c.id == openedCard.id && sceneOpened)
                        setOpenedCard(c);
                }
            });
        });

        // When another client removes this card, this client will be
        // sent to the board scene
        server.registerForMessages("/topic/cards/remove", Cards.class, c->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(openedCard != null && c.id == openedCard.id && sceneOpened) {
                        mainCtrl.closeSecondaryStage();
                        mainCtrl.showWarningCardDeletion();
                    }
                }
            });
        });

        // when tags are updated, the card details scene needs to
        // receive this information
        server.registerForMessages("/topic/boards/update", Boards.class, b->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(board != null && board.id == b.id) {
                        board = b;
                    }
                }
            });
        });
    }

    /**
     * The redefinition of the card name on the board is achieved
     * through setting the display properties. This method sends the
     * information entered in cardDetails to the board to display the card name.
     * A warning is displayed if the input field is empty.
     */
    @FXML
    void save() {
        warning.setVisible(false);
        rename = false;

        if(cardTitleInput.getText().isBlank()) {
            warning.setVisible(true);
            return;
        }

        openedCard.title = cardTitleInput.getText();
        openedCard.description = description.getText();

        // if we have multiple created subtasks
        // a JSON error will be thrown because it will have to serialize
        // multiple objects with the same id (0)
        // this block of code ensures that the ids sent are distinct
        int index = 0;
        if(openedCard.subtasks != null) {
            for(Subtask subtask : openedCard.subtasks)
                if(subtask.id == 0)
                    subtask.id = --index;
        }

        subtaskName.setText("");

        // this new list is created because otherwise there would be errors
        // when serializing
        // because the list references the board that references tags, meaning
        // there would be conflicts in ids
        Lists blankList = new Lists(null, 0, null);
        blankList.id = openedCard.list.id;
        openedCard.list = blankList;

        server.renameCard(openedCard);
        sceneOpened = false;
        changesFromTags = false;
        changesFromSubtasks = false;
        changesInTitleOrDescription = false;
        mainCtrl.closeSecondaryStage();
    }

    /**
     * The user can close the card details without the modifications made
     * to be saved by pressing the 'Close' button. However, because this
     * can happen accidentally, a warning asking for confirmation of
     * closing the card without saving the modifications is displayed
     */
    @FXML
    public void closeCardDetails(){

        if(!openedCard.title.equals(cardTitleInput.getText()) ||
                !openedCard.description.equals(description.getText())) {
            changesInTitleOrDescription = true;
        }
        else{
            changesInTitleOrDescription = false;
        }

        if(openedCard.tags != null){
            if(!openedCard.tags.equals(initialTags)){
                changesFromTags = true;
            }
            else {
                changesFromTags = false;
            }
        }

        if(openedCard.subtasks != null){
            if(!openedCard.subtasks.equals(initialSubtasks)){
                changesFromSubtasks = true;
            }
            else {
                changesFromSubtasks = false;
            }
        }

        if(!openedCard.colorStyle.equals(initialCardColors)){
            changesInCustomization = true;
        }
        else{
            changesInCustomization = false;
        }

        System.out.println(changesInTitleOrDescription);
        System.out.println(changesFromSubtasks);
        System.out.println(changesFromTags);

        if(changesInTitleOrDescription || changesFromSubtasks
                || changesFromTags || changesInCustomization){
            mainCtrl.showConfirmCloseCard();
        }
        else {
            close();
        }
    }

    /**
     * Simply closes the cardDetails scene without any warning, as no modifications
     * has been made and need to be saved
     */
    public void close(){
        sceneOpened = false;
        mainCtrl.closeSecondaryStage();
        mainCtrl.showBoard(board);
    }

    /**
     * When the warning regarding the confirmation of closing the card without
     * saving the modifications appears, the user can choose to not close the card
     * by pressing on the 'No' button
     */
    @FXML
    public void noClose(){
        mainCtrl.closeThirdStage();
        mainCtrl.showCardDetail();
    }

    /**
     * The user can close the card details without the modifications made
     * to be saved by pressing the 'Yes' button on the warning that appears
     * once the user has pressed the 'close' button on the card details scene
     */
    @FXML
    public void closeWithoutSaving(){
        sceneOpened = false;
        mainCtrl.closeSecondaryStage();
        mainCtrl.closeThirdStage();
        mainCtrl.showBoard(board);
    }

    /**
     * Used to refresh the details of the opened card
     * after something related to it has changed
     *
     */
    public void refreshOpenedCard() {
        if(!openedCard.title.equals(cardTitleInput.getText()) ||
            !openedCard.description.equals(description.getText())) {
        }
        openedCard.title = cardTitleInput.getText();
        openedCard.description = description.getText();
        setOpenedCard(openedCard);
        String[] colors = this.colors.split(" ");
        String[] newColors = ((String)board.colorPreset.get(openedCard.colorStyle)).split(" ");
        cardTitleInput.getParent().setStyle("-fx-background-color: " + newColors[0] + ";");
        cardTitleInput.setStyle("-fx-text-fill: " + newColors[1] + ";");
        description.setStyle("-fx-text-fill: " + newColors[1] + ";");
    }

    /**
     * Sets openedCard to be the current card
     * Also displays information about the opened card
     *
     * @param card The new value of the field
     */
    public void setOpenedCard(Cards card) {
        sceneOpened = true;

        this.openedCard = card;
        List<Node> tagContainers = new ArrayList<>();
        for(Node child : tagList.getChildren())
            if(child.getProperties().get("tag") != null)
                tagContainers.add(child);
        tagList.getChildren().removeAll(tagContainers);


        tagsCardDetails = new TagsCardDetails(tagList, this);
        tagsCardDetails.renderTags(openedCard);

        cardTitleInput.setText(card.title);
        description.setText(card.description);

        HBox header = (HBox) taskList.getChildren().get(0);
        taskList.getChildren().clear();

        taskList.getChildren().add(header);
        for(int i=0; i<openedCard.subtasks.size(); i++)
            if(openedCard.subtasks.get(i) != null) {
                openedCard.subtasks.get(i).position = i;
                renderSubtask(openedCard.subtasks.get(i), i);
            }

        inputsOpen = 0;
    }

    /**
     * Method used for rendering a subtask in the subtask list
     *
     * @param subtask Object containing information about
     * @param position The position of the Subtask in the list of subtasks
     */

    public void renderSubtask(Subtask subtask, int position) {
        // styling for the subtask container
        HBox subtaskContainer = new HBox();
        subtaskContainer.setPrefWidth(214);
        subtaskContainer.setPrefHeight(32);
        subtaskContainer.setAlignment(Pos.CENTER);
        subtaskContainer.getProperties().put("subtask", subtask);

        // styling for the menu button
        menuButtonStyling(subtaskContainer);

        // styling for the text field and the checkbox
        CheckBox checkBox = new CheckBox();
        checkBox.setStyle("-fx-padding: 0 0 0 2; -fx-font-size: 13; -fx-font-family: 'Bell MT';");
        checkBox.setText(subtask.title);
        checkBox.setPrefWidth(174);
        checkBox.setPrefHeight(32);
        checkBox.setOnAction(this::checkboxClicked);
        subtaskContainer.getChildren().add(checkBox);
        checkBox.setSelected(subtask.checked);

        // styling for the up arrow button
        Button upArrow = new Button();
        upArrow.setText("\uD83D\uDD3C");
        upArrow.setPrefWidth(23);
        upArrow.setStyle("-fx-margin: 1 1 0 0; -fx-padding: 0 2 0 2;");
        upArrow.setId(Integer.toString(position));
        upArrow.setOnAction(this::swapSubtasks);
        subtaskContainer.getChildren().add(upArrow);
        HBox.setMargin(upArrow, new Insets(1, 1, 0, 0));

        // styling for the down arrow button
        Button downArrow = new Button();
        downArrow.setText("\uD83D\uDD3D");
        downArrow.setPrefWidth(23);
        downArrow.setStyle("-fx-margin: 1 4 0 0; -fx-padding: 0 2 0 2;");
        downArrow.setId(Integer.toString(position));
        downArrow.setOnAction(this::swapSubtasks);
        subtaskContainer.getChildren().add(downArrow);
        HBox.setMargin(downArrow, new Insets(1, 4, 0, 0));

        // adding the current subtask container to the container of subtasks
        taskList.getChildren().add(subtaskContainer);
    }

    /**
     * Styling for the menu button and its menu items
     * The menu button is that button on the left used for renaming/removing
     *
     * @param subtaskContainer Reference to the subtask container
     */
    public void menuButtonStyling(HBox subtaskContainer) {
        // styling for the menu button with a 'pen' on it
        MenuButton menuButton = new MenuButton();
        menuButton.setPrefWidth(20);
        menuButton.setPrefHeight(18);
        String menuButtonStyle = "-fx-padding: -5 -22 -5 -5; -fx-background-color:  #fff2cc; " +
                "-fx-border-color:  #f0cca8; -fx-background-radius:  4; -fx-border-radius: 4;";
        menuButton.setStyle(menuButtonStyle);
        menuButton.setPopupSide(Side.LEFT);
        menuButton.setText("\uD83D\uDD8A");

        // styling for the menu items of the 'pen' button
        MenuItem rename = new MenuItem();
        rename.setText("Rename");
        MenuItem delete = new MenuItem();
        delete.setText("Delete");
        delete.setOnAction(this::deleteSubtask);
        rename.setOnAction(this::renameSubtask);
        menuButton.getItems().addAll(rename, delete);
        subtaskContainer.getChildren().add(menuButton);
        HBox.setMargin(menuButton, new Insets(0, 0, 0, 5));
    }

    /**
     * Method that is called when 2 subtasks will be swapped
     *
     * @param actionEvent Object containing information about the action event
     */
    public void swapSubtasks(ActionEvent actionEvent) {
        Button arrow = (Button)actionEvent.getTarget();
        int position = Integer.parseInt(arrow.getId());
        List<Subtask> subtaskList = openedCard.subtasks;

        if(arrow.getText().equals("\uD83D\uDD3C")) {
            // up arrow
            if(position > 0) {
                swapSubtasks(subtaskList, position, position - 1);
            }
        } else {
            // down arrow
            if(position < subtaskList.size() - 1) {
                swapSubtasks(subtaskList, position, position + 1);
            }
        }

        refreshOpenedCard();
    }

    /**
     * Swaps subtasks at positions i and j in the subtasks list
     *
     * @param subtaskList The list containing the subtasks
     * @param i The position of the first subtask
     * @param j The position of the second subtask
     */
    private void swapSubtasks(List<Subtask> subtaskList, int i, int j) {
        Subtask tmp = subtaskList.get(i);
        subtaskList.set(i, subtaskList.get(j));
        subtaskList.set(j, tmp);
    }

    /**
     * Opens a text field for the user to input the new subtask
     */
    @FXML
    public void addSubtask(){
        inputsOpen++;

        if(inputsOpen == 1){
            taskList.getChildren().add(inputSubtask);
        }
        else {
            subtaskName.setStyle("-fx-background-color: #ffcccc; " +
                    "-fx-border-color: #b30000; -fx-background-radius: 4; " +
                    "-fx-border-radius: 4;");
            warningSubtask.setText("Please complete this action before \n attempting a new one!");
            warningSubtask.setVisible(true);
        }
    }

    /**
     * Closes the text field and creates the new Subtask, then adds it
     * to the subtasks list and to the database
     */
    @FXML
    public void createSubtask(){
        subtaskName.setStyle("");
        warningSubtask.setVisible(false);

        changesFromSubtasks = true;

        if(subtaskName.getText().equals("")){
            subtaskName.setStyle("-fx-background-color: #ffcccc; " +
                    "-fx-border-color: #b30000; -fx-background-radius: 4; " +
                    "-fx-border-radius: 4;");
            warningSubtask.setText("Name cannot be blank!");
            warningSubtask.setVisible(true);
        }
        else {
            int position;
            boolean checked = false;

            // when renaming a subtask, the actual subtask it deleted and a new one,
            // with the same details as it, but with the new name is created
            // therefore, the position and checked value of the subtask can be different from
            // the initial ones when a subtask is just created
            if(rename){
                openedCard.subtasks.remove(toRename);
                position = toRename.position;
                checked = toRename.checked;
            }
            else {
                position = taskList.getChildren().size() - 1;
            }
            Subtask newSubtask = new Subtask(subtaskName.getText(),
                    checked, position); // openedCard,
            subtaskName.setStyle("");
            warningSubtask.setVisible(false);
            inputsOpen--;
            subtaskName.setText("");

            if(rename){
                openedCard.subtasks.add(position, newSubtask);
                rename = false;
            }
            else {
                openedCard.subtasks.add(newSubtask);
            }
            refreshOpenedCard();
        }

    }

    /**
     * Deletes the subtask from the list of subtasks and from the database
     * @param event Object containing information about the action event
     */
    private void deleteSubtask(ActionEvent event){
        MenuItem menuItem = (MenuItem) event.getSource();
        ContextMenu popup = menuItem.getParentPopup();
        HBox toDelete = (HBox) popup.getOwnerNode().getParent();

        Subtask subtask = (Subtask) toDelete.getProperties().get("subtask");
        openedCard.subtasks.remove(subtask);
        refreshOpenedCard();
    }

    /**
     * Renames a subtask from the list,
     * A text field for inputting the new name is shown instead of the subtask
     * Renaming is done by deleting the actual subtask and creating
     * another one, with the same details, but with the different titles
     * @param event Object containing information about the action event
     */
    private void renameSubtask(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        ContextMenu popup = menuItem.getParentPopup();
        HBox currentSubtask = (HBox) popup.getOwnerNode().getParent();
        inputsOpen++;

        if(inputsOpen == 1){
            // will inform the createSubtask method that the new subtask is actually a renamed one
            rename = true;
            toRename = (Subtask) currentSubtask.getProperties().get("subtask");
            subtaskName.setText(toRename.title);

            int indexInVbox = taskList.getChildren().indexOf(currentSubtask);
            taskList.getChildren().set(indexInVbox, inputSubtask);

        }
        else {
            subtaskName.setStyle("-fx-background-color: #ffcccc; " +
                    "-fx-border-color: #b30000; -fx-background-radius: 4; " +
                    "-fx-border-radius: 4;");
            warningSubtask.setText("Please complete this action before \n attempting a new one!");
            warningSubtask.setVisible(true);
        }

    }

    /**
     * Changes the completion state of a subtask when
     * its corresponding checkbox is clicked
     *
     * @param event Object storing information about the action event
     */

    private void checkboxClicked(ActionEvent event) {
        CheckBox checkBox = (CheckBox) event.getSource();
        HBox subtaskContainer = (HBox) checkBox.getParent();

        Subtask subtask = (Subtask) subtaskContainer.getProperties().get("subtask");
        int subtaskIndex = openedCard.subtasks.indexOf(subtask);
        Subtask updatedSubtask = openedCard.subtasks.get(subtaskIndex);
        updatedSubtask.checked = checkBox.isSelected();

        refreshOpenedCard();
    }

    /**
     * Setter for the board property of the object
     *
     * @param board The new board object
     */
    public void setBoard(Boards board) {
        this.board = board;
    }
    /**
     * The trigger event of the button opens the personalization selection window for that card
     */
    public void customization() {
        mainCtrl.openCardCustomization();

    }

    /**
     * If a user deletes a card another user is currently viewing the details of,
     * the second user receives a warning regarding the deletion of the current
     * card and the fact that they have been redirected to the board
     * This method serves to close the window of the warning scene
     */
    @FXML
    public void closeWarningCardDeletion(){
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Pressing the '+' button will make the add tag scene appear
     *
     */
    public void showAddTagToCard() {
        mainCtrl.showAddTagToCard(openedCard, board, this);
    }

}
