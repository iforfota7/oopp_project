package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.inject.Inject;

/**
 * Delete List controller
 */
public class DEListCtrl {

    private BoardCtrl boardCtrl;

    @Inject
    public DEListCtrl(BoardCtrl boardCtrl){
        this.boardCtrl = boardCtrl;
    }
    @FXML
    void deleteList(ActionEvent event) {
        boardCtrl.deleteL();
    }

    @FXML
    void undeleteList(ActionEvent event) {
        boardCtrl.undeleteL();
    }

}
