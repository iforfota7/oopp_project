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
    public void start(Stage primaryStage) throws IOException {
        var selectServer = FXML.load(SelectServerCtrl.class,
                "client", "scenes", "SelectServer.fxml");

        // List rename&delete&add scene loader
        var renameList = FXML.load(RnListCtrl.class,"client", "scenes", "RnList.fxml" );
        var deleteList = FXML.load(DeListCtrl.class,"client", "scenes", "DeList.fxml" );
        var addList = FXML.load(AdListCtrl.class,"client", "scenes", "AdList.fxml" );
        var cardDetails = FXML.load(CardDetailsCtrl.class,"client", "scenes",
                "CardDetails.fxml" );
        var addCard = FXML.load(NewCardCtrl.class,"client", "scenes", "ADDNewCard.fxml");
        var confirmUsername = FXML.load(ConfirmUsernameCtrl.class,
                "client", "scenes", "ConfirmUsername.fxml");
        var confirmAdmin = FXML.load(ConfirmAdminCtrl.class,
                "client", "scenes", "ConfirmAdmin.fxml");
        var boardOverview = FXML.load(BoardOverviewCtrl.class,
                "client", "scenes", "BoardOverview.fxml");
        var addBoard = FXML.load(AddBoardCtrl.class, "client", "scenes",
                "AddNewBoard.fxml");
        var joinBoard = FXML.load(JoinBoardByIDCtrl.class,
                "client","scenes","JoinBoardByID.fxml");
        var userDetails = FXML.load(UserDetailsCtrl.class, "client", "scenes",
                "UserDetails.fxml");
        var customization = FXML.load(CustomizationCtrl.class,
                "client","scenes","Customization.fxml");
        var board = FXML.load(BoardCtrl.class, "client", "scenes", "Board.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);

        mainCtrl.initializeBoard(board, selectServer, confirmUsername,
                boardOverview, addBoard, joinBoard, userDetails);
        mainCtrl.initializeLists(primaryStage, renameList, deleteList, addList);
        mainCtrl.initializeCards(cardDetails, addCard);
        mainCtrl.initializeAdmin(confirmAdmin);
        mainCtrl.initializeCustomization(customization);
    }

}