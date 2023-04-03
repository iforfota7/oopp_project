package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
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


    /**
     * Initialises the tag's details scene with the tag's specific details
     * @param t the tag for which the details' scene to be shown
     */
    public void initialize(Tags t){
        this.tag = t;
        picker.setValue(Color.valueOf(this.tag.color));
        cardTitleInput.setText(this.tag.title);
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

        this.tag.title = cardTitleInput.getText();
        String color = "#" + picker.getValue().toString().substring(2, 8);
        this.tag.color = color;

        server.renameTag(tag);

        mainCtrl.closeNewTag();

    }
}
