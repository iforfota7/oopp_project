package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.inject.Inject;

/**
 * Controller for UserDetails
 */
public class UserDetailsCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;

    @FXML
    private Label username;
    @FXML
    private Label serverAddress;
    @FXML
    private Label isAdmin;

    @Inject
    public UserDetailsCtrl(MainCtrl mainCtrl, ServerUtils server){
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Method to close the secondary stage which shows the user's details
     */
    @FXML
    void closeUserDetails(){
        mainCtrl.closeUserDetails();
    }

}
