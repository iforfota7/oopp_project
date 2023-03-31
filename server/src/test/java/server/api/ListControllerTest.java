package server.api;

import commons.Boards;
import commons.Cards;
import commons.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.AbstractMessageChannel;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListControllerTest {

    public long listCount;
    public SimpMessagingTemplate msgs;
    public TestListsRepository repo;
    public ListController sut;

    /**
     * Before each method for tests
     */
    @BeforeEach
    public void setup() {

        listCount = 0;
        msgs = new SimpMessagingTemplate(new AbstractMessageChannel() {
            @Override
            protected boolean sendInternal(Message<?> message, long timeout) {
                return true;
            }
        });
        repo = new TestListsRepository();
        sut = new ListController(repo, msgs);
    }

    @Test
    void getAllTest() {

        Lists l1 = getList("a",0);
        Lists l2 = getList("b",1);
        Lists l3 = getList("c",2);
        Lists l4 = getList("d",3);

        sut.addList(l1);
        sut.addList(l2);
        sut.addList(l3);
        sut.addList(l4);

        List<Lists> lists = new ArrayList<>();
        lists.add(l1);
        lists.add(l2);
        lists.add(l3);
        lists.add(l4);

        assertTrue(repo.calledMethods.contains("existsById"));
        assertTrue(repo.calledMethods.contains("maxPositionInsideBoard"));
        assertTrue(repo.calledMethods.contains("incrementListPosition"));
        assertTrue(repo.calledMethods.contains("save"));

        assertEquals(lists, sut.getAll());
        assertTrue(repo.calledMethods.contains("findAllByOrderByPositionInsideBoardAsc"));
    }

    @Test
    void getAllInAscOrderTest() {

        Lists l1 = getList("a",0);
        Lists l2 = getList("b",1);
        Lists l3 = getList("c",2);
        Lists l4 = getList("d",3);
        Lists l5 = getList("e",1);

        sut.addList(l1);
        sut.addList(l2);
        sut.addList(l3);
        sut.addList(l4);
        sut.addList(l5);

        List<Lists> lists = new ArrayList<>();
        lists.add(l1);
        lists.add(l5);
        lists.add(l2);
        lists.add(l3);
        lists.add(l4);

        assertEquals(lists, sut.getAll());
    }

    @Test
    void addListTest() {

        Lists l1 = getList("asdasd", 0);
        sut.addList(l1);
        Lists l2 = getList("asdasd", 1);
        sut.addList(l2);

        List<Lists> lists = new ArrayList<>();
        lists.add(l1);
        lists.add(l2);

        assertEquals(lists, sut.getAll());
    }

    @Test
    void addNullListTest() {

        assertEquals(ResponseEntity.badRequest().build(), sut.addList(null));
    }

    @Test
    void addListNullTitleTest() {

        Lists l1 = getList(null, 0);

        assertEquals(ResponseEntity.badRequest().build(), sut.addList(l1));
    }

    @Test
    void addListEmptyTitleTest() {

        Lists l1 = getList( "", 0);

        assertEquals(ResponseEntity.badRequest().build(), sut.addList(l1));
    }


    @Test
    void addListNegativePositionTest() {

        Lists l1 = getList("asdasd", -91412);

        assertEquals(ResponseEntity.badRequest().build(), sut.addList(l1));
    }

    @Test
    void addListAlreadyExistsTest() {

        Lists l1 = getList("asdasd", 0);
        sut.addList(l1);

        assertEquals(ResponseEntity.badRequest().build(), sut.addList(l1));
    }

    @Test
    void addListInvalidPositivePositionTest() {

        Lists l1 = getList("asdasd", 0);
        Lists l2 = getList("asdasd", 5);
        sut.addList(l1);

        assertEquals(ResponseEntity.badRequest().build(), sut.addList(l2));
    }

    @Test
    void addListIncrementPositionTest() {

        Lists l1 = getList("a", 0);
        Lists l2 = getList("b", 1);
        Lists l3 = getList("c", 2);
        Lists l4 = getList("d", 3);
        Lists l5 = getList("e", 1);

        sut.addList(l1);
        sut.addList(l2);
        sut.addList(l3);
        sut.addList(l4);
        sut.addList(l5);

        List<Integer> positions = new ArrayList<>();
        positions.add(l1.positionInsideBoard);
        positions.add(l2.positionInsideBoard);
        positions.add(l3.positionInsideBoard);
        positions.add(l4.positionInsideBoard);
        positions.add(l5.positionInsideBoard);

        assertEquals(positions.toString(), "[0, 2, 3, 4, 1]");
    }

    @Test
    void removeListTest() {

        Lists l1 = getList("asdasd", 0);
        sut.addList(l1);
        sut.removeList(l1);

        assertTrue(repo.calledMethods.contains("existsById"));
        assertTrue(repo.calledMethods.contains("removeCardsInsideList"));
        assertTrue(repo.calledMethods.contains("delete"));
        assertTrue(repo.calledMethods.contains("decrementListPosition"));

        assertEquals("[]", sut.getAll().toString());
    }

    @Test
    void removeNullListTest() {

        assertEquals(ResponseEntity.badRequest().build(), sut.removeList(null));
    }

    @Test
    void removeListInvalidIdTest() {

        Lists l1 = getList("hi", 0);

        //l1 wasn't added, therefore it's ID is invalid inside the repository
        assertEquals(ResponseEntity.badRequest().build(), sut.removeList(l1));
    }

    @Test
    void removeListAlsoRemovesCardsInsideListTest() {

        Lists l1 = getList("hi", 0);
        Lists l2 = getList("hii", 1);

        sut.addList(l1);
        sut.addList(l2);

        repo.cards.add(new Cards("t",0,l1,"Test description", null));
        repo.cards.add(new Cards("e",1,l1,"Test description", null));
        repo.cards.add(new Cards("a",2,l1,"Test description", null));
        repo.cards.add(new Cards("b",0,l2,"Test description", null));
        repo.cards.add(new Cards("o",1,l2,"Test description", null));
        repo.cards.add(new Cards("l",2,l2,"Test description", null));

        sut.removeList(l1);

        List<Cards> cards = new ArrayList<>();
        cards.add(new Cards("b",0,l2,"Test description", null));
        cards.add(new Cards("o",1,l2,"Test description", null));
        cards.add(new Cards("l",2,l2,"Test description", null));

        assertEquals(cards, repo.cards);
    }

    @Test
    void removeListDecrementsPositionOfOtherListsTest() {

        Lists l1 = getList("a", 0);
        Lists l2 = getList("b", 1);
        Lists l3 = getList("c", 2);
        Lists l4 = getList("d", 3);

        sut.addList(l1);
        sut.addList(l2);
        sut.addList(l3);
        sut.addList(l4);

        sut.removeList(l2);

        List<Integer> positions = new ArrayList<>();
        positions.add(l1.positionInsideBoard);
        positions.add(l3.positionInsideBoard);
        positions.add(l4.positionInsideBoard);

        assertEquals("[0, 1, 2]", positions.toString());
    }

    @Test
    void successfullyRenameList() {
        Lists l = getList("a", 0);
        sut.addList(l);

        assertEquals(1, repo.lists.size());
        assertEquals("a", repo.lists.get(0).title);

        Lists l2 = getList("b", 0);
        l2.id = l.id;
        sut.renameList(l2);

        assertEquals(1, repo.lists.size());
        assertEquals("b", repo.lists.get(0).title);
    }

    @Test
    void renamedListDoesNotExist() {
        Lists l1 = getList("a", 0);
        sut.addList(l1);
        Lists l2 = getList("b", 1);

        assertEquals(ResponseEntity.badRequest().build(), sut.renameList(l2));
    }

    @Test
    void renamedListWrongPosition() {
        Lists l1 = getList("a", 0);
        sut.addList(l1);
        Lists l2 = getList("b", 1);
        l2.id = l1.id;

        assertEquals(ResponseEntity.badRequest().build(), sut.renameList(l2));
    }

    @Test
    void renameNullList() {
        assertEquals(ResponseEntity.badRequest().build(), sut.renameList(null));
    }

    /**
     * Creates a new list
     * @param t the name of the list
     * @param p the position of the list
     * @return the list
     */
    public Lists getList(String t, int p) {

        Lists list = new Lists(t,p, new Boards("test", null));
        list.id = listCount;
        listCount++;
        return list;
    }
}