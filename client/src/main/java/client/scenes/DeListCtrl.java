package client.scenes;

import javafx.fxml.FXML;

import javax.inject.Inject;

/**
 * Delete List controller
 */
public class DeListCtrl {

    private BoardCtrl boardCtrl;

    /**
     * Constructor method for DeListCtrl
     * @param boardCtrl instance of BoardCtrl
     */
    @Inject
    public DeListCtrl(BoardCtrl boardCtrl){
        this.boardCtrl = boardCtrl;
    }

    /**
     * Calls method to delete list
     */
    @FXML
    void deleteList() {
        boardCtrl.deleteL();
    }

    /**
     * Calls method to cancel deletion of list
     */
    @FXML
    void undeleteList() {
        boardCtrl.undeleteL();
    }

}
