package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import jakarta.ws.rs.ProcessingException;
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
    public void connect() throws RuntimeException {
        String text = inputServer.getText();

        //if empty do nothing
        if(text == null || text == ""){
            return;
        }

        //transforms to complete localhost url -- might need to be generalised
        switch(text.charAt(0)) {
            case 'h': //assume proper url
                ServerUtils.setServer(text);
                break;
            case ':': //just port specified, so make localhost address
                ServerUtils.setServer("http://localhost" + text);
                break;
            case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 0:
                //just port specified (but without colon) so make into localhost address
                ServerUtils.setServer("http://localhost:" + text);
                break;
            default: //assume any other proper url input, but missing protocol
                ServerUtils.setServer("http://" + text);
        }

        // check whether valid url, and if valid go to next scene
        if(!ServerUtils.checkServer()) throw new RuntimeException();
        else Main.setScenetoBoard();
    }
}
