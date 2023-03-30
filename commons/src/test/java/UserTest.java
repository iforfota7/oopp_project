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
    Boards board1;
    Boards board2;

    @BeforeEach
    void setUp(){

        board1 = new Boards("Board 1", null);
        board2 = new Boards("Board 2", null);

        List<Boards> boards1 = new ArrayList<>();
        boards1.add(board1);
        List<Boards> boards2 = new ArrayList<>();
        boards2.add(board2);

        user1 = new User("iforfota7", boards1, true);
        user11 = new User("iforfota7", boards1, true);
        user2 = new User("iforfota77", boards1, true);
        user3 = new User("iforfota7", boards2, true);
        user4 = new User("iforfota7", boards1, false);
    }

    @Test
    void testEqualsTrue(){
        assertTrue(user1.equals(user1));
        assertTrue(user1.equals(user11));
    }

    @Test
    void testEqualsFalse(){
        assertFalse(user1.equals(user2));
        assertFalse(user1.equals(user3));
        assertFalse(user1.equals(user4));
    }

    @Test
    void testHashCode(){
        int hashCode1 = user1.hashCode();
        int hashCode11 = user11.hashCode();
        int hashCode2 = user2.hashCode();
        int hashCode3 = user3.hashCode();
        int hashCode4 = user4.hashCode();

        assertTrue(user1.hashCode() == hashCode1);
        assertTrue(user1.hashCode() == hashCode11);
        assertFalse(user1.hashCode() == hashCode2);
        assertFalse(user1.hashCode() == hashCode3);
        assertFalse(user1.hashCode() == hashCode4);
    }

    @Test
    void testToString(){
        String stringUser1 = "User{username='iforfota7', boards=[Boards{id=0, name='Board 1', lists=null}], isAdmin=true}";
        String stringUser11 = "User{username='iforfota7', boards=[Boards{id=0, name='Board 1', lists=null}], isAdmin=true}";
        String stringUser2 = "User{username='iforfota77', boards=[Boards{id=0, name='Board 1', lists=null}], isAdmin=true}";
        String stringUser3 = "User{username='iforfota7', boards=[Boards{id=0, name='Board 2', lists=null}], isAdmin=true}";
        String stringUser4 = "User{username='iforfota7', boards=[Boards{id=0, name='Board 1', lists=null}], isAdmin=false}";

        assertEquals(user1.toString(), stringUser1);
        assertEquals(user1.toString(), stringUser11);
        assertNotEquals(user1.toString(), stringUser2);
        assertNotEquals(user1.toString(), stringUser3);
        assertNotEquals(user1.toString(), stringUser4);
    }
}
