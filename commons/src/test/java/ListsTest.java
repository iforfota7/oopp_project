import commons.Boards;
import commons.Cards;
import commons.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ListsTest {

    Lists list1, list11, list2, list3, list4;
    Cards card1, card2, card3, card11;
    Boards board1;
    Boards board2;

    @BeforeEach
    void setUp() {

        board1 = new Boards("Board1", new ArrayList<>());
        board2 = new Boards("Board2", new ArrayList<>());

        list1 = new Lists("List 1", 1, board1);
        list11 = new Lists("List 1", 1, board1);
        list2 = new Lists("List 2", 1, board1);
        list3 = new Lists("List 1", 3, board1);
        list4 = new Lists("List 1", 1, board2);

        card1 = new Cards("Card 1", 1,
                list1, "", new ArrayList<>());

        card2 = new Cards("Card 2", 1,
                list2, "", new ArrayList<>());

        card3 = new Cards("Card 1", 3,
                list3, "", new ArrayList<>());

        card11 = new Cards("Card 1", 1,
                list11, "", new ArrayList<>());
    }

    @Test
    void testEqualsTrue() {
        assertEquals(list1, list1);
        assertEquals(list1, list11);

        list1.cards.add(card1);
        list11.cards.add(card1);
        assertEquals(list1, list11);

        list11.cards.clear();
        list11.cards.add(card11);
        assertEquals(list1, list11);

        board2.name = board1.name;
        list4.cards.add(card1);
        assertEquals(list1, list4);

        list1.cards.add(card3);
        list11.cards.add(card3);
        assertEquals(list1, list11);

    }

    @Test
    void testEqualsFalse() {
        assertNotEquals(list1, list2);
        assertNotEquals(list1, list3);
        assertNotEquals(list1, list4);

        list11.cards.add(card11);
        assertNotEquals(list1, list11);

        list1.cards.add(card3);
        list11.cards.add(card3);
        assertNotEquals(list1, list11);

        list2.cards.add(card2);
        assertNotEquals(list1, list2);

        list11.cards.clear();
        list1.cards = null;
        assertNotEquals(list1, list11);

        list11.cards.remove(card11);
        list11.cards.add(card11);
        assertNotEquals(list1, list11);
    }

    @Test
    void testHashCode() {

        assertEquals(list1.hashCode(), list11.hashCode());
        assertNotEquals(list1.hashCode(), list2.hashCode());
        assertNotEquals(list1.hashCode(), list3.hashCode());
        assertNotEquals(list1.hashCode(), list4.hashCode());

        list11.cards.add(card2);
        assertNotEquals(list1.hashCode(), list11.hashCode());

        board2.name = board1.name;
        list1.cards.add(card1);
        list4.cards.add(card1);
        assertEquals(list1.hashCode(), list4.hashCode());

        list1.cards.add(card2);
        list11.cards.add(card1);
        assertNotEquals(list1.hashCode(), list11.hashCode());

    }

    @Test
    void testToString() {
        String stringList1 = "Lists{id=0, title='List 1', " +
                "positionInsideBoard=1, cards=[], board=Board1}";

        assertEquals(list1.toString(), stringList1);
        assertEquals(list11.toString(), list11.toString());
        assertNotEquals(list1.toString(), list2.toString());
        assertNotEquals(list1.toString(), list3.toString());
        assertNotEquals(list1.toString(), list4.toString());

        list1.cards.add(card1);
        list11.cards.add(card11);
        assertEquals(list1.toString(), list11.toString());

        list11.cards.add(card1);
        assertNotEquals(list1.toString(), list11.toString());

        list11.cards.clear();
        list1.cards.add(card2);
        list11.cards.add(card2);
        list11.cards.add(card1);
        assertNotEquals(list1.toString(), list11.toString());
    }
}
