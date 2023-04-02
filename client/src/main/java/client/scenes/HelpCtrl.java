package client.scenes;

import javafx.fxml.FXML;
import javax.inject.Inject;

public class HelpCtrl {
    private final MainCtrl mainCtrl;

    /**
     * Constructor method for HelpCtrl
     * @param mainCtrl instance of MainCtrl
     */
    @Inject
    public HelpCtrl(MainCtrl mainCtrl){
        this.mainCtrl = mainCtrl;
    }

    /**
     * Method that closes the scene
     */
    @FXML
    void close(){
        mainCtrl.closeSecondaryStage();
    }

}
