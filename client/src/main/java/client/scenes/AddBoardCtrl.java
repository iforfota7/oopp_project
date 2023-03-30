package client.scenes;

import client.utils.ServerUtils;
import commons.Boards;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.inject.Inject;

/**
 *  Controller for Add Board
 */
public class AddBoardCtrl {
    private final BoardOverviewCtrl boardOverviewCtrl;
    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private final BoardCtrl boardCtrl;

    @FXML
    private TextField boardName;

    @FXML
    private Label warning;

    /**
     * Constructor method for controller
     * @param boardOverviewCtrl board overview controller instance
     * @param mainCtrl main controller instance
     * @param server serverUtils instance
     * @param boardCtrl instance of BoardCtrl
     */
    @Inject
    public AddBoardCtrl(BoardOverviewCtrl boardOverviewCtrl, MainCtrl mainCtrl, ServerUtils server,
                        BoardCtrl boardCtrl){
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.boardCtrl = boardCtrl;
    }

    /**
     * the initialization and customization of the
     * Board display name is only achieved through creating a new window.
     */
    @FXML
    void saveNewBoard(){
        warning.setVisible(false);
        try {
            Boards b = server.addBoard(new Boards(boardName.getText(), null));
            boardName.setText("");
            server.addBoardToUser(b);
            mainCtrl.closeSecondaryStage();
            boardOverviewCtrl.refresh();
        }
        catch(Exception e){
            warning.setVisible(true);
        }
    }

}
