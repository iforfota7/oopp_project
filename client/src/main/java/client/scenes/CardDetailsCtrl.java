package client.scenes;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CardDetailsCtrl {
    @FXML
    private TextField cardTitleInput;
    @FXML
    private Text warning;
    @FXML
    private TextArea description;
    @FXML
    private VBox taskList;
    @FXML
    private GridPane tagList;

    private final BoardCtrl boardCtrl;

    @Inject
    public CardDetailsCtrl(BoardCtrl boardCtrl){
        this.boardCtrl = boardCtrl;
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

        boardCtrl.refreshCard(cardTitleInput.getText());
    }
}
