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

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import commons.Cards;
import commons.Lists;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import commons.Quote;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ServerUtils {

    private static String SERVER;

    public Lists addList(Lists list){
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/lists").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(list, APPLICATION_JSON), Lists.class);
    }

    public Lists removeList(Lists list){
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/lists/remove").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(list, APPLICATION_JSON), Lists.class);
    }

    public Cards addCard(Cards card){
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/cards").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(card, APPLICATION_JSON), Cards.class);
    }

    public Cards removeCard(Cards card){
        return ClientBuilder.newClient(new ClientConfig()).target(SERVER).
                path("api/cards/remove").request(APPLICATION_JSON).accept(APPLICATION_JSON).
                post(Entity.entity(card, APPLICATION_JSON), Cards.class);
    }


    public List<Lists> getLists() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lists") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Lists>>() {});
    }

    public List<Cards> getCards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/cards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Cards>>() {});
    }


    /**
     * Setter method for the server attribute
     * @param server the server address to be set
     */
    public static void setServer(String server){
        SERVER = server;
    }

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

    private StompSession session =  connect("ws://localhost:8080/websocket");

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

    public void send(String dest, Object o){
        session.send(dest, o);
    }


}