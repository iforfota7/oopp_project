package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import commons.Boards;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import javax.inject.Inject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BoardOverviewCtrl implements Initializable {

    private MainCtrl mainCtrl;
    private List<Label> boards;
    private int numberOfBoards = 3;
    private int positionInColumn;

    @FXML
    GridPane gridPane;
    @FXML
    private Label board1;
    @FXML
    private Label board2;
    @FXML
    private Label board3;

    private Label currentBoard;

    private final ServerUtils server;
    private BooleanProperty adminLock = new SimpleBooleanProperty(false);

    public boolean getAdminLock() {
        return adminLock.get();
    }

    /**
     * Creates a list of boards holding all labels
     * Initializes the onMouseClicked event for these labels
     *
     * @param url
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resourceBundle
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boards = new ArrayList<>();

        // hardcoded for now
        boards.add(board1);
        boards.add(board2);
        boards.add(board3);

        for(Label board : boards)
            board.setOnMouseClicked(this::goToBoard);
    }

    /**
     * Constructor for the BoardOverviewCtrl
     *
     * @param mainCtrl Used for navigating through the scenes
     * @param server
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
        Main.setSceneToBoard(((Label)event.getSource()).getText());
        this.currentBoard = (Label) event.getSource();
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
    public  void removeCurrentBoard() {
        boards.remove(currentBoard);
        Pane parent = (Pane) currentBoard.getParent();
        parent.getChildren().remove(currentBoard);
        mainCtrl.refreshBoards();
    }

    @FXML
    public void addBoard(){
        mainCtrl.showAddBoard();
    }

    public void addNewBoard(Boards b){
        numberOfBoards++;
        positionInColumn = (numberOfBoards - 1) % 3;
        int row = (numberOfBoards - 1) / 3;

        Label newBoard = createNewBoard(b.getName());

        gridPane.add(newBoard, positionInColumn, row);
        gridPane.setMargin(gridPane.getChildren().get(numberOfBoards - 1),
                new Insets(10, 10 , 10 ,10));
    }

    public Label createNewBoard(String title) {
        Label newBoard = new Label(title);
        newBoard.setStyle("-fx-font-size: 15px; -fx-alignment: CENTER;" +
                "-fx-background-color: #ffffff; -fx-border-color: #8d78a6; -fx-border-radius: 3px");
        newBoard.setPrefWidth(170);
        newBoard.setPrefHeight(128);
        return newBoard;
    }

    public int getNumberOfBoards(){
        return  numberOfBoards;
    }
    @FXML
    private Button lockBtu;

    private String adminPassword = "6464";
    @FXML
    void adminLogin() {
        if (adminLock.getValue()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Admin!");
            alert.setHeaderText(null);
            alert.setContentText("Admin has been unlocked!");
            alert.showAndWait();
        } else {
            mainCtrl.showConfirmAdmin();
        }
    }
    public void openAdminFeatures() {
        adminLock.set(true);
        mainCtrl.closeConfirmAdmin();
        lockBtu.setStyle("-fx-border-color: green");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login successful!");
        alert.setHeaderText(null);
        alert.setContentText("Welcome admin!");
        alert.showAndWait();
    }
}
