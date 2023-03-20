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

import client.MyFXML;
import com.google.inject.Injector;
import javafx.stage.Stage;


public class MainCtrl {


    public void initialize(Stage primaryStage, Injector injector, MyFXML fxml) {
        var selectServer = fxml.load(SelectServerCtrl.class, "client", "scenes", "SelectServer.fxml");
        var renameList = fxml.load(RNListCtrl.class,"client", "scenes", "RNList.fxml" );
        var deleteList = fxml.load(DEListCtrl.class,"client", "scenes", "DEList.fxml" );
        var addList = fxml.load(ADListCtrl.class,"client", "scenes", "ADList.fxml" );
        var cardDetails = fxml.load(CardDetailsCtrl.class,"client", "scenes", "CardDetails.fxml" );
        var showScenesCtrl = injector.getInstance(ShowScenesCtrl.class);
        showScenesCtrl.openScenes(primaryStage, selectServer, renameList, deleteList, addList, cardDetails);
    }

}