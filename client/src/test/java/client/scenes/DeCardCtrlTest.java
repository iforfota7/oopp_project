package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeCardCtrlTest {
    private TestBoardCtrl testBoardCtrl;
    private DeCardCtrl sut;

    @BeforeEach
    public void setup() {
        testBoardCtrl = new TestBoardCtrl();
        sut = new DeCardCtrl(testBoardCtrl);
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
