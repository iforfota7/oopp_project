package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import commons.Boards;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import javax.inject.Inject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BoardOverviewCtrl implements Initializable {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private List<Boards> boardsList;
    private int numberOfBoards = 3;
    private int positionInColumn;

    @FXML
    GridPane gridPane;
    @FXML
    AnchorPane anchorPane;
    @FXML
    private Label board1;
    @FXML
    private Label board2;
    @FXML
    private Label board3;

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
        boardsList = new ArrayList<>();

     //   refresh();
    }

    /**
     * Constructor for the BoardOverviewCtrl
     *
     * @param mainCtrl Used for navigating through the scenes
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
    }

    public void disconnect() {
        mainCtrl.showSelectServer();
    }

    @FXML
    public void addBoard(ActionEvent event){
        mainCtrl.showAddBoard();
    }

    public void addNewBoard(Boards b){
        numberOfBoards++;
        positionInColumn = (numberOfBoards - 1) % 3;
        int row = (numberOfBoards - 1) / 3;

        Label newBoard = createNewBoard(b.getName());
        //newBoard.setText("Board");
        newBoard.setAccessibleRole(AccessibleRole.TEXT);

        gridPane.add(newBoard, positionInColumn, row);

        gridPane.setMargin(gridPane.getChildren().get(numberOfBoards - 1),
                new Insets(10, 10 , 10 ,10));

        /*if(positionInColumn == 0){
            //gridPane.addRow(row, newBoard, null, null);
            gridPane.add(newBoard, positionInColumn, row);
        }
        else{
            gridPane.add(newBoard, positionInColumn, row);
        }*/
    }

    public Label createNewBoard(String title) {
        Label newBoard = new Label(title);
        newBoard.setStyle("-fx-background-color: #ffffff; -fx-text-fill:  #0d0d0d; " +
                "-fx-border-color: #8d78a6; -fx-border-radius: 3px; -fx-text-fill: #000000;" +
                "-fx-z-index: 999;");
        newBoard.setPrefWidth(165);
        newBoard.setPrefHeight(75);
        newBoard.setAlignment(Pos.CENTER);
        newBoard.setText("Board");
        newBoard.setFont(new Font(15));
        newBoard.setOnMouseClicked(this::goToBoard);
        return newBoard;
    }

    public int getNumberOfBoards(){
        return  numberOfBoards;
    }

    public void refresh(){
        gridPane.getChildren().clear();
        boardsList = server.getBoards();
        for (Boards boards : boardsList) {
            addNewBoard(boards);
        }
    }

}
