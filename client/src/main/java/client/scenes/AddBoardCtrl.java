package client.scenes;

import client.utils.ServerUtils;
import commons.Boards;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

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
     * Save a newly created board in server, close the scene and refresh board overview scene
     */
    @FXML
    void saveNewBoard(){
        server.addBoard(new Boards(boardName.getText(), null));
        boardName.setText("");

        mainCtrl.closeAddBoard();
        boardOverviewCtrl.refresh();
    }

}
