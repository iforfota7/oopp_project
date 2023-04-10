package client.scenes;

import client.utils.ServerUtils;
import commons.Lists;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

/**
 * A window used to switch the current card color preset
 */
public class CardCustomizationCtrl {

    @FXML
    private VBox colorPresetHolder;
    @FXML
    private final BoardCtrl boardCtrl;
    @FXML
    private final MainCtrl mainCtrl;
    @FXML
    private final ServerUtils server;
    @FXML
    private final CardDetailsCtrl cardDetailsCtrl;
    private boolean shortcutActivated;

    /**
     * Auxiliary call to mainCtrl Inject function
     * @param boardCtrl                 instance of BoardCtrl
     * @param mainCtrl                  The master controller, which will later be replaced
     *                                  by a class of window controllers
     * @param server                    Used for connection to backend and websockets to function
     * @param cardDetailsCtrl           Used to pass the information of
     *                                  the current card to the scene
     */
    @Inject
    public CardCustomizationCtrl(BoardCtrl boardCtrl, MainCtrl mainCtrl,
                                 ServerUtils server, CardDetailsCtrl cardDetailsCtrl) {
        this.boardCtrl = boardCtrl;
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.cardDetailsCtrl = cardDetailsCtrl;
        this.shortcutActivated = false;
    }

    /**
     * This method implements setting the corresponding
     * color preset to the current card when the user clicks on any button
     */
    void checkColorPreset() {
        String[] presets = boardCtrl.getCurrentBoard().colorPreset.keySet().toArray(new String[0]);
        colorPresetHolder.getChildren().clear();
        for (String preset : presets) {
            Button btn = new Button(preset);
            btn.setPrefWidth(160);
            btn.setPrefHeight(20);
            String[] colors = ((String) cardDetailsCtrl.board.colorPreset.get(preset)).split(" ");
            btn.setStyle("-fx-background-color: " + colors[0] + "; " +
                    "-fx-text-fill: " + colors[1] + ";");
            btn.setOnAction(event -> {
                cardDetailsCtrl.openedCard.colorStyle = ((Button) event.getSource()).getText();
                if(shortcutActivated) {
                    Lists blankList = new Lists(null, 0, null);
                    blankList.id = cardDetailsCtrl.openedCard.list.id;
                    cardDetailsCtrl.openedCard.list = blankList;

                    server.renameCard(cardDetailsCtrl.openedCard);
                }
                else
                    cardDetailsCtrl.refreshOpenedCard();

                shortcutActivated = false;
                close();
            });
            colorPresetHolder.getChildren().add(btn);
        }
    }

    /**
     * Closes the CardCustomization window
     */
    @FXML
    public void close(){
        mainCtrl.closeThirdStage();
    }

    /**
     * Setter for the shortcut activate attribute
     * @param shortcutActivated The new value of the attribute
     */
    public void setShortcutActivated(boolean shortcutActivated) {
        this.shortcutActivated = shortcutActivated;
    }
}
