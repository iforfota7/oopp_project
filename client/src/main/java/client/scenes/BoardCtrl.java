package client.scenes;

import client.CollisionChecking;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;

public class BoardCtrl {

    @FXML
    private AnchorPane card1Container;

    @FXML
    private AnchorPane list1Container;
    @FXML
    private AnchorPane list2Container;
    @FXML
    private AnchorPane list3Container;

    @FXML
    private AnchorPane rootContainer;

    @FXML
    private Hyperlink card1;

    private double originalX;
    private double originalY;

    public void dragDetected(MouseEvent mouseEvent) {
        card1Container.getParent().toFront();
        card1Container.toFront();
        card1Container.startFullDrag();
    }

    public void mousePressed(MouseEvent mouseEvent) {
        originalX = mouseEvent.getX();
        originalY = mouseEvent.getY();
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        card1Container.setLayoutX(card1Container.getLayoutX() + mouseEvent.getX() - originalX);
        card1Container.setLayoutY(card1Container.getLayoutY() + mouseEvent.getY() - originalY);
    }

    public void mouseReleased() {

        Bounds bound1 = card1Container.localToScene(card1Container.getBoundsInLocal());

        Bounds bound2 = list2Container.localToScene(list2Container.getBoundsInLocal());
        if(CollisionChecking.collide(bound1, bound2)) {
            System.out.println("Collision with list 2");
        }

        bound2 = list3Container.localToScene(list3Container.getBoundsInLocal());
        if(CollisionChecking.collide(bound1, bound2)) {
            System.out.println("Collision with list 3");
        }
    }

}
