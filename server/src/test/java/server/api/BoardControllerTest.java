package server.api;


import commons.Boards;
import commons.Tags;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.AbstractMessageChannel;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class BoardControllerTest {

    public long boardCount;

    public TestBoardsRepository repo;

    public BoardController sut;



    public SimpMessagingTemplate msgs;

    /**
     * COnfiguration before each test
     */
    @BeforeEach
    public void setup(){
        this.boardCount = 0;
        repo =  new TestBoardsRepository();
        msgs = new SimpMessagingTemplate(new AbstractMessageChannel() {
            @Override
            protected boolean sendInternal(Message<?> message, long timeout) {
                return true;
            }
        });
        sut = new BoardController(repo, msgs);
           }

   @Test
    void getAllTest(){
        Boards b1 = getBoards("a");
        Boards b2 = getBoards("b");
        Boards b3 = getBoards("c");
        Boards b4 = getBoards("d");
        sut.addBoard(b1);
        sut.addBoard(b2);
        sut.addBoard(b3);
        sut.addBoard(b4);

        List<Boards> boards = new ArrayList<>();

        boards.add(b1);
        boards.add(b2);
        boards.add(b3);
        boards.add(b4);

        assertEquals(boards, sut.getAll());

   }


    @Test
    void addBoardTest(){
        Boards b1 = getBoards("da");
        sut.addBoard(b1);
        Boards b2 = getBoards("daf");
        sut.addBoard(b2);

        List<Boards> boards = new ArrayList<>();
        boards.add(b1);
        boards.add(b2);

        assertEquals(boards, sut.getAll());

    }

    @Test
    void addNullBoardTest(){
        assertEquals(ResponseEntity.badRequest().build(), sut.addBoard(null));
    }

    @Test
    void addBoardNullTitleTest(){
        Boards b1 = getBoards(null);
        assertEquals(ResponseEntity.badRequest().build(), sut.addBoard(b1));
    }

    @Test
    void addBoardAlreadyExistsTest(){
        Boards b1 = getBoards("fsdfsd");
        sut.addBoard(b1);

        assertEquals(ResponseEntity.badRequest().build(), sut.addBoard(b1));
    }

    @Test
    void addBoardEmptyTitleTest(){
        Boards b1 = getBoards("");
        assertEquals(ResponseEntity.badRequest().build(), sut.addBoard(b1));
    }

    @Test
    void removeBoardTest(){
        Boards b1 = getBoards("af");
        sut.addBoard(b1);

        sut.removeBoard(b1);
        assertEquals("[]", sut.getAll().toString());
    }

    @Test
    void removeNullBoardTest(){
        assertEquals(ResponseEntity.badRequest().build(), sut.removeBoard(null));
    }

    @Test
    void removeBoardInvalidIdTest(){
        Boards b1 = getBoards("hi");
        assertEquals(ResponseEntity.badRequest().build(), sut.removeBoard(b1));
    }

    @Test
    void removeBoardNullNameTest(){
        Boards b1 = getBoards(null);
        assertEquals(ResponseEntity.badRequest().build(), sut.removeBoard(b1));
    }

    @Test
    void successfullyRenameBoardTest(){
        Boards b = getBoards("a");
        sut.addBoard(b);

        assertEquals(1, repo.boards.size());
        assertEquals("a", repo.boards.get(0).name);

        Boards b2 = getBoards("b");
        b2.id = b.id;

        sut.renameBoard(b2);

        assertEquals(1, repo.boards.size());
        assertEquals("b", repo.boards.get(0).name);
    }

    @Test
    void renamedBoardDoesExist(){
        Boards b1 = getBoards("a");
        sut.addBoard(b1);
        Boards b2 = getBoards("b");
        Boards b3 = getBoards("b");
        sut.addBoard(b3);
        assertEquals(ResponseEntity.badRequest().build(), sut.renameBoard(b2));
    }



    @Test
    void renameNullBoard(){
        assertEquals(ResponseEntity.badRequest().build(), sut.renameBoard(null));
    }


    @Test
    void updateBoardSuccessfully(){
        Boards b = getBoards("a");
        sut.addBoard(b);

        assertEquals(1, repo.boards.size());
        assertEquals("a", repo.boards.get(0).name);

        Boards b2 = getBoards("a");
        b2.tags = new ArrayList<>();
        b2.tags.add(new Tags("eee", "black", "white"));
        b2.id = b.id;

        sut.updateBoard(b2);

        assertEquals(1, repo.boards.size());
        assertEquals("eee", repo.boards.get(0).tags.get(0).title);
    }

    @Test
    void updateBoardUsernameWrong(){
        Boards b = getBoards("a");
        sut.addBoard(b);

        assertEquals(1, repo.boards.size());
        assertEquals("a", repo.boards.get(0).name);

        Boards b2 = getBoards("b");
        b2.tags = new ArrayList<>();
        b2.tags.add(new Tags("eee", "black", "white"));
        b2.id = b.id;
        assertEquals(ResponseEntity.badRequest().build(),   sut.updateBoard(b2));
    }


    @Test
    void updateBoardNull(){
        assertEquals(ResponseEntity.badRequest().build(), sut.updateBoard(null));
    }

    @Test
    void updateBoardBoardNullName(){
        assertEquals(ResponseEntity.badRequest().build(), sut.updateBoard(getBoards(null)));
    }

    @Test
    void updateBoardNotExists(){
        assertEquals(ResponseEntity.badRequest().build(), sut.updateBoard(getBoards("ff")));
    }

    @Test
    void findBoardFail(){
        assertEquals(null, sut.findBoard("eee"));
    }

    @Test
    void findBoardSuccessful(){
        Boards b4 = getBoards("55");
        sut.addBoard(b4);
        assertEquals("55", sut.findBoard("55").name);
    }

    @Test
    void getBoardName(){
        Boards b4 = getBoards("55");
        sut.addBoard(b4);
        assertEquals("55", sut.getBoard("55").name);

    }

    @Test
    void getBoardNameNull(){
        Boards b4 = getBoards("55");
        assertEquals(null, sut.getBoard("55"));

    }

    @Test
    void cssTest(){
        Boards b4 = getBoards("55");
        sut.addBoard(b4);
        assertEquals(HttpStatus.OK, sut.setCss(b4).getStatusCode());

    }

    @Test
    void cssTestNull(){
        Boards b4 = getBoards("55");
        assertEquals(ResponseEntity.badRequest().build(), sut.setCss(b4));

    }


    /**Creates new board for the repo
     * @param t String title
     * @return board
     */
   public Boards getBoards(String t){
        Boards board = new Boards(t, null, null);
        board.id = boardCount;
        boardCount++;
        return board;
   }

}
