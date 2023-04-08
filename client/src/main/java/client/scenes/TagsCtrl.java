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
import java.util.ArrayList;
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
    private List<String> stringURLS;

    /**
     * The method adds the tags into an arrayList, and renders them
     * afterwards into the list of tags
     * @param board the board to which the tags belong to
     */
    public void initialize(Boards board){
        if(!stringURLS.contains(server.getServer())) {
            stringURLS.add(server.getServer());
            webSocketTags();
        }

        this.board = board;
        tagList.getChildren().clear();
        for(Tags tags : board.tags)
            addNewTag(tags);
    }

    /**
     * Initialises the websockets for the tags
     */
    private void webSocketTags() {
        server.registerForMessages("/topic/boards/update", Boards.class, board -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    initialize(board);
                }
            });
        });

    }

    /**
     * Constructor of the TagsCtrl class
     * @param mainCtrl Used for navigating between scenes
     * @param server Used for sending requests to the backend
     */
    @Inject
    public TagsCtrl(MainCtrl mainCtrl, ServerUtils server){
        this.server = server;
        this.mainCtrl = mainCtrl;
        stringURLS = new ArrayList<>();
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

        tagBody.setStyle("-fx-background-radius: 4; -fx-background-color: " + tag.backgroundColor);

        Label tagTitle = new Label(tag.title);
        tagTitle.setPrefHeight(18.4);
        tagTitle.setPrefWidth(98);
        tagTitle.setLayoutX(4);
        tagTitle.setLayoutY(5);
        tagTitle.setAlignment(Pos.CENTER);
        tagTitle.setTextFill(Color.web(tag.fontColor));
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
     * @param mouseEvent Object storing information about the mouse event
     */
    @FXML
    public void tagDetail(MouseEvent mouseEvent) {
        Tags t = (Tags) ((Node) mouseEvent.getSource()).getProperties().get("tag");
        mainCtrl.showTagDetail(t, board);
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
        board.tags.remove(t);

        server.removeTagFromCards(t);
        server.updateBoard(board);
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
        mainCtrl.closeSecondaryStage();
    }

    /**
     * Setter for the board attribute of the controller
     *
     * @param board The new board object
     */
    public void setBoard(Boards board) {
        this.board = board;
    }
}
