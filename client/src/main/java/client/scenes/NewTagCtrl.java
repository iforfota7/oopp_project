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


public class NewTagCtrl {
    @FXML
    private TextField cardTitleInput;
    @FXML
    private Text warning;

    @FXML
    private ColorPicker picker;

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    private Boards board;

    /**
     * Constructor of the NewTagCtrl class
     * @param server instance of the serverUtils class
     * @param mainCtrl instance of the mainCtrl class
     */
    @Inject
    public NewTagCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initialises the NewCardCtrl to the board which the tag is to be created into
     * @param b the board which the tag is to be created into
     */
    public void initialize(Boards b){this.board = b;}

    /**
     * Saves the new created tag into the application
     * A warning is displayed if the input field for the title is empty.
     */
    @FXML
    void save() {
        warning.setVisible(false);

        if(cardTitleInput.getText().isBlank()) {

            warning.setVisible(true);
            return;
        }

        board.tags.add(new Tags(cardTitleInput.getText(), colorTag(picker.getValue()), board));
        server.updateBoard(board);

        cardTitleInput.clear();
        this.mainCtrl.closeThirdStage();
    }

    /**
     * Changes the colorPicker value of a color into a CSS string value of that color
     * @param tagColor the colorPicker value of a color
     * @return the CSS string value of that color
     */
    private String colorTag(Color tagColor){
        return String.format("#%02X%02X%02X",
                (int)(tagColor.getRed() * 255),
                (int)(tagColor.getGreen() * 255),
                (int)(tagColor.getBlue() * 255));
    }
}
