package client.scenes;

import org.springframework.stereotype.Service;

@Service
public class AdListCtrlServices {

    private AdListCtrl adListCtrl;
    private BoardCtrl boardCtrl;
    private MainCtrl mainCtrl;

    /**
     * Checks if a new list is added to the board
     * Adds the list if listTitle is not blank and closes the scene
     * Otherwise, a warning will be shown
     *
     * @param listTitle The title of the new list
     * @return True iff a new list is rendered
     */
    public boolean saveNewList(String listTitle) {
        adListCtrl.setWarningVisibility(false);
        if(listTitle.isBlank()){
            adListCtrl.setWarningVisibility(true);
            return false;
        }

        int positionInsideBoard = boardCtrl.getFirstRow().getChildren().size();
        boardCtrl.addListToBoard(listTitle, positionInsideBoard);
        adListCtrl.setNewListName("");
        mainCtrl.closeSecondaryStage();
        return true;
    }

    /**
     * Used to set all controller needed for this service
     *
     * @param adListCtrl Reference to adListCtrl
     * @param boardCtrl Reference to boardCtrl
     * @param mainCtrl Reference to mainCtrl
     */
    public void setCtrl(AdListCtrl adListCtrl, BoardCtrl boardCtrl, MainCtrl mainCtrl) {
        this.adListCtrl = adListCtrl;
        this.boardCtrl = boardCtrl;
        this.mainCtrl = mainCtrl;
    }
}
