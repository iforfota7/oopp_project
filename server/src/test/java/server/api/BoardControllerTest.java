package server.api;


import commons.Boards;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.function.Consumer;
public class BoardControllerTest {

    public long boardCount;

    public TestBoardsRepository repo;

    public BoardController sut;



    public SimpMessagingTemplate msgs;

    @BeforeEach
    public void setup(){
        this.boardCount = 0;
        repo =  new TestBoardsRepository();
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

   /* @Test
    void addBoardAlreadyExistsTest(){
        Boards b1 = getBoards("fsdfsd");
        sut.addBoard(b1);

        assertEquals(ResponseEntity.badRequest().build(), sut.addBoard(b1));
    }*/

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







   public Boards getBoards(String t){
        Boards board = new Boards(t, null, null);
        board.id = boardCount;
        boardCount++;
        return board;
   }

}
