package client.scenes;

import javafx.fxml.FXML;

import javax.inject.Inject;

/**
 * Delete List controller
 */
public class DEListCtrl {

    private final BoardCtrl boardCtrl;

    /**
     * Implement the prompt window when deleting a list,
     * allowing the user to choose between deleting or canceling, and complete the corresponding functionality.
     * @param boardCtrl boardCtrl
     */
    @Inject
    public DEListCtrl(BoardCtrl boardCtrl){
        this.boardCtrl = boardCtrl;
    }
    @FXML
    void deleteList() {
        boardCtrl.deleteL();
    }

    @FXML
    void undeleteList() {
        boardCtrl.undeleteL();
    }

}
