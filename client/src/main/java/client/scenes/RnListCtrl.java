package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

/**
 * Change list name controller
 */
public class RnListCtrl {
    private BoardCtrl boardCtrl;

    @Inject
    public RnListCtrl(BoardCtrl boardCtrl){
        this.boardCtrl = boardCtrl;
    }
    @FXML
    private TextField newName;

    @FXML
    void saveNewName(ActionEvent event) {
        var name = newName.getText();
        boardCtrl.rnList(name);
    }

}
