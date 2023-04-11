package client.scenes;

import client.utils.ServerUtils;
import commons.Boards;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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
    private List<String> serverURLS;
    private boolean sceneOpened;

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
        serverURLS = new ArrayList<>();
    }

    /**
     * This method implements setting the corresponding
     * color preset to the current card when the user clicks on any button
     * @param board board object which contains the opened card
     */
    void checkColorPreset(Boards board) {
        if(!serverURLS.contains(server.getServer())) {
            serverURLS.add(server.getServer());
            websocketConfig();
        }

        String[] presets = board.colorPreset.keySet().toArray(new String[0]);
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
                cardDetailsCtrl.refreshOpenedCard();
                close();
            });
            colorPresetHolder.getChildren().add(btn);
        }
    }

    /**
     * Method for configuring websockets in the
     * Card Customization scene
     *
     */
    public void websocketConfig() {
        server.registerForMessages("/topic/boards/setCss", Boards.class, b->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(sceneOpened)
                        checkColorPreset(b);
                }
            });
        });
    }

    /**
     * Closes the CardCustomization window
     */
    @FXML
    public void close(){
        sceneOpened = false;
        mainCtrl.closeThirdStage();
    }

    /**
     * Setter for the sceneOpened property
     *
     * @param sceneOpened The new value of sceneOpened
     */
    public void setSceneOpened(boolean sceneOpened) {
        this.sceneOpened = sceneOpened;
    }

}
