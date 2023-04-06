package client.scenes;

import org.springframework.stereotype.Service;

@Service
public class AdListCtrlServices {

    private AdListCtrl adListCtrl;
    private BoardCtrl boardCtrl;

    public boolean saveNewList(String listTitle) {
        adListCtrl.setWarningVisibility(false);
        if(listTitle.isBlank()){
            adListCtrl.setWarningVisibility(true);
            return false;
        }

        int positionInsideBoard = boardCtrl.getFirstRow().getChildren().size();
        boardCtrl.addListToBoard(listTitle, positionInsideBoard);

        adListCtrl.setNewListName("");
        return true;
    }

    public void setCtrl(AdListCtrl adListCtrl, BoardCtrl boardCtrl) {
        this.adListCtrl = adListCtrl;
        this.boardCtrl = boardCtrl;
    }
}
