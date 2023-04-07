package client.scenes;

import javafx.scene.layout.HBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdListCtrlTest {

    private TestBoardCtrl testBoardCtrl;
    private TestMainCtrl testMainCtrl;
    private TestAdListCtrl testAdListCtrl;
    private AdListCtrlServices sut;

    @BeforeEach
    public void setup() {
        testBoardCtrl = new TestBoardCtrl();
        testMainCtrl = new TestMainCtrl();
        sut = new AdListCtrlServices();
        testAdListCtrl = new TestAdListCtrl(testBoardCtrl, testMainCtrl, sut);
        sut.setCtrl(testAdListCtrl, testBoardCtrl, testMainCtrl);
    }

    @Test
    public void emptyTitle() {
        assertFalse(sut.saveNewList(""));
        assertTrue(testAdListCtrl.warning.isVisible());
        assertFalse(testAdListCtrl.calledMethods.contains("setNewListName"));
        assertFalse(testMainCtrl.calledMethods.contains("closeSecondaryStage"));
    }

    @Test
    public void nonEmptyTitle() {
        testBoardCtrl.firstRow.getChildren().add(new HBox());

        assertTrue(sut.saveNewList("title"));
        assertFalse(testAdListCtrl.warning.isVisible());
        assertTrue(testAdListCtrl.calledMethods.contains("setNewListName"));
        assertTrue(testMainCtrl.calledMethods.contains("closeSecondaryStage"));
        assertTrue(testBoardCtrl.calledMethods.contains("addListToBoard 1"));
    }
}