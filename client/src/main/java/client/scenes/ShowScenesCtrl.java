package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;


public class ShowScenesCtrl {

    private Stage primaryStage, secondaryStage;
    private Scene start;
    private Scene renameList;
    private Scene deleteList;
    private Scene addList;
    private Scene cardDetails;
    private int numberOfLists = 2;

    public void openScenes(Stage primaryStage,
                           Pair<SelectServerCtrl, Parent> selectServer,
                           Pair<RNListCtrl, Parent> renameList,
                           Pair<DEListCtrl, Parent> deleteList,
                           Pair<ADListCtrl, Parent> addList,
                           Pair<CardDetailsCtrl, Parent> cardDetails){
        this.primaryStage = primaryStage;
        this.start = new Scene(selectServer.getValue());
        this.renameList = new Scene(renameList.getValue());
        this.deleteList = new Scene(deleteList.getValue());
        this.addList = new Scene(addList.getValue());
        this.cardDetails = new Scene(cardDetails.getValue());
        this.primaryStage.setScene(start);
        primaryStage.show();
    }
    /**
     * Sets scene of stage to passed board
     * @param board the scene to be displayed
     */
    public void setBoard(Pair<BoardCtrl, Parent> board){
        this.start = new Scene(board.getValue());
        primaryStage.setScene(start);
        primaryStage.setTitle("Input boardId");
        primaryStage.show();
    }
    /**
     * Show scene of Rename List
     */
    public void showRenameList() {
        secondaryStage = new Stage();
        secondaryStage.setScene(this.renameList);
        secondaryStage.setTitle("Rename list!");
        secondaryStage.show();
    }

    /**
     * Show scene of Delete List
     */
    public void showDeleteList() {
        secondaryStage = new Stage();
        secondaryStage.setScene(this.deleteList);
        secondaryStage.setTitle("Delete List!");
        secondaryStage.show();
    }

    /**
     * Show scene of addList
     */
    public void showAddList() {
        secondaryStage = new Stage();
        secondaryStage.setScene(this.addList);
        secondaryStage.setTitle("New List!");
        secondaryStage.show();
    }

    public void closeRNList() {
        secondaryStage.close();
    }
    public void closeDEList() {
        secondaryStage.close();
    }

    public void closeADList() {
        secondaryStage.close();
    }

    /**
     * Adds a new list to the board
     * @param list the list to be added to the board
     * @param row the hbox to which the list should be added (the row)
     */
    public void addNewList(VBox list, HBox row){
        numberOfLists++;
        row.getChildren().add(list);

    }

    /**
     * Show scene of cardDetails
     */
    public void showCardDetail() {
        secondaryStage = new Stage();
        secondaryStage.setScene(this.cardDetails);
        secondaryStage.setTitle("Card Details");
        secondaryStage.show();
    }

    /**
     * close scene of cardDetails
     */
    public void closeCardDetails() {
        secondaryStage.close();
    }
}
