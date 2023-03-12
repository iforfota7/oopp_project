package client.scenes;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;

import javax.inject.Inject;

/**
 *  Controller for Add List
 */
public class ADListCtrl {
    private final BoardCtrl boardCtrl;
    public TextField newListOrder;

    @Inject
    public ADListCtrl(BoardCtrl boardCtrl){
        this.boardCtrl = boardCtrl;
    }
    @FXML
    private TextField newListName;

    /**
     * the initialization and customization of the List display name is only achieved through creating a new window.
     */
    @FXML
    void saveNewList() {
        boardCtrl.addNewList(newListName.getText());
    }

}
