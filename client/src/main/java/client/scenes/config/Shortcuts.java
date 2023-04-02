package client.scenes.config;

import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;

public class Shortcuts {

    private Hyperlink currentCard;

    /**
     * Whenever the user hovers above a card using their mouse,
     * it will be highlighted iff the list of that card doesn't have
     * a highlighted card, and the previously highlighted card (if any)
     * will be un-highlighted.
     * @param mouseEvent Object containing information about the mouse event
     */
    public void onMouseHover(MouseEvent mouseEvent) {

        Hyperlink hovered = (Hyperlink) mouseEvent.getSource();
        if(currentCard==null ||
                ((Hyperlink) mouseEvent.getSource()).getParent()
                        .getParent()!=currentCard.getParent().getParent()) {

            if(currentCard!=null)
                currentCard.setStyle("-fx-background-color:  #E6E6FA;");

            hovered.setStyle("-fx-background-color:  #E6E6FA; " +
                    "-fx-border-color: red; -fx-border-style:solid");
            currentCard = hovered;
        }

        mouseEvent.consume();
    }
}
