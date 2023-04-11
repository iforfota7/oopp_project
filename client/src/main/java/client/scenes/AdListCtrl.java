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
        adListCtrlServices.setCtrl(this, boardCtrl, mainCtrl);

        if(adListCtrlServices.saveNewList(newListName.getText())) {
            mainCtrl.closeSecondaryStage();
        }
    }

    /**
     * Setter for the visibility of the warning
     *
     * @param isVisible True iff the warning should be visible
     */
    public void setWarningVisibility(Boolean isVisible) {
        warning.setVisible(isVisible);
    }

    /**
     * Setter for the text found in the text field
     *
     * @param text The new text in the text field
     */
    public void setNewListName(String text) {
        newListName.setText(text);
    }

    /**
     * Closes the window for creating a new list.
     */
    @FXML
    public void close() {
        warning.setVisible(false);
        newListName.clear();
        mainCtrl.closeSecondaryStage();
    }
}
