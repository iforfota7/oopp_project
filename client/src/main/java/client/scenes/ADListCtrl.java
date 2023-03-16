package client.scenes;

import client.utils.ServerUtils;
import commons.Lists;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import javax.inject.Inject;

/**
 *  Controller for Add List
 */
public class ADListCtrl {
    private final BoardCtrl boardCtrl;
    private final ServerUtils server;
    @Inject
    public ADListCtrl(BoardCtrl boardCtrl, ServerUtils server){
        this.boardCtrl = boardCtrl;
        this.server = server;
    }
    @FXML
    private TextField newListName;

    @FXML
    private TextField newListOrder;

    /**
     * the initialization and customization of the List display name is only achieved through creating a new window.
     */
    @FXML
    void saveNewList(ActionEvent event)
    {

        server.addList(new Lists(newListName.getText(), 1));
    }

}
