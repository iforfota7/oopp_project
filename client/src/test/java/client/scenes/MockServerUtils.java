package client.scenes;

import client.utils.ServerUtils;
import commons.User;
import commons.Boards;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.function.Consumer;

public class MockServerUtils extends ServerUtils {

    private String serverAddress;
    private String username;

    /**
     * Mocks the behaviour of the connect method in ServerUtils
     * @param url the url for the websockets
     * @return a new instance of StompSession
     */
    @Override
    public StompSession connect(String url){

        return new StompSession() {
            @Override
            public String getSessionId() {
                return null;
            }
            @Override
            public boolean isConnected() {
                return false;
            }
            @Override
            public void setAutoReceipt(boolean enabled) { }
            @Override
            public Receiptable send(String destination, Object payload) {
                return null;
            }
            @Override
            public Receiptable send(StompHeaders headers, Object payload) {
                return null;
            }
            @Override
            public Subscription subscribe(String destination, StompFrameHandler handler) {
                return null;
            }
            @Override
            public Subscription subscribe(StompHeaders headers, StompFrameHandler handler) {
                return null;
            }
            @Override
            public Receiptable acknowledge(String messageId, boolean consumed) {
                return null;
            }
            @Override
            public Receiptable acknowledge(StompHeaders headers, boolean consumed) {
                return null;
            }
            @Override
            public void disconnect() { }
            @Override
            public void disconnect(StompHeaders headers) { }
        };
    }

    /**
     * Mocks the behaviour of the registerForMessages method in ServerUtils
     * @param dest destination
     * @param type the type
     * @param consumer a consumer
     * @param <T> the generic type of the object being returned
     */
    @Override
    public <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer) { }

    @Override
    public User refreshAdmin(User user){
        return user;
    }

    /**
     * Mocks the behaviour of the registerForUpdates method in ServerUtils
     * @param consumer - Deleted board...
     */
    @Override
    public void registerForUpdates(Consumer<Boards> consumer) {}

    @Override
    public void setServer(String server){
        this.serverAddress = server;
    }

    @Override
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * Checks whether the address is http://localhost:8080
     * Mimicking behaviour to test whether the server address is valid
     * @return true if it is, otherwise falls
     */
    @Override
    public boolean checkServer(){
        return (this.serverAddress != null && !this.serverAddress.equals("")
                && this.serverAddress.equals("http://localhost:8080"));
    }

    @Override
    public void setWebsockets(){
    }

    /**
     * Username exists if it is test
     * @return if the username is test, return true, otherwise false
     */
    @Override
    public boolean existsUser(){
        return this.username.equals("test");
    }

    @Override
    public User addUser(User user){
        return user;
    }

    @Override
    public User findUser(){
        return new User(username, null, false);
    }
}
