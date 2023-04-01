import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    User user1;
    User user11;
    User user2;
    User user3;
    User user4;
    Boards board1, board2, board3;

    @BeforeEach
    void setUp(){

        board1 = new Boards("Board 1", new ArrayList<>());
        board2 = new Boards("Board 2", new ArrayList<>());
        board3 = new Boards("Board 1", new ArrayList<>());

        List<Boards> boards1 = new ArrayList<>();
        List<Boards> boards2 = new ArrayList<>();

        user1 = new User("iforfota7", boards1, true);
        user11 = new User("iforfota7", boards1, true);
        user2 = new User("iforfota77", boards1, true);
        user3 = new User("iforfota7", boards2, true);
        user4 = new User("iforfota7", boards1, false);
    }

    @Test
    void testEqualsTrue(){
        user1.boards.add(board1);

        assertEquals(user1, user1);
        assertEquals(user1, user11);

        user3.boards.add(board3);
        assertEquals(user1, user3);
    }

    @Test
    void testEqualsFalse(){
        user1.boards.add(board1);
        user3.boards.add(board2);

        assertNotEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(user1, user4);

        user1.boards.add(board2);
        user3.boards.add(board1);
        assertNotEquals(user1, user3);

        user1.boards = new ArrayList<>();
        user3.boards = null;
        assertNotEquals(user1, user3);

        user1.boards.add(board1);
        assertNotEquals(user1, user11);
    }

    @Test
    void testHashCode(){
        user1.boards.add(board1);
        user3.boards.add(board2);

        int hashCode1 = user1.hashCode();

        assertEquals(user1.hashCode(), hashCode1);
        assertEquals(user1.hashCode(), user11.hashCode());
        assertNotEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
        assertNotEquals(user1.hashCode(), user4.hashCode());

        user3.boards.add(board3);
        user1.boards.add(board2);
        assertNotEquals(user1.hashCode(), user3.hashCode());

        user3.boards.remove(board2);
        user3.boards.add(board2);
        assertEquals(user1.hashCode(), user3.hashCode());

        user3.boards.remove(board2);
        user3.boards.add(board1);
        user1.boards.remove(board2);
        user1.boards.add(board3);
        assertEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    void testToString(){

        user1.boards.add(board1);
        user3.boards.add(board2);

        String stringUser1 = "User{username='iforfota7', " +
                "boards=[Boards{id=0, name='Board 1', lists=[]}], isAdmin=true}";

        assertEquals(user1.toString(), stringUser1);
        assertEquals(user1.toString(), user11.toString());
        assertNotEquals(user1.toString(), user2.toString());
        assertNotEquals(user1.toString(), user3.toString());

        user3.boards.add(board1);
        user1.boards.add(board2);
        assertNotEquals(user1.toString(), user3.toString());

        user3.boards.remove(board2);
        user3.boards.add(board2);
        assertEquals(user1.toString(), user3.toString());

        user3.boards.clear();
        user3.boards.add(board3);
        user3.boards.add(board2);
        assertEquals(user1.toString(), user3.toString());

        user3.boards = null;
        user1.boards = new ArrayList<>();
        assertNotEquals(user1.toString(), user3.toString());

    }
}
