package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class SelectServerCtrl {

    @FXML
    private AnchorPane container;

    @FXML
    private TextField inputServer;

    @FXML
    private Button connect;

    /**
     * Method to be executed when connect button is clicked
     * Gets url from text-field and sets it as server url in ServerUtils
     */
    public void connect(){
        String text = inputServer.getText();

        //if empty do nothing
        if(text == null || text == ""){
            return;
        }

        //transforms to complete localhost url -- might need to be generalised
        switch(text.charAt(0)) {
            case 'h':
                ServerUtils.setServer(text);
                break;
            case 'l':
                ServerUtils.setServer("http://" + text);
                break;
            case ':':
                ServerUtils.setServer("http://localhost" + text);
                break;
            default:
                ServerUtils.setServer("http://localhost:" + text);
        }

        // check whether valid url, and if valid go to next scene
        if(!ServerUtils.checkServer()) System.out.println("Not a valid url");
        else Main.setScenetoBoard();
    }
}
