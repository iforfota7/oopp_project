import commons.Boards;
import commons.Cards;
import commons.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ListsTest {

    Lists list1;
    Lists list11;
    Lists list2;
    Lists list3;
    Lists list4;


    @BeforeEach
    void setUp() {

        Boards board1 = new Boards("Board1", new ArrayList<>());
        Boards board2 = new Boards("Board2", new ArrayList<>());

        list1 = new Lists("List 1", 1, board1);
        list11 = new Lists("List 1", 1, board1);
        list2 = new Lists("List 2", 1, board1);
        list3 = new Lists("List 1", 3, board1);
        list4 = new Lists("List 1", 1, board2);
    }

    @Test
    void testEqualsTrue() {
        assertTrue(list1.equals(list1));
        assertTrue(list1.equals(list11));
    }

    @Test
    void testEqualsFalse() {
        assertFalse(list1.equals(list2));
        assertFalse(list1.equals(list3));
        assertFalse(list1.equals(list4));
    }

    @Test
    void testHashCode() {
        int hashCode1 = list1.hashCode();
        int hashCode11 = list1.hashCode();
        int hashCode2 = list2.hashCode();
        int hashCode3 = list3.hashCode();
        int hashCode4 = list4.hashCode();

        assertTrue(list1.hashCode() == hashCode1);
        assertTrue(list1.hashCode() == hashCode11);
        assertFalse(list1.hashCode() == hashCode2);
        assertFalse(list1.hashCode() == hashCode3);
        assertFalse(list1.hashCode() == hashCode4);
    }

    @Test
    void testToString() {
        String stringList1 = "Lists{id=0, title='List 1', positionInsideBoard=1, cards=, board=Board1}";
        String stringList11 = "Lists{id=0, title='List 1', positionInsideBoard=1, cards=, board=Board1}";
        String stringList2 = "Lists{id=0, title='List 2', positionInsideBoard=1, cards=, board=Board1}";
        String stringList3 = "Lists{id=0, title='List 1', positionInsideBoard=3, cards=, board=Board1}";
        String stringList4 = "Lists{id=0, title='List 1', positionInsideBoard=1, cards=, board=Board2}";

        assertEquals(list1.toString(), stringList1);
        assertEquals(list11.toString(), stringList11);
        assertNotEquals(list1.toString(), stringList2);
        assertNotEquals(list1.toString(), stringList3);
        assertNotEquals(list1.toString(), stringList4);

    }
}
