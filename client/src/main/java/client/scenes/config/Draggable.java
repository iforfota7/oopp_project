package client.scenes.config;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Cards;
import commons.Lists;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


public class Draggable {

    private ServerUtils server;

    /**
     * Constructor method for Draggable
     * @param server instance of ServerUtils
     */
    @Inject
    public Draggable(ServerUtils server){
        this.server = server;
    }

    /**
     * Method that initializes the drag event
     *
     * @param mouseEvent Object containing information about the mouse event
     */
    public void dragDetected(MouseEvent mouseEvent) {
        AnchorPane dragged = (AnchorPane) mouseEvent.getSource();
        Dragboard db = dragged.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        Label cardTitle = (Label)((HBox)((VBox)((AnchorPane)dragged.getParent())
                .getChildren().get(1)).getChildren().get(0)).getChildren().get(0);
        content.putString(cardTitle.getText());
        db.setContent(content);
        mouseEvent.consume();
    }

    /**
     * Describes what happens when the dragged card enters a list
     * The drop target list is highlighted for a better user experience
     *
     * @param event Object containing information about the drag event
     */
    public void dragEntered(DragEvent event){
        if(event.getGestureSource() != event.getSource() &&
                ((AnchorPane) event.getGestureSource()).getParent() != event.getSource() &&
                event.getSource() instanceof VBox){

            ((Region) event.getSource()).setBackground(  new Background(
                    new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));

        }
        event.consume();
    }

    /**
     * Describes what happens when the dragged card exits the list
     * The color of the list is reverted and the visual cue is removed
     *
     * @param event Object containing information about the drag event
     */
    public void dragExited(DragEvent event){
        if(event.getGestureSource()!=event.getSource() &&
                ((AnchorPane)event.getGestureSource()).getParent()!=event.getSource() &&
                event.getSource() instanceof VBox){
            ((Region) event.getSource()).setBackground(  new Background(
                    new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }

        if(event.getSource() instanceof  VBox) {
            removeVisualCue((Node)event.getSource());
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
     * @param dropTarget The element on which the card could be dropped
     * @return The list object associated with this element
     */
    private Lists computeTargetList(Node dropTarget) {
        dropTarget = getListContainerFromDropTarget(dropTarget);
        return (Lists)dropTarget.getParent().getProperties().get("list");
    }

    /**
     * Removes the visual cue, such that it could be re-rendered
     *
     * @param dropTarget The node on which the card would be dropped
     */
    private void removeVisualCue(Node dropTarget) {
        dropTarget = getListContainerFromDropTarget(dropTarget);
        for(Node child : ((VBox) dropTarget).getChildren()) {
            if("visual-cue".equals(child.getId())) {
                ((VBox) dropTarget).getChildren().remove(child);
                return;
            }
        }
    }

    /**
     * Computes how to change the card's position when it is dropped
     * It takes into consideration the cursor coordinates in order to calculate
     * the position inside that specific list
     *
     * @param event The object containing information about the drag event
     * @param sourceCard The card that is being dragged
     * @param node The element on which the card could be dropped
     * @return The new position of the dragged card
     */
    private int positionOfDroppedCard(DragEvent event, Cards sourceCard, Node node) {
        Lists targetList = computeTargetList(node);
        node = getListContainerFromDropTarget(node);

        int childIndex = 0;
        int answer = -1;
        boolean foundPosition = false;
        int originalPosition = sourceCard.positionInsideList;

        for (Node cardContainer : ((VBox) node).getChildren())
            if (cardContainer instanceof AnchorPane) {
                // we iterate through the cards to determine where to drop this card

                // the Y coordinate for the middle of the card
                double midYPoint = cardContainer.localToScene(0, 0).getY()
                        + ((AnchorPane) cardContainer).getHeight() / 2;

                // the Y coordinate of the mouse when the card has been dropped
                double mouseY = ((Node) event.getSource()).localToScene(event.getX(),
                        event.getY()).getY();

                if (mouseY <= midYPoint) {
                    answer = childIndex;
                    foundPosition = true;
                    break;
                }
                childIndex++;
            }

        // the card was dropped below all existing cards
        if (!foundPosition)
            answer = childIndex;

        // we treat the special case of dropping the card in the same list
        if (sourceCard.list.id == targetList.id && originalPosition < answer)
            answer--;

        return answer;
    }

    /**
     * Creates and renders a visual cue that indicates where will the card be dropped
     *
     * @param dropTarget The element on which the source card is dropped
     * @param targetList The Lists object that corresponds to the list container
     *                   inside which the card is dropped
     * @param sourceCard The card that is being dragged
     * @param positionInsideList The position at which the card will be dropped
     *                          at this moment in time
     */
    public void createVisualCue(Node dropTarget, Lists targetList,
                                Cards sourceCard, int positionInsideList) {

        Separator listSeparator = new Separator();
        listSeparator.setMaxWidth(125);
        listSeparator.setPrefHeight(1);
        listSeparator.setTranslateX(-1);
        listSeparator.setStyle("-fx-background-color: #8d78a6;");
        listSeparator.setId("visual-cue");

        VBox listContainer = getListContainerFromDropTarget(dropTarget);

        int index = 0;
        int childCount = 0;
        for(Node child : listContainer.getChildren()) {
            if(child instanceof AnchorPane) {
                if(childCount == positionInsideList)
                    break;
                childCount++;
            }
            index++;

        }

        if(targetList.id == sourceCard.list.id &&
                sourceCard.positionInsideList < positionInsideList)
            index++;

        if(targetList.id != sourceCard.list.id ||
                positionInsideList != sourceCard.positionInsideList)
            listContainer.getChildren().add(index, listSeparator);
    }

    /**
     * Method describing what happens when the source card enters a list
     * The list is set to accept the incoming card and a visual cue is rendered,
     * letting the user know the position at which the card will be dropped
     *
     * @param event The object containing information about the drag event
     */
    public void dragOver(DragEvent event){
        if(event.getGestureSource()!=event.getSource() &&
                ((AnchorPane)event.getGestureSource())
                        .getParent()!=event.getSource()){
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }

        // information about the dragged card
        Cards sourceCard = (Cards) ((AnchorPane)((Node) event.getGestureSource())
                .getParent()).getChildren().get(1).getProperties().get("card");

        // information about the node where the card has been dropped
        Node node = (Node)event.getSource();

        int positionInsideList = positionOfDroppedCard(event, sourceCard, node);
        Lists targetList = computeTargetList(node);

        removeVisualCue(node);
        createVisualCue(node, targetList, sourceCard, positionInsideList);

        event.consume();
    }

    /**
     * Performs the drag and drop procedure using the helper methods above
     *
     * @param event An object containing information about the drag event
     */
    public void dragDropped(DragEvent event){
        // information about the dragged card
        Cards sourceCard = (Cards) ((AnchorPane)((Node) event.getGestureSource())
                .getParent()).getChildren().get(1).getProperties().get("card");

        // information about the node where the card has been dropped
        Node node = (Node)event.getSource();

        sourceCard.positionInsideList = positionOfDroppedCard(event, sourceCard, node);
        sourceCard.list = computeTargetList(node);
        removeVisualCue((Node)event.getSource());

        server.moveCard(sourceCard);

        event.setDropCompleted(true);
        event.consume();

    }

}