package client.scenes.config;

import client.scenes.BoardCtrl;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Boards;
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
    private Cards currentCardObject;
    private Boards board;
    private HBox currentBoard;
    private MainCtrl mainCtrl;
    private ServerUtils server;
    private BoardCtrl boardCtrl;
    private int x; //position of list inside board
    private int y; //position of card inside list

    /**
     * Constructor for Shortcuts class,
     * to be used inside BoardCtrl
     * @param mainCtrl an instance of MainCtrl
     * @param server an instance of ServerUtils
     * @param boardCtrl an instance of BoardCtrl
     * @param currentCardObject the current highlighted object
     */
    public Shortcuts(MainCtrl mainCtrl, ServerUtils server, BoardCtrl boardCtrl,
                     Cards currentCardObject) {

        this.mainCtrl = mainCtrl;
        this.server = server;
        this.boardCtrl = boardCtrl;
        this.currentCardObject = currentCardObject;

        board = boardCtrl.getBoard();
        currentBoard = boardCtrl.getFirstRow();

        setPositionForCard();
    }

    /**
     * Getter for the card object
     *
     * @return The card object
     */
    public Cards getCurrentCardObject() {
        return currentCardObject;
    }

    /**
     * Given the container, it gets the card
     *
     * @return the card object that is stored in the
     *          highlighted container
     */
    private Cards getCardFromContainer() {
        return (Cards)currentCard.getProperties().get("card");
    }

    /**
     * Sets the border style for the highlighted
     * anchor pane
     *
     */
    public void highlightCurrentCard() {
        if(currentCard == null)
            return;

        currentCard.setStyle(
                "-fx-border-color: red; -fx-border-style:solid");
    }

    /**
     * Computes the x, y positions of a card
     *
     */
    public void setPositionForCard() {
        if(currentCardObject == null)
            return;

        if(!existsCardInBoard()) {
            currentCardObject = null;
            return;
        }

        y = currentCardObject.positionInsideList;
        this.x = 0;
        for(Lists list : board.lists) {
            if(list.id == currentCardObject.list.id) {
                break;
            }
            this.x++;
        }
    }

    /**
     * Checks if the current card exists in the board
     *
     * @return True iff the current card exists
     */
    public boolean existsCardInBoard() {
        for(Lists lists : board.lists)
            for(Cards cards : lists.cards)
                if(cards.id == currentCardObject.id)
                    return true;
        return false;
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

        currentCard = hovered;
        highlightCurrentCard();
        currentCardObject = getCardFromContainer();
        setPositionForCard();

        mouseEvent.consume();
    }

    /**
     * Chooses one of the shortcuts based on the key pressed
     * @param keyEvent an object containing information about the pressed key
     */
    public void activateShortcut(KeyEvent keyEvent) {

        if(keyEvent.getCode() == KeyCode.H)
            openHelpScene();

        else if (keyEvent.getCode() == KeyCode.UP && keyEvent.isControlDown()) {
            calculateNewHighlightPosition(x, y-1);
            highlightCardAtPosition();
        }
        else if (keyEvent.getCode() == KeyCode.DOWN && keyEvent.isControlDown()) {
            calculateNewHighlightPosition(x, y+1);
            highlightCardAtPosition();
        }
        else if (keyEvent.getCode() == KeyCode.LEFT && keyEvent.isControlDown()) {
            calculateNewHighlightPosition(x-1, y);
            highlightCardAtPosition();
        }
        else if (keyEvent.getCode() == KeyCode.RIGHT && keyEvent.isControlDown()) {
            calculateNewHighlightPosition(x+1, y);
            highlightCardAtPosition();
        }

        else if (keyEvent.getCode() == KeyCode.UP && keyEvent.isShiftDown())
            swapCard(y-1);
        else if (keyEvent.getCode() == KeyCode.DOWN && keyEvent.isShiftDown())
            swapCard(y+1);
    }

    /**
     * Calculates new coordinates of the highlight
     * @param x new position of list inside board
     * @param y new position of card inside list
     */
    private void calculateNewHighlightPosition(int x, int y) {

        if(currentCardObject==null) return;

        int sizeOfCurrentList = board.lists.get(this.x).cards.size();
        int sizeOfCurrentBoard = board.lists.size();
        if(y==-1 || y==sizeOfCurrentList) return;
        if(x==-1 || x==sizeOfCurrentBoard) return;

        if(x!=this.x) {

            int dir = x < this.x ? -1 : 1;

            boolean moved = false;
            while(x >= 0 && x < board.lists.size()) {
                int newListSize = board.lists.get(x).cards.size();
                if(newListSize == 0) {
                    x += dir;
                    continue;
                }
                if(y >= newListSize)
                    y = newListSize - 1;

                moved = true;
                break;
            }

            if(moved) {
                this.x = x;
                this.y = y;
                return;
            }

            return;
        }

        this.y=y;

    }

    /**
     * Highlights the card at the given (x,y) position
     */
    private void highlightCardAtPosition() {

        if(currentCard==null) return;

        currentCard.setStyle("");

        currentCard = (AnchorPane) ((AnchorPane) ((VBox) ((VBox) currentBoard.getChildren().get(x))
                .getChildren().get(0)).getChildren().get(y+2)).getChildren().get(2);

        currentCard.setStyle("-fx-border-color: red; -fx-border-style:solid");
        currentCardObject = getCardFromContainer();
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
     * Swaps the current card with the one below or above based on y
     * @param y position of card inside list
     */
    private void swapCard(int y) {

        if(currentCard==null) return;

        int sizeOfCurrentList = board.lists.get(this.x).cards.size();
        if(y==-1 || y==sizeOfCurrentList) return;

        currentCardObject.positionInsideList = y;
        currentCardObject = server.moveCard(currentCardObject);
    }

    /**
     * Gets the current card fxml element
     * @return the current card fxml element
     */
    public AnchorPane getCurrentCard() {
        return currentCard;
    }

    /**
     * Setter for the currently highlighted card container
     * @param currentCard the currently highlighted card container
     */
    public void setCurrentCard(AnchorPane currentCard) {
        this.currentCard = currentCard;
    }

    /**
     * Setter for the currently highlighted card
     * @param cards the currently highlighted card
     */
    public void setCurrentCardObject(Cards cards) {
        this.currentCardObject = cards;
    }
}
