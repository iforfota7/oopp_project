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

    @FXML
    void cancel(ActionEvent event) {
        mainCtrl.closeShowUsername();
    }

    @FXML
    void confirm(ActionEvent event) {
        mainCtrl.closeShowUsername();
        Main.setSceneToBoard();
    }

}
