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
    private final SelectServerCtrl selectServerCtrl;

    @FXML
    private Label username;
    @FXML
    private Label serverAddress;
    @FXML
    private Label isAdmin;
    @FXML
    private Button adminLogout;

    @FXML
    private Button adminLogin;

    private BooleanProperty adminLock = new SimpleBooleanProperty(false);

    /**
     * Constructor method for UserDetailsCtrl
     * @param mainCtrl instance of MainCtrl
     * @param server instance of ServerUtils
     * @param selectServerCtrl instance of SelectServerCtrl
     */
    @Inject
    public UserDetailsCtrl(MainCtrl mainCtrl, ServerUtils server,
                           SelectServerCtrl selectServerCtrl){
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.selectServerCtrl = selectServerCtrl;
    }

    /**
     * Method to close the secondary stage which shows the user's details
     */
    @FXML
    void closeUserDetails(){
        mainCtrl.closeSecondaryStage();
        mainCtrl.showBoardOverview();
    }

    /**
     * Display different button prompts and button colors based on the logged-in user.
     * @param currentUser Current user element
     */
    public void setUser(User currentUser) {
        this.adminLock.set(server.checkAdmin());
        this.username.setText(currentUser.username);
        if(adminLock.get()){
            this.isAdmin.setText("Yes!");
            adminLogin.setVisible(false);
            adminLogout.setVisible(true);
        }else {
            this.isAdmin.setText("No!");
            adminLogin.setVisible(true);
            adminLogin.setText("Input password to become admin");
            adminLogout.setVisible(false);
        }
    }

    /**
     * show the login admin scene.
     */
    @FXML
    void adminLogin() {
        if (adminLock.getValue()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Admin!");
            alert.setHeaderText(null);
            alert.setContentText("Admin has been unlocked!");
            alert.showAndWait();
        } else {
            mainCtrl.closeSecondaryStage();
            mainCtrl.showConfirmAdmin();
        }
    }

    /**
     * Click the button to synchronize and modify
     * the 'admin' attribute of the user in the frontend of the database, and set it to false.
     */
    @FXML
    void adminLogout() {
        selectServerCtrl.removeAdmin();
        mainCtrl.closeSecondaryStage();
        mainCtrl.showBoardOverview();
    }
}
