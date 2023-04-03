package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javax.inject.Inject;

/**
 * Delete List controller
 */
public class DeListCtrl {

    private BoardCtrl boardCtrl;

    @Inject
    public DeListCtrl(BoardCtrl boardCtrl){
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
