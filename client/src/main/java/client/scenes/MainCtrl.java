/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {
    private Stage primaryStage, secondaryStage;
    private Scene board, renameList, deleteList,addList;

    private RNListCtrl rnListCtrl;
    private DEListCtrl deListCtrl;
    private ADListCtrl addListCtrl;

    private int numberOfLists = 2;
    private VBox vBoard;
    private HBox actualRow;

    public void initialize(Stage primaryStage, Pair<SelectServerCtrl, Parent> board, Pair<RNListCtrl,Parent> renameList,
                           Pair<DEListCtrl, Parent> deleteList, Pair<ADListCtrl, Parent> addList) {
        this.primaryStage = primaryStage;

        this.board = new Scene(board.getValue());

        this.renameList = new Scene(renameList.getValue());
        this.rnListCtrl = renameList.getKey();

        this.deleteList = new Scene(deleteList.getValue());
        this.deListCtrl = deleteList.getKey();

        this.addList = new Scene(addList.getValue());
        this.addListCtrl = addList.getKey();

        showStart();
        primaryStage.show();
    }

    public void showStart() {
        primaryStage.setTitle("Start");
        primaryStage.setScene(board);
    }

    /**
     * Sets scene of stage to passed board
     * @param board the scene to be displayed
     */
    public void setBoard(Pair<BoardCtrl, Parent> board){
        this.board = new Scene(board.getValue());
        showStart();
    }

    public void showRenameList() {
        secondaryStage = new Stage();
        secondaryStage.setScene(renameList);
        secondaryStage.setTitle("Rename list!");
        secondaryStage.show();
    }

    public void showDeleteList() {
        secondaryStage = new Stage();
        secondaryStage.setScene(deleteList);
        secondaryStage.setTitle("Delete List!");
        secondaryStage.show();
    }

    public void showAddList() {
        secondaryStage = new Stage();
        secondaryStage.setScene(addList);
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
     * Adds a new list by calculating its coordinates of the board and adding it to the root's children
     * @param list the list to be added
     * @param line the root where to add the list
     */
    public void addNewListToHBox(VBox list, HBox line){
        line.getChildren().add(list);
    }

    public void addNewList(VBox list, HBox actualRow, VBox vBoard){
        numberOfLists++;
        int positionOfTheList = numberOfLists % 4;

        this.actualRow = actualRow;
        this.vBoard = vBoard;

        /*if(positionOfTheList == 0){
            HBox newHBox = new HBox(27, list);
            vBoard.getChildren().add(newHBox);
            BoardCtrl.setNewRow(newHBox);

            addNewListToHBox(list, newHBox);
            this.actualRow = newHBox;
        }
        else{*/
            addNewListToHBox(list, this.actualRow);
        //}
    }
}