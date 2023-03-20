package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;


public class ShowScenesCtrl {

    private Stage primaryStage;
    private static Stage secondaryStage;
    private Scene start;
    private static Scene renameList;
    private static Scene deleteList;
    private static Scene addList;
    private static Scene cardDetails;
    private static int numberOfLists = 2;

    public void openScenes(Stage primaryStage,
                           Pair<SelectServerCtrl, Parent> selectServer,
                           Pair<RNListCtrl, Parent> renameList,
                           Pair<DEListCtrl, Parent> deleteList,
                           Pair<ADListCtrl, Parent> addList,
                           Pair<CardDetailsCtrl, Parent> cardDetails){
        this.primaryStage = primaryStage;
        this.start = new Scene(selectServer.getValue());
        ShowScenesCtrl.renameList = new Scene(renameList.getValue());
        ShowScenesCtrl.deleteList = new Scene(deleteList.getValue());
        ShowScenesCtrl.addList = new Scene(addList.getValue());
        ShowScenesCtrl.cardDetails = new Scene(cardDetails.getValue());
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
    public static void showRenameList() {
        secondaryStage = new Stage();
        secondaryStage.setScene(renameList);
        secondaryStage.setTitle("Rename list!");
        secondaryStage.show();
    }

    /**
     * Show scene of Delete List
     */
    public static void showDeleteList() {
        secondaryStage = new Stage();
        secondaryStage.setScene(deleteList);
        secondaryStage.setTitle("Delete List!");
        secondaryStage.show();
    }

    /**
     * Show scene of addList
     */
    public static void showAddList() {
        secondaryStage = new Stage();
        secondaryStage.setScene(addList);
        secondaryStage.setTitle("New List!");
        secondaryStage.show();
    }

    public static void closeRNList() {
        secondaryStage.close();
    }
    public static void closeDEList() {
        secondaryStage.close();
    }

    public static void closeADList() {
        secondaryStage.close();
    }

    /**
     * Adds a new list to the board
     * @param list the list to be added to the board
     * @param row the hbox to which the list should be added (the row)
     */
    public static void addNewList(VBox list, HBox row){
        numberOfLists++;
        row.getChildren().add(list);

    }

    /**
     * Show scene of cardDetails
     */
    public static void showCardDetail() {
        secondaryStage = new Stage();
        secondaryStage.setScene(cardDetails);
        secondaryStage.setTitle("Card Details");
        secondaryStage.show();
    }

    /**
     * close scene of cardDetails
     */
    public static void closeCardDetails() {
        secondaryStage.close();
    }
}
