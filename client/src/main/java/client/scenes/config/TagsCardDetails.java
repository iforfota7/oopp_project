package client.scenes.config;

import client.scenes.CardDetailsCtrl;
import commons.Cards;
import commons.Tags;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class TagsCardDetails {

    private GridPane tagList;
    private Cards openedCard;
    private CardDetailsCtrl cardDetailsCtrl;

    /**
     * Creates a new instance of the TagsCardDetails class
     *
     * @param tagList the grid pane that contains the tags in card details
     * @param cardDetailsCtrl reference to the card details controller
     */
    public TagsCardDetails(GridPane tagList, CardDetailsCtrl cardDetailsCtrl) {
        this.tagList = tagList;
        this.cardDetailsCtrl = cardDetailsCtrl;
    }

    /**
     * Method used for rendering all tags
     *
     * @param card Reference to the card object that contains
     *             the list of tags to be rendered
     */
    public void renderTags(Cards card) {
        this.openedCard = card;

        if(card.tags != null)
            for(int i=0; i<card.tags.size(); i++)
                addNewTag(card.tags.get(i), i + 1);
    }

    /**
     * Method for creating the necessary fxml containers
     * for tags
     *
     * @param t The tag object associated with the container
     * @param position The position at which it will be rendered
     */
    public void addNewTag(Tags t, int position){

        HBox newTag = createNewTag(t);
        newTag.getProperties().put("tag", t);

        Pair<Integer, Integer> positionInGridPane =
                positionInGridPane(position, tagList.getColumnCount());

        tagList.add(newTag, positionInGridPane.getKey(), positionInGridPane.getValue());
    }

    /**
     * Based on the position in the list of elements and the number of columns
     * that the grid pane has, it computes the position of the newly
     * inserted element
     *
     * @param position The position in the list
     * @param columns The number of column the grid pane has
     * @return Pair containing the column and row
     *          at which the element will be rendered
     */
    public Pair<Integer, Integer> positionInGridPane(int position, int columns) {
        Integer row = position / columns;
        Integer column = position % columns;

        return new Pair<>(column, row);
    }

    /**
     * Creates the representation of a new tag in frontend
     * @param tag the tag to be created a representation to
     * @return an HBox, the body of the tag
     */
    public HBox createNewTag(Tags tag){
        HBox tagBody = new HBox(5);

        tagBody.setStyle("-fx-background-radius: 4; -fx-background-color: " + tag.backgroundColor);
        tagBody.setAlignment(Pos.CENTER);

        Label tagTitle = new Label(tag.title);
        tagTitle.setTextFill(Color.web(tag.fontColor));
        tagTitle.setPrefHeight(11);
        tagTitle.setPrefWidth(70);

        tagTitle.setAlignment(Pos.CENTER);
        tagTitle.setStyle("-fx-font-size: 10; -fx-background-color: #fafafa; " +
                "-fx-background-radius: 4;");

        Button deleteTag = new Button();
        deleteTag.setText("x");
        deleteTag.setStyle("-fx-font-size: 10; -fx-background-color: #ffffff");
        deleteTag.setPadding(new Insets(-2, 5, -2, 5));
        deleteTag.setOnAction(this::removeTagFromCard);
        deleteTag.getProperties().put("tag", tag);

        tagBody.getChildren().addAll(tagTitle, deleteTag);

        GridPane.setMargin(tagBody, new Insets(2, 2, 2, 2));
        return tagBody;
    }

    private void removeTagFromCard(ActionEvent actionEvent) {
        Tags removedTag = (Tags)((Node)actionEvent.getTarget()).getProperties().get("tag");
        if(openedCard.tags != null)
            openedCard.tags.remove(removedTag);

        cardDetailsCtrl.refreshOpenedCard();
    }


}
