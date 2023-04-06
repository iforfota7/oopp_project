package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdListCtrlTest {

    private TestBoardCtrl testBoardCtrl;
    private TestMainCtrl testMainCtrl;
    private AdListCtrlServices adListCtrlServices;
    private AdListCtrl sut;

    @BeforeEach
    public void setup() {
        testBoardCtrl = new TestBoardCtrl();
        testMainCtrl = new TestMainCtrl();
        adListCtrlServices = new AdListCtrlServices();
        sut = new AdListCtrl(testBoardCtrl,testMainCtrl, adListCtrlServices);
    }

    @Test
    void addListToBoard() {

        assertTrue(sut.addListToBoard("pasians",0));
    }

    @Test
    void getPositionOfListInsideBoard() {

        assertEquals(0, sut.getPositionOfListInsideBoard());
    }
}