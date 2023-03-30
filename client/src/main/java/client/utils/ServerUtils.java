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
package client.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import commons.Boards;
import commons.Cards;
import commons.Lists;
import commons.User;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import static jakarta.ws.rs.core.MediaType.*;

public class ServerUtils {

    private static String SERVER;
    private static String USERNAME;

    /**
     * Method that adds a user to the database
     * @param user the user to be added
     * @return the response object
     */
    public User addUser(User user){
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/user").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(user, APPLICATION_JSON), User.class);
    }

    /**
     * Find whether a user exists or not
     * @param user a user which should be checked
     * @return true if user already in database, otherwise false
     */
    public boolean existsUser(User user){
        if(ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/user/find/" + user.username).
                request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .get(new GenericType<User>(){}) == null) return false;
        return true;
    }

    /**
     * Find whether a board exists or not using its ID
     * @param boardName the id of the board that is being searched for
     * @return true if the board is in the database, otherwise false
     */
    public Boards existsBoardByName(String boardName) {
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/boards/find/"+boardName).
                request(APPLICATION_JSON).accept(APPLICATION_JSON).
                get(new GenericType<Boards>(){});

    }

    /**
     * Add a new list to the database
     * @param list the list to be added
     * @return the response object
     */
    public Lists addList(Lists list){
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/lists/").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(list, APPLICATION_JSON), Lists.class);
    }

    /**
     * Method that renames an existing list in the database
     * @param list the list containing the new name
     * @return the response object
     */
    public Lists renameList(Lists list){
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/lists/rename").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(list, APPLICATION_JSON), Lists.class);
    }

    /**
     * Method that removes a list from the database
     * @param list the list to be deleted
     * @return the response object
     */
    public Lists removeList(Lists list){
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/lists/remove").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(list, APPLICATION_JSON), Lists.class);
    }

    /**
     * Method to add a new card to the database
     * @param card the card to be added
     * @return the response object
     */
    public Cards addCard(Cards card){
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/cards").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(card, APPLICATION_JSON), Cards.class);
    }

    /**
     * Method that removes card from the database
     * @param card the card to be deleted
     * @return the response object
     */
    public Cards removeCard(Cards card){
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/cards/remove").request(APPLICATION_JSON).accept(APPLICATION_JSON).
               post(Entity.entity(card, APPLICATION_JSON_TYPE), Cards.class);
    }

    /**
     * Method that renames a card in the database
     * @param card the card to be renamed with new properties
     * @return the response object
     */
    public Cards renameCard(Cards card){
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/cards/rename").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(card, APPLICATION_JSON), Cards.class);
    }

    /**
     * Method that moves card to new list or within list
     * @param card the card to be moved
     * @return the response object
     */
    public Cards moveCard(Cards card){
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/cards/move").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(card, APPLICATION_JSON), Cards.class);
    }

    /**
     * Method that retrieves all lists from the server
     * @return a list of all lists in the database
     */
    public List<Lists> getLists() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lists") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Lists>>() {});
    }

    /**
     * Method that adds Board to the database
     * @param board the board to be added
     * @return the response object
     */
    public Boards addBoard(Boards board) {
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/boards").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(board, APPLICATION_JSON), Boards.class);
    }

    /**
     * Method that gets all lists in a certain board
     * @param boardName the id of the board
     * @return a list of lists in the board
     */
    public List<Lists> getListsByBoard(long boardName) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lists/all/" + boardName) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Lists>>() {});
    }

    /**
     * Method that retrieves all the boards
     * @return a list of all boards
     */
    public List<Boards> getBoards() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/boards/all")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Boards>>() {});
    }

    /**
     * Setter method for the server attribute
     * @param server the server address to be set
     */
    public static void setServer(String server){
        SERVER = server;
    }

    /**
     * Setter method for the username attribute
     * @param username the username to be set
     */
    public static void setUsername(String username) { USERNAME = username;}

    /**
     * Sends a simple get request to /api/test-connection in order to check if the
     * provided URL is a running instance of a Talio server
     *
     * @return True iff the client-server connection can be established
     */
    public static boolean checkServer(){
        try {
            ClientBuilder.newClient(new ClientConfig())
                    .target(SERVER).path("api/test-connection")
                    .request(TEXT_PLAIN)
                    .accept(TEXT_PLAIN)
                    .get();
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    private final StompSession session =  connect("ws://localhost:8080/websocket");

    /**
     * Connect method for websockets
     * @param url the url for the websockets
     * @return StompSession
     */
    private StompSession connect(String url){
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try{
            return stomp.connect(url, new StompSessionHandlerAdapter() {}).get();
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }catch(ExecutionException e){
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * RegisterForMessages method
     * @param dest destination
     * @param type the type
     * @param consumer a consumer
     * @param <T> the type
     */
    public <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer){
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @SuppressWarnings("unchecked")
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }

    /**
     * Method that refreshes whether user is admin on login
     * @param user the user
     * @return the response object
     */
    public User refreshAdmin(User user) {
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/user/refreshAdmin").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(user, APPLICATION_JSON), User.class);
    }

    /**
     * Method that removes Board from database
     * @param board the board to be removed
     * @return the response object
     */
    public Boards removeBoard(Boards board){
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/boards/remove/").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(board, APPLICATION_JSON), Boards.class);
    }

    /**
     * Method that checks whether a user is an admin
     * @return true if the user is admin, false otherwise
     */
    public boolean checkAdmin() {
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/user/find/" + USERNAME).
                request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .get(new GenericType<User>() {
                }).isAdmin;
    }

    /**
     * Method that adds the current board to the user
     * @param board the current board
     * @return the response entity
     */
    public void addBoardToUser(Boards board){
        User user = ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/user/find/" + USERNAME).
                request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .get(new GenericType<User>() {});
        if(user.boards == null || user.boards.size() == 0) user.boards = new ArrayList<>(){};

        user.boards.add(board);

        ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/user/update").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(user, APPLICATION_JSON), User.class);
    }

    /**
     * Method that retrieves all boards a user has viewed
     * @return the list of boards a user has seen
     */
    public List<Boards> viewedBoards(){
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/user/boards/" + USERNAME)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Boards>>() {});
    }

}