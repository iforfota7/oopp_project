package client.scenes;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.inject.Inject;

/**
 *  Controller for Add List
 */
public class AdListCtrl {

    private BoardCtrl boardCtrl;
    private MainCtrl   mainCtrl;
    private AdListCtrlServices adListCtrlServices;

    /**
     * Constructor for the AdListCtrl class
     * @param boardCtrl an instance of BoardCtrl
     * @param mainCtrl an instance of MainCtrl
     * @param adListCtrlServices the AdListCtrl class's services
     */
    @Inject
    public AdListCtrl(BoardCtrl boardCtrl, MainCtrl mainCtrl,
                      AdListCtrlServices adListCtrlServices){

        this.boardCtrl = boardCtrl;
        this.mainCtrl = mainCtrl;
        this.adListCtrlServices = adListCtrlServices;
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
    public void saveNewList() {

        warning.setVisible(false);

        if(newListName.getText().isBlank()) {

            warning.setVisible(true);
            return;
        }

        int positionInsideBoard = getPositionOfListInsideBoard();
        addListToBoard(newListName.getText(), positionInsideBoard);

        newListName.setText("");
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Calls the AdListCtrl service's method for adding a list
     * to a board.
     * @param newListName the name of the list
     * @param positionInsideBoard the position of the list inside the board
     * @return true if adding the list was successful
     */
    public boolean addListToBoard(String newListName, int positionInsideBoard) {

        adListCtrlServices.addListToBoard(boardCtrl, newListName, positionInsideBoard);
        return true;
    }

    /**
     * Calls the AdListCtrl service's method for getting a list's position
     * inside the board it's being added to.
     * @return the list's position inside the board
     */
    public int getPositionOfListInsideBoard() {

        return adListCtrlServices
                .getPositionOfListInsideBoard(boardCtrl);
    }
}
