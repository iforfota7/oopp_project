import commons.Boards;
import commons.Cards;
import commons.Lists;
import commons.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardsTest {

    Cards card1;
    Cards card11;
    Cards card2;
    Cards card3;
    Cards card4;
    Cards card5;
    Cards card6;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    List<Subtask> listSubtasks1;
    List<Subtask> listSubtasks2;
    Lists list;

    @BeforeEach
    void setUp(){
        Boards board = new Boards("Board1", new ArrayList<>(), new ArrayList<>());

        list = new Lists("list 1", 1, board);
        list.id = 1;
        Lists list2 = new Lists("list 2", 2, board);
        list2.id = 2;

        listSubtasks1 = new ArrayList<>();
        listSubtasks2 = new ArrayList<>();

        card1 = new Cards("Card 1", 1, list,
                "This is Card 1", listSubtasks1);
        card11 = new Cards("Card 1", 1, list,
                "This is Card 1", listSubtasks1);
        card2 = new Cards("Card 2", 1, list,
                "This is Card 1", listSubtasks1);
        card3 = new Cards("Card 1", 3, list,
                "This is Card 1", listSubtasks1);
        card4 = new Cards("Card 1", 1, list2,
                "This is Card 1", listSubtasks1);
        card5 = new Cards("Card 1", 1, list2,
                "This is Card 5", listSubtasks1);
        card6 = new Cards("Card 1", 1, list,
                "This is Card 1", listSubtasks2);

        subtask1 = new Subtask("Subtask 1", false, 1);
        subtask2 = new Subtask("Subtask 2", false, 2);
        subtask3 = new Subtask("Subtask 3", false, 1);

        listSubtasks1.add(subtask1);
        listSubtasks2.add(subtask2);
        listSubtasks2.add(subtask3);
    }

    @Test
    void testConstructor(){
        assertNotNull(new Cards("Card", 1,list,
                "This card", listSubtasks1));
        assertNotNull(card5);
        assertNotNull(new Cards());
    }

    @Test
    void testEqualsTrue(){
        assertEquals(card1, card1);
        assertEquals(card1, card11);

        card11.subtasks.add(subtask3);
        assertEquals(card1, card11);

        listSubtasks2.clear();
        listSubtasks2.add(subtask1);
        card11.subtasks.remove(subtask3);
        assertEquals(card1, card6);

        listSubtasks2.add(subtask2);
        listSubtasks2.add(subtask3);

        card11.list.id = 12435;
        assertEquals(card1, card11);
    }

    @Test
    void testEqualsFalse(){
        assertNotEquals(card1, card2);
        assertNotEquals(card1, card3);
        assertNotEquals(card1, card4);
        assertNotEquals(card1, card5);
        assertNotEquals(card1, card6);
    }

    @Test
    void testHashCode(){
        int hashcode1 = card1.hashCode();

        assertEquals(card1.hashCode(), card11.hashCode());
        assertNotEquals(card1.hashCode(), card2.hashCode());
        assertNotEquals(card1.hashCode(), card3.hashCode());
        assertNotEquals(card1.hashCode(), card4.hashCode());
        assertNotEquals(card1.hashCode(), card5.hashCode());
        assertNotEquals(card1.hashCode(), card6.hashCode());

        card11.list.id = 12321;
        assertNotEquals(hashcode1, card11.hashCode());

        listSubtasks1.add(subtask3);
        assertNotEquals(hashcode1, card1.hashCode());

        listSubtasks1.add(subtask2);
        listSubtasks2.add(subtask1);
        assertNotEquals(card6.hashCode(), card1.hashCode());

        listSubtasks1.clear(); listSubtasks2.clear();
        listSubtasks1.add(subtask1);
        listSubtasks2.add(subtask1);
        assertEquals(card1.hashCode(), card6.hashCode());

        listSubtasks2.remove(subtask1);
        listSubtasks2.add(subtask3);
        listSubtasks2.add(subtask2);
    }

    @Test
    void testToString(){
        System.out.println(card1.toString());
        String stringCard1 = "Cards{id=0, title='Card 1', " +
                "positionInsideList=1, description='This is Card 1', " +
                "list=Lists{id=1, title='list 1', positionInsideBoard=1, " +
                "cards=[], board=Board1}, colorStyle='null', subtasks=[Subtask{id=0, " +
                "title='Subtask 1', checked=false, position=1}], tags=[]}";

        assertEquals(card1.toString(), stringCard1);
        assertEquals(card1.toString(), card11.toString());
        assertNotEquals(card1.toString(), card2.toString());

        listSubtasks2.clear();
        listSubtasks2.add(subtask2);
        listSubtasks2.add(subtask1);
        listSubtasks1.add(subtask2);
        assertNotEquals(card1.toString(), card6.toString());

        assertNotEquals(card1.toString(), card6.toString());
        card6.subtasks = listSubtasks1;
        assertEquals(card1.toString(), card6.toString());
    }
}
