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

    @BeforeEach
    void setUp(){
        Boards board = new Boards("Board1", new ArrayList<>());
        Lists list = new Lists("list 1", 1, board);
        Cards card1 = new Cards("Card 1", 1, list,
                "", new ArrayList<>());
        Cards card2 = new Cards("Card 2", 2, list,
                "", new ArrayList<>());
        card2.id = 1;
        subtask1 = new Subtask("Subtask 1", false, card1);
        subtask11 = new Subtask("Subtask 1", false, card1);
        subtask2 = new Subtask("Subtask 2", false, card1);
        subtask3 = new Subtask("Subtask 1", true, card1);
        subtask4 = new Subtask("Subtask 1", false, card2);

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
    }

    @Test
    void testHashCode() {
        int hashcode1 = subtask1.hashCode();
        int hashcode11 = subtask11.hashCode();
        int hashcode2 = subtask2.hashCode();
        int hashcode3 = subtask3.hashCode();
        int hashcode4 = subtask4.hashCode();

        assertFalse(hashcode1 == hashcode2);
        assertFalse(hashcode1 == hashcode3);
        assertFalse(hashcode1 == hashcode4);
        assertTrue(hashcode1 == hashcode11);
    }

    @Test
    void testToString() {
        String stringSubtask1 = "Subtask{id=0, title='Subtask 1', " +
                "checked=false, card.id=0}";
        String stringSubtask2 = "Subtask{id=0, title='Subtask 2', " +
                "checked=false, card.id=0}";
        String stringSubtask3 = "Subtask{id=0, title='Subtask 1', " +
                "checked=true, card.id=0}";
        String stringSubtask4 = "Subtask{id=0, title='Subtask 1', " +
                "checked=false, card.id=1}";
        String stringSubtask11 = "Subtask{id=0, title='Subtask 1', " +
                "checked=false, card.id=0}";
        assertEquals(subtask1.toString(), stringSubtask1);
        assertNotEquals(subtask1.toString(), stringSubtask2);
        assertNotEquals(subtask1.toString(), stringSubtask3);
        assertNotEquals(subtask1.toString(), stringSubtask4);
        assertEquals(subtask11.toString(), stringSubtask11);
    }

}
