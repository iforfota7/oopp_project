package client.scenes;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.inject.Inject;


public class CardDetailsCtrl {

    @FXML
    private TextField cardTitleInput;

    @FXML
    private TextArea descriptionInput;

    @FXML
    private TextArea tagsInput;

    @FXML
    private Button taskOneDelete;

    @FXML
    private CheckBox taskOneInput;

    @FXML
    private Button taskThreeDelete;

    @FXML
    private CheckBox taskThreeInput;

    @FXML
    private Button taskTwoDelete;

    @FXML
    private CheckBox taskTwoInput;

    private BoardCtrl boardCtrl;

    @Inject
    public CardDetailsCtrl(BoardCtrl boardCtrl){
        this.boardCtrl = boardCtrl;
    }
    @FXML
    void save() {
//         Cards card = new Cards();
//         boardCtrl.RefreshCard(card);
        boardCtrl.RefreshCard(cardTitleInput.getText());
    }
}
