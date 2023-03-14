package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;

import javax.inject.Inject;

/**
 *  Controller for Add List
 */
public class ADListCtrl {
<<<<<<< HEAD
    private final BoardCtrl boardCtrl;
=======
    private BoardCtrl boardCtrl;
>>>>>>> 88cfe6b716b0809e7b79882121b68cc6a52e972e

    @Inject
    public ADListCtrl(BoardCtrl boardCtrl){
        this.boardCtrl = boardCtrl;
    }
    @FXML
    private TextField newListName;

    @FXML
    private TextField newListOrder;

    @FXML
    void saveNewList(ActionEvent event) {
        boardCtrl.addNewList(newListName.getText());
    }

}
