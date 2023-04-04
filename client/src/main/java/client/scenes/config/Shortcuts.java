package client.scenes.config;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Shortcuts {

    private AnchorPane currentCard;

    /**
     * Whenever the user hovers above a card using their mouse,
     * it will be highlighted, and the previously highlighted card (if any)
     * will be un-highlighted.
     * @param mouseEvent Object containing information about the mouse event
     */
    public void onMouseHover(MouseEvent mouseEvent) {

        AnchorPane hovered = (AnchorPane) mouseEvent.getSource();

        if(currentCard!=null)
            currentCard.setStyle("");

        hovered.setStyle(
                "-fx-border-color: red; -fx-border-style:solid");
        currentCard = hovered;

        mouseEvent.consume();
    }

    /**
     * Getter for the currently highlighted card
     * @return the currently highlighted card
     */
    public AnchorPane getCurrentCard() {
        return currentCard;
    }

    /**
     * Setter for the currently highlighted card
     * @param currentCard the currently highlighted card
     */
    public void setCurrentCard(AnchorPane currentCard) {
        this.currentCard = currentCard;
    }
}
