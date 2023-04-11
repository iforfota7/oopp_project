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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import commons.Boards;
import commons.Cards;
import commons.Lists;
import commons.User;
import jakarta.ws.rs.core.Response;
import commons.*;
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

    private String serverAddress;
    private String username;

    /**
     * Method that adds a user to the database
     * @param user the user to be added
     * @return the response object
     */
    public User addUser(User user){
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/user").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(user, APPLICATION_JSON), User.class);
    }

    /**
     * Find whether a user exists or not
     * @return true if user already in database, otherwise false
     */
    public boolean existsUser(){
        if(findUser()== null) return false;
        return true;
    }

    /**
     * Find whether current user is in the database
     * @return the user if they exist
     */
    public User findUser(){
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/user/find/" + username).
                request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .get(new GenericType<User>(){});
    }

    /**
     * Generate the password for becoming an admin on the server
     */
    public void generatePassword(){
        ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/user/admin/").
                request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .get();
    }

    /**
     * Test whether the user input password is correct
     * @param password the input password
     * @return true if the password is right, otherwise false
     */
    public boolean checkPassword(String password){
        String x = ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/user/admin/password").request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(password, APPLICATION_JSON), String.class);
        return x.equals("true");
    }

    /**
     * Find whether a board exists or not using its ID
     * @param boardName the id of the board that is being searched for
     * @return true if the board is in the database, otherwise false
     */
    public Boards existsBoardByName(String boardName) {
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
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
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/lists/").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(list, APPLICATION_JSON), Lists.class);
    }

    /**
     * Method that renames an existing list in the database
     * @param list the list containing the new name
     * @return the response object
     */
    public Lists renameList(Lists list){
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/lists/rename").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(list, APPLICATION_JSON), Lists.class);
    }

    /**
     * Method that removes a list from the database
     * @param list the list to be deleted
     * @return the response object
     */
    public Lists removeList(Lists list){
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/lists/remove").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(list, APPLICATION_JSON), Lists.class);
    }

    /**
     * Method to add a new card to the database
     * @param card the card to be added
     * @return the response object
     */
    public Cards addCard(Cards card){
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/cards").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(card, APPLICATION_JSON), Cards.class);
    }

    /**
     * Method that removes card from the database
     * @param card the card to be deleted
     * @return the response object
     */
    public Cards removeCard(Cards card){
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/cards/remove").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(card, APPLICATION_JSON_TYPE), Cards.class);
    }

    /**
     * Method that renames a card in the database
     * @param card the card to be renamed with new properties
     * @return the response object
     */
    public Cards renameCard(Cards card){
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/cards/rename").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(card, APPLICATION_JSON), Cards.class);
    }

    /**
     * Method that moves card to new list or within list
     * @param card the card to be moved
     * @return the response object
     */
    public Cards moveCard(Cards card){
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/cards/move").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(card, APPLICATION_JSON), Cards.class);
    }

    /**
     * Method to find the card from the server by its id and return
     * this Cards object
     * @param cardId the id of the card to be found
     * @return the Cards object of the card with the given id
     */
    public Cards getCardById(long cardId) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverAddress).path("api/cards/get/" + cardId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<Cards>(){});
    }

    /**
     * Method that adds Subtask to the database
     * @param subtask the subtask to be added
     * @return the response object
     */
    public Subtask addSubtask(Subtask subtask){
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/subtask").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(subtask, APPLICATION_JSON), Subtask.class);
    }

    /**
     * Method that removes subtask from the database
     * @param subtask the subtask to be deleted
     * @return the response object
     */
    public Subtask deleteSubtask(Subtask subtask){
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/subtask/remove").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(subtask, APPLICATION_JSON), Subtask.class);
    }

    /**
     * Method that retrieves all lists from the serverAddress
     * @return a list of all lists in the database
     */
    public List<Lists> getLists() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(serverAddress).path("api/lists") //
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
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
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
                .target(serverAddress).path("api/lists/all/" + boardName) //
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
                .target(serverAddress).path("api/boards/all")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Boards>>() {});
    }

    /**
     * Method that renames a board
     * @param board the new board to be saved with a changed name
     * @return the new board
     */
    public Boards renameBoard(Boards board){
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/boards/rename").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(board, APPLICATION_JSON), Boards.class);
    }

    /**
     * Updates the information of a board
     * The constraint is that the name remains the same
     *
     * @param board The board to be updated
     * @return the updated board
     */
    public Boards updateBoard(Boards board) {
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/boards/update").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(board, APPLICATION_JSON), Boards.class);
    }

    /**
     * Setter method for the server attribute
     * @param server the server address to be set
     */
    public void setServer(String server){
        serverAddress = server;
    }

    /**
     * Getter for the serverAddress property
     *
     * @return the serverAddress property
     */
    public String getServer() {
        return serverAddress;
    }

    /**
     * Method for connecting websockets to a particular Talio server
     *
     */
    public void setWebsockets() {
        session = connect(serverAddress.replaceFirst("http", "ws") + "/websocket");
    }

    /**
     * Setter method for the username attribute
     * @param username the username to be set
     */
    public void setUsername(String username) { this.username = username;}

    /**
     * Sends a simple get request to /api/test-connection in order to check if the
     * provided URL is a running instance of a Talio server
     *
     * @return True iff the client-server connection can be established
     */
    public boolean checkServer(){
        try {
            Response response = ClientBuilder.newClient(new ClientConfig())
                    .target(serverAddress).path("api/test-connection")
                    .request(TEXT_PLAIN)
                    .accept(TEXT_PLAIN)
                    .get();
            return response.getStatus() == 200;
        }catch(Exception e) {
            return false;
        }
    }

    private StompSession session =  null;

    /**
     * Connect method for websockets
     * @param url the url for the websockets
     * @return StompSession
     */
    public StompSession connect(String url){
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
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/user/refreshAdmin").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(user, APPLICATION_JSON), User.class);
    }

    /**
     * Method that removes Board from database
     * @param board the board to be removed
     * @return the response object
     */
    public Boards removeBoard(Boards board){
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/boards/remove/").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(board, APPLICATION_JSON), Boards.class);
    }

    /**
     * Method that removes Board from user
     * @param board the board to be removed
     * @return the response object
     */
    public User hideBoardFromUser(Boards board){
        // get the current user using the saved USERNAME
        User user = ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/user/find/" + username).
                request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .get(new GenericType<User>() {});

        // if the user has the board in their list of boards, remove it
        if(user.boards != null && user.boards.size() != 0 && user.boards.contains(board)){
            user.boards.remove(board);
        }

        // send the updated user to the server so that the database is changed too
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/user/update").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(user, APPLICATION_JSON), User.class);
    }


    /**
     * Method that checks whether a user is an admin
     * @return true if the user is admin, false otherwise
     */
    public boolean checkAdmin() {
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/user/find/" + username).
                request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .get(new GenericType<User>() {
                }).isAdmin;
    }

    /**
     * Find the board from the server by boardID and return the Board
     * @param boardID ID of the board to be found.
     * @return Target board
     */
    public Boards getBoardByID(String boardID) {
        return  ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/boards/get/"+boardID).
                request(APPLICATION_JSON).accept(APPLICATION_JSON).
                get(new GenericType<Boards>(){});
    }

    /**
     * By sending the current Board to the server, compare and store the new color information,
     * and update the database
     * @param board User's newly revised board
     * @return new board
     */
    public Boards setBoardCss(Boards board) {
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/boards/setCss/").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(board, APPLICATION_JSON), Boards.class);
    }

    /**
     * Method that adds the current board to the user
     * @param board the current board
     */
    public void addBoardToUser(Boards board){
        // get the current user using the saved USERNAME
        User user = ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/user/find/" + username).
                request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .get(new GenericType<User>() {});

        // if the user has no boards, make a new list
        if(user.boards == null || user.boards.size() == 0) user.boards = new ArrayList<>(){};
        // add the current board to the users list of boards if it isn't already in the list

        boolean hasInList = false;
        for(Boards boards : user.boards)
            if (boards.id == board.id) {
                hasInList = true;
                break;
            }
        if(!hasInList)
            user.boards.add(board);

        updateUser(user);
    }

    /**
     * Method that retrieves all boards a user has viewed
     * @return the list of boards a user has seen
     */
    public List<Boards> viewedBoards(){
        return ClientBuilder.newClient(new ClientConfig())
                .target(serverAddress).path("api/user/boards/" + username)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Boards>>() {});
    }

    /**
     * Update the users attributes to new ones posted
     * @param user the user to be updated
     * @return the user that was updated
     */
    public User updateUser(User user){
        // send the updated user to the server so that the database is changed too
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/user/update").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(user, APPLICATION_JSON), User.class);
    }

    /**
     * Method for removing references from all cards
     * to a specific tag
     *
     * @param tags The tag object we are removing
     * @return The removed tag object
     */
    public Tags removeTagFromCards(Tags tags) {
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/cards/removeTag").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(tags, APPLICATION_JSON), Tags.class);
    }

    /**
     * If the preset of a card was removed
     * it will be reverted back to the default preset
     *
     * @param board Used for obtaining the map and default preset
     * @return Status code 200 if the operation was successful
     */
    public Cards revertPreset(Boards board) {
        return ClientBuilder.newClient(new ClientConfig()).target(serverAddress).
                path("api/cards/revertPreset").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(board, APPLICATION_JSON), Cards.class);
    }

    private static final ExecutorService EXEC = Executors.newCachedThreadPool();

    /** Calls endpoint on backend for long polling constantly when it recieves the board
     * @param consumer - Deleted board...
     */
    public void registerForUpdates(Consumer<Boards> consumer) {

        EXEC.submit(()->{
                while(!EXEC.isShutdown()) {
                    try {
                        var res = ClientBuilder.newClient(new ClientConfig()).
                                target(serverAddress).path("api/boards/longPolling").
                                request(APPLICATION_JSON).
                                accept(APPLICATION_JSON).get(Response.class);
                        if (res.getStatus() == 204) {
                            continue;
                        }
                        var q = res.readEntity(Boards.class);
                        consumer.accept(q);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }

        });


    }

    /**
     * Shutdowns Thread executor service.
     */
    public void stop(){
        EXEC.shutdownNow();
    }

}