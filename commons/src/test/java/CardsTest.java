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

    @BeforeEach
    public void setUp(){
        Boards board = new Boards("Board1", new ArrayList<>());
        Lists list = new Lists("list 1", 1, board);
        list.id = 1;
        Lists list2 = new Lists("list 2", 2, board);
        list2.id = 2;

        List<Subtask> listSubtasks1 = new ArrayList<>();
        List<Subtask> listSubtasks2 = new ArrayList<>();

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
        card6 = new Cards("Card 1", 1, list2,
                "This is Card 5", listSubtasks2);

        Subtask subtask1 = new Subtask(1, "Subtask 1", false, card1, 1);
        Subtask subtask2 = new Subtask(2, "Subtask 2", false, card1, 2);

        listSubtasks1.add(subtask1);
        listSubtasks2.add(subtask2);
    }

    @Test
    void testEqualsFalse(){
        assertFalse(card1.equals(card2));
        assertFalse(card1.equals(card3));
        assertFalse(card1.equals(card4));
        assertFalse(card1.equals(card5));
        assertFalse(card1.equals(card6));
    }

    @Test
    void testEqualsTrue(){
        assertTrue(card1.equals(card1));
        assertTrue(card1.equals(card11));
    }

    @Test
    void testHashCode(){
        int hashcode1 = card1.hashCode();
        int hashcode11 = card11.hashCode();
        int hashcode2 = card2.hashCode();
        int hashcode3 = card3.hashCode();
        int hashcode4 = card4.hashCode();
        int hashcode5 = card5.hashCode();
        int hashcode6 = card6.hashCode();

        assertTrue(card1.hashCode() == hashcode1);
        assertTrue(card1.hashCode() == hashcode11);
        assertFalse(card1.hashCode() == hashcode2);
        assertFalse(card1.hashCode() == hashcode3);
        assertFalse(card1.hashCode() == hashcode4);
        assertFalse(card1.hashCode() == hashcode5);
        assertFalse(card1.hashCode() == hashcode6);

    }

    @Test
    void testToString(){
        card1.subtasks = new ArrayList<>();
        card11.subtasks = new ArrayList<>();
        card2.subtasks = new ArrayList<>();
        card3.subtasks = new ArrayList<>();
        card4.subtasks = new ArrayList<>();
        card5.subtasks = new ArrayList<>();

        String stringCard1 = "Cards{id=0, title='Card 1', positionInsideList=1, " +
                "description='This is Card 1', " +
                "list=Lists{id=1, title='list 1', positionInsideBoard=1, " +
                "cards=, board=Board1}, subtasks=[]}";
        String stringCard11 = "Cards{id=0, title='Card 1', positionInsideList=1, " +
                "description='This is Card 1', " +
                "list=Lists{id=1, title='list 1', positionInsideBoard=1, " +
                "cards=, board=Board1}, subtasks=[]}";
        String stringCard3 = "Cards{id=0, title='Card 1', positionInsideList=3, " +
                "description='This is Card 1', " +
                "list=Lists{id=1, title='list 1', positionInsideBoard=1, " +
                "cards=, board=Board1}, subtasks=[]}";
        String stringCard2 = "Cards{id=0, title='Card 2', positionInsideList=1, " +
                "description='This is Card 1', " +
                "list=Lists{id=1, title='list 1', positionInsideBoard=1, " +
                "cards=, board=Board1}, subtasks=[]}";
        String stringCard4 = "Cards{id=0, title='Card 1', positionInsideList=1, " +
                "description='This is Card 1', " +
                "list=Lists{id=2, title='list 2', positionInsideBoard=2, " +
                "cards=, board=Board1}, subtasks=[]}";
        String stringCard5 = "Cards{id=0, title='Card 1', positionInsideList=1, " +
                "description='This is Card 5', " +
                "list=Lists{id=2, title='list 2', positionInsideBoard=2, " +
                "cards=, board=Board1}, subtasks=[]}";
        String stringCard6 = "Cards{id=0, title='Card 1', positionInsideList=1, " +
                "description='This is Card 5', " +
                "list=Lists{id=2, title='list 2', positionInsideBoard=2, " +
                "cards=, board=Board1}, " +
                "subtasks=[Subtask{id=2, title='Subtask 2', checked=false, " +
                "card=Cards{id=0, title='Card 1', positionInsideList=1, " +
                "description='This is Card 1', " +
                "list=Lists{id=1, title='list 1', positionInsideBoard=1, " +
                "cards=, board=Board1}, subtasks=[]}, position=2}]}";

        assertEquals(card1.toString(), stringCard1);
        assertEquals(card1.toString(), stringCard11);
        assertNotEquals(card1.toString(), stringCard2);
        assertNotEquals(card1.toString(), stringCard3);
        assertNotEquals(card1.toString(), stringCard4);
        assertNotEquals(card1.toString(), stringCard5);
        assertNotEquals(card1.toString(), stringCard6);
    }
}
