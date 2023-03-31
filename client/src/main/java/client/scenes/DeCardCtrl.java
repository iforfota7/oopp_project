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
     * Method to confirm the deletion of a card
     */
    @FXML
    void deleteCard() {
        boardCtrl.deleteCard();
    }

    /**
     * Method that cancels the deletion of a card
     */
    @FXML
    void undeleteCard() {
        boardCtrl.undeleteCard();
    }

}
