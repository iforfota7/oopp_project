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
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;

import client.scenes.*;
import client.scenes.BoardCtrl;
import com.google.inject.Injector;

import client.scenes.MainCtrl;
import client.scenes.SelectServerCtrl;


import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);


    /**
     * Main method that runs client side application
     * @param args any arguments
     * @throws URISyntaxException possibly throws this exception
     * @throws IOException possibly throws this exception
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    /**
     * Initialisation of the fxml files
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * @throws IOException possibly throws this exception
     */
    @Override
    public void start(Stage primaryStage)  throws IOException {
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);

        initializeBoard(mainCtrl, primaryStage);
        initializeUtils(mainCtrl);
        initializeAdmin(mainCtrl);
        initializeLists(mainCtrl, primaryStage);
        initializeCards(mainCtrl);
        initializeTags(mainCtrl);
        initializeCustomization(mainCtrl);
    }

    /**
     * Initializes the fxml files related to boards and server details, such as
     * the user's username and other details
     * @param mainCtrl instance of the mainCtrl object used to initialize the files
     * @param primaryStage the primary stage used to display the files' scenes
     */
    public void initializeBoard(MainCtrl mainCtrl, Stage primaryStage){
        var boardOverview = FXML.load(BoardOverviewCtrl.class,
                "client", "scenes", "BoardOverview.fxml");
        var board = FXML.load(BoardCtrl.class, "client", "scenes", "Board.fxml");

        var addBoard = FXML.load(AddBoardCtrl.class, "client", "scenes",
                "AddNewBoard.fxml");
        var joinBoard = FXML.load(JoinBoardByIDCtrl.class,
                "client","scenes","JoinBoardByID.fxml");
        var renameBoard = FXML.load(RenameBoardCtrl.class, "client", "scenes", "RnBoard.fxml");

        var selectServer = FXML.load(SelectServerCtrl.class,
                "client", "scenes", "SelectServer.fxml");
        var confirmUsername = FXML.load(ConfirmUsernameCtrl.class,
                "client", "scenes", "ConfirmUsername.fxml");
        var userDetails = FXML.load(UserDetailsCtrl.class, "client", "scenes",
                "UserDetails.fxml");

        mainCtrl.initializeBoard(board, selectServer, confirmUsername,
                boardOverview, addBoard, joinBoard, userDetails, renameBoard);

        primaryStage.setOnCloseRequest(e -> {
            board.getKey().stop();
        });

    }

    /**
     * Initializes the fxml files related to admin
     * @param mainCtrl instance of the mainCtrl object used to initialize the files
     */
    public void initializeAdmin(MainCtrl mainCtrl){
        var confirmAdmin = FXML.load(ConfirmAdminCtrl.class,
                "client", "scenes", "ConfirmAdmin.fxml");
        mainCtrl.initializeAdmin(confirmAdmin);
    }

    /**
     * Initializes the fxml files related to utils of this applications
     * @param mainCtrl instance of the mainCtrl object used to initialize the files
     */
    public void initializeUtils(MainCtrl mainCtrl){
        var helpScene = FXML.load(HelpCtrl.class, "client", "scenes", "Help.fxml");
        var helpOverviewScene = FXML.load(HelpCtrl.class, "client", "scenes", "HelpOverview.fxml");
        var helpShortcuts = FXML.load(HelpCtrl.class, "client", "scenes", "HelpShortcuts.fxml");

        mainCtrl.initializeUtils(helpScene, helpOverviewScene, helpShortcuts);
    }

    /**
     * Initializes the fxml files related to lists
     * @param mainCtrl instance of the mainCtrl object used to initialize the files
     * @param primaryStage the primary stage used to display the files' scenes
     */
    public void initializeLists(MainCtrl mainCtrl, Stage primaryStage){
        var renameList = FXML.load(RnListCtrl.class,"client", "scenes", "RnList.fxml" );
        var deleteList = FXML.load(DeListCtrl.class,"client", "scenes", "DeList.fxml" );
        var addList = FXML.load(AdListCtrl.class,"client", "scenes", "AdList.fxml" );

        mainCtrl.initializeLists(primaryStage, renameList, deleteList, addList);

    }

    /**
     * Initializes the fxml files related to cards
     * @param mainCtrl instance of the mainCtrl object used to initialize the files
     */
    public void initializeCards(MainCtrl mainCtrl){
        var cardDetails = FXML.load(CardDetailsCtrl.class,"client", "scenes",
                "CardDetails.fxml" );
        var confirmCloseCard = FXML.load(CardDetailsCtrl.class, "client",
                "scenes", "CardDetailsConfirmExit.fxml");
        var warningCardDeletion = FXML.load(CardDetailsCtrl.class, "client",
                "scenes", "WarningDeletionCard.fxml");
        var addCard = FXML.load(NewCardCtrl.class,"client", "scenes", "ADDNewCard.fxml");
        var deleteCard = FXML.load(DeCardCtrl.class, "client", "scenes", "DeCard.fxml");

        mainCtrl.initializeCards(cardDetails, addCard, deleteCard,
                confirmCloseCard, warningCardDeletion);
    }

    /**
     * Initializes the fxml files related to tags
     * @param mainCtrl instance of the mainCtrl object used to initialize the files
     */
    public void initializeTags(MainCtrl mainCtrl){
        var tagsControl = FXML.load(TagsCtrl.class, "client", "scenes", "tagsController.fxml");
        var tagDetails = FXML.load(TagDetailsCtrl.class, "client", "scenes", "tagDetail.fxml");
        var addTag = FXML.load(AddTagCtrl.class, "client", "scenes", "AddTag.fxml");
        var addTagToCard = FXML.load(AddTagToCardCtrl.class,
                "client", "scenes", "AddTagToCard.fxml");

        mainCtrl.initializeTags(tagDetails, addTag, tagsControl, addTagToCard);
    }

    /**
     * Initializes the fxml files related to customization
     * @param mainCtrl instance of the mainCtrl object used to initialize the files
     */
    public void initializeCustomization(MainCtrl mainCtrl) {
        var customization = FXML.load(CustomizationCtrl.class,
                "client","scenes","Customization.fxml");

        var cardCustomization = FXML.load(CardCustomizationCtrl.class,
                "client", "scenes", "CardCustomization.fxml");

        mainCtrl.initializeCustomization(customization,cardCustomization);

    }
}