package client.scenes;

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
    private BoardCtrl boardCtrl;
    private BoardOverviewCtrl boardOverviewCtrl;
    private SelectServerCtrl selectServerCtrl;

    private MainCtrl mainCtrl;

    @Inject
    public ConfirmAdminCtrl(BoardCtrl boardCtrl, BoardOverviewCtrl boardOverviewCtrl,
                            SelectServerCtrl selectServerCtrl, MainCtrl mainCtrl){
        this.boardCtrl = boardCtrl;
        this.boardOverviewCtrl = boardOverviewCtrl;
        this.selectServerCtrl = selectServerCtrl;
        this.mainCtrl = mainCtrl;
    }

    @FXML
    private PasswordField inputPassword;
    @FXML
    private Label errorLabel;
    /**
     *Compare the current board password with the entered password to determine if they are equal.
     * If they are equal, enable the admin function and display the relevant buttons.
     * If they are not equal, show a prompt in the scene.
     */
    @FXML
    void adminLogin() {
        String adminPassword = boardCtrl.getAdminPassword();
        if(adminPassword.equals(inputPassword.getText())){
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
            mainCtrl.closeConfirmAdmin();
            selectServerCtrl.refreshUserDetails();
        }else {
            inputPassword.clear();
            inputPassword.setStyle("-fx-border-color: red;");
            errorLabel.setVisible(true);
        }
    }



}
