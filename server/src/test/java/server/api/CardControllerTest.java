package server.api;

import commons.Cards;
import commons.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class CardControllerTest {

    public AtomicInteger cardCount;
    public TestCardsRepository repo;
    public CardController sut;

    @BeforeEach
    public void setup() {

        cardCount = new AtomicInteger(0);
        repo = new TestCardsRepository();
        sut = new CardController(repo);
    }

    @Test
    void getAllCards() {

        Lists list = new Lists("todo", 0);

        Cards c1 = getCard("a",0, list, cardCount);
        Cards c2 = getCard("b",1, list, cardCount);
        Cards c3 = getCard("c",2, list, cardCount);
        Cards c4 = getCard("d",3, list, cardCount);

        sut.addCard(c1);
        sut.addCard(c2);
        sut.addCard(c3);
        sut.addCard(c4);

        List<Cards> cards = new ArrayList<>();
        cards.add(c1);
        cards.add(c2);
        cards.add(c3);
        cards.add(c4);

        assertEquals(cards, sut.getAllCards());
    }

    @Test
    void getCardsByListId() {

        Lists list = new Lists("todo", 0);
        Lists list2 = new Lists("todo", 0);
        list.id=0;
        list2.id=1;

        Cards c1 = getCard("a",0, list, cardCount);
        Cards c2 = getCard("b",1, list2, cardCount);
        Cards c3 = getCard("c",1, list, cardCount);
        Cards c4 = getCard("d",2, list, cardCount);

        sut.addCard(c1);
        sut.addCard(c2);
        sut.addCard(c3);
        sut.addCard(c4);

        List<Cards> cards = new ArrayList<>();
        cards.add(c1);
        cards.add(c3);
        cards.add(c4);

        assertEquals(cards, sut.getCardsByListId(0));
    }

    @Test
    void getCardsByListIdInAscOrder() {

        Lists list = new Lists("todo", 0);
        Lists list2 = new Lists("todo", 0);
        list.id = 0;
        list2.id = 1;

        Cards c1 = getCard("a", 0, list, cardCount);
        Cards c2 = getCard("b", 1, list2, cardCount);
        Cards c3 = getCard("c", 1, list, cardCount);
        Cards c4 = getCard("d", 2, list, cardCount);
        Cards c5 = getCard("e", 1, list, cardCount);

        sut.addCard(c1);
        sut.addCard(c2);
        sut.addCard(c3);
        sut.addCard(c4);
        sut.addCard(c5);

        List<Cards> cards = new ArrayList<>();
        cards.add(c1);
        cards.add(c5);
        cards.add(c3);
        cards.add(c4);

        assertEquals(cards, sut.getCardsByListId(0));
    }

    //we technically already test adding/saving cards in the above two tests, so maybe not necessary to do it again?

    @Test
    void addNullCard() {

        assertEquals(ResponseEntity.badRequest().build(), sut.addCard(null));
    }

    @Test
    void addCardNullTitleTest() {

        Lists list = new Lists("todo", 0);
        Cards c1 = getCard(null, 0, list, cardCount);

        assertEquals(ResponseEntity.badRequest().build(), sut.addCard(c1));
    }

    @Test
    void addCardEmptyTitleTest() {

        Lists list = new Lists("todo", 0);
        Cards c1 = getCard( "", 0, list, cardCount);

        assertEquals(ResponseEntity.badRequest().build(), sut.addCard(c1));
    }

    @Test
    void addCardNegativePositionTest() {

        Lists list = new Lists("todo", 0);
        Cards c1 = getCard( "", Integer.MIN_VALUE, list, cardCount);

        assertEquals(ResponseEntity.badRequest().build(), sut.addCard(c1));
    }

    @Test
    void addCardAlreadyExistsTest() {

        Lists list = new Lists("todo", 0);
        Cards c1 = getCard("asdasd", 0, list, cardCount);
        sut.addCard(c1);

        assertEquals(ResponseEntity.badRequest().build(), sut.addCard(c1));
    }

    @Test
    void addCardInvalidPositivePositionTest() {

        Lists list = new Lists("todo", 0);
        Cards c1 = getCard("asdasd", 0, list, cardCount);
        Cards c2 = getCard("asdasd", 5, list, cardCount);
        sut.addCard(c1);

        assertEquals(ResponseEntity.badRequest().build(), sut.addCard(c2));
    }

    @Test
    void addCardIncrementPositionTest() {

        Lists list = new Lists("todo", 0);
        list.id = 0;

        Cards c1 = getCard("a", 0, list, cardCount);
        Cards c2 = getCard("b", 1, list, cardCount);
        Cards c3 = getCard("c", 2, list, cardCount);
        Cards c4 = getCard("d", 3, list, cardCount);
        Cards c5 = getCard("e", 1, list, cardCount);

        sut.addCard(c1);
        sut.addCard(c2);
        sut.addCard(c3);
        sut.addCard(c4);
        sut.addCard(c5);

        List<Cards> cards = new ArrayList<>();
        cards.add(c1);
        cards.add(c5);
        cards.add(c2);
        cards.add(c3);
        cards.add(c4);

        assertEquals(cards, sut.getCardsByListId(0));
    }

    @Test
    void removeCard() {

        Lists list = new Lists("wae", 0);
        Cards c1 = getCard("asdasd", 0, list, cardCount);
        sut.addCard(c1);
        sut.removeCard(c1);

        assertEquals("[]", sut.getAllCards().toString());
    }

    @Test
    void removeNullCardTest() {

        assertEquals(ResponseEntity.badRequest().build(), sut.removeCard(null));
    }

    @Test
    void removeCardInvalidIdTest() {

        Lists list = new Lists("wae", 0);
        Cards c1 = getCard("asdasd", 0, list, cardCount);

        //c1 wasn't added, therefore it's ID is invalid inside the repository
        assertEquals(ResponseEntity.badRequest().build(), sut.removeCard(c1));
    }

    @Test
    void removeCardDecrementsPositionOfOtherCardsTest() {

        Lists list = new Lists("todo", 0);
        list.id = 0;

        Cards c1 = getCard("a", 0, list, cardCount);
        Cards c2 = getCard("b", 1, list, cardCount);
        Cards c3 = getCard("c", 2, list, cardCount);
        Cards c4 = getCard("d", 3, list, cardCount);
        Cards c5 = getCard("e", 4, list, cardCount);

        sut.addCard(c1);
        sut.addCard(c2);
        sut.addCard(c3);
        sut.addCard(c4);
        sut.addCard(c5);

        sut.removeCard(c2);

        List<Integer> positions = new ArrayList<>();
        positions.add(c1.positionInsideList);
        positions.add(c3.positionInsideList);
        positions.add(c4.positionInsideList);
        positions.add(c5.positionInsideList);

        assertEquals("[0, 1, 2, 3]", positions.toString());
    }

    public static Cards getCard(String t, int p, Lists list, AtomicInteger cardCount) {

        Cards card = new Cards(t,p,list);
        card.id = cardCount.longValue();
        cardCount.incrementAndGet();
        return card;
    }
}