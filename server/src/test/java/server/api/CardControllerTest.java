package server.api;

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

class CardControllerTest {

    public long cardCount;
    public SimpMessagingTemplate msgs;
    public TestCardsRepository repo;
    public CardController sut;

    @BeforeEach
    public void setup() {

        cardCount = 0;
        msgs = new SimpMessagingTemplate(new AbstractMessageChannel() {
            @Override
            protected boolean sendInternal(Message<?> message, long timeout) {
                return true;
            }
        });
        repo = new TestCardsRepository();
        sut = new CardController(repo, msgs);
    }

    @Test
    void getAllCards() {

        Lists list = new Lists("todo", 0);

        Cards c1 = getCard("a",0, list);
        Cards c2 = getCard("b",1, list);
        Cards c3 = getCard("c",2, list);
        Cards c4 = getCard("d",3, list);

        sut.addCard(c1);
        sut.addCard(c2);
        sut.addCard(c3);
        sut.addCard(c4);

        List<Cards> cards = new ArrayList<>();
        cards.add(c1);
        cards.add(c2);
        cards.add(c3);
        cards.add(c4);

        assertTrue(repo.calledMethods.contains("existsById"));
        assertTrue(repo.calledMethods.contains("maxPositionInsideList"));
        assertTrue(repo.calledMethods.contains("incrementCardPosition"));
        assertTrue(repo.calledMethods.contains("save"));

        assertEquals(cards, repo.cards);
    }

    @Test
    void getCardsByListId() {

        Lists list = new Lists("todo", 0);
        Lists list2 = new Lists("todo", 0);

        Cards c1 = getCard("a",0, list);
        Cards c2 = getCard("b",1, list2);
        Cards c3 = getCard("c",1, list);
        Cards c4 = getCard("d",2, list);

        sut.addCard(c1);
        sut.addCard(c2);
        sut.addCard(c3);
        sut.addCard(c4);

        List<Cards> cards = new ArrayList<>();
        cards.add(c1);
        cards.add(c3);
        cards.add(c4);

        assertEquals(cards, list.cards);
    }

    @Test
    void getCardsByListIdInAscOrder() {

        Lists list = new Lists("todo", 0);
        Lists list2 = new Lists("todo", 0);

        Cards c1 = getCard("a", 0, list);
        Cards c2 = getCard("b", 1, list2);
        Cards c3 = getCard("c", 1, list);
        Cards c4 = getCard("d", 2, list);
        Cards c5 = getCard("e", 1, list);

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

        assertEquals(cards, list.cards);
    }

    @Test
    void addNullCard() {

        assertEquals(ResponseEntity.badRequest().build(), sut.addCard(null));
    }

    @Test
    void addCardNullTitleTest() {

        Lists list = new Lists("todo", 0);
        Cards c1 = getCard(null, 0, list);

        assertEquals(ResponseEntity.badRequest().build(), sut.addCard(c1));
    }

    @Test
    void addCardEmptyTitleTest() {

        Lists list = new Lists("todo", 0);
        Cards c1 = getCard( "", 0, list);

        assertEquals(ResponseEntity.badRequest().build(), sut.addCard(c1));
    }

    @Test
    void addCardNegativePositionTest() {

        Lists list = new Lists("todo", 0);
        Cards c1 = getCard( "", Integer.MIN_VALUE, list);

        assertEquals(ResponseEntity.badRequest().build(), sut.addCard(c1));
    }

    @Test
    void addCardAlreadyExistsTest() {

        Lists list = new Lists("todo", 0);
        Cards c1 = getCard("asdasd", 0, list);
        sut.addCard(c1);

        assertEquals(ResponseEntity.badRequest().build(), sut.addCard(c1));
    }

    @Test
    void addCardInvalidPositivePositionTest() {

        Lists list = new Lists("todo", 0);
        Cards c1 = getCard("asdasd", 0, list);
        Cards c2 = getCard("asdasd", 5, list);
        sut.addCard(c1);

        assertEquals(ResponseEntity.badRequest().build(), sut.addCard(c2));
    }

    @Test
    void addCardIncrementPositionTest() {

        Lists list = new Lists("todo", 0);

        Cards c1 = getCard("a", 0, list);
        Cards c2 = getCard("b", 1, list);
        Cards c3 = getCard("c", 2, list);
        Cards c4 = getCard("d", 3, list);
        Cards c5 = getCard("e", 1, list);

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

        assertEquals(cards, list.cards);
    }

    @Test
    void removeCard() {

        Lists list = new Lists("wae", 0);
        Cards c1 = getCard("asdasd", 0, list);
        sut.addCard(c1);
        sut.removeCard(c1);

        assertTrue(repo.calledMethods.contains("existsById"));
        assertTrue(repo.calledMethods.contains("delete"));
        assertTrue(repo.calledMethods.contains("decrementCardPosition"));

        assertEquals("[]", repo.cards.toString());
    }

    @Test
    void removeNullCardTest() {

        assertEquals(ResponseEntity.badRequest().build(), sut.removeCard(null));
    }

    @Test
    void removeCardInvalidIdTest() {

        Lists list = new Lists("wae", 0);
        Cards c1 = getCard("asdasd", 0, list);

        //c1 wasn't added, therefore it's ID is invalid inside the repository
        assertEquals(ResponseEntity.badRequest().build(), sut.removeCard(c1));
    }

