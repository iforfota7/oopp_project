package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import commons.Boards;
import commons.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;
import java.util.ArrayList;

public class SelectServerCtrl {

    @FXML
    private AnchorPane container;

    @FXML
    private TextField inputServer;

    @FXML
    private TextField inputUsername;

    @FXML
    private Button connect;

    private final ServerUtils server;
    @Inject
    public SelectServerCtrl(ServerUtils server){
        this.server = server;
    }

    /**
     * Method to be executed when connect button is clicked
     * Gets url from text-field and sets it as server url in ServerUtils
     */
    public void connect() {
        String address = inputServer.getText();
        String username = inputUsername.getText();

        //if empty do nothing
        if(address == null || address.equals("")){
            return;
        }

        // transforms to complete url
        // if begins with colon assumed to be localhost address with specified port
        if(address.startsWith("http://")) ServerUtils.setServer(address);
        else if(address.startsWith(":")) ServerUtils.setServer("http://localhost" + address);
        else ServerUtils.setServer("http://" + address);

        if(!ServerUtils.checkServer()){
            inputServer.setText("invalid");
        }

        ServerUtils.setUsername(username);
        User user = new User(username, new ArrayList<>(), false);
        server.addUser(user);

        Main.setSceneToBoard();

    }
}
