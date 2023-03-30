package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Cards;
import commons.Subtask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


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
     *
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
                    if(c.id == openedCard.id)
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
        mainCtrl.closeCardDetails();
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
        for(Subtask subtask : card.subtasks)
            renderSubtask(subtask);


    }

    /**
     * Method used for rendering a subtask in the subtask list
     *
     * @param subtask Object containing information about
     */
    public void renderSubtask(Subtask subtask) {
        HBox subtaskContainer = new HBox();
        subtaskContainer.setPrefWidth(214);
        subtaskContainer.setPrefHeight(32);
        subtaskContainer.setAlignment(Pos.CENTER);

        MenuButton menuButton = new MenuButton();
        menuButton.setPrefWidth(25);
        menuButton.setPrefHeight(18);
        String menuButtonStyle = "-fx-padding: -5 -22 -5 -5; -fx-background-color:  #fff2cc; " +
                "-fx-border-color:  #f0cca8; -fx-background-radius:  4; -fx-border-radius: 4;";
        menuButton.setStyle(menuButtonStyle);
        menuButton.setPopupSide(Side.LEFT);
        menuButton.setText("\uD83D\uDD8A");

        MenuItem rename = new MenuItem();
        rename.setText("Rename");
        MenuItem delete = new MenuItem();
        delete.setText("Delete");
        menuButton.getItems().addAll(rename, delete);
        subtaskContainer.getChildren().add(menuButton);
        HBox.setMargin(menuButton, new Insets(0, 0, 0, 5));

        CheckBox checkBox = new CheckBox();
        checkBox.setStyle("-fx-padding: 0 0 0 2; -fx-font-size: 13; -fx-font-family: 'Bell MT';");
        checkBox.setText(subtask.title);
        checkBox.setPrefWidth(174);
        checkBox.setPrefHeight(32);
        subtaskContainer.getChildren().add(checkBox);
        checkBox.setSelected(subtask.checked);

        Button upArrow = new Button();
        upArrow.setText("\uD83D\uDD3C");
        upArrow.setPrefWidth(23);
        upArrow.setStyle("-fx-margin: 1 1 0 0; -fx-padding: 0 2 0 2;");

        subtaskContainer.getChildren().add(upArrow);
        HBox.setMargin(upArrow, new Insets(1, 1, 0, 0));

        Button downArrow = new Button();
        downArrow.setText("\uD83D\uDD3D");
        downArrow.setPrefWidth(23);
        downArrow.setStyle("-fx-margin: 1 4 0 0; -fx-padding: 0 2 0 2;");
        subtaskContainer.getChildren().add(downArrow);
        HBox.setMargin(downArrow, new Insets(1, 4, 0, 0));

        taskList.getChildren().add(subtaskContainer);
    }
}
