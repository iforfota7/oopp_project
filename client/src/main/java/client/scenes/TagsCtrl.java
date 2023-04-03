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

    /**
     * The method adds the tags into an arrayList, and renders them
     * afterwards into the list of tags
     * @param board the board to which the tags belong to
     */
    public void initialize(Boards board){
        this.board = board;
        tagList.getChildren().clear();
        List<Tags> tags = server.getTagsByBoard(board.id);
        for(int i = 0; i < tags.size(); i++)
            addNewTag(tags.get(i));
    }

    /**
     * Initialises the websockets for the tags
     */
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

    /**
     * Constructor of the TagsCtrl class
     * @param mainCtrl
     * @param server
     */
    @Inject
    public TagsCtrl(MainCtrl mainCtrl, ServerUtils server){
        this.server = server;
        this.mainCtrl = mainCtrl;
        webSocketTags();

    }

    /**
     * Adds a new tag to the list of tags
     * @param t the tag to be added to the list
     */
    public void addNewTag(Tags t){

        AnchorPane newTag = createNewTag(t);

        ((Label)newTag.getChildren().get(0)).getProperties().put("tag", t);
        this.currentTag = newTag;
        newTag.setId("tag"+t.id);

        tagList.getChildren().add(newTag);
    }

    /**
     * Creates the representation of a new tag in frontend
     * @param tag the tag to be created a representation to
     * @return an anchorPane, the body of the tag
     */
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

    /**
     * Opens the tag's detail scene
     * @param mouseEvent
     */
    @FXML
    public void tagDetail(MouseEvent mouseEvent) {
        Tags t = (Tags) ((Node) mouseEvent.getSource()).getProperties().get("tag");
        mainCtrl.showTagDetail(t);
    }

    /**
     * Deletes a tag from the tag list
     * @param event the event that triggered the deletion of the tag
     */
    @FXML
    public void deleteTag(ActionEvent event) {
        Button deleteCard = (Button) event.getSource();
        Tags t = (Tags) ((Label)((AnchorPane)deleteCard.getParent())
                .getChildren().get(0)).getProperties().get("tag");
        server.removeTag(t);
    }

    /**
     * Opens the scene for adding a new tag
     * @param event the event that triggers the creation of a tag
     */
    @FXML
    void openAddNewTag(ActionEvent event){
        mainCtrl.showAddTag(board);
    }

    /**
     * Closes the tag list scene
     */
    @FXML
    void close()  {
        mainCtrl.closeTags();
    }
}
