package client.scenes;

import javafx.fxml.FXML;

import javax.inject.Inject;

/**
 * Delete List controller
 */
public class DeCardCtrl {

    private BoardCtrl boardCtrl;

    /**
     * Constructor method for DeCardCtrl
     * @param boardCtrl instance of BoardCtrl
     */
    @Inject
    public DeCardCtrl(BoardCtrl boardCtrl){
        this.boardCtrl = boardCtrl;
    }

    /**
     * Calls the method to definitively deleting a card
     */
    @FXML
    void deleteCard() {
        boardCtrl.deleteCard();
    }

    /**
     * Calls the method to cancel the deletion of a card
     */
    @FXML
    void undeleteCard() {
        boardCtrl.undeleteCard();
    }

}