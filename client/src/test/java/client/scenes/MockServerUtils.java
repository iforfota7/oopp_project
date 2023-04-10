package client.scenes;

import client.utils.ServerUtils;
import commons.Boards;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.function.Consumer;

public class MockServerUtils extends ServerUtils {

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

    /**
     * Mocks the behaviour of the registerForUpdates method in ServerUtils
     * @param consumer - Deleted board...
     */
    @Override
    public void registerForUpdates(Consumer<Boards> consumer) {}
}
