package client.scenes;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertTrue;

class CardCustomizationCtrlTest {

    private MockServerUtils testServerUtils;
    private MockMainCtrl testMainCtrl;
    private MockBoardCtrl testBoardCtrl;
    private CardDetailsCtrl cardDetailsCtrl;

    private BoardCtrl boardCtrl;

    private CardCustomizationCtrl sut;

    @BeforeEach
    public void setUp() {
        testBoardCtrl = new MockBoardCtrl();
        testServerUtils = new MockServerUtils();
        testMainCtrl = new MockMainCtrl();
        cardDetailsCtrl =new CardDetailsCtrl(testServerUtils, testMainCtrl);
        sut = new CardCustomizationCtrl(testBoardCtrl,testMainCtrl,
                testServerUtils, cardDetailsCtrl);
    }

    @Test
    void closeTest() {
        sut.close();
        assertTrue(testMainCtrl.calledMethods.contains("closeStage"));
    }

}