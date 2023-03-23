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
     * Returns the list container based on the element on which the card could be dropped
     *
     * @param dropTarget The element on which the card is dropped
     * @return The list container
     */

    private VBox getListContainerFromDropTarget(Node dropTarget) {
        //this while statement takes care of the case where the card is dropped on a card
        while(!(dropTarget instanceof VBox))
            dropTarget = dropTarget.getParent();

        //this is statement takes care of the case where the card is dropped on a list
        if(((VBox) dropTarget).getChildren().get(0).getId().equals("header"))
            dropTarget = ((VBox) dropTarget).getChildren().get(0);

        return (VBox)dropTarget;
    }

    /**
     * Returns the object associated with the list container on which the card could be dropped
     *
     * @param dropTarget the element on which the card could be dropped
     * @return The list object associated with this element
     */

    private Lists computeTargetList(Node dropTarget) {
        dropTarget = getListContainerFromDropTarget(dropTarget);
        return (Lists)dropTarget.getParent().getProperties().get("list");
    }

    /**
     * Computes how to change the card's position when it is dropped
     * It takes into consideration the cursor coordinates in order to calculate
     * the position inside that specific list
     *
     * @param event the object containing information about the drag event
     * @param sourceCard the card that is being dragged
     * @param node the element on which the card could be dropped
     * @return the new position of the dragged card
     */

    private int positionOfDroppedCard(DragEvent event, Cards sourceCard, Node node) {
        Lists targetList = computeTargetList(node);
        node = getListContainerFromDropTarget(node);

        int childIndex = 0;
        int answer = -1;
        boolean foundPosition = false;
        int originalPosition = sourceCard.positionInsideList;

        for(Node cardContainer : ((VBox) node).getChildren())
            if(cardContainer instanceof AnchorPane) {
                // we iterate through the cards to determine where to drop this card

                // the Y coordinate for the middle of the card
                double midYPoint = cardContainer.localToScene(0, 0).getY() + ((AnchorPane) cardContainer).getHeight() / 2;

                // the Y coordinate of the mouse when the card has been dropped
                double mouseY = ((Node)event.getSource()).localToScene(event.getX(), event.getY()).getY();

                if(mouseY <= midYPoint) {
                    answer = childIndex;
                    foundPosition = true;
                    break;
                }
                childIndex++;
            }

        // the card was dropped below all existing cards
        if(!foundPosition)
            answer = childIndex;

        // we treat the special case of dropping the card in the same list
        if(sourceCard.list.id == targetList.id && originalPosition < answer)
            answer--;

        return answer;
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

        sourceCard.positionInsideList = positionOfDroppedCard(event, sourceCard, node);
        sourceCard.list = computeTargetList(node);

        server.moveCard(sourceCard);

        event.setDropCompleted(true);
        event.consume();
    }

    public void dragDone(DragEvent event){
        event.consume();
    }

}
