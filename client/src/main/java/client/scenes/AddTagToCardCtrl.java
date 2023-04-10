package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Boards;
import commons.Cards;
import commons.Lists;
import commons.Tags;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class AddTagToCardCtrl {

    private final MainCtrl mainCtrl;
    @FXML
    private VBox tagList;
    @FXML
    private Text tagLimitText;
    private List<Tags> selectedTags;
    private Cards openedCard;
    private Boards board;
    private CardDetailsCtrl cardDetailsCtrl;
    private final ServerUtils server;
    private final List<String> serverURLS;
    private boolean shortcutActivated;

    /**
     * Creates an instance of AddTagToCardCtrl
     *
     * @param mainCtrl Used for navigating through different scenes
     * @param server Used for configuring websockets
     */
    @Inject
    public AddTagToCardCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        serverURLS = new ArrayList<>();
        shortcutActivated = false;
    }

    /**
     * The scene is closed by pressing on the 'close' button
     *
     */
    public void close() {
        shortcutActivated = false;
        mainCtrl.closeThirdStage();
    }

    /**
     * Method used for adding all selected tags
     * to the list of tags of the opened card
     * If the number of selected tags is above 10,
     * clicking this button has no effect
     *
     */
    public void addTags() {
        int tagCount = openedCard.tags.size() + selectedTags.size();
        if(tagCount > 10)
            return;

        openedCard.tags.addAll(selectedTags);

        if(shortcutActivated) {
            Lists blankList = new Lists(null, 0, null);
            blankList.id = openedCard.list.id;
            openedCard.list = blankList;

            server.renameCard(openedCard);
        }
        else
            cardDetailsCtrl.refreshOpenedCard();

        shortcutActivated = false;
        mainCtrl.closeThirdStage();
    }

    /**
     * Initializes the scene by rendering tags
     *
     * @param openedCard Reference to the card object
     * @param board Reference to the board object
     * @param cardDetailsCtrl Reference to the card details scene
     *                        controller
     */
    public void init(Cards openedCard, Boards board, CardDetailsCtrl cardDetailsCtrl) {
        this.openedCard = openedCard;
        this.board = board;
        tagList.getChildren().clear();
        selectedTags = new ArrayList<>();
        this.cardDetailsCtrl = cardDetailsCtrl;

        if(!serverURLS.contains(server.getServer())) {
            serverURLS.add(server.getServer());
            websocketConfig();
        }

        updateTagLimitText();
        List<Tags> renderedTags = renderedTags(openedCard, board);
        for(Tags tag : renderedTags)
            addNewTag(tag);
    }

    /**
     * The add tag to card scene should reload when
     * changes occur in the tag management scene
     * or when the tags for cards are modified
     *
     */
    public void websocketConfig() {
        server.registerForMessages("/topic/boards/update", Boards.class, b->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(board != null && board.id == b.id)
                        init(openedCard, b, cardDetailsCtrl);
                }
            });
        });

        server.registerForMessages("/topic/cards/rename", Cards.class, c->{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(openedCard != null && c.id == openedCard.id)
                        init(c, board, cardDetailsCtrl);
                }
            });
        });
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

        tagBody.setStyle("-fx-background-radius: 4; -fx-background-color: " + tag.backgroundColor);

        Label tagTitle = new Label(tag.title);
        tagTitle.setOnMouseClicked(this::selectDeselectTags);
        tagTitle.setTextFill(Color.web(tag.fontColor));
        tagTitle.setPrefHeight(18.4);
        tagTitle.setPrefWidth(98);
        AnchorPane.setTopAnchor(tagTitle, 5.0);
        AnchorPane.setRightAnchor(tagTitle, 5.0);
        AnchorPane.setBottomAnchor(tagTitle, 5.0);
        AnchorPane.setLeftAnchor(tagTitle, 5.0);

        tagTitle.setAlignment(Pos.CENTER);
        tagTitle.setStyle("-fx-background-color: #fafafa; -fx-background-radius: 4;");

        tagBody.getChildren().addAll(tagTitle);
        return tagBody;
    }

    /**
     * Method used for highlighting/un-highlighting
     * the selected tags
     * The body of the tags will be colored with the color
     * of their border
     *
     * @param mouseEvent Object containing information
     *                   about the mouse event
     */
    public void selectDeselectTags(MouseEvent mouseEvent) {
        Label tagContainer = (Label)mouseEvent.getSource();
        Tags clickedTag = (Tags)tagContainer.getProperties().get("tag");

        // select a tag if it isn't selected
        if(!selectedTags.contains(clickedTag)) {
            String tagColor = clickedTag.backgroundColor;
            tagContainer.setStyle("-fx-background-color: "+
                    tagColor + "; -fx-background-radius: 4;");
            selectedTags.add(clickedTag);
        }

        // deselect a tag if it is already selected
        else {
            tagContainer.setStyle("-fx-background-color: #fafafa; " +
                    "-fx-background-radius: 4;");
            selectedTags.remove(clickedTag);
        }

        updateTagLimitText();
    }

    /**
     * Updates the text which lets the user know how many tags
     * they have selected
     * If this number is above 10, the text becomes red
     * allowing the user to understand that they have to deselect
     * some tags
     *
     */
    public void updateTagLimitText() {
        int tagCount = openedCard.tags.size() + selectedTags.size();
        tagLimitText.setText(setTagLimitText(tagCount));

        if(tagCount <= 10)
            tagLimitText.setFill(Color.BLACK);
        else
            tagLimitText.setFill(Color.RED);
    }

    /**
     * Creates the tag limit text using the amount of tags
     * the user has selected for the card
     *
     * @param tagCount The amount of tags the user selected
     * @return The computed String object
     */
    public String setTagLimitText(int tagCount) {
        return "You have selected " + tagCount + " out of 10 tags";
    }

    /**
     * Setter for the shortcut activate attribute
     *
     * @param shortcutActivated The new value of the attribute
     */
    public void setShortcutActivated(boolean shortcutActivated) {
        this.shortcutActivated = shortcutActivated;
    }

}
