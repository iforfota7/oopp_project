package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeCardCtrlTest {
    private MockBoardCtrl mockBoardCtrl;
    private DeCardCtrl sut;

    /**
     * Execute this before each test
     */
    @BeforeEach
    public void setup() {
        mockBoardCtrl = new MockBoardCtrl();
        sut = new DeCardCtrl(mockBoardCtrl);
    }

    @Test
    void constructorTest(){
        assertTrue(sut != null);
    }

    @Test
    void deleteCardFromList() {
        assertTrue(sut.deleteCard());
    }

    @Test
    void undeleteCardFromList() {
        assertTrue(sut.undeleteCard());
    }
}
