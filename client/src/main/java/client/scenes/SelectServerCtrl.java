package client.scenes;

import client.utils.ServerUtils;
import commons.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.util.ArrayList;

public class SelectServerCtrl {


    @FXML
    private TextField inputServer;

    @FXML
    private Text serverWarning;

    @FXML
    private TextField inputUsername;

    @FXML
    private Button connect;

    private User currentUser;

    private final ServerUtils server;

    private final MainCtrl mainCtrl;
    private BoardOverviewCtrl boardOverviewCtrl;

    /**
     * Constructor method for SelectServerCtrl
     * @param server instance of ServerUtils
     * @param mainCtrl instance of MainCtrl
     */
    @Inject
    public SelectServerCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Method that returns the current User
     * @return the current User
     */
    public User getCurrentUser() {
        return currentUser;
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
        if(address.startsWith("http://")) server.setServer(address);
        else if(address.startsWith(":")) server.setServer("http://localhost" + address);
        else server.setServer("http://" + address);
        // if you can connect to the specified server address
        if(server.checkServer()){
            serverWarning.setVisible(false);
            server.setWebsockets();

            // set the username in the frontend
            ServerUtils.setUsername(username);
            // create user from information
            User user = new User(username, new ArrayList<>(), false);
            exists = server.existsUser(user);
            if(!exists){
                try{
                    server.addUser(user); // try to add user if not already in database
                }
                catch(Exception e){
                    System.out.println(e); // probably need a better way of communicating the error
                }
            }
            this.currentUser = user;
        }
        else serverWarning.setVisible(true);
        // if server exists
        if(!serverWarning.isVisible()){
            // if user does not exist, continue
            // otherwise show confirmation scene
            if(!exists) mainCtrl.showBoardOverview();
            else{
                if(currentUser.isAdmin){
                    boardOverviewCtrl.openAdminFeatures();
                }
                mainCtrl.showConfirmUsername();
            }
        }
    }

    /**
     * Set user's permission level to admin in the database.
     */
    public void setAdmin() {
        currentUser.isAdmin = true;
        server.refreshAdmin(currentUser);
    }

    /**
     * Set user's permission level back to user in the database.
     */
    public void removeAdmin() {
        currentUser.isAdmin = false;
        server.refreshAdmin(currentUser);
    }

    /**
     * Display the updated user information after refresh.
     */
    public void refreshUserDetails() {
        mainCtrl.showUserDetails(currentUser);
    }


}
