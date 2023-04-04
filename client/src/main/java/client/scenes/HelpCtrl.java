package client.scenes;

import javafx.fxml.FXML;
import javax.inject.Inject;

public class HelpCtrl {
    private final MainCtrl mainCtrl;

    /**
     * Constructor method for HelpCtrl class
     * @param mainCtrl
     */
    @Inject
    public HelpCtrl(MainCtrl mainCtrl){
        this.mainCtrl = mainCtrl;
    }

    /**
     * Method to close the Help window
     */
    @FXML
    void close(){
        mainCtrl.closeSecondaryStage();
    }

}
