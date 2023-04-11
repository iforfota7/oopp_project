package client.scenes;

import commons.Boards;
import commons.Cards;
import commons.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardDetailsCtrlTest {

    private MockServerUtils testServerUtils;
    private MockMainCtrl testMainCtrl;
    private CardDetailsCtrl sut;

    @BeforeEach
    public void setUp() {

        testServerUtils = new MockServerUtils();
        testMainCtrl = new MockMainCtrl();
        sut = new CardDetailsCtrl(testServerUtils, testMainCtrl);
    }

    @Test
    void swapSubtasksTest() {

        Subtask s1 = new Subtask();
        Subtask s2 = new Subtask();
        Subtask s3 = new Subtask();
        Subtask s4 = new Subtask();

        List<Subtask> subtaskList = new ArrayList<>();
        subtaskList.add(s1);
        subtaskList.add(s2);
        subtaskList.add(s3);
        subtaskList.add(s4);

        assertTrue(sut.swapSubtasksService(subtaskList, 0, 1));
    }

    @Test
    void swapSubtasksOutOfBoundsTest() {

        Subtask s1 = new Subtask();
        Subtask s2 = new Subtask();
        Subtask s3 = new Subtask();
        Subtask s4 = new Subtask();

        List<Subtask> subtaskList = new ArrayList<>();

        assertFalse(sut.swapSubtasksService(subtaskList, 0, 1));
    }

    @Test
    void ensureIdDistinctTest() {

        Cards openedCard = new Cards();

        Subtask s1 = new Subtask();
        Subtask s2 = new Subtask();
        Subtask s3 = new Subtask();
        Subtask s4 = new Subtask();

        openedCard.subtasks = new ArrayList<>();

        openedCard.subtasks.add(s1);
        openedCard.subtasks.add(s2);
        openedCard.subtasks.add(s3);
        openedCard.subtasks.add(s4);

        assertTrue(sut.ensureIdDistinct(openedCard));
    }

    @Test
    void ensureIdDistinctNoSubtasksTest() {

        Cards openedCard = new Cards();

        assertFalse(sut.ensureIdDistinct(openedCard));
    }

    @Test
    void closeTest() {

        Boards board = new Boards();
        sut.setBoard(board);
        boolean result = sut.close();

        assertFalse(result);
        assertTrue(testMainCtrl.calledMethods.contains("closeSecondaryStage"));
        assertTrue(testMainCtrl.calledMethods.contains("showBoard"));
    }
}