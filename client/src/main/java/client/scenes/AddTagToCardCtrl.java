package client.scenes;

import com.google.inject.Inject;
import commons.Boards;
import commons.Cards;
import commons.Tags;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class AddTagToCardCtrl {

    private final MainCtrl mainCtrl;
    @FXML
    private VBox tagList;

    /**
     * Creates an instance of AddTagToCardCtrl
     *
     * @param mainCtrl Used for navigating through different scenes
     */
    @Inject
    public AddTagToCardCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * The scene is closed by pressing on the 'close' button
     *
     */
    public void close() {
        mainCtrl.closeThirdStage();
    }

    /**
     * Initializes the scene by rendering tags
     *
     * @param openedCard Reference to the card object
     * @param board Reference to the board object
     */
    public void init(Cards openedCard, Boards board) {
        tagList.getChildren().clear();

        List<Tags> renderedTags = renderedTags(openedCard, board);
        for(Tags tag : renderedTags)
            addNewTag(tag);
    }

    /**
     * Method for choosing which tags to render
     * The only tags that are rendered are the ones that the
     * card doesn't contain yet
     *
     * @param openedCard Reference to the card object
     * @param board Reference to the board object
     * @return The list of rendered tags
     */
    public List<Tags> renderedTags(Cards openedCard, Boards board) {
        if(board.tags == null)
            return new ArrayList<>();

        if(openedCard.tags == null)
            return board.tags;

        List<Tags> renderedTags = new ArrayList<>();
        for(Tags tag : board.tags)
            if(!openedCard.tags.contains(tag))
                renderedTags.add(tag);
        return renderedTags;
    }

    /**
     * Adds a new tag to the list of tags
     * @param t the tag to be added to the list
     */
    public void addNewTag(Tags t){

        AnchorPane newTag = createNewTag(t);

        ((Label)newTag.getChildren().get(0)).getProperties().put("tag", t);
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
        AnchorPane.setTopAnchor(tagTitle, 5.0);
        AnchorPane.setRightAnchor(tagTitle, 5.0);
        AnchorPane.setBottomAnchor(tagTitle, 5.0);
        AnchorPane.setLeftAnchor(tagTitle, 5.0);
//        tagTitle.setLayoutX(4);
//        tagTitle.setLayoutY(5);
        tagTitle.setAlignment(Pos.CENTER);
        tagTitle.setStyle("-fx-background-color: #fafafa; -fx-background-radius: 4;");

        tagBody.getChildren().addAll(tagTitle);
        return tagBody;
    }

}
