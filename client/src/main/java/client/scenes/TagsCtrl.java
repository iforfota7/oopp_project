package client.scenes;

import client.scenes.config.Draggable;
import client.utils.ServerUtils;
import commons.Boards;
import commons.Cards;
import commons.Lists;
import commons.Tags;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javax.inject.Inject;
import java.util.List;

public class TagsCtrl {
    private final ServerUtils server;

    public Boards board;

    private Hyperlink currentTag;

    private MainCtrl mainCtrl;

    @FXML
    private AnchorPane rootTagContainer;


    @FXML
    private VBox tagsList;


    public void initialize(Boards board){

        this.board = board;
        createNewList(this.board);
        ((VBox) tagsList.getChildren().get(0)).getChildren().clear();
        List<Tags> tags = server.getTagsByBoard(board.id);
        for(int i = 0; i<tags.size(); i++)
        addNewTag((VBox) tagsList.getChildren().get(0), tags.get(i));
    }
private void webSocketTags() {
    server.registerForMessages("/topic/tags/add", Tags.class, t -> {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (t.board.name.equals(board.name)) {
                    VBox l = tagsList;
                    addNewTag((VBox) l.getChildren().get(0), t);

                }
            }
        });
    });

    server.registerForMessages("/topic/tags/remove", Tags.class, t->{
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(t.board.name.equals(
                        board.name)) {
                    VBox l = (VBox) rootTagContainer.lookup("#tagsList");
                    AnchorPane card = (AnchorPane) rootTagContainer.lookup("#tag"+t.id);
                    ((VBox) l.getChildren().get(0)).getChildren().remove(card);

                }

            }
        });
    });

    server.registerForMessages("/topic/tags/rename", Tags.class, t->{
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(t.board.name.equals(board.name)) {
                 Hyperlink tag =   ((Hyperlink)((AnchorPane) rootTagContainer.lookup("#tag"+t.id)).
                            getChildren().get(0));
                    tag.setText(t.title);
                    tag.setBackground(new Background(new BackgroundFill(Color.valueOf(t.color), null, null)));

                }
            }
        });
    });

}


    @Inject
    public TagsCtrl(MainCtrl mainCtrl, ServerUtils server){
        this.server = server;
        this.mainCtrl = mainCtrl;
        webSocketTags();

    }


    public void addNewTag(VBox anchor, Tags t){


        // create a new anchor pane for the card
        AnchorPane newTag = newAnchorPane();

        // add text and the delete button for the card
        newTag.getChildren().addAll(newHyperlink(), newDeleteCardButton());


        // append the card to the list

        this.currentTag = (Hyperlink) newTag.getChildren().get(0);
        newTag.getProperties().put("tag", t);
        newTag.setId("tag"+t.id);
        this.currentTag.setText(t.title);
        this.currentTag.setBackground(new Background(new BackgroundFill(Color.valueOf(t.color), null, null)));
        anchor.getChildren().add(anchor.getChildren().size(), newTag);

        // show card detail scene to be able to set details of card

    }



    @FXML
    void tagDetail(ActionEvent event) {
        Tags t =   (Tags) ((Node) event.getSource()).getParent().getProperties().get("tag");
        mainCtrl.showTagDetail(t);

    }




    public Hyperlink newHyperlink(){
        Hyperlink tag = new Hyperlink();

        // set positioning, sizing, text alignment, and background color of the hyperlink
        tag.setLayoutX(41);
        tag.setLayoutY(1);
        tag.setPrefSize(95, 23);
        tag.setAlignment(Pos.CENTER);

        // set the card to execute cardDetail on action
        tag.setOnAction(this::tagDetail);
        return tag;
    }

    public Button newDeleteCardButton(){
        Button button = new Button();

        // set the text, positioning, mnemonic parsing, and style of the button
        button.setText("X");
        button.setLayoutX(11);
        button.setLayoutY(3);
        button.setMnemonicParsing(false);
        button.setStyle("-fx-background-color: #f08080; -fx-font-size: 9.0");

        // set the button to delete the card it is a part of when clicked
        button.setOnAction(this::deleteTag);
        return button;
    }

    @FXML
    public void deleteTag(ActionEvent event) {
        Button deleteCard = (Button) event.getTarget();
        Tags t = (Tags) deleteCard.getParent().getProperties().get("tag");
        server.removeTag(t);
    }


    public AnchorPane newAnchorPane(){
        AnchorPane anchor = new AnchorPane();
        anchor.setLayoutX(0);
        anchor.setLayoutY(0);

        return anchor;
    }

    public VBox createNewList(Boards b){
        // creating the listView element
        VBox list = (VBox) rootTagContainer.lookup("#tagsList");
        VBox headerList = new VBox(6);
        HBox footerList = new HBox(30);

        headerList.setId("header");

        headerList.setMinSize(150, 235);
        footerList.setMinSize(150, 25);
        headerList.setAlignment(Pos.TOP_CENTER);
        footerList.setAlignment(Pos.TOP_CENTER);
        footerList.setStyle("-fx-padding: 0 7 0 7");

        // creating the adding card button, aligning and customising it
        Button addCardButton = createAddTagButton();


        footerList.getChildren().addAll(addCardButton);

        // creating the separator under the title, aligning and customising it
        Separator listSeparator = createSeparator();



        list.getChildren().addAll(headerList, footerList);
        list.getProperties().put("board", b);
        return list;
    }


    public Button createAddTagButton(){
        Button addButton = new Button();
        addButton.setText("+");
        addButton.setStyle("-fx-border-radius: 50; -fx-background-radius: 70; " +
                "-fx-background-color: #c8a5d9; -fx-border-color: #8d78a6; " +
                "-fx-font-size: 10px;");
        addButton.setPrefWidth(24);
        addButton.setPrefHeight(23);
        addButton.setOnAction(this::openAddNewTag);
        return addButton;
    }

    public Separator createSeparator(){
        Separator listSeparator = new Separator();
        listSeparator.setPrefWidth(150);
        listSeparator.setPrefHeight(4);
        listSeparator.setStyle("-fx-padding: -10 0 0 0;");
        return listSeparator;
    }

    void openAddNewTag(ActionEvent event){
        mainCtrl.showAddTag(board);
    }



}
