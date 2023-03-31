import commons.Boards;
import commons.Cards;
import commons.Lists;
import commons.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskTest {
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    Subtask subtask4;
    Subtask subtask11;
    Subtask subtask5;

    @BeforeEach
    void setUp(){
        Boards board = new Boards("Board1", new ArrayList<>());
        Lists list = new Lists("list 1", 1, board);
        Cards card1 = new Cards("Card 1", 1, list,
                "", new ArrayList<>());
        Cards card2 = new Cards("Card 2", 2, list,
                "", new ArrayList<>());
        card2.id = 1;
        subtask1 = new Subtask(1, "Subtask 1", false, card1, 1);
        subtask11 = new Subtask(1, "Subtask 1", false, card1, 1);
        subtask2 = new Subtask(1, "Subtask 2", false, card1, 1);
        subtask3 = new Subtask(1, "Subtask 1", true, card1, 1);
        subtask4 = new Subtask(1, "Subtask 1", false, card2, 1);
        subtask5 = new Subtask(1, "Subtask 1", false, card1, 5);

    }

    @Test
    void testEqualsTrue() {
        assertTrue(subtask1.equals(subtask1));
        assertTrue(subtask1.equals(subtask11));
    }

    @Test
    void testEqualsFalse() {
        assertFalse(subtask1.equals(subtask2));
        assertFalse(subtask1.equals(subtask3));
        assertFalse(subtask1.equals(subtask4));
        assertFalse(subtask1.equals(subtask5));
    }

    @Test
    void testHashCode() {
        int hashcode1 = subtask1.hashCode();
        int hashcode11 = subtask11.hashCode();
        int hashcode2 = subtask2.hashCode();
        int hashcode3 = subtask3.hashCode();
        int hashcode4 = subtask4.hashCode();
        int hashcode5 = subtask5.hashCode();

        assertFalse(hashcode1 == hashcode2);
        assertFalse(hashcode1 == hashcode3);
        assertFalse(hashcode1 == hashcode4);
        assertFalse(hashcode1 == hashcode5);
        assertTrue(hashcode1 == hashcode11);
    }

    @Test
    void testToString() {
        String stringSubtask1 = "Subtask{id=1, title='Subtask 1', checked=false, " +
                "card=Cards{id=0, title='Card 1', positionInsideList=1, description='', " +
                "list=Lists{id=0, title='list 1', positionInsideBoard=1, " +
                "cards=, board=Board1}, subtasks=[]}, position=1}";
        String stringSubtask2 = "Subtask{id=1, title='Subtask 2', checked=false, " +
                "card=Cards{id=0, title='Card 1', positionInsideList=1, description='', " +
                "list=Lists{id=0, title='list 1', positionInsideBoard=1, " +
                "cards=, board=Board1}, subtasks=[]}, position=1}";
        String stringSubtask3 = "Subtask{id=1, title='Subtask 1', checked=true, " +
                "card=Cards{id=0, title='Card 1', positionInsideList=1, description='', " +
                "list=Lists{id=0, title='list 1', positionInsideBoard=1, " +
                "cards=, board=Board1}, subtasks=[]}, position=1}";
        String stringSubtask4 = "Subtask{id=1, title='Subtask 1', checked=false, " +
                "card=Cards{id=1, title='Card 2', positionInsideList=2, description='', " +
                "list=Lists{id=0, title='list 1', positionInsideBoard=1, " +
                "cards=, board=Board1}, subtasks=[]}, position=1}";
        String stringSubtask11 = "Subtask{id=1, title='Subtask 1', checked=false, " +
                "card=Cards{id=0, title='Card 1', positionInsideList=1, description='', " +
                "list=Lists{id=0, title='list 1', positionInsideBoard=1, " +
                "cards=, board=Board1}, subtasks=[]}, position=1}";
        String  stringSubtask5 = "Subtask{id=1, title='Subtask 1', checked=false, " +
                "card=Cards{id=0, title='Card 1', positionInsideList=1, description='', " +
                "list=Lists{id=0, title='list 1', positionInsideBoard=1, " +
                "cards=, board=Board1}, subtasks=[]}, position=5}";
        assertEquals(subtask1.toString(), stringSubtask1);
        assertEquals(subtask1.toString(), stringSubtask11);
        assertNotEquals(subtask1.toString(), stringSubtask2);
        assertNotEquals(subtask1.toString(), stringSubtask3);
        assertNotEquals(subtask1.toString(), stringSubtask4);
        assertNotEquals(subtask1.toString(), stringSubtask5);
    }

}
