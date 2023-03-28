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
    private SelectServerCtrl selectServerCtrl;

    @Inject
    public ConfirmAdminCtrl(BoardCtrl boardCtrl, SelectServerCtrl selectServerCtrl){
        this.boardCtrl = boardCtrl;
        this.selectServerCtrl = selectServerCtrl;
    }

    @FXML
    private PasswordField inputPassword;
    @FXML
    private Label errorLabel;

    @FXML
    void adminLogin() {
        String adminPassword = boardCtrl.getAdminPassword();
        checkPassword(adminPassword.equals(inputPassword.getText()));
    }

    /**
     *Compare the current board password with the entered password to determine if they are equal.
     * If they are equal, enable the admin function and display the relevant buttons.
     * If they are not equal, show a prompt in the scene.
     * @param b boolean answer of equals(input,currentPassword)
     */
    public void checkPassword(boolean b) {
        if(b){
            errorLabel.setVisible(false);
            inputPassword.setStyle("-fx-border-color: #D3D3D3;");
            boardCtrl.openAdminFeatures();
            inputPassword.clear();
            selectServerCtrl.setAdmin();
        }
        else {inputPassword.clear();
            inputPassword.setStyle("-fx-border-color: red;");
            errorLabel.setVisible(true);
        }
    }
}
