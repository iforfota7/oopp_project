package client.scenes;

import client.lib.CollisionChecking;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BoardCtrl implements Initializable {

    @FXML
    private AnchorPane card1Container;

    @FXML
    private AnchorPane list1Container;
    @FXML
    private AnchorPane list2Container;
    @FXML
    private AnchorPane list3Container;

    List<AnchorPane> listContainers;

    private double originalX;
    private double originalY;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        listContainers = new ArrayList<>();
        listContainers.add(list1Container);
        listContainers.add(list2Container);
        listContainers.add(list3Container);
    }

    public void dragDetected() {
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

    public void mouseReleased() {

        Bounds bound1 = card1Container.localToScene(card1Container.getBoundsInLocal());

        for(AnchorPane listContainer : listContainers) {
            Bounds bound2 = listContainer.localToScene(listContainer.getBoundsInLocal());
            if(!listContainer.equals(card1Container.getParent()) && CollisionChecking.collide(bound1, bound2)) {
                System.out.println("Collision with " + listContainer.getId());
            }
        }
    }

}
