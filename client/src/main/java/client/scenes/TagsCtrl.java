package client.scenes;

import client.utils.ServerUtils;
import commons.Boards;
import commons.Tags;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javax.inject.Inject;
import java.util.List;

public class TagsCtrl {
    private final ServerUtils server;

    public Boards board;

    private AnchorPane currentTag;

    private MainCtrl mainCtrl;
    @FXML
    private VBox tagList;
    @FXML
    private AnchorPane rootTagContainer;


    public void initialize(Boards board){
        this.board = board;
        tagList.getChildren().clear();
        List<Tags> tags = server.getTagsByBoard(board.id);
        for(int i = 0; i < tags.size(); i++)
            addNewTag(tags.get(i));
    }
    private void webSocketTags() {
        server.registerForMessages("/topic/tags/add", Tags.class, t -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (t.board.name.equals(board.name)) {
                        addNewTag(t);
                    }
                }
            });
        });

        server.registerForMessages("/topic/tags/remove", Tags.class, t->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(t.board.name.equals(board.name)) {
                        AnchorPane tag = (AnchorPane) rootTagContainer.lookup("#tag"+t.id);
                        tagList.getChildren().remove(tag);

                    }
                }
            });
        });

        server.registerForMessages("/topic/tags/rename", Tags.class, t->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(t.board.name.equals(board.name)) {
                        Label tag = ((Label)((AnchorPane) rootTagContainer.lookup("#tag"+t.id)).
                                getChildren().get(0));
                        tag.setText(t.title);
                        ((AnchorPane)tag.getParent())
                                .setStyle("-fx-background-radius: 4; -fx-background-color: " + t.color);
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

    public void addNewTag(Tags t){

        AnchorPane newTag = createNewTag(t);

        ((Label)newTag.getChildren().get(0)).getProperties().put("tag", t);
        this.currentTag = newTag;
        newTag.setId("tag"+t.id);

        tagList.getChildren().add(newTag);
    }

    public AnchorPane createNewTag(Tags tag){
        AnchorPane tagBody = new AnchorPane();
        tagBody.setPrefWidth(125.6);
        tagBody.setPrefHeight(28);

        tagBody.setStyle("-fx-background-radius: 4; -fx-background-color: " + tag.color);

        Label tagTitle = new Label(tag.title);
        tagTitle.setPrefHeight(18.4);
        tagTitle.setPrefWidth(98);
        tagTitle.setLayoutX(4);
        tagTitle.setLayoutY(5);
        tagTitle.setAlignment(Pos.CENTER);
        tagTitle.setStyle("-fx-background-color: #fafafa; -fx-background-radius: 4;");
        tagTitle.setOnMouseClicked(this::tagDetail);

        Button deleteTag = new Button();
        deleteTag.setText("x");
        deleteTag.setStyle("-fx-font-size: 10; -fx-background-color: #ffffff");
        deleteTag.setPadding(new Insets(-2, 5, -1, 5));
        deleteTag.setLayoutX(106);
        deleteTag.setLayoutY(7);
        deleteTag.setOnAction(this::deleteTag);

        tagBody.getChildren().addAll(tagTitle, deleteTag);
        return tagBody;
    }

    @FXML
    public void tagDetail(MouseEvent mouseEvent) {
        Tags t = (Tags) ((Node) mouseEvent.getSource()).getProperties().get("tag");
        mainCtrl.showTagDetail(t);
    }

    @FXML
    public void deleteTag(ActionEvent event) {
        Button deleteCard = (Button) event.getSource();
        Tags t = (Tags) ((Label)((AnchorPane)deleteCard.getParent())
                .getChildren().get(0)).getProperties().get("tag");
        server.removeTag(t);
    }

    @FXML
    void openAddNewTag(ActionEvent event){
        mainCtrl.showAddTag(board);
    }

}
