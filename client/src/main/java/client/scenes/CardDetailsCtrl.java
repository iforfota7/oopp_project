package client.scenes;


import javafx.fxml.FXML;

import javafx.scene.control.TextField;

import javax.inject.Inject;


public class CardDetailsCtrl {

    @FXML
    private TextField cardTitleInput;


    private final BoardCtrl boardCtrl;

    @Inject
    public CardDetailsCtrl(BoardCtrl boardCtrl){
        this.boardCtrl = boardCtrl;
    }

    /**
     *The redefinition of the card name on the board is achieved through setting the display properties.
     * This method sends the information entered in cardDetail to the board to display the card name.
     */
    @FXML
    void save() {

        boardCtrl.RefreshCard(cardTitleInput.getText());
    }
}
