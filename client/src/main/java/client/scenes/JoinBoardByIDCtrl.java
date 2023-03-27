package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.sun.security.auth.callback.TextCallbackHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class JoinBoardByIDCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField inputBoardID;
    @FXML
    private Text warning;

    @Inject
    public JoinBoardByIDCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server=server;
        this.mainCtrl=mainCtrl;
    }

    /**
     * Join the board specified in the input field of the scene.
     * This method is used when clicking the "join" button in
     * the JoinBoardByID scene.
     */
    public void join() {

        warning.setVisible(false);

        if(inputBoardID.getText().isBlank()) {
            warning.setText("Please enter a board ID");
            warning.setVisible(true);
            return;
        }

        if(server.existsBoardByID(inputBoardID.getText())) {

            warning.setVisible(false);
            Main.setSceneToBoard(inputBoardID.getText());
            inputBoardID.clear();
        }
        else {
            warning.setText("This board does not exist!");
            warning.setVisible(true);
        }
    }

    /**
     * Cancel the action of joining a board by its ID.
     * This method is used when clicking the "cancel" button in
     * the JoinBoardByID scene.
     */
    public void cancel() {

        warning.setVisible(false);
        inputBoardID.clear();
        mainCtrl.closeJoinBoardByID();
    }
}
