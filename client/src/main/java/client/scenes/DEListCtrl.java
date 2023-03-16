package client.scenes;

import javafx.fxml.FXML;

import javax.inject.Inject;

/**
 * Delete List controller
 */
public class DEListCtrl {
    private final MainCtrl mainCtrl;
    private final BoardCtrl boardCtrl;

    /**
     * A delete function with double confirmation ensures
     * that users will not accidentally delete a list and lose a large amount of information.
     *
     * @param mainCtrl link to mainCtrl
     * @param boardCtrl link to boardCtrl
     */
    @Inject
    public DEListCtrl(MainCtrl mainCtrl, BoardCtrl boardCtrl){
        this.mainCtrl = mainCtrl;
        this.boardCtrl = boardCtrl;
    }

    /**
     * Confirm deletion of list.
     */
    @FXML
     void confirmDeleteList() {
        boardCtrl.doubleConfirmDeleteList();
    }

    /**
     * Cancel deletion. This will not execute the delete command and will simply close the window, saving the list.
     */
    @FXML
    void cancelDeleteList() {
        boardCtrl.cancelDeleteList();
    }

}
