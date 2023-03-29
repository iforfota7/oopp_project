package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import commons.Boards;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class BoardOverviewCtrl{
    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private SelectServerCtrl selectServerCtrl;
    private List<Boards> boardsList;
    private int numberOfBoards = 0;

    /**
     * Constructor for the BoardOverviewCtrl
     *
     * @param mainCtrl         Used for navigating through the scenes
     * @param server           Used for sending request to the server
     * @param selectServerCtrl Used for sending request to the serverServerCtrl
     */

    @Inject
    public BoardOverviewCtrl(MainCtrl mainCtrl, ServerUtils server,
                             SelectServerCtrl selectServerCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.selectServerCtrl = selectServerCtrl;
    }
    @FXML
    GridPane gridPane;

    @FXML
    private Label adminLabel;
    @FXML
    private Label userLabel;


    private BooleanProperty adminLock = new SimpleBooleanProperty(false);

    public boolean getAdminLock() {
        adminLock.set(server.checkAdmin(selectServerCtrl.getCurrentUser()));
        return adminLock.get();
    }

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
     * Go to a specific board when a board label has been clicked
     *
     * @param event Object that contains information about the mouse event
     */
    public void goToBoard(MouseEvent event) {

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
        int positionInColumn = (numberOfBoards - 1) % 3;
        int row = (numberOfBoards - 1) / 3;


        StackPane newBoard = createNewBoard(b);
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

    public StackPane createNewBoard(Boards b) {
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

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(newBoard);
        stackPane.getProperties().put("board", b);
        Button removeBoardButton = new Button("X");
        removeBoardButton.setStyle("-fx-background-color: #f08080;" +
                " -fx-text-fill: #ffffff; -fx-padding: 2px 6px; -fx-font-size: 10px");
        removeBoardButton.setOnMouseClicked(this::removeBoard);
        removeBoardButton.setUserData(b.name);
        removeBoardButton.setVisible(adminLock.get());
        stackPane.getChildren().add(removeBoardButton);
        StackPane.setAlignment(removeBoardButton, Pos.TOP_RIGHT);
        return stackPane;
    }
    /**
     *The functionality of the delete current board button will be displayed
     * after obtaining admin privileges.
     *  It returns to the board overview interface and deletes the current board.
     */
    private void removeBoard(MouseEvent mouseEvent) {
        Button removeButton = (Button) mouseEvent.getSource();
        Boards board = (Boards) removeButton.getParent().getProperties().get("board");
        server.removeBoard(board);
        refresh();
    }

    /**
     * Refreshes the Board Overview, by fetching the Boards
     * from the database
     */
    public void refresh(){
        gridPane.getChildren().clear();
        boardsList = server.getBoards();
        numberOfBoards = 0;
        for (Boards boards : boardsList) {
            addNewBoard(boards);
        }
    }
    /**
     * Opens a new window with userDetails scene
     */
    @FXML
    public void showUserDetails(){
        mainCtrl.showUserDetails(selectServerCtrl.getCurrentUser());
    }

    /**
     * Show admin-specific buttons and features based on the user's admin permissions.
     * Unveiled hidden delete buttons.
     */
    public void openAdminFeatures() {
        adminLock.set(true);
        mainCtrl.closeConfirmAdmin();
        adminLabel.setVisible(true);
        userLabel.setVisible(false);
    }

    /**
     * Hide admin-specific buttons and related features based on user's admin privileges.
     * Hide delete buttons.
     */
    public void closeAdminFeatures(){
        adminLabel.setVisible(false);
        userLabel.setVisible(true);
    }
}
