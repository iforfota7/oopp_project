package client.scenes;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertTrue;

class CardCustomizationCtrlTest {

    private MockServerUtils testServerUtils;
    private MockMainCtrl testMainCtrl;
    private MockBoardCtrl testBoardCtrl;
    private CardDetailsCtrl sut;

    private BoardCtrl boardCtrl;

    private CardCustomizationCtrl ccc;

    @BeforeEach
    public void setUp() {
        testBoardCtrl = new MockBoardCtrl();
        testServerUtils = new MockServerUtils();
        testMainCtrl = new MockMainCtrl();
        sut =new CardDetailsCtrl(testServerUtils, testMainCtrl);
        ccc= new MockCardCustomizationCtrl(testBoardCtrl,testMainCtrl,testServerUtils,sut);
    }

    @Test
    void closeTest() {
        ccc.close();
        assertTrue(testMainCtrl.calledMethods.contains("closeStage"));
    }

}