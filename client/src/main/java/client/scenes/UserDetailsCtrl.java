package client.scenes;

import client.utils.ServerUtils;
import commons.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
    @FXML
    private Button adminLogin;
    @Inject
    public UserDetailsCtrl(MainCtrl mainCtrl, ServerUtils server){
        this.mainCtrl = mainCtrl;
        this.server = server;
    }
    private BooleanProperty adminLock = new SimpleBooleanProperty(false);

    /**
     * Method to close the secondary stage which shows the user's details
     */
    @FXML
    void closeUserDetails(){
        mainCtrl.closeUserDetails();
        mainCtrl.showBoardOverview();
    }

    public void setUser(User currentUser) {
        this.adminLock.set(currentUser.isAdmin());
        this.username.setText(currentUser.getUsername());
        if(currentUser.isAdmin()){
            this.isAdmin.setText("Yes!");
            adminLogin.setStyle("-fx-border-color: green");
            adminLogin.setText("The admin has been unlocked !");
        }else {
            this.isAdmin.setText("No!");
            adminLogin.setStyle("-fx-border-color: black");
            adminLogin.setText("Input password to become admin");
        }
    }
    private String adminPassword = "6464";
    @FXML
    void adminLogin() {
        if (adminLock.getValue()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Admin!");
            alert.setHeaderText(null);
            alert.setContentText("Admin has been unlocked!");
            alert.showAndWait();
        } else {
            mainCtrl.closeUserDetails();
            mainCtrl.showConfirmAdmin();
        }
    }
}
