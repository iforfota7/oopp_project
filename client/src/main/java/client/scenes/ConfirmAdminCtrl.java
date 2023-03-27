package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import javax.inject.Inject;

/**
 * Verify the admin identity by entering the password
 * and open a part of display functions to specific users.
 */
public class ConfirmAdminCtrl {
    private BoardCtrl boardCtrl;

    @Inject
    public ConfirmAdminCtrl(BoardCtrl boardCtrl){
        this.boardCtrl = boardCtrl;
    }

    @FXML
    private PasswordField inputPassword;
    @FXML
    private Label errorLabel;

    @FXML
    void adminLogin() {
        String adminPassword = boardCtrl.getAdminPassword();
        checkBack(adminPassword.equals(inputPassword.getText()));
    }

    public void checkBack(boolean b) {
        if(b){
            errorLabel.setVisible(false);
            inputPassword.setStyle("-fx-border-color: #D3D3D3;");
            boardCtrl.openAdminFeatures();
            inputPassword.clear();
        }
        else {inputPassword.clear();
            inputPassword.setStyle("-fx-border-color: red;");
            errorLabel.setVisible(true);
        }
    }
}
