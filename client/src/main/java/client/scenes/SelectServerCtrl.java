package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import commons.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.util.ArrayList;

public class SelectServerCtrl {

    @FXML
    private AnchorPane container;

    @FXML
    private TextField inputServer;

    @FXML
    private Text serverWarning;

    @FXML
    private TextField inputUsername;

    @FXML
    private Text usernameWarning;

    @FXML
    private Button connect;

    private final ServerUtils server;

    private final MainCtrl mainCtrl;
    @Inject
    public SelectServerCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Method to be executed when connect button is clicked
     * Gets url from text-field and sets it as server url in ServerUtils
     * Gets username from text-field and sets it as username in ServerUtils
     */
    public void connect() {
        String address = inputServer.getText();
        String username = inputUsername.getText();
        boolean exists = false;

        //if address is empty do nothing
        if(address == null || address.equals("")){
            return;
        }

        // transforms to complete url
        // if begins with colon assumed to be localhost address with specified port
        if(address.startsWith("http://")) ServerUtils.setServer(address);
        else if(address.startsWith(":")) ServerUtils.setServer("http://localhost" + address);
        else ServerUtils.setServer("http://" + address);

        // if you can connect to the specified server address
        if(ServerUtils.checkServer()){
            serverWarning.setVisible(false);

            // set the username in the frontend
            ServerUtils.setUsername(username);

            // create user from information
            User user = new User(username, new ArrayList<>(), false);

            exists = server.existsUser(user);

            // if the user does not already exist, add them to the database
            // otherwise set warning to visible
            if(!exists) server.addUser(user);
            else usernameWarning.setVisible(true);
        }
        else serverWarning.setVisible(true);

        // if server exists
        if(!serverWarning.isVisible()){
            // if user does not exist, continue
            // otherwise show confirmation scene
            if(!exists) Main.setSceneToBoard();
            else{
                mainCtrl.showConfirmUsername();
            }
        }

    }
}
