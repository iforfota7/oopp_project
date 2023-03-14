package server.api;

import commons.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ListControllerTest {

    public AtomicInteger listCount;
    public TestListsRepository repo;
    public ListController sut;

    @BeforeEach
    public void setup() {

        listCount = new AtomicInteger(0);
        repo = new TestListsRepository();
        sut = new ListController(repo);
    }

    @Test
    void getAllTest() {

        Lists l1 = getList("a",0, listCount);
        Lists l2 = getList("b",1, listCount);
        Lists l3 = getList("c",2, listCount);
        Lists l4 = getList("d",3, listCount);

        sut.addList(l1);
        sut.addList(l2);
        sut.addList(l3);
        sut.addList(l4);

        List<Lists> lists = new ArrayList<>();
        lists.add(l1);
        lists.add(l2);
        lists.add(l3);
        lists.add(l4);

        assertEquals(lists, sut.getAll());
    }

    @Test
    void getAllInAscOrderTest() {

        Lists l1 = getList("a",0, listCount);
        Lists l2 = getList("b",1, listCount);
        Lists l3 = getList("c",2, listCount);
        Lists l4 = getList("d",3, listCount);
        Lists l5 = getList("e",1, listCount);

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

        Lists l1 = getList("asdasd", 0, listCount);
        sut.addList(l1);
        Lists l2 = getList("asdasd", 1, listCount);
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

        Lists l1 = getList(null, 0, listCount);

        assertEquals(ResponseEntity.badRequest().build(), sut.addList(l1));
    }

    @Test
    void addListEmptyTitleTest() {

        Lists l1 = getList( "", 0, listCount);

        assertEquals(ResponseEntity.badRequest().build(), sut.addList(l1));
    }


    @Test
    void addListNegativePositionTest() {

        Lists l1 = getList("asdasd", -91412, listCount);

        assertEquals(ResponseEntity.badRequest().build(), sut.addList(l1));
    }

    @Test
    void addListAlreadyExistsTest() {

        Lists l1 = getList("asdasd", 0, listCount);
        sut.addList(l1);

        assertEquals(ResponseEntity.badRequest().build(), sut.addList(l1));
    }

    @Test
    void addListInvalidPositivePositionTest() {

        Lists l1 = getList("asdasd", 0, listCount);
        Lists l2 = getList("asdasd", 5, listCount);
        sut.addList(l1);

        assertEquals(ResponseEntity.badRequest().build(), sut.addList(l2));
    }

    @Test
    void addListIncrementPositionTest() {

        Lists l1 = getList("a", 0, listCount);
        Lists l2 = getList("b", 1, listCount);
        Lists l3 = getList("c", 2, listCount);
        Lists l4 = getList("d", 3, listCount);
        Lists l5 = getList("e", 1, listCount);

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

        Lists l1 = getList("asdasd", 0, listCount);
        sut.addList(l1);
        sut.removeList(l1);

        assertEquals("[]", sut.getAll().toString());
    }

    @Test
    void removeNullListTest() {

        assertEquals(ResponseEntity.badRequest().build(), sut.removeList(null));
    }

    @Test
    void removeListInvalidIdTest() {

        Lists l1 = getList("hi", 0, listCount);

        //l1 wasn't added, therefore it's ID is invalid inside the repository
        assertEquals(ResponseEntity.badRequest().build(), sut.removeList(l1));
    }

    @Test
    void removeListAlsoRemovesCardsInsideListTest() {

        //skip for now
    }

    @Test
    void removeListDecrementsPositionOfOtherListsTest() {

        Lists l1 = getList("a", 0, listCount);
        Lists l2 = getList("b", 1, listCount);
        Lists l3 = getList("c", 2, listCount);
        Lists l4 = getList("d", 3, listCount);

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

    public static Lists getList(String t, int p, AtomicInteger listCount) {

        Lists list = new Lists(t,p);
        list.id = listCount.longValue();
        listCount.incrementAndGet();
        return list;
    }
}