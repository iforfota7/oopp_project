package client.scenes;

import client.Main;
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

    private MainCtrl mainCtrl;

    private Boards board;


    @Inject
    public NewTagCtrl(ServerUtils server, MainCtrl mainCtrl){

        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void initialize(Boards b){this.board = b;}

    /**
     * The redefinition of the card name on the board
     * is achieved through setting the display properties.
     * This method sends the information entered in cardDetail
     * to the board to display the card name.
     * A warning is displayed if the input field is empty.
     */
    @FXML
    void save() {
        warning.setVisible(false);

        if(cardTitleInput.getText().isBlank()) {

            warning.setVisible(true);
            return;
        }
        System.out.println(board);
        server.addTag(new Tags(cardTitleInput.getText(), colorTag(picker.getValue()), board));
        cardTitleInput.clear();
        this.mainCtrl.closeNewTag();
    }

    private String colorTag(Color tagColor){
        return String.format("#%02X%02X%02X",
                (int)(tagColor.getRed() * 255),
                (int)(tagColor.getGreen() * 255),
                (int)(tagColor.getBlue() * 255));
    }
}
