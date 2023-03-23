package client.scenes.config;

import client.utils.ServerUtils;
import commons.Cards;
import commons.Lists;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Draggable {

    ServerUtils server;

    public Draggable(ServerUtils server){
        this.server = server;
    }

    public void dragDetected(MouseEvent mouseEvent) {
        Hyperlink dragged = (Hyperlink) mouseEvent.getSource();
        Dragboard db = dragged.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString(dragged.getText());
        db.setContent(content);
        mouseEvent.consume();
    }

    public void dragEntered(DragEvent event){
        if(event.getGestureSource()!=event.getSource() &&
                ((Hyperlink)event.getGestureSource()).getParent()!=event.getSource() &&
                event.getSource() instanceof VBox){

            ((Region) event.getSource()).setBackground(  new Background( new BackgroundFill(Color.PINK,
                    CornerRadii.EMPTY, Insets.EMPTY)));

        }
        event.consume();
    }
    public void dragExited(DragEvent event){
        if(event.getGestureSource()!=event.getSource() &&
                ((Hyperlink)event.getGestureSource()).getParent()!=event.getSource() &&
                event.getSource() instanceof VBox){
            ((Region) event.getSource()).setBackground(  new Background( new BackgroundFill(Color.WHITE,
                    CornerRadii.EMPTY, Insets.EMPTY)));
        }
        event.consume();
    }

    public void dragOver(DragEvent event){
        if(event.getGestureSource()!=event.getSource() &&
                ((Hyperlink)event.getGestureSource()).getParent()!=event.getSource()){
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    /**
     * Computes how to change the card's position when it is dropped
     * It takes into consideration the cursor coordinates in order to calculate
     * the position inside that specific list
     *
     * @param event An object containing information about the drag event
     */
    public void dragDropped(DragEvent event){
        // information about the dragged card
        Cards sourceCard = (Cards) ((Node) event.getGestureSource()).getParent().getProperties().get("card");

        // information about the node where the card has been dropped
        Node node = (Node)event.getSource();

        //this while loop takes care of the case where the card is dropped on a card
        while(!(node instanceof VBox))
            node = node.getParent();

        //this is statement takes care of the case where the card is dropped on a list
        if(((VBox) node).getChildren().get(0).getId().equals("header"))
            node = ((VBox) node).getChildren().get(0);

        // node should now be the interior of the list

        int childIndex = 0;
        boolean foundPosition = false;
        int originalPosition = sourceCard.positionInsideList;

        Lists targetList;
        if(node.getProperties().get("list") == null) {
            // we dropped on a card
            targetList = (Lists)node.getParent().getProperties().get("list");
        }

        else {
            // we dropped on a list
            targetList = (Lists)node.getProperties().get("list");
        }

        for(Node cardContainer : ((VBox) node).getChildren())
            if(cardContainer instanceof AnchorPane) {
                // we iterate through the cards to determine where to drop this card

                // the Y coordinate for the middle of the card
                double midYPoint = cardContainer.localToScene(0, 0).getY() + ((AnchorPane) cardContainer).getHeight() / 2;

                // the Y coordinate of the mouse when the card has been dropped
                double mouseY = ((Node)event.getSource()).localToScene(event.getX(), event.getY()).getY();

                System.out.println(midYPoint + " " + mouseY);
                if(mouseY <= midYPoint) {
                    sourceCard.positionInsideList = childIndex;
                    foundPosition = true;
                    break;
                }
                childIndex++;
            }

        // the card was dropped below all existing cards
        if(!foundPosition)
            sourceCard.positionInsideList = childIndex;

        // we treat the special case of dropping the card in the same list
        if(sourceCard.list.id == targetList.id && originalPosition < sourceCard.positionInsideList)
            sourceCard.positionInsideList--;
        sourceCard.list = targetList;

        server.moveCard(sourceCard);

        event.setDropCompleted(true);
        event.consume();
    }

    public void dragDone(DragEvent event){
        event.consume();
    }

}
