package client.scenes;

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

    /**
     * Constructor method for ConfirmUsernameCtrl
     * @param mainCtrl instance of MainCtrl
     */
    @Inject
    public ConfirmUsernameCtrl(MainCtrl mainCtrl){
        this.mainCtrl = mainCtrl;
    }

    /**
     * Makes call to close confirm username scene
     */
    @FXML
    void cancel() {
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Closes confirm username scene and shows the board
     */
    @FXML
    void confirm() {
        mainCtrl.closeSecondaryStage();
        mainCtrl.showBoardOverview();
    }

}
