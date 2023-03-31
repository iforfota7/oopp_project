import commons.Boards;
import commons.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardsTest {

    Boards board1;
    Boards board11;
    Boards board2;
    Boards board3;

    @BeforeEach
    void setUp(){
        Lists list1 = new Lists("List 1", 1, board1);
        Lists list11 = new Lists("List 1", 1, board1);
        Lists list2 = new Lists("List 2", 2, board1);
        Lists list3 = new Lists("List 3", 3, board1);

        List<Lists> lists1 = new ArrayList<>();
        List<Lists> lists2 = new ArrayList<>();

        lists1.add(list1);
        lists1.add(list2);

        lists2.add(list11);
        lists2.add(list3);

        board1 = new Boards("Board 1", new ArrayList<>());
        board11 = new Boards("Board 1", new ArrayList<>());
        board2 = new Boards("Board 2", new ArrayList<>());
        board3 = new Boards("Board 1", lists1);
    }

    @Test
    void testEqualsTrue() {
        assertTrue(board1.equals(board1));
        assertTrue(board1.equals(board11));
    }

    @Test
    void testEqualsFalse() {
        assertFalse(board1.equals(board2));
        assertFalse(board1.equals(board3));
    }

    @Test
    void testHashcode() {
        int hashcode1 = board1.hashCode();
        int hashcode11 = board11.hashCode();
        int hashcode2 = board2.hashCode();
        int hashcode3 = board3.hashCode();

        assertTrue(board1.hashCode() == hashcode1);
        assertTrue(board1.hashCode() == hashcode11);
        assertFalse(board1.hashCode() == hashcode2);
        assertTrue(board1.hashCode() == hashcode3);
    }

    @Test
    void testToString() {
        String stringBoard1 = "Boards{id=0, name='Board 1', lists=[]}";
        String stringBoard11 = "Boards{id=0, name='Board 1', lists=[]}";
        String stringBoard2 = "Boards{id=0, name='Board 2', lists=[]}";

        assertEquals(board1.toString(), stringBoard1);
        assertEquals(board1.toString(), stringBoard11);
        assertNotEquals(board1.toString(), stringBoard2);
    }
}
