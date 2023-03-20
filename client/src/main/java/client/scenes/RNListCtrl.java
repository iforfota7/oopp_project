package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

/**
 * Change list name controller
 */
public class RNListCtrl {
    private BoardCtrl boardCtrl;

    @Inject
    public RNListCtrl(BoardCtrl boardCtrl){
        this.boardCtrl = boardCtrl;
    }
    @FXML
    private TextField newName;

    @FXML
    void saveNewName() {
        var Name = newName.getText();
        boardCtrl.saveNewListName(Name);
    }

}
