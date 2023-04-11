package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.inject.Inject;

/**
 * Change list name controller
 */
public class RnListCtrl {
    private final BoardCtrl boardCtrl;
    private final MainCtrl mainCtrl;

    /**
     * Constructor method for RnListCtrl
     * @param boardCtrl instance of BoardCtrl
     * @param mainCtrl instance of MainCtrl
     */
    @Inject
    public RnListCtrl(BoardCtrl boardCtrl, MainCtrl mainCtrl){
        this.boardCtrl = boardCtrl;
        this.mainCtrl = mainCtrl;
    }
    @FXML
    private TextField newName;
    @FXML
    private Text warning;

    /**
     * This method is used for changing the title of a list.
     * A warning is displayed if the input field is empty.
     */
    @FXML
    void saveNewName() {

        warning.setVisible(false);

        if(newName.getText().isBlank()) {
            warning.setVisible(true);
            return;
        }

        boardCtrl.rnList(newName.getText());
        newName.clear();
    }

    /**
     * Closes the window for editing a list's title.
     */
    @FXML
    void close() {
        warning.setVisible(false);
        newName.clear();
        mainCtrl.closeSecondaryStage();
    }
}
