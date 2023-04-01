package client.scenes;

import commons.Cards;
import javafx.fxml.FXML;

import javax.inject.Inject;

/**
 * Delete List controller
 */
public class DeCardCtrl {

    private BoardCtrl boardCtrl;
    private Cards card;

    /**
     * Constructor for DeCardCtrl class
     * @param boardCtrl instance of BoardCtrl
     * @param card instance of a card
     */
    @Inject
    public DeCardCtrl(BoardCtrl boardCtrl, Cards card){
        this.boardCtrl = boardCtrl;
        this.card = card;
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
