package client.scenes;

import client.utils.ServerUtils;
import commons.Boards;
import commons.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.util.List;

public class SelectServerCtrl {


    @FXML
    private TextField inputServer;

    @FXML
    private Text serverWarning;

    @FXML
    private TextField inputUsername;

    private User currentUser;

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    private final SelectServerCtrlServices selectServerCtrlServices;

    /**
     * Constructor method for SelectServerCtrl
     * @param server instance of ServerUtils
     * @param mainCtrl instance of MainCtrl
     * @param selectServerCtrlServices instance of SelectServerCtrlServices
     */
    @Inject
    public SelectServerCtrl(ServerUtils server, MainCtrl mainCtrl,
                            SelectServerCtrlServices selectServerCtrlServices){
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.selectServerCtrlServices = selectServerCtrlServices;
    }

    /**
     * Method that returns the current User
     * @return the current User
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Setter method for current user primarily used for testing
     * @param user the user to be set to
     */
    public void setCurrentUser(User user) {this.currentUser = user; }

    /**
     * Setter method for the serverWarning primarily used for tests
     * @param text the text serverWarning should be set to
     */
    public void setServerWarning(Text text) {this.serverWarning = text; }

    /**
     * Method to be executed when connect button is clicked
     * Gets url username from text-field
     */
    public void connect() {
        String address = inputServer.getText();
        String username = inputUsername.getText();
        connect(address, username);
    }

    /**
     * Method that connects the user to the board overview
     * @param address sets it as server url in ServerUtils if valid
     * @param username sets it as username in ServerUtils and finds current user
     * @return true if the method completed successfully and the user sees a new scene
     */
    public boolean connect(String address, String username){
        this.currentUser = selectServerCtrlServices.checkConnection(address, username, server);
        boolean exists = currentUser != null;

        if(!exists) serverWarning.setVisible(true);
        else serverWarning.setVisible(false);

        // if server exists
        if(!serverWarning.isVisible()){
            // if user does not exist, continue
            // otherwise show confirmation scene
            if(!exists) mainCtrl.showBoardOverview();
            else{
                mainCtrl.showConfirmUsername();
            }
            return true;
        }
        return false;
    }

    /**
     * Set user's permission level to admin in the database.
     * @return true if the user was successfully set as admin
     */
    public boolean setAdmin() {
        currentUser.isAdmin = true;
        server.refreshAdmin(currentUser);
        return true;
    }

    /**
     * Set user's permission level back to user in the database.
     * @return true if the user was successfully removed as an admin
     */
    public boolean removeAdmin() {
        currentUser.isAdmin = false;
        server.refreshAdmin(currentUser);
        return true;
    }

    /**
     * Display the updated user information after refresh.
     * @return true if the scene was successfully shown
     */
    public boolean refreshUserDetails() {
        mainCtrl.showUserDetails(currentUser);
        return true;
    }

    /**
     * Set the boards of the current user
     * @param boards the new list of boards
     */
    public void setBoardsOfCurrentUser(List<Boards> boards){
        this.currentUser.boards = boards;
    }
}
