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
    Cards card1;
    Cards card2;

    @BeforeEach
    void setUp(){
        Boards board = new Boards("Board1", new ArrayList<>());
        Lists list = new Lists("list 1", 1, board);
        card1 = new Cards("Card 1", 1, list,
                "", new ArrayList<>());
        card2 = new Cards("Card 2", 2, list,
                "", new ArrayList<>());
        card2.id = 1;
        subtask1 = new Subtask("Subtask 1", false, card1, 1);
        subtask11 = new Subtask("Subtask 1", false, card1, 1);
        subtask2 = new Subtask("Subtask 2", false, card1, 1);
        subtask3 = new Subtask("Subtask 1", true, card1, 1);
        subtask4 = new Subtask("Subtask 1", false, card2, 1);
        subtask5 = new Subtask("Subtask 1", false, card1, 5);
    }

    @Test
    void testEqualsTrue() {
        assertEquals(subtask1, subtask1);
        assertEquals(subtask1, subtask11);

        long temp = card2.id;
        card2.id = card1.id;

        assertEquals(subtask1, subtask4);

        card2.id = temp;

        subtask11.card.id = 3;
        assertEquals(subtask1, subtask11);
    }

    @Test
    void testEqualsFalse() {
        assertNotEquals(subtask1, subtask2);
        assertNotEquals(subtask1, subtask3);
        assertNotEquals(subtask1, subtask4);
        assertNotEquals(subtask1, subtask5);
    }

    @Test
    void testHashCode() {
        int hashcode1 = subtask1.hashCode();
        int hashcode11 = subtask11.hashCode();
        int hashcode2 = subtask2.hashCode();
        int hashcode3 = subtask3.hashCode();
        int hashcode4 = subtask4.hashCode();
        int hashcode5 = subtask5.hashCode();

        assertNotEquals(hashcode1, hashcode2);
        assertNotEquals(hashcode1, hashcode3);
        assertNotEquals(hashcode1, hashcode4);
        assertNotEquals(hashcode1, hashcode5);
        assertEquals(hashcode1, hashcode11);

        subtask11.card = card2;
        assertNotEquals(subtask1.hashCode(), subtask11.hashCode());
    }

    @Test
    void testToString() {
        String stringSubtask1 = "Subtask{id=0, title='Subtask 1', " +
                "checked=false, card.id=0, position=1}";
        String stringSubtask2 = subtask2.toString();
        String stringSubtask3 = subtask3.toString();
        String stringSubtask4 = subtask4.toString();
        String stringSubtask11 = subtask11.toString();
        String stringSubtask5 = subtask5.toString();

        assertEquals(subtask1.toString(), stringSubtask1);
        assertEquals(subtask1.toString(), stringSubtask11);
        assertNotEquals(subtask1.toString(), stringSubtask2);
        assertNotEquals(subtask1.toString(), stringSubtask3);
        assertNotEquals(subtask1.toString(), stringSubtask4);
        assertNotEquals(subtask1.toString(), stringSubtask5);

        subtask1.position = 3;
        String stringSubtask6 = subtask1.toString();

        assertEquals(subtask1.toString(), stringSubtask6);
        assertNotEquals(subtask1.toString(), stringSubtask1);
    }

}
