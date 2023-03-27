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

    @Inject
    public AddBoardCtrl(BoardOverviewCtrl boardOverviewCtrl, MainCtrl mainCtrl, ServerUtils server){
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @FXML
    void saveNewBoard(ActionEvent event){
        System.out.println(boardName.getText());
        //server.addBoard(new Boards(boardName.getText(), null));
        boardName.setText("");

        mainCtrl.closeAddBoard();
        boardOverviewCtrl.addNewBoard(new Boards(boardName.getText(), null));
    }

}
