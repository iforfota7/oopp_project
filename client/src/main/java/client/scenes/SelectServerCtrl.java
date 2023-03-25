package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.apache.catalina.Server;

import javax.inject.Inject;

public class SelectServerCtrl {

    @FXML
    private AnchorPane container;

    @FXML
    private TextField inputServer;

    @FXML
    private TextField inputUsername;

    @FXML
    private Button connect;

    private final BoardCtrl boardCtrl;
    private final ServerUtils server;
    private final MainCtrl   mainCtrl;
    @Inject
    public SelectServerCtrl(BoardCtrl boardCtrl, ServerUtils server, MainCtrl mainCtrl){
        this.boardCtrl = boardCtrl;
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Method to be executed when connect button is clicked
     * Gets url from text-field and sets it as server url in ServerUtils
     */
    public void connect() throws RuntimeException {
        String text = inputServer.getText();

        //if empty do nothing
        if(text == null || text.equals("")){
            return;
        }

        ServerUtils.setUsername(inputUsername.getText());
        server.addUser(new User(inputUsername.getText(), ))

        // transforms to complete url
        // if begins with colon assumed to be localhost address with specified port
        if(text.startsWith("http://")) ServerUtils.setServer(text);
        else if(text.startsWith(":")) ServerUtils.setServer("http://localhost" + text);
        else ServerUtils.setServer("http://" + text);

        // check whether valid url, and if valid go to next scene
        if(ServerUtils.checkServer()) Main.setSceneToBoard();
        else inputServer.setText("invalid");
    }
}
