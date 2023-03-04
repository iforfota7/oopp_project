package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class BoardCtrl {

    @FXML
    private Label BoardName;

    @FXML
    private Hyperlink card1;

    @FXML
    private Hyperlink card2;

    @FXML
    private Hyperlink card3;

    @FXML
    private Button deleteCard1;

    @FXML
    private Button deleteCard2;

    @FXML
    private Button deleteCard3;

    @FXML
    private TextField filterInput;

    @FXML
    private Button helpBtu;

    @FXML
    private ListView<?> listLeft;

    @FXML
    private Button listLeftDelete;

    @FXML
    private Label listLeftName;

    @FXML
    private ListView<?> listMid;

    @FXML
    private Button listMidDelete;

    @FXML
    private Label listMidName;

    @FXML
    private ListView<?> listRight;

    @FXML
    private Button listRightDelete;

    @FXML
    private Label listRightName;

    @FXML
    private RadioButton lockBtu;

    @FXML
    void Delete(ActionEvent event) {

    }

    @FXML
    void DeleteCard(ActionEvent event) {

    }

    @FXML
    void lockOrUnlock(ActionEvent event) {

    }

    @FXML
    void openCard(ActionEvent event) {

    }

    @FXML
    void openHelp(ActionEvent event) {

    }

}
