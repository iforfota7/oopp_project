package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Boards;
import commons.Tags;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class TagDetailsCtrl {
    @FXML
    private TextField cardTitleInput;
    @FXML
    private Text warning;
    @FXML
    private ColorPicker picker;
    private Tags tag;
    private ServerUtils server;
    private MainCtrl mainCtrl;
    private Boards board;


    /**
     * Initialises the tag's details scene with the tag's specific details
     * @param t the tag for which the details' scene to be shown
     * @param board reference to the board object that contains this tag
     */
    public void initialize(Tags t, Boards board){
        this.tag = t;
        picker.setValue(Color.valueOf(this.tag.color));
        cardTitleInput.setText(this.tag.title);
        this.board = board;
    }

    /**
     * Constructor for the TagDetailsCtrl class
     * @param server used for sending requests to the server
     * @param mainCtrl instance of the mainCtrl class
     */
    @Inject
    public TagDetailsCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Saves the changes made to the tag into the application
     * A warning is displayed if the input field for the name is empty.
     */
    @FXML
    void save() {
        warning.setVisible(false);

        if(cardTitleInput.getText().isBlank()) {
            warning.setVisible(true);
            return;
        }

        int tagPositionInBoard = board.tags.indexOf(tag);

        tag.title = cardTitleInput.getText();
        String color = "#" + picker.getValue().toString().substring(2, 8);
        tag.color = color;
        board.tags.set(tagPositionInBoard, tag);

        server.updateBoard(board);

        mainCtrl.closeThirdStage();

    }
}
