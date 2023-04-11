package client.scenes;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class NewCardCtrl {
    @FXML
    private TextField cardTitleInput;
    @FXML
    private Text warning;


    private final BoardCtrl boardCtrl;
    private final MainCtrl mainCtrl;

    /**
     * Constructor method for NewCardCtrl
     * @param boardCtrl instance of BoardCtrl
     * @param mainCtrl instance of MainCtrl
     */
    @Inject
    public NewCardCtrl(BoardCtrl boardCtrl, MainCtrl mainCtrl){
        this.boardCtrl = boardCtrl;
        this.mainCtrl = mainCtrl;
    }

    /**
     * The redefinition of the card name on the board
     * is achieved through setting the display properties.
     * This method sends the information entered in cardDetail
     * to the board to display the card name.
     * A warning is displayed if the input field is empty.
     *
     */


    @FXML
    void save() {
        warning.setVisible(false);

        if(cardTitleInput.getText().isBlank()) {

            warning.setVisible(true);
            return;
        }

        boardCtrl.addCardToList(cardTitleInput.getText());
        cardTitleInput.clear();
    }

    /**
     * Closes the window for adding a new card.
     */
    @FXML
    void close() {
        warning.setVisible(false);
        cardTitleInput.clear();
        mainCtrl.closeSecondaryStage();
    }
}
