package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import commons.Boards;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class BoardOverviewCtrl{

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private List<Boards> boardsList;
    private int numberOfBoards = 3;
    private int positionInColumn;

    @FXML
    GridPane gridPane;

    /**
     * Creates a list of boards holding all labels
     * Initializes the onMouseClicked event for these labels
     *
     */
    public void init() {
        boardsList = new ArrayList<>();

        refresh();
    }

    /**
     * Constructor for the BoardOverviewCtrl
     *
     * @param mainCtrl Used for navigating through the scenes
     * @param server Used for sending request to the server
     */
    @Inject
    public BoardOverviewCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Go to a specific board when a board label has been clicked
     *
     * @param event Object that contains information about the mouse event
     */
    public void goToBoard(MouseEvent event) {
        System.out.println((Boards)((Label)event.getSource()).getProperties().get("board"));
        Main.setSceneToBoard((Boards)((Label)event.getSource()).getProperties().get("board"));
    }

    /**
     * Opens the joinBoardByID scene.
     */
    public void joinBoardByID() { mainCtrl.showJoinBoardByID(); }

    /**
     * When the user clicks the button, they are sent back
     * to the Board Overview scene
     *
     */
    public void disconnect() {
        mainCtrl.showSelectServer();
    }

    @FXML
    public void addBoard(){
        mainCtrl.showAddBoard();
    }

    /**
     * Renders a new Board in the overview
     *
     * @param b The board object to be displayed
     */

    public void addNewBoard(Boards b){
        numberOfBoards++;
        positionInColumn = (numberOfBoards - 1) % 3;
        int row = (numberOfBoards - 1) / 3;

        Label newBoard = createNewBoard(b);
        newBoard.setAccessibleRole(AccessibleRole.TEXT);

        gridPane.add(newBoard, positionInColumn, row);
        gridPane.setMargin(gridPane.getChildren().get(numberOfBoards - 1),
                new Insets(10, 10 , 10 ,10));
    }

    /**
     * Creates the board element in FXML
     *
     * @param b The title of the board
     * @return The Label controller that will be displayed
     */

    public Label createNewBoard(Boards b) {
        Label newBoard = new Label(b.name);
        newBoard.setStyle("-fx-background-color: #ffffff; -fx-text-fill:  #0d0d0d; " +
                "-fx-border-color: #8d78a6; -fx-border-radius: 3px; -fx-text-fill: #000000;" +
                "-fx-z-index: 999;");
        newBoard.setPrefWidth(165);
        newBoard.setPrefHeight(75);
        newBoard.setAlignment(Pos.CENTER);
        newBoard.setText(b.name);
        newBoard.getProperties().put("board", b);
        newBoard.setFont(new Font(15));
        newBoard.setOnMouseClicked(this::goToBoard);
        return newBoard;
    }

    /**
     * Refreshes the Board Overview, by fetching the Boards
     * from the database
     *
     */

    public void refresh(){
        gridPane.getChildren().clear();
        boardsList = server.getBoards();
        numberOfBoards = 0;
        for (Boards boards : boardsList) {
            addNewBoard(boards);
        }
    }

}
