package client.scenes;

import commons.Cards;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javax.inject.Inject;

/**
 * Delete List controller
 */
public class DeCardCtrl {

    private BoardCtrl boardCtrl;
    private Cards card;

    @Inject
    public DeCardCtrl(BoardCtrl boardCtrl, Cards card){
        this.boardCtrl = boardCtrl;
        this.card = card;
    }
    @FXML
    void deleteCard(ActionEvent event) {
        boardCtrl.deleteCard();
    }

    @FXML
    void undeleteCard(ActionEvent event) {
        boardCtrl.undeleteCard();
    }

}
