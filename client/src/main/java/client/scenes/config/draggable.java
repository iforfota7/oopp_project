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

public class draggable {

    ServerUtils server;

    public draggable(ServerUtils server){
        this.server = server;
    }

    public void dragDetected(MouseEvent mouseEvent) {
        System.out.println("onDragDetected");
        Hyperlink dragged = (Hyperlink) mouseEvent.getSource();
        Dragboard db = dragged.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString(dragged.getText());
        db.setContent(content);
        mouseEvent.consume();
    }

    public void dragEntered(DragEvent event){
        System.out.println("onDragEntered");
        if(event.getGestureSource()!=event.getSource() &&
                ((Hyperlink)event.getGestureSource()).getParent()!=event.getSource()){

            ((Region) event.getSource()).setBackground(  new Background( new BackgroundFill(Color.PINK,
                    CornerRadii.EMPTY, Insets.EMPTY)));

        }
        event.consume();
    }
    public void dragExited(DragEvent event){
        if(event.getGestureSource()!=event.getSource() &&
                ((Hyperlink)event.getGestureSource()).getParent()!=event.getSource()){
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

    public void dragDropped(DragEvent event){
        System.out.println("onDragDropped");
        Cards c = (Cards) ((Node) event.getGestureSource()).getParent().getProperties().get("card");
        System.out.println(c);
        server.removeCard(c);

        if(((Node) event.getSource()).getProperties().get("list")==null){
            Cards co = (Cards) ((Node) event.getSource()).getParent().getProperties().get("card");
            System.out.println(co);
            c.positionInsideList = co.positionInsideList;
            c.list = co.list;
            server.addCard(c);
        }else{
            Lists l = (Lists) ((Node) event.getSource()).getProperties().get("list");
            System.out.println(l);
            c.positionInsideList = l.cards.size();
            if(c.list.id == l.id)
                --c.positionInsideList;
            System.out.println(l.cards.size());
            c.list = l;
            server.addCard(c);
        }


        event.setDropCompleted(true);
        event.consume();
    }

    public void dragDone(DragEvent event){
        System.out.println("onDragDone");
        event.consume();
    }

}
