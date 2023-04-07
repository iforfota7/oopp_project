package client.scenes.config;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Cards;
import commons.Lists;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Shortcuts {

    private AnchorPane currentCard;
    private VBox currentList;
    private HBox currentBoard;
    private MainCtrl mainCtrl;
    private ServerUtils server;

    /**
     * Constructor for Shortcuts class,
     * to be used inside BoardCtrl
     * @param mainCtrl an instance of MainCtrl
     * @param server an instance of ServerUtils
     */
    public Shortcuts(MainCtrl mainCtrl, ServerUtils server) {

        this.mainCtrl = mainCtrl;
        this.server = server;

    }
    /**
     * Constructor for Shortcuts class,
     * to be used inside MainCtrl
     * @param mainCtrl an instance of MainCtrl
     */
    public Shortcuts(MainCtrl mainCtrl) {

        this.mainCtrl = mainCtrl;
    }

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
        currentList = (VBox) hovered.getParent().getParent().getParent();
        currentBoard = (HBox) currentList.getParent();

        mouseEvent.consume();
    }

    /**
     * Chooses one of the shortcuts based on the key pressed
     * @param keyEvent an object containing information about the pressed key
     */
    public void activateShortcut(KeyEvent keyEvent) {

        if(keyEvent.getCode() == KeyCode.H)
            openHelpScene();
        else if (keyEvent.getCode() == KeyCode.UP && keyEvent.isControlDown())
            moveHighlightUp();
        else if (keyEvent.getCode() == KeyCode.DOWN && keyEvent.isControlDown())
            moveHighlightDown();
        else if (keyEvent.getCode() == KeyCode.RIGHT && keyEvent.isControlDown())
            moveHighlightRight();
        else if (keyEvent.getCode() == KeyCode.LEFT && keyEvent.isControlDown())
            moveHighlightLeft();
        else if (keyEvent.getCode() == KeyCode.UP && keyEvent.isShiftDown())
            swapAboveCard();
        else if (keyEvent.getCode() == KeyCode.DOWN && keyEvent.isShiftDown())
            swapBelowCard();
    }

    /**
     * Closes the shortcuts help scene by pressing "H" while
     * it is in focus.
     * @param keyEvent an object containing information about the pressed key
     */
    public void closeHelpScene(KeyEvent keyEvent) {

        if(keyEvent.getCode() == KeyCode.H) {
            mainCtrl.closeSecondaryStage();
        }
        keyEvent.consume();
    }

    /**
     * Opens a help scene showing all available shortcuts if
     * the pressed key was "H"
     */
    public void openHelpScene() {

        mainCtrl.showHelpShortcutsScene();
    }

    /**
     * Moves the highlight to the card above the
     * current one using the UP arrow key
     */
    public void moveHighlightUp() {

        if(currentCard==null) return;

        int positionInsideList = ((Cards) currentCard.getParent()
                .getProperties().get("card")).positionInsideList;

        if(positionInsideList==0) return;

        currentCard.setStyle("");

        //positionInsideList+1 is the above card, because we set the indexes from 2
        currentCard = (AnchorPane) ((AnchorPane) currentCard.getParent().getParent()
                .getChildrenUnmodifiable().get(positionInsideList+1))
                .getChildrenUnmodifiable().get(2);

        currentCard.setStyle(
                "-fx-border-color: red; -fx-border-style:solid");
    }

    /**
     * Moves the highlight to the card below the
     * current one using the DOWN arrow key
     */
    private void moveHighlightDown() {

        if(currentCard==null) return;

        int positionInsideList = ((Cards) currentCard.getParent()
                .getProperties().get("card")).positionInsideList;

        //size()-3 to exclude label and separator, as well as to
        //align size with 0-index
        if(positionInsideList==currentCard.getParent().getParent()
                .getChildrenUnmodifiable().size()-3) return;

        currentCard.setStyle("");

        //positionInsideList+3 is the below card, because we set the indexes from 2
        currentCard = (AnchorPane) ((AnchorPane) currentCard.getParent().getParent()
                .getChildrenUnmodifiable().get(positionInsideList+3))
                .getChildrenUnmodifiable().get(2);

        currentCard.setStyle(
                "-fx-border-color: red; -fx-border-style:solid");
    }

    /**
     * Moves the highlight to the opposing card in the list to
     * the right of the current card using the RIGHT arrow key
     */
    private void moveHighlightRight() {

        if(currentCard==null) return;

        int positionInsideList = ((Cards) currentCard.getParent()
                .getProperties().get("card")).positionInsideList;

        int positionInsideBoard = ((Lists) currentList
                .getProperties().get("list")).positionInsideBoard;

        if (positionInsideBoard == currentBoard
                .getChildrenUnmodifiable().size()-1) return;

        int sizeOfAdjacentList = ((VBox) ((VBox) currentBoard
                .getChildrenUnmodifiable().get(positionInsideBoard+1))
                .getChildrenUnmodifiable().get(0))
                .getChildrenUnmodifiable().size();

        if(sizeOfAdjacentList==2) return;

        currentCard.setStyle("");

        if(positionInsideList >= sizeOfAdjacentList-3) {

            currentCard = (AnchorPane) ((AnchorPane)
                    ((VBox) ((VBox) currentBoard
                            .getChildrenUnmodifiable().get(positionInsideBoard + 1))
                            .getChildrenUnmodifiable().get(0))
                            .getChildrenUnmodifiable().get(sizeOfAdjacentList-1))
                    .getChildrenUnmodifiable().get(2);
        }
        else {

            currentCard = (AnchorPane) ((AnchorPane)
                    ((VBox) ((VBox) currentBoard
                            .getChildrenUnmodifiable().get(positionInsideBoard + 1))
                            .getChildrenUnmodifiable().get(0))
                            .getChildrenUnmodifiable().get(positionInsideList+2))
                    .getChildrenUnmodifiable().get(2);
        }

        currentList = (VBox) currentCard.getParent().getParent().getParent();
        currentBoard = (HBox) currentList.getParent();

        currentCard.setStyle(
                "-fx-border-color: red; -fx-border-style:solid");
    }

    /**
     * Moves the highlight to the opposing card in the list to
     * the left of the current card using the LEFT arrow key
     */
    private void moveHighlightLeft() {

        if(currentCard==null) return;

        int positionInsideList = ((Cards) currentCard.getParent()
                .getProperties().get("card")).positionInsideList;

        int positionInsideBoard = ((Lists) currentList
                .getProperties().get("list")).positionInsideBoard;

        if (positionInsideBoard == 0) return;

        int sizeOfAdjacentList = ((VBox) ((VBox) currentBoard
                .getChildrenUnmodifiable().get(positionInsideBoard-1))
                .getChildrenUnmodifiable().get(0))
                .getChildrenUnmodifiable().size();

        if(sizeOfAdjacentList==2) return;

        currentCard.setStyle("");

        if(positionInsideList >= sizeOfAdjacentList-3) {

            currentCard = (AnchorPane) ((AnchorPane)
                    ((VBox) ((VBox) currentBoard
                            .getChildrenUnmodifiable().get(positionInsideBoard - 1))
                            .getChildrenUnmodifiable().get(0))
                            .getChildrenUnmodifiable().get(sizeOfAdjacentList-1))
                    .getChildrenUnmodifiable().get(2);
        }
        else {

            currentCard = (AnchorPane) ((AnchorPane)
                    ((VBox) ((VBox) currentBoard
                            .getChildrenUnmodifiable().get(positionInsideBoard - 1))
                            .getChildrenUnmodifiable().get(0))
                            .getChildrenUnmodifiable().get(positionInsideList+2))
                    .getChildrenUnmodifiable().get(2);
        }

        currentList = (VBox) currentCard.getParent().getParent().getParent();
        currentBoard = (HBox) currentList.getParent();

        currentCard.setStyle(
                "-fx-border-color: red; -fx-border-style:solid");
    }

    /**
     * swaps up
     */
    private void swapAboveCard() {

        if(currentCard==null) return;

        int positionInsideList = ((Cards) currentCard.getParent()
                .getProperties().get("card")).positionInsideList;

        if(positionInsideList==0) return;

        Cards aboveCard = ((Cards)
                ((VBox) currentList.getChildrenUnmodifiable()
                .get(0)).getChildren().get(positionInsideList+1)
                .getProperties().get("card"));

        aboveCard.positionInsideList = ((Cards) currentCard.getParent()
                .getProperties().get("card")).positionInsideList;
//
//        ((Cards) currentCard.getParent()
//                .getProperties().get("card")).positionInsideList-=1;

        server.moveCard(aboveCard);
    }

    /**
     * swaps down
     */
    private void swapBelowCard() {

        if(currentCard==null) return;

        int positionInsideList = ((Cards) currentCard.getParent()
                .getProperties().get("card")).positionInsideList;

        if(positionInsideList==currentCard.getParent().getParent()
                .getChildrenUnmodifiable().size()-3) return;

        Cards belowCard = ((Cards)
                ((VBox) currentList.getChildrenUnmodifiable()
                        .get(0)).getChildren().get(positionInsideList+3)
                        .getProperties().get("card"));

        belowCard.positionInsideList = ((Cards) currentCard.getParent()
                .getProperties().get("card")).positionInsideList;
//
//        ((Cards) currentCard.getParent()
//                .getProperties().get("card")).positionInsideList+=1;

        server.moveCard(belowCard);
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
