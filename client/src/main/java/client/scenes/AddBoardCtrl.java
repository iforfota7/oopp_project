package client.scenes;

import client.utils.ServerUtils;
import commons.Boards;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

/**
 *  Controller for Add Board
 */
public class AddBoardCtrl {
    private final BoardOverviewCtrl boardOverviewCtrl;
    private final MainCtrl mainCtrl;
    private final ServerUtils server;

    @FXML
    private TextField boardName;

    /**
     * Constructor method for controller
     * @param boardOverviewCtrl board overview controller instance
     * @param mainCtrl main controller instance
     * @param server serverUtils instance
     */
    @Inject
    public AddBoardCtrl(BoardOverviewCtrl boardOverviewCtrl, MainCtrl mainCtrl, ServerUtils server){
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * the initialization and customization of the
     * Board display name is only achieved through creating a new window.
     */
    @FXML
    void saveNewBoard(){
        server.addBoard(new Boards(boardName.getText(), null, null));
        boardName.setText("");

        mainCtrl.closeSecondaryStage();
        boardOverviewCtrl.refresh();
    }

}
