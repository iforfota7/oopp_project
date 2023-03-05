package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;

public class BoardCtrl {

    @FXML
    private AnchorPane card1Container;

    @FXML
    private Hyperlink card1;

    private double originalX;
    private double originalY;

    public void dragDetected(MouseEvent mouseEvent) {
        card1Container.getParent().toFront();
        card1Container.toFront();
    }

    public void mousePressed(MouseEvent mouseEvent) {
        originalX = mouseEvent.getX();
        originalY = mouseEvent.getY();
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        card1Container.setLayoutX(card1Container.getLayoutX() + mouseEvent.getX() - originalX);
        card1Container.setLayoutY(card1Container.getLayoutY() + mouseEvent.getY() - originalY);
    }

    public void mouseReleased(MouseEvent mouseEvent) {

    }

}
