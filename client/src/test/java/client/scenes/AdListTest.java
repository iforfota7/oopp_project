package client.scenes;

import javafx.scene.layout.HBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdListTest {

    private MockBoardCtrl mockBoardCtrl;
    private MockMainCtrl mockMainCtrl;
    private MockAdListCtrl mockAdListCtrl;
    private AdListCtrlServices sut;

    @BeforeEach
    public void setup() {
        mockBoardCtrl = new MockBoardCtrl();
        mockMainCtrl = new MockMainCtrl();
        sut = new AdListCtrlServices();
        mockAdListCtrl = new MockAdListCtrl(mockBoardCtrl, mockMainCtrl, sut);
        sut.setCtrl(mockAdListCtrl, mockBoardCtrl, mockMainCtrl);
    }

    @Test
    public void emptyTitle() {
        assertFalse(sut.saveNewList(""));
        assertTrue(mockAdListCtrl.warning.isVisible());
        assertFalse(mockAdListCtrl.calledMethods.contains("setNewListName"));
        assertFalse(mockMainCtrl.calledMethods.contains("closeSecondaryStage"));
    }

    @Test
    public void nonEmptyTitle() {
        mockBoardCtrl.firstRow.getChildren().add(new HBox());

        assertTrue(sut.saveNewList("title"));
        assertFalse(mockAdListCtrl.warning.isVisible());
        assertTrue(mockAdListCtrl.calledMethods.contains("setNewListName"));
        assertTrue(mockMainCtrl.calledMethods.contains("closeSecondaryStage"));
        assertTrue(mockBoardCtrl.calledMethods.contains("addListToBoard 1"));
    }

    @Test
    public void closeTest() {
        mockAdListCtrl.close();
        assertTrue(mockAdListCtrl.calledMethods.contains("close"));
    }
}