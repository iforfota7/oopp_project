package client.scenes.config;

import client.scenes.BoardCtrl;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Boards;
import commons.Cards;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Shortcuts {

    private AnchorPane currentCard;
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
     */
    public Shortcuts(MainCtrl mainCtrl, ServerUtils server, BoardCtrl boardCtrl) {

        this.mainCtrl = mainCtrl;
        this.server = server;
        this.boardCtrl = boardCtrl;

        board = boardCtrl.getBoard();
        currentBoard = boardCtrl.getFirstRow();
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

        x = ((Cards) currentCard.getParent().getProperties().get("card")).positionInsideList;
        y = board.lists.get(x).positionInsideBoard;

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
            highlightCardAtPosition(x, y);
        }
        else if (keyEvent.getCode() == KeyCode.DOWN && keyEvent.isControlDown()) {
            calculateNewHighlightPosition(x, y+1);
            highlightCardAtPosition(x, y);
        }
        else if (keyEvent.getCode() == KeyCode.LEFT && keyEvent.isControlDown()) {
            calculateNewHighlightPosition(x-1, y);
            highlightCardAtPosition(x, y);
        }
        else if (keyEvent.getCode() == KeyCode.RIGHT && keyEvent.isControlDown()) {
            calculateNewHighlightPosition(x+1, y);
            highlightCardAtPosition(x, y);
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

        if(currentCard==null) return;

        int sizeOfCurrentList = board.lists.get(this.x).cards.size();

        int sizeOfCurrentBoard = board.lists.size();

        if(y==-1 || y==sizeOfCurrentList) return;
        if(x==-1 || x==sizeOfCurrentBoard) return;

        if(x!=this.x) {

            //-2 to exclude label and separator
            int sizeOfAdjacentList = board.lists.get(x).cards.size();

            if(sizeOfAdjacentList==0) return;

            if(y>=sizeOfAdjacentList-1) {
                this.y = sizeOfAdjacentList - 1;
                this.x = x;
                return;
            }
        }

        //if in the same list, or at same position in other list
        this.y=y; this.x=x;
    }

    /**
     * Highlights the card at the given (x,y) position
     * @param x position of list inside board
     * @param y position of card inside list
     */
    private void highlightCardAtPosition(int x, int y) {

        if(currentCard==null) return;

        currentCard.setStyle("");

        currentCard = (AnchorPane) ((AnchorPane) ((VBox) ((VBox) currentBoard.getChildren().get(x))
                .getChildren().get(0)).getChildren().get(y+2)).getChildren().get(2);

        currentBoard = boardCtrl.getFirstRow();

        currentCard.setStyle("-fx-border-color: red; -fx-border-style:solid");
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
        System.out.println(((Cards)currentCard.getParent().getProperties().get("card")).title);

        //-2 to exclude label and separator
        int sizeOfCurrentList = board.lists.get(this.x).cards.size();

        if(y==-1 || y==sizeOfCurrentList) return;

        Cards adjacentCard = board.lists.get(this.x).cards.get(y);

        adjacentCard.positionInsideList = this.y;

        server.moveCard(adjacentCard);
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
