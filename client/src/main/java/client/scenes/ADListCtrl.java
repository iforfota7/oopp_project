package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;

import javax.inject.Inject;

/**
 *  Controller for Add List
 */
public class ADListCtrl {
    private final BoardCtrl boardCtrl;

    @Inject
    public ADListCtrl(BoardCtrl boardCtrl){
        this.boardCtrl = boardCtrl;
    }
    @FXML
    private TextField newListName;

    @FXML
    private TextField newListOrder;

    /**
     * the initialization and customization of the List display name is only achieved through creating a new window.
     */
    @FXML
    void saveNewList(ActionEvent event) {
        boardCtrl.showNewList(newListName.getText());
    }

}
