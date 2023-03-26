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
    private Scene board, renameList, deleteList, addList, cardDetails, newCard, confirmUsername, boardOverview;
    private Scene selectServer;

    private RnListCtrl rnListCtrl;
    private DeListCtrl deListCtrl;
    private AdListCtrl addListCtrl;
    private CardDetailsCtrl cardDetailsCtrl;
    private NewCardCtrl newCardCtrl;
    private ConfirmUsernameCtrl confirmUsernameCtrl;
    private BoardOverviewCtrl boardOverviewCtrl;
    private SelectServerCtrl selectServerCtrl;

    public void initialize(Stage primaryStage, Pair<SelectServerCtrl, Parent> selectServer,
                           Pair<RnListCtrl,Parent> renameList, Pair<DeListCtrl, Parent> deleteList,
                           Pair<AdListCtrl, Parent> addList, Pair<CardDetailsCtrl,
                            Parent>cardDetails, Pair<NewCardCtrl, Parent> newCardCtrl,
                           Pair<ConfirmUsernameCtrl, Parent> confirmUsername,
                           Pair<BoardOverviewCtrl, Parent> boardOverview) {

        this.primaryStage = primaryStage;

        this.board = new Scene(selectServer.getValue());
        this.selectServer = this.board;
        this.selectServerCtrl = selectServer.getKey();

        this.renameList = new Scene(renameList.getValue());
        this.rnListCtrl = renameList.getKey();

        this.deleteList = new Scene(deleteList.getValue());
        this.deListCtrl = deleteList.getKey();

        this.addList = new Scene(addList.getValue());
        this.addListCtrl = addList.getKey();

        this.cardDetails = new Scene(cardDetails.getValue());
        this.cardDetailsCtrl = cardDetails.getKey();

        this.newCard= new Scene(newCardCtrl.getValue());
        this.newCardCtrl = newCardCtrl.getKey();

        this.confirmUsername = new Scene(confirmUsername.getValue());
        this.confirmUsernameCtrl = confirmUsername.getKey();

        this.boardOverview = new Scene(boardOverview.getValue());
        this.boardOverviewCtrl = boardOverview.getKey();

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

    /**
     * Show scene of Rename List
     */
    public void showRenameList() {
        secondaryStage = new Stage();
        secondaryStage.setScene(renameList);
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
        secondaryStage.setScene(addList);
        secondaryStage.setTitle("New List!");
        secondaryStage.show();
    }

    /**
     * Show confirmUsername scene
     */
    public void showConfirmUsername(){
        secondaryStage = new Stage();
        secondaryStage.setScene(confirmUsername);
        secondaryStage.setTitle("Confirm Username!");
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
     * Closes the confirmUsername scene
     */
    public void closeConfirmUsername() {secondaryStage.close();}

    /**
     * Adds a new list to the board
     * @param list the list to be added to the board
     * @param row the hbox to which the list should be added (the row)
     */
    public void addNewList(VBox list, HBox row){
        row.getChildren().add(list);

    }


    /**
     * Show scene of cardDetails
     */
    public void showCardDetail() {
        secondaryStage = new Stage();
        secondaryStage.setScene(cardDetails);
        secondaryStage.setTitle("Card Details");
        secondaryStage.show();
    }

    public void showAddCard(){
        secondaryStage = new Stage();
        secondaryStage.setScene(newCard);
        secondaryStage.setTitle("Add new Card");
        secondaryStage.show();
    }

    /**
     * close scene of cardDetails
     */
    public void closeCardDetails() {
        secondaryStage.close();
    }
    public void closeNewCard(){secondaryStage.close();}

    public void showBoardOverview() {
        primaryStage.setTitle("Board Overview");
        primaryStage.setScene(boardOverview);
    }

    public void showSelectServer() {
        primaryStage.setTitle("Start");
        primaryStage.setScene(selectServer);
    }

}