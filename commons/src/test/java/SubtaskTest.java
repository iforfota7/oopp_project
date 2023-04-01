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
    void testConstructor(){
        assertNotNull(new Subtask("Subtask", true, card1, 1));
        assertNotNull(subtask5);
    }

    @Test
    void testEqualsTrue() {
        assertEquals(subtask1, subtask1);
        assertEquals(subtask1, subtask11);

        card2.id = card1.id;

        assertEquals(subtask1, subtask4);

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

        assertNotEquals(hashcode1, subtask2.hashCode());
        assertNotEquals(hashcode1, subtask3.hashCode());
        assertNotEquals(hashcode1, subtask4.hashCode());
        assertNotEquals(hashcode1, subtask5.hashCode());
        assertEquals(hashcode1, subtask11.hashCode());

        subtask11.card = card2;
        assertNotEquals(subtask1.hashCode(), subtask11.hashCode());
    }

    @Test
    void testToString() {
        String stringSubtask1 = "Subtask{id=0, title='Subtask 1', " +
                "checked=false, card.id=0, position=1}";

        assertEquals(subtask1.toString(), stringSubtask1);
        assertEquals(subtask1.toString(), subtask11.toString());
        assertNotEquals(subtask1.toString(), subtask2.toString());
        assertNotEquals(subtask1.toString(), subtask3.toString());
        assertNotEquals(subtask1.toString(), subtask4.toString());
        assertNotEquals(subtask1.toString(), subtask5.toString());

        subtask1.position = 3;
        String stringSubtask6 = subtask1.toString();

        assertEquals(subtask1.toString(), stringSubtask6);
        assertNotEquals(subtask1.toString(), stringSubtask1);
    }

}
