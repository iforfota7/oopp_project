package client.scenes;

import client.utils.ServerUtils;
import commons.Boards;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class RenameBoardCtrl {
    private final BoardOverviewCtrl boardOverviewCtrl;
    private final MainCtrl mainCtrl;
    private final ServerUtils server;

    @FXML
    private TextField boardName;

    @Inject
    public RenameBoardCtrl(BoardOverviewCtrl boardOverviewCtrl, MainCtrl mainCtrl, ServerUtils server){
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @FXML
    void saveNewBoard(ActionEvent event){

      //  server.renameBoard();
        boardName.setText("");
        mainCtrl.closeAddBoard();

    }
}