    @Test
    void removeCardDecrementsPositionOfOtherCardsTest() {

        Lists list = new Lists("todo", 0);
        list.id = 0;

        Cards c1 = getCard("a", 0, list);
        Cards c2 = getCard("b", 1, list);
        Cards c3 = getCard("c", 2, list);
        Cards c4 = getCard("d", 3, list);
        Cards c5 = getCard("e", 4, list);

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

    @Test
    void renameCardSuccessfully() {
        Lists list = new Lists("todo", 0);
        list.id = 0;

        Cards c = getCard("a", 0, list);
        sut.addCard(c);

        assertEquals(1, repo.cards.size());
        assertEquals("a", repo.cards.get(0).title);

        Cards c2 = getCard("b", 0, list);
        c2.id = c.id;
        sut.renameCard(c2);

        assertEquals(1, repo.cards.size());
        assertEquals("b", repo.cards.get(0).title);
    }

    @Test
    void renameNullCard() {
        assertEquals(ResponseEntity.badRequest().build(), sut.renameCard(null));
    }

    @Test
    void CardDoesNotExistRename() {
        Lists list = new Lists("todo", 0);
        list.id = 0;

        Cards c = getCard("a", 0, list);

        assertEquals(ResponseEntity.badRequest().build(), sut.renameCard(c));
    }

    @Test
    void CardWrongListIDRename() {
        Lists list = new Lists("todo", 0);
        list.id = 0;

        Cards c = getCard("a", 0, list);
        sut.addCard(c);

        Lists list2 = new Lists("todo", 0);
        Cards c2 = getCard("b", 0, list2);
        c2.id = c.id;
        list2.id = 5;
        assertEquals(ResponseEntity.badRequest().build(), sut.renameCard(c2));
    }

    @Test
    void CardWrongPositionRename() {
        Lists list = new Lists("todo", 0);
        list.id = 0;

        Cards c = getCard("a", 0, list);
        sut.addCard(c);

        Cards c2 = getCard("b", 1, list);
        c2.id = c.id;
        assertEquals(ResponseEntity.badRequest().build(), sut.renameCard(c2));
    }

    @Test
    void MovedCardDoesNotExist() {
        Cards card = getCard("My Card", 0, null);
        assertEquals(ResponseEntity.badRequest().build(), sut.moveCard(card));
    }

    @Test
    void MoveCardAddFailed() {
        Lists list = new Lists("My List", 0);
        Cards oldCard = getCard("My Card", 0, list);
        sut.addCard(oldCard);

        Cards newCard = getCard("My Card", 10, list);
        newCard.id = oldCard.id;

        assertThrows(RuntimeException.class, () -> {
            sut.moveCard(newCard);
        });
    }

    @Test
    void moveCardDifferentList() {
        Lists list1 = new Lists("My List", 0);
        Lists list2 = new Lists("My Second List", 1);
        list2.id = 1;
        Cards oldCard = getCard("My Card", 0, list1);
        Cards secondCard = getCard("My Second Card", 0, list2);
        sut.addCard(oldCard);
        sut.addCard(secondCard);

        Cards newCard = getCard("My Card", 0, list2);
        newCard.id = oldCard.id;

        sut.moveCard(newCard);

        List<Cards> allCards = repo.cards;
        assertEquals(2, allCards.size());

        assertTrue(allCards.contains(secondCard));
        assertEquals(1, secondCard.positionInsideList);

        assertTrue(allCards.contains(newCard));
        assertEquals(0, newCard.positionInsideList);
    }

    @Test
    void moveCardSameListAbove() {
        Lists list = new Lists("My List", 0);
        Cards oldCard = getCard("My Card", 0, list);
        Cards secondCard = getCard("My Second Card", 1, list);
        sut.addCard(oldCard);
        sut.addCard(secondCard);

        Cards newCard = getCard("My Card", 1, list);
        newCard.id = oldCard.id;

        sut.moveCard(newCard);

        List<Cards> allCards = repo.cards;
        assertEquals(2, allCards.size());

        assertTrue(allCards.contains(secondCard));
        assertEquals(0, secondCard.positionInsideList);

        assertTrue(allCards.contains(newCard));
        assertEquals(1, newCard.positionInsideList);
    }

    @Test
    void moveCardSameListBelow() {
        Lists list = new Lists("My List", 0);
        Cards secondCard = getCard("My Second Card", 0, list);
        Cards oldCard = getCard("My Card", 1, list);
        sut.addCard(secondCard);
        sut.addCard(oldCard);

        Cards newCard = getCard("My Card", 0, list);
        newCard.id = oldCard.id;

        sut.moveCard(newCard);

        List<Cards> allCards = repo.cards;
        assertEquals(2, allCards.size());

        assertTrue(allCards.contains(secondCard));
        assertEquals(1, secondCard.positionInsideList);

        assertTrue(allCards.contains(newCard));
        assertEquals(0, newCard.positionInsideList);
    }

    public Cards getCard(String t, int p, Lists list) {

        Cards card = new Cards(t,p,list);
        card.id = cardCount;
        cardCount++;
        return card;
    }
}