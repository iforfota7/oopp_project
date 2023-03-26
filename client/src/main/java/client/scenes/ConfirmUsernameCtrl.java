package client.scenes;

import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.inject.Inject;

/**
 * Delete List controller
 */
public class ConfirmUsernameCtrl {

    @FXML
    Button cancel;

    @FXML
    Button confirm;

    private MainCtrl mainCtrl;

    @Inject
    public ConfirmUsernameCtrl(MainCtrl mainCtrl){
        this.mainCtrl = mainCtrl;
    }

    /**
     * Makes call to close confirm username scene
     * @param event triggers the method
     */
    @FXML
    void cancel(ActionEvent event) {
        mainCtrl.closeConfirmUsername();
    }

    /**
     * Closes confirm username scene and shows the board
     * @param event triggers the method
     */
    @FXML
    void confirm(ActionEvent event) {
        mainCtrl.closeConfirmUsername();
        mainCtrl.showBoardOverview();
    }

}
