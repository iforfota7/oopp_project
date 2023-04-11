import commons.Boards;
import commons.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardsTest {

    Boards board1, board11, board2, board3;
    Lists list1, list2, list11, list3;

    @BeforeEach
    void setUp(){
        list1 = new Lists("List 1", 1, board1);
        list11 = new Lists("List 1", 1, board1);
        list2 = new Lists("List 2", 2, board1);
        list3 = new Lists("List 3", 3, board1);

        List<Lists> lists1 = new ArrayList<>();
        List<Lists> lists2 = new ArrayList<>();

        board1 = new Boards("Board 1", lists1, new ArrayList<>());
        board11 = new Boards("Board 1", lists1, new ArrayList<>());
        board2 = new Boards("Board 2", lists2, new ArrayList<>());
        board3 = new Boards("Board 1", new ArrayList<>(), new ArrayList<>());
    }

    @Test
    void testConstructor(){
        assertNotNull(new Boards("Board", new ArrayList<>(),new ArrayList<>()));
        assertNotNull(new Boards());
        assertNotNull(board2);
    }

    @Test
    void testEqualsTrue() {
        assertEquals(board1, board1);
        assertEquals(board1, board11);
        assertEquals(board1, board3);

        board1.lists.add(list1);
        board11.lists.add(list11);
        assertEquals(board1, board11);

    }

    @Test
    void testEqualsFalse() {
        assertNotEquals(board1, board2);

        board3.lists = null;
        assertNotEquals(board1, board3);

        board3.lists = new ArrayList<>();
        board3.lists.add(list3);
        board1.lists.add(list11);
        assertNotEquals(board1, board3);

        board1.lists.add(list3);
        board3.lists.add(list11);
        assertNotEquals(board1, board3);
    }

    @Test
    void testHashcode() {

        list2 = new Lists("List 2", 2, board1);
        list3 = new Lists("List 3", 3, board1);

        assertEquals(board1.hashCode(), board11.hashCode());
        assertNotEquals(board1.hashCode(), board2.hashCode());
        assertEquals(board1.hashCode(), board3.hashCode());

        board1.lists.add(list3);
        board3.lists.add(list3);
        assertEquals(board1.hashCode(), board3.hashCode());

        board3.lists.clear();
        board1.lists.add(list2);
        board3.lists.add(list2);
        board3.lists.add(list3);
        assertNotEquals(board1.hashCode(), board3.hashCode());
    }

    @Test
    void testToString() {

        list1 = new Lists("List 1", 1, board1);
        list11 = new Lists("List 1", 1, board1);
        list2 = new Lists("List 2", 2, board1);
        list3 = new Lists("List 3", 3, board1);

        board1.lists.add(list1);
        String stringBoard1 = "Boards{id=0, name='Board 1', lists=" +
                "[Lists{id=0, title='List 1', positionInsideBoard=1, " +
                "cards=[], board=Board 1}], boardBgColor='#E6E6FA', boardFt" +
                "Color='#000000', listBgColor='#ffffff', listFtColor='#000000'," +
                " defaultColor='default', colorPreset={default=#e6e6fa #000000}, tags=[]}";

        assertEquals(board1.toString(), stringBoard1);
        assertEquals(board1.toString(), board11.toString());
        assertNotEquals(board1.toString(), board2.toString());

        board1.lists.add(list3);
        board3.lists.add(list3);
        board3.lists.add(list1);
        assertNotEquals(board1.toString(), board3.toString());

        board1.lists.remove(list3);
        board3.lists.clear();
        board3.lists.add(list11);
        assertEquals(board1.toString(), board3.toString());

    }
}
