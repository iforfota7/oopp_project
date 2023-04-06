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

import client.scenes.config.Shortcuts;
import commons.Tags;
import commons.User;
import commons.Boards;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {
    private Stage primaryStage, secondaryStage, thirdStage;

    private Scene board, renameList, deleteList, addList;
    private Scene cardDetails, newCard, confirmUsername;
    private Scene boardOverview, addBoard, renameBoard;
    private Scene tagControl, addTag, tagDetails;
    private Scene selectServer, joinBoardByID, userDetails, deleteCard;
    private Scene confirmAdmin, help, helpOverview, helpShortcuts;

    private SelectServerCtrl selectServerCtrl;
    private ConfirmUsernameCtrl confirmUsernameCtrl;

    private BoardOverviewCtrl boardOverviewCtrl;
    private JoinBoardByIDCtrl joinBoardByIDCtrl;
    private BoardCtrl boardCtrl;
    private AddBoardCtrl addBoardCtrl;
    private RenameBoardCtrl renameBoardCtrl;

    private HelpCtrl helpCtrl;
    private UserDetailsCtrl userDetailsCtrl;
    private ConfirmAdminCtrl confirmAdminCtrl;

    private RnListCtrl rnListCtrl;
    private DeListCtrl deListCtrl;
    private AdListCtrl addListCtrl;

    private CardDetailsCtrl cardDetailsCtrl;
    private NewCardCtrl newCardCtrl;
    private DeCardCtrl deCardCtrl;

    private AddTagCtrl addTagCtrl;
    private TagsCtrl tagsCtrl;
    private TagDetailsCtrl tagDetailsCtrl;
    private Shortcuts shortcuts;



    /**
     * Initialize method for board related scenes
     * @param board boardCtrl parent pair for board scene
     * @param selectServer selectServerCtrl parent pair for selectServer scene
     * @param confirmUsername confirmUsernameCtrl parent pair for confirmUsername scene
     * @param boardOverview boardOverviewCtrl parent pair for boardOverview scene
     * @param addBoard addBoardCtrl parent pair for addBoard scene
     * @param joinBoardByID joinBoardByIDCtrl parent pair for joinBoardByID scene
     * @param userDetails userDetailsCtrl parent pair for userDetails scene
     * @param renameBoard renameBoardCtrl parent pair for renameBoard scene
     */
    public void initializeBoard(Pair<BoardCtrl, Parent> board,
                                Pair<SelectServerCtrl, Parent> selectServer,
                                Pair<ConfirmUsernameCtrl, Parent> confirmUsername,
                                Pair<BoardOverviewCtrl, Parent> boardOverview,
                                Pair<AddBoardCtrl, Parent> addBoard,
                                Pair<JoinBoardByIDCtrl, Parent> joinBoardByID,
                                Pair<UserDetailsCtrl, Parent> userDetails,
                                Pair<RenameBoardCtrl, Parent> renameBoard) {

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

        this.renameBoard = new Scene(renameBoard.getValue());
        this.renameBoardCtrl = renameBoard.getKey();
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
     * @param deCardCtrl deCardCtrl parent pair for deCard scene
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
     * Initialise method for 'useful' scenes
     * @param helpCtrl helpCtrl parent pair for Help scene
     * @param helpOverviewCtrl helpOverviewCtrl parent pair for Help scene in board overview
     * @param helpShortcutsCtrl helpShortcutsCtrl parent pair for keyboard shortcuts Help scene
     */
    public void initializeUtils(Pair<HelpCtrl, Parent> helpCtrl,
                                Pair<HelpCtrl, Parent> helpOverviewCtrl,
                                Pair<HelpCtrl, Parent> helpShortcutsCtrl){
        this.help = new Scene(helpCtrl.getValue());
        this.helpCtrl = helpCtrl.getKey();

        this.helpOverview = new Scene(helpOverviewCtrl.getValue()); // uses same ctrl as help
        this.helpShortcuts = new Scene(helpShortcutsCtrl.getValue()); // uses same ctrl as help
    }

    /**
     * @param tagDetails tagDetailsCtrl parent pair for tagDetails scene
     * @param addTagCtrl newTagCtrl parent pair for addTag
     * @param tagControl tagControl parent pair for TagsController
     */
    public void initializeTags(Pair<TagDetailsCtrl, Parent> tagDetails,
                               Pair<AddTagCtrl, Parent> addTagCtrl,
                               Pair<TagsCtrl, Parent> tagControl){
        this.tagDetails = new Scene(tagDetails.getValue());
        this.tagDetailsCtrl = tagDetails.getKey();

        this.addTag = new Scene(addTagCtrl.getValue());
        this.addTagCtrl = addTagCtrl.getKey();

        this.tagControl = new Scene(tagControl.getValue());
        this.tagsCtrl = tagControl.getKey();

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
        boardCtrl.addBoardToUser(b);
        primaryStage.setTitle("Board");
        primaryStage.setScene(board);
        if(secondaryStage!=null && secondaryStage.isShowing()) secondaryStage.close();
        boardCtrl.initialize(b);
    }

    /**
     * Show scene in which a board can be renamed
     */
    public void showRenameBoard(){
        secondaryStage = new Stage();
        secondaryStage.setScene(renameBoard);
        secondaryStage.setTitle("Rename board!");
        secondaryStage.show();
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
        if(secondaryStage != null && secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setScene(renameList);
        secondaryStage.setTitle("Rename list!");
        secondaryStage.show();
    }

    /**
     * Show scene of Delete List
     */
    public void showDeleteList() {
        if(secondaryStage != null && secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setScene(this.deleteList);
        secondaryStage.setTitle("Delete List!");
        secondaryStage.show();
    }

    /**
     * Show scene of addList
     */
    public void showAddList() {
        if(secondaryStage != null && secondaryStage.isShowing()) return;
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
     * Show scene of cardDetails
     */
    public void showCardDetail() {
        if(secondaryStage != null && secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setScene(cardDetails);

        // card details are not saved if the window is closed
        // using the 'x' button
        secondaryStage.setOnCloseRequest(event -> {
            cardDetailsCtrl.close();
        });
        secondaryStage.setTitle("Card Details");
        cardDetailsCtrl.init();
        secondaryStage.show();
    }

    /**
     * Shows the tag details scene
     *
     * @param t The Tags object for which we show the scene
     * @param board The board object which contains the tag
     */

    public void showTagDetail(Tags t, Boards board){
        if(thirdStage != null && thirdStage.isShowing()) return;
        thirdStage = new Stage();
        thirdStage.setScene(tagDetails);
        thirdStage.setTitle("Tag Details");
        thirdStage.show();
        tagDetailsCtrl.initialize(t, board);
    }

    /**
     * Show addCard scene
     */
    public void showAddCard(){
        if(secondaryStage != null && secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setScene(newCard);
        secondaryStage.setTitle("Add new Card");
        secondaryStage.show();
    }

    /**
     * Opens the scene for adding a new tag
     *
     * @param board The board object inside which we add a tag
     */
    public void showAddTag(Boards board){
        if(thirdStage != null && thirdStage.isShowing()) return;
        thirdStage = new Stage();
        thirdStage.setScene(addTag);
        thirdStage.setTitle("Add new Tag");
        thirdStage.show();
        addTagCtrl.initialize(board);
    }

    /**
     * Opens the primary scene for viewing tags
     *
     * @param b The board object for which we open this scene
     */
    public void showTagControl(Boards b){
        if(secondaryStage != null && secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setScene(tagControl);
        secondaryStage.setTitle("Tags Control");
        secondaryStage.show();
        tagsCtrl.initialize(b);
    }


    /**
     * Closes an instance of a third stage
     *
     */
    public void closeThirdStage(){thirdStage.close();}

    /**
     * Opens a secondary window which asks for confirmation for
     * deleting a card
     */
    public void showDeleteCard(){
        if(secondaryStage != null && secondaryStage.isShowing()) return;
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
        if(secondaryStage != null && secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setScene(addBoard);
        secondaryStage.setTitle("Add a new Board!");
        secondaryStage.show();
    }


    /**
     * show admin password input window
     */
    public void showConfirmAdmin() {
        if(secondaryStage != null && secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setScene(confirmAdmin);
        secondaryStage.setTitle("Admin LogIn");
        secondaryStage.show();
    }

    /**
     * Open a new window that displays the joinBoardByID scene
     */
    public void showJoinBoardByID() {
        if(secondaryStage != null && secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setTitle("Join board by ID");
        secondaryStage.setScene(joinBoardByID);
        secondaryStage.show();
    }


    /**
     * Shows in a second window the guide to use the application
     * in the board after pressing the 'help' button
     */
    public void showHelpScene(){
        if(secondaryStage != null && secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setTitle("Help");
        secondaryStage.setScene(help);
        secondaryStage.show();
    }

    /**
     * Shows in a second window the guide to use the application
     * in the board Overview after pressing the 'help' button
     */
    public void showHelpOverviewScene(){
        if(secondaryStage != null && secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setTitle("Help");
        secondaryStage.setScene(helpOverview);
        secondaryStage.show();
    }


    /**
     * Shows in a second window the list of available keyboard shortcuts
     * after pressing 'H' anywhere in the board scene.
     */
    public void showHelpShortcutsScene(){
        if(secondaryStage != null && secondaryStage.isShowing()) return;
        secondaryStage = new Stage();
        secondaryStage.setTitle("Keyboard shortcuts");
        secondaryStage.setScene(helpShortcuts);
        secondaryStage.show();
    }

    /**
     * Shows in a second window the user's details: username, server
     * address and whether it is an admin or not
     * @param currentUser the user whose details are shown
     */
    public void showUserDetails(User currentUser){
        if(secondaryStage != null && secondaryStage.isShowing()) return;
        userDetailsCtrl.setUser(currentUser);
        secondaryStage = new Stage();
        secondaryStage.setScene(userDetails);
        secondaryStage.setTitle("User Details");
        secondaryStage.show();
    }

    /**
     * Getter for the board scene
     * @return the board scene
     */
    public Scene getBoard() {
        return board;
    }

    /**
     * This method closes any general secondary stage
     */
    public void closeSecondaryStage(){
        secondaryStage.close();
    }
}
