package client.scenes.config;

import client.utils.ServerUtils;
import commons.Cards;
import commons.Lists;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
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
                ((Hyperlink)event.getGestureSource()).getParent()!=event.getSource()){

            ((Region) event.getSource()).setBackground(
                    new Background( new BackgroundFill(Color.PINK,
                    CornerRadii.EMPTY, Insets.EMPTY)));

        }
        event.consume();
    }
    public void dragExited(DragEvent event){
        if(event.getGestureSource()!=event.getSource() &&
                ((Hyperlink)event.getGestureSource()).getParent()!=event.getSource()){
            ((Region) event.getSource()).setBackground(
                    new Background( new BackgroundFill(Color.WHITE,
                    CornerRadii.EMPTY, Insets.EMPTY)));
        }
        event.consume();
    }

    public void dragOver(DragEvent event){
        if(event.getGestureSource()!=event.getSource() &&
                ((Hyperlink)event.getGestureSource())
                        .getParent()!=event.getSource()){
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    public void dragDropped(DragEvent event){
        Cards sourceCard = (Cards) ((Node) event.getGestureSource())
                .getParent().getProperties().get("card");

        if(((Node) event.getSource()).getProperties().get("list")==null){
            // the card is dropped on another card

            Cards destinationCard = (Cards) ((Node) event.getSource())
                    .getParent().getProperties().get("card");

            if(sourceCard.list.id == destinationCard.list.id &&
                    sourceCard.positionInsideList <= destinationCard.positionInsideList)
                destinationCard.positionInsideList--;

            sourceCard.positionInsideList = destinationCard.positionInsideList;
            sourceCard.list = destinationCard.list;

            server.moveCard(sourceCard);
        }else{
            // the card is dropped on a list

            Lists destinationList = (Lists) ((Node) event.getSource())
                    .getProperties().get("list");
            sourceCard.positionInsideList = destinationList.cards.size();
            if(sourceCard.list.id == destinationList.id)
                sourceCard.positionInsideList--;

            sourceCard.list = destinationList;

            server.moveCard(sourceCard);
        }


        event.setDropCompleted(true);
        event.consume();
    }

    public void dragDone(DragEvent event){
        event.consume();
    }

}
