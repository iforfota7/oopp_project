package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SelectServerCtrlServicesTest {
    private MockServerUtils mockServerUtils;
    private SelectServerCtrlServices sut;

    @BeforeEach
    public void setup() {
        mockServerUtils = new MockServerUtils();
        sut = new SelectServerCtrlServices();
    }

    @Test
    public void emptyAddressTest(){
        assertNull(sut.checkConnection(null, "test", mockServerUtils));
    }

    @Test
    public void testFullAddress(){
        assertNotNull(sut.checkConnection("http://localhost:8080", "test", mockServerUtils));
    }

    @Test
    public void portTest(){
        assertNotNull(sut.checkConnection(":8080", "test", mockServerUtils));
    }

    @Test
    public void noProtocolTest(){
        assertNotNull(sut.checkConnection("localhost:8080", "test", mockServerUtils));
    }

    @Test
    public void emptyUsername(){
        assertNull(sut.checkConnection(":8080", "", mockServerUtils));
    }

    @Test
    public void nonExistingServer(){
        assertNull(sut.checkConnection("dfghjk", "test", mockServerUtils));
    }

    @Test
    public void newUsername(){
        // likely to have a new username not in the database
        Random random = new Random();
        String username = "" + random.nextInt();
        assertNotNull(sut.checkConnection(":8080", username, mockServerUtils));
    }
}