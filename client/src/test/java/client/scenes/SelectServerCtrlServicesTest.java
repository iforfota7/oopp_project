package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SelectServerCtrlServicesTest {
    private TestServerUtils testServerUtils;
    private SelectServerCtrlServices sut;

    @BeforeEach
    public void setup() {
        testServerUtils = new TestServerUtils();
        sut = new SelectServerCtrlServices();
    }

    @Test
    public void emptyAddressTest(){
        assertNull(sut.checkConnection(null, "test", testServerUtils));
    }

    @Test
    public void testFullAddress(){
        assertNotNull(sut.checkConnection("http://localhost:8080", "test", testServerUtils));
    }

    @Test
    public void portTest(){
        assertNotNull(sut.checkConnection(":8080", "test", testServerUtils));
    }

    @Test
    public void noProtocolTest(){
        assertNotNull(sut.checkConnection("localhost:8080", "test", testServerUtils));
    }

    @Test
    public void emptyUsername(){
        assertNull(sut.checkConnection(":8080", "", testServerUtils));
    }

    @Test
    public void nonExistingServer(){
        assertNull(sut.checkConnection("dfghjk", "test", testServerUtils));
    }

    @Test
    public void newUsername(){
        // likely to have a new username not in the database
        Random random = new Random();
        String username = "" + random.nextInt();
        assertNotNull(sut.checkConnection(":8080", username, testServerUtils));
    }
}