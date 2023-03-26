package client.scenes;

import client.utils.ServerUtils;
import commons.Boards;
import commons.Lists;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.util.ArrayList;

/**
 *  Controller for Add List
 */
public class AdListCtrl {
    private final BoardCtrl boardCtrl;
    private final ServerUtils server;
    private final MainCtrl   mainCtrl;
    @Inject
    public AdListCtrl(BoardCtrl boardCtrl, ServerUtils server, MainCtrl mainCtrl){
        this.boardCtrl = boardCtrl;
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
    @FXML
    private TextField newListName;

    @FXML
    private TextField newListOrder;

    /**
     * the initialization and customization of the
     * List display name is only achieved through creating a new window.
     */
    @FXML
    void saveNewList(ActionEvent event){
        int positionInsideBoard = boardCtrl.getFirstRow().getChildren().size();

        boardCtrl.addListToBoard(newListName.getText(), positionInsideBoard);
        newListName.setText("");

        mainCtrl.closeADList();
    }

}
