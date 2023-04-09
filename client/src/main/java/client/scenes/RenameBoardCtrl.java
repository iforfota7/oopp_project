package client.scenes;

import client.utils.ServerUtils;
import commons.Boards;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class RenameBoardCtrl {
    private final BoardOverviewCtrl boardOverviewCtrl;
    private final MainCtrl mainCtrl;
    private final ServerUtils server;

    @FXML
    private TextField boardName;
    @FXML
    private Label warning;

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
        warning.setVisible(false);
        Boards board = boardOverviewCtrl.getCurrentBoard();
        String name = boardName.getText();
        if(name == null || name.equals("")) warning.setVisible(true);
        else {
            try {
                board.name = name;
                server.renameBoard(board);
                boardName.setText("");
                mainCtrl.closeSecondaryStage();
                boardOverviewCtrl.refresh();
                warning.setVisible(false);
            } catch (Exception e) {
                warning.setVisible(true);
            }
        }
    }

    @FXML
    void cancel(){
        warning.setVisible(false);
        boardName.setText("");
        mainCtrl.closeSecondaryStage();
    }

}
