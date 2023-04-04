package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import javax.inject.Inject;

/**
 * Verify the admin identity by entering the password
 * and open a part of display functions to specific users.
 */
public class ConfirmAdminCtrl {
    private  BoardOverviewCtrl boardOverviewCtrl;
    private  SelectServerCtrl selectServerCtrl;

    private MainCtrl mainCtrl;
    private ServerUtils server;

    /**
     * Constructor method for ConfirmAdminCtrl
     * @param boardOverviewCtrl instance of BoardOverviewCtrl
     * @param selectServerCtrl instance of SelectServerCtrl
     * @param mainCtrl instance of MainCtrl
     * @param server instance of ServerUtils
     */
    @Inject
    public ConfirmAdminCtrl(BoardOverviewCtrl boardOverviewCtrl,
                            SelectServerCtrl selectServerCtrl, MainCtrl mainCtrl,
                            ServerUtils server){
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.selectServerCtrl = selectServerCtrl;
        this.mainCtrl = mainCtrl;
        this.server = server;
    }
    @FXML
    private PasswordField inputPassword;
    @FXML
    private Label errorLabel;

    /**
     *The method here implements the functionality of the admin button,
     *  allowing users to enter a password to verify their admin privileges.
     *  If the password is correct, the corresponding button functionality will be displayed,
     *  allowing the user to upgrade to an admin and perform renaming and deletion functions.
     */
    @FXML
    void adminLogin() {
        boolean check = server.checkPassword(inputPassword.getText());
        if(check){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login successful!");
            alert.setHeaderText(null);
            alert.setContentText("Welcome admin!");
            alert.showAndWait();
            errorLabel.setVisible(false);
            inputPassword.setStyle("-fx-border-color: #D3D3D3;");
            boardOverviewCtrl.openAdminFeatures();
            inputPassword.clear();
            selectServerCtrl.setAdmin();
            mainCtrl.closeSecondaryStage();
            selectServerCtrl.refreshUserDetails();
        }else {
            inputPassword.clear();
            inputPassword.setStyle("-fx-border-color: red;");
            errorLabel.setVisible(true);
        }
    }

}
