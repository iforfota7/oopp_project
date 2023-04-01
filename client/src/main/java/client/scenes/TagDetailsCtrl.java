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
import org.apache.catalina.Server;

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


    public void initialize(Tags t){
        this.tag = t;
        picker.setValue(Color.valueOf(this.tag.color));
        cardTitleInput.setText(this.tag.title);
    }

    @Inject
    public TagDetailsCtrl(ServerUtils server, MainCtrl mainCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * The redefinition of the card name on the board is achieved
     * through setting the display properties. This method sends the
     * information entered in cardDetails to the board to display the card name.
     * A warning is displayed if the input field is empty.
     */
    @FXML
    void save() {
        warning.setVisible(false);

        if(cardTitleInput.getText().isBlank()) {
            warning.setVisible(true);
            return;
        }

        this.tag.title = cardTitleInput.getText();
        this.tag.color = picker.getValue().toString();

        server.renameTag(tag);

        mainCtrl.closeNewTag();

    }
}
