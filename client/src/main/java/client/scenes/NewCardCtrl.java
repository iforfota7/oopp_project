package client.scenes;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NewCardCtrl {
    @FXML
    private TextField cardTitleInput;


    private final BoardCtrl boardCtrl;

    @Inject
    public NewCardCtrl(BoardCtrl boardCtrl){
        this.boardCtrl = boardCtrl;
    }

    /**
     *The redefinition of the card name on the board is achieved through setting the display properties.
     * This method sends the information entered in cardDetail to the board to display the card name.
     */
    @FXML
    void save() {
       boardCtrl.addCardToList(cardTitleInput.getText());
    }
}
