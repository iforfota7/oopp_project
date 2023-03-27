package client.scenes;

import client.Main;
import commons.Boards;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

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
     */
    @Inject
    public BoardOverviewCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
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
        newBoard.setStyle("-fx-font-size: 15px; -fx-alignment: CENTER;" +
                "-fx-background-color: #ffffff; -fx-border-color: #8d78a6; -fx-border-radius: 3px");
        newBoard.setPrefWidth(170);
        newBoard.setPrefHeight(128);
        return newBoard;
    }

    public int getNumberOfBoards(){
        return  numberOfBoards;
    }

}
