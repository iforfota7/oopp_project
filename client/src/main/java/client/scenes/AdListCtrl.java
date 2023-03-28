package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.inject.Inject;

/**
 *  Controller for Add List
 */
public class AdListCtrl {
    private final BoardCtrl boardCtrl;
    private final ServerUtils server;
    private final MainCtrl   mainCtrl;
    @Inject
    public AdListCtrl(BoardCtrl boardCtrl, ServerUtils server, MainCtrl mainCtrl){
        this.boardCtrl = boardCtrl;
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    @FXML
    private TextField newListName;
    @FXML
    private Text warning;

    /**
     * The initialization and customization of the
     * List display name is only achieved through creating a new window.
     * A warning is displayed if the input field is empty.
     */
    @FXML
    void saveNewList() {
        warning.setVisible(false);

        if(newListName.getText().isBlank()) {

            warning.setVisible(true);
            return;
        }

        int positionInsideBoard = boardCtrl.getFirstRow().getChildren().size();

        boardCtrl.addListToBoard(newListName.getText(), positionInsideBoard);
        newListName.setText("");
        mainCtrl.closeADList();
    }

}
