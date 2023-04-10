package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertFalse(sut.checkConnection(null, "test", mockServerUtils));
    }

    @Test
    public void testFullAddress(){
        assertTrue(sut.checkConnection("http://localhost:8080", "test", mockServerUtils));
    }

    @Test
    public void portTest(){
        assertTrue(sut.checkConnection(":8080", "test", mockServerUtils));
    }

    @Test
    public void noProtocolTest(){
        assertTrue(sut.checkConnection("localhost:8080", "test", mockServerUtils));
    }

    @Test
    public void emptyUsername(){
        assertFalse(sut.checkConnection(":8080", "", mockServerUtils));
    }

    @Test
    public void nonExistingServer(){
        assertFalse(sut.checkConnection("dfghjk", "test", mockServerUtils));
    }

    @Test
    public void newUsername(){
        String username = "user"; // since method overriden to only have "test" be correct
        assertTrue(sut.checkConnection(":8080", username, mockServerUtils));
    }
}