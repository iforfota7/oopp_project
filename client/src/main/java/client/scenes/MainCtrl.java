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

import commons.User;
import commons.Boards;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {
    private Stage primaryStage, secondaryStage;

    private Scene board, renameList, deleteList, addList;
    private Scene cardDetails, newCard, confirmUsername, boardOverview, addBoard, renameBoard;
    private Scene selectServer, joinBoardByID, userDetails, deleteCard;
    private Scene confirmAdmin, help;

    private RnListCtrl rnListCtrl;
    private DeListCtrl deListCtrl;
    private AdListCtrl addListCtrl;
    private CardDetailsCtrl cardDetailsCtrl;
    private NewCardCtrl newCardCtrl;
    private ConfirmUsernameCtrl confirmUsernameCtrl;
    private ConfirmAdminCtrl confirmAdminCtrl;
    private BoardOverviewCtrl boardOverviewCtrl;
    private SelectServerCtrl selectServerCtrl;
    private JoinBoardByIDCtrl joinBoardByIDCtrl;
    private BoardCtrl boardCtrl;
    private AddBoardCtrl addBoardCtrl;
    private UserDetailsCtrl userDetailsCtrl;

    private RenameBoardCtrl renameBoardCtrl;
    private HelpCtrl helpCtrl;

    private DeCardCtrl deCardCtrl;


    /**
     * Initialize method for board related scenes
     * @param board boardCtrl parent pair for board scene
     * @param selectServer selectServerCtrl parent pair for selectServer scene
     * @param confirmUsername confirmUsernameCtrl parent pair for confirmUsername scene
     * @param boardOverview boardOverviewCtrl parent pair for boardOverview scene
     * @param addBoard addBoardCtrl parent pair for addBoard scene
     * @param joinBoardByID joinBoardByIDCtrl parent pair for joinBoardByID scene
     * @param userDetails userDetailsCtrl parent pair for userDetails scene
     */
    public void initializeBoard(Pair<BoardCtrl, Parent> board,
                                Pair<SelectServerCtrl, Parent> selectServer,
                                Pair<ConfirmUsernameCtrl, Parent> confirmUsername,
                                Pair<BoardOverviewCtrl, Parent> boardOverview,
                                Pair<AddBoardCtrl, Parent> addBoard,
                                Pair<JoinBoardByIDCtrl, Parent> joinBoardByID,
                                Pair<UserDetailsCtrl, Parent> userDetails) {

        this.board = new Scene(board.getValue());
        this.boardCtrl = board.getKey();

        this.selectServer = new Scene(selectServer.getValue());
        this.selectServerCtrl = selectServer.getKey();

        this.confirmUsername = new Scene(confirmUsername.getValue());
        this.confirmUsernameCtrl = confirmUsername.getKey();

        this.boardOverview = new Scene(boardOverview.getValue());
        this.boardOverviewCtrl = boardOverview.getKey();

        this.addBoard = new Scene(addBoard.getValue());
        this.addBoardCtrl = addBoard.getKey();

        this.joinBoardByID = new Scene(joinBoardByID.getValue());
        this.joinBoardByIDCtrl = joinBoardByID.getKey();

        this.userDetails = new Scene(userDetails.getValue());
        this.userDetailsCtrl = userDetails.getKey();
    }

    /**
     * Initialize method for list related scenes
     * @param primaryStage primaryStageCtrl initializer
     * @param renameList renameListCtrl parent pair for renameList scene
     * @param deleteList deleteListCtrl parent pair for deleteList scene
     * @param addList addListCtrl parent pair for addList scene
     */
    public void initializeLists( Stage primaryStage, Pair<RnListCtrl,Parent> renameList,
                Pair<DeListCtrl, Parent> deleteList,
                Pair<AdListCtrl, Parent> addList) {

        this.primaryStage = primaryStage;

        this.renameList = new Scene(renameList.getValue());
        this.rnListCtrl = renameList.getKey();

        this.deleteList = new Scene(deleteList.getValue());
        this.deListCtrl = deleteList.getKey();

        this.addList = new Scene(addList.getValue());
        this.addListCtrl = addList.getKey();

        showStart();
        primaryStage.show();
    }

    /**
     * Initialize method for card related scenes
     * @param cardDetails cardDetailsCtrl parent pair for cardDetails scene
     * @param newCardCtrl newCardCtrl parent pair for newCard scene
     * @param deCardCtrl deCardCtrl parent pair for deleteCard scene
     */
    public void initializeCards(Pair<CardDetailsCtrl, Parent> cardDetails,
            Pair<NewCardCtrl, Parent> newCardCtrl,
            Pair<DeCardCtrl, Parent> deCardCtrl) {

        this.cardDetails = new Scene(cardDetails.getValue());
        this.cardDetailsCtrl = cardDetails.getKey();

        this.newCard = new Scene(newCardCtrl.getValue());
        this.newCardCtrl = newCardCtrl.getKey();

        this.deleteCard = new Scene(deCardCtrl.getValue());
        this.deCardCtrl = deCardCtrl.getKey();
    }

    /**
     * Initialize method for utils related scenes
     * @param helpCtrl helpCtrl parent pair for help scene
     */
    public void initializeUtils(Pair<HelpCtrl, Parent> helpCtrl){
        this.help = new Scene(helpCtrl.getValue());
        this.helpCtrl = helpCtrl.getKey();
    }

    /**
     * Initialize method for admin related scenes
     * @param confirmAdmin confirmAdminCtrl parent pair for confirmAdmin scene
     */
    public void initializeAdmin(Pair<ConfirmAdminCtrl, Parent> confirmAdmin) {
        this.confirmAdmin = new Scene(confirmAdmin.getValue());
        this.confirmAdminCtrl = confirmAdmin.getKey();
    }

    /**
     * Show selectServer scene
     */
    public void showStart() {
        primaryStage.setTitle("Select Server");
        primaryStage.setScene(selectServer);
    }

    /**
     * Show board scene
     * @param b the board to be shown
     */
    public void showBoard(Boards b) {
        boardCtrl.setBoardName(b);
        primaryStage.setTitle("Board");
        primaryStage.setScene(board);
        if(secondaryStage!=null && secondaryStage.isShowing()) secondaryStage.close();
        boardCtrl.initialize(b);
    }

    /**
     * Sets scene of stage to passed board
     * @param b used to display the title of the board
     */
    public void setBoard(Boards b){
        showBoard(b);
    }

    /**
     * Show scene of Rename List
     */
    public void showRenameList() {
        if(secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setScene(renameList);
        secondaryStage.setTitle("Rename list!");
        secondaryStage.show();
    }

    /**
     * Show scene of Delete List
     */
    public void showDeleteList() {
        if(secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setScene(this.deleteList);
        secondaryStage.setTitle("Delete List!");
        secondaryStage.show();
    }

    /**
     * Show scene of addList
     */
    public void showAddList() {
        if(secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setScene(addList);
        secondaryStage.setTitle("New List!");
        secondaryStage.show();
    }

    /**
     * Show confirmUsername scene
     */
    public void showConfirmUsername(){
        if(secondaryStage != null && secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setScene(confirmUsername);
        secondaryStage.setTitle("Confirm Username!");
        secondaryStage.show();
    }

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
        if(secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setScene(cardDetails);
        secondaryStage.setTitle("Card Details");
        secondaryStage.show();
    }

    /**
     * Show addCard scene
     */
    public void showAddCard(){
        if(secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setScene(newCard);
        secondaryStage.setTitle("Add new Card");
        secondaryStage.show();
    }

    /**
     * Method that shows the confirmation scene for deleting a card
     */
    public void showDeleteCard(){
        if(secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setScene(deleteCard);
        secondaryStage.setTitle("Delete Card");
        secondaryStage.show();
    }

    /**
     * Method that sets the scene to the Board Overview scene
     */
    public void showBoardOverview() {
        String titleLabel;
        if(boardOverviewCtrl.getAdminLock()){
            boardOverviewCtrl.openAdminFeatures();
            titleLabel = " (Admin)";
        }else {
            boardOverviewCtrl.closeAdminFeatures();
            titleLabel = " (User)";
        }
        boardOverviewCtrl.init();
        primaryStage.setTitle("Board Overview"+titleLabel);
        primaryStage.setScene(boardOverview);
    }

    /**
     * Method that sets the scene to the Select Server scene
     */
    public void showSelectServer() {
        primaryStage.setTitle("Start");
        primaryStage.setScene(selectServer);
    }

    /**
     * creates a secondary stage which asks for a title for the new board
     */
    public void showAddBoard(){
        if(secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setScene(addBoard);
        secondaryStage.setTitle("Add a new Board!");
        secondaryStage.show();
    }


    /**
     * show admin password input window
     */
    public void showConfirmAdmin() {
        if(secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setScene(confirmAdmin);
        secondaryStage.setTitle("Admin LogIn");
        secondaryStage.show();
    }

    /**
     * Open a new window that displays the joinBoardByID scene
     */
    public void showJoinBoardByID() {
        if(secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setTitle("Join board by ID");
        secondaryStage.setScene(joinBoardByID);
        secondaryStage.show();
    }


    /**
     * Method that shows the help scene
     */
    public void showHelpScene(){
        if(secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setTitle("Help");
        secondaryStage.setScene(help);
        secondaryStage.show();
    }

    /**
     * Method that shows the current users details
     * @param currentUser the current user
     */
    public void showUserDetails(User currentUser){
        if(secondaryStage.isShowing()) return;
        userDetailsCtrl.setUser(currentUser);
        secondaryStage = new Stage();
        secondaryStage.setScene(userDetails);
        secondaryStage.setTitle("User Details");
        secondaryStage.show();
    }

    /**
     * This method closes any general secondary stage
     */
    public void closeSecondaryStage(){
        secondaryStage.close();
    }
}