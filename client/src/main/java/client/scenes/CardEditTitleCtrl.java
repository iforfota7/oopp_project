package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Cards;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CardEditTitleCtrl {
    @FXML
    private TextField cardTitleInput;
    @FXML
    private Text warning;

    private MainCtrl mainCtrl;
    private BoardCtrl boardCtrl;
    private ServerUtils server;
    private Cards card;

    /**
     * Constructor of CardEditTitleCtrl
     * @param mainCtrl an instance of MainCtrl
     * @param boardCtrl an instance of BoardCtrl
     * @param server an instance of ServerUtils
     */
    @Inject
    public CardEditTitleCtrl(MainCtrl mainCtrl, BoardCtrl boardCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.boardCtrl = boardCtrl;
        this.server = server;
    }

    /**
     * The redefinition of the card name on the board is achieved
     * through setting the display properties. This method sends the
     * information entered in cardDetails to the board to display the card name.
     * A warning is displayed if the input field is empty.
     */
    @FXML
    void save() {
        warning.setVisible(false);

        if(cardTitleInput.getText().isBlank()) {
            warning.setVisible(true);
            return;
        }

        card.title = cardTitleInput.getText();
        server.renameCard(card);
        boardCtrl.refresh();
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Sets the card object to the provided card
     * @param card the card to be edited
     */
    public void init(Cards card) {
        this.card = card;
    }
}
