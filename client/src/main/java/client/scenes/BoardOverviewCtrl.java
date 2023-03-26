package client.scenes;

import client.Main;
import javafx.scene.input.MouseEvent;

import javax.inject.Inject;

public class BoardOverviewCtrl {

    private MainCtrl mainCtrl;
    @Inject
    public BoardOverviewCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void doSth(MouseEvent event) {
        Main.setSceneToBoard();
    }
}
