package server.api;


import commons.Boards;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.AbstractMessageChannel;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Random;

public class UserControllerTest {

    public long userCount;

    public TestUserRepository repo;

    public UserController sut;

    public SimpMessagingTemplate msgs;



    @BeforeEach
    public void setup(){
        this.userCount = 0;
        repo = new TestUserRepository();
        msgs = new SimpMessagingTemplate(new AbstractMessageChannel() {
            @Override
            protected boolean sendInternal(Message<?> message, long timeout) {
                return true;
            }
        });

        sut = new UserController(repo, msgs);
        }


   @Test
    void addUserTest(){

    User u1 = getUser("eee", false);
    sut.addUser(u1);
    User u2 = getUser("uuu", true);
    sut.addUser(u2);



    assertEquals(u1, sut.findUser("eee"));

    assertEquals(u2, sut.findUser("uuu"));

    }

    @Test
    void addNullUser(){
        assertEquals(ResponseEntity.badRequest().build(), sut.addUser(null));
    }

    @Test
    void addNullNameUser(){
        assertEquals(ResponseEntity.badRequest().build(), sut.addUser(getUser(null, false)));

    }

    @Test
    void addEmptyNameUser(){
        assertEquals(ResponseEntity.badRequest().build(), sut.addUser(getUser("", false)));

    }

    @Test
    void updateUser(){
        User u1 = getUser("eee", false);
        sut.addUser(u1);

        User u2 = getUser("eee", true);
        sut.updateUser(u2);
    }

    /**Creates new user for the repo
     * @param username String username
     * @param admin boolean admin
     * @return - new user
     */
    public User getUser(String username, boolean admin){

    User user = new User(username, null, admin);
    userCount++;
    return user;

}


@Test
void checkPasswordNull(){
        String password = sut.passwordCreate();
        assertEquals(null, sut.checkPassword(null));
}



@Test
void checkPasswordRandom(){
        String password = sut.passwordCreate();
        assertEquals("true", sut.checkPassword(password));
}

@Test
void checkWrongPassword(){
        String password = sut.passwordCreate();
        assertEquals(null, sut.checkPassword(password+"sdsdv"));
}


@Test
    void userGetPassword(){
        Random r = new Random(100);
    String password = r.ints(48, 123)
            .filter(i -> (i <= 57 || i >= 65 && i <= 90 || i >= 97))
            .limit(15)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
        assertEquals(password, sut.passwordCreate());
}


@Test
    void refreshAdminUser(){
        assertEquals(ResponseEntity.badRequest().build(), sut.refreshAdmin(null));
}

@Test
    void refreshAdminUserEmpty(){
    assertEquals(ResponseEntity.badRequest().build(), sut.refreshAdmin(getUser("", true)));
}

@Test
    void refreshAdminUserNull(){
    assertEquals(ResponseEntity.badRequest().build(), sut.refreshAdmin(getUser(null, false)));

}

    @Test
    void refreshAdmin(){
        User u = getUser("fff", true);
        sut.addUser(u);
        assertEquals(ResponseEntity.ok().build(), sut.refreshAdmin(u));
    }

    @Test
    void getAllBoards(){
        User u = getUser("fff", true);
        u.boards = new ArrayList<>();
        u.boards.add(new Boards("fsfsd", null, null));
        u.boards.add(new Boards("assbd", null, null));
        sut.addUser(u);
        assertEquals(u.boards, sut.getAllBoards(u.username));
    }

}


