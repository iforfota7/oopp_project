package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class RenameBoardCtrl {
    private final BoardOverviewCtrl boardOverviewCtrl;
    private final MainCtrl mainCtrl;
    private final ServerUtils server;

    @FXML
    private TextField boardName;

    /**
     * Constructor method for RenameBoardCtrl
     * @param boardOverviewCtrl instance of BoardOverviewCtrl
     * @param mainCtrl instance of MainCtrl
     * @param server instance of ServerUtils
     */
    @Inject
    public RenameBoardCtrl(BoardOverviewCtrl boardOverviewCtrl,
                           MainCtrl mainCtrl, ServerUtils server){
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Method that saves new board
     */
    @FXML
    void saveNewBoard(){

      //  server.renameBoard();
        boardName.setText("");
        mainCtrl.closeSecondaryStage();

    }
}
