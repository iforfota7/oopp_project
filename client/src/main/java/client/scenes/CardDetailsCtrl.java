package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Cards;
import commons.Subtask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private Cards openedCard;

    /**
     * Initializes the card details controller object
     * @param server Used for sending requests to the server
     * @param mainCtrl Used for navigating through different scenes
     */
    @Inject
    public CardDetailsCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
        websocketConfig();
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
                    if(openedCard != null && c.id == openedCard.id)
                        setOpenedCard(c);
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

        if(cardTitleInput.getText().isBlank()) {
            warning.setVisible(true);
            return;
        }

        openedCard.title = cardTitleInput.getText();
        openedCard.description = description.getText();
        server.renameCard(openedCard);
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Sets openedCard to be the current card
     * Also displays information about the opened card
     *
     * @param card The new value of the field
     */
    public void setOpenedCard(Cards card) {
        this.openedCard = card;

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

        // styling for the menu button
        menuButtonStyling(subtaskContainer);

        // styling for the text field and the checkbox
        CheckBox checkBox = new CheckBox();
        checkBox.setStyle("-fx-padding: 0 0 0 2; -fx-font-size: 13; -fx-font-family: 'Bell MT';");
        checkBox.setText(subtask.title);
        checkBox.setPrefWidth(174);
        checkBox.setPrefHeight(32);
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
        menuButton.setPrefWidth(25);
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

        setOpenedCard(openedCard);
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
}
