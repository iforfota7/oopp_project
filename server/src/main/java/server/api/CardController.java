package server.api;

import commons.Cards;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.CardsRepository;

import javax.transaction.Transactional;
import java.util.Objects;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardsRepository repo;
    private final SimpMessagingTemplate msgs;
    /**
     * Constructor for CardController
     * @param repo - Repository for cards entities
     * @param msgs - Messaging template
     */
    public CardController(CardsRepository repo, SimpMessagingTemplate msgs) {
        this.repo = repo;
        this.msgs = msgs;
    }

    /**
     * Method for adding a card to the repo (can also be used for updating the cards)
     * @param card the card to be added to the repo
     * @return a 200 OK response for a successful http request
     */

    @Transactional
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Cards> addCard(@RequestBody Cards card){
        if(card == null || isNullOrEmpty(card.title) || card.positionInsideList<0){
            return ResponseEntity.badRequest().build();
        }

        // if the instance exists in the repository, the client gets returned a bad request
        if(repo.existsById (card.id))
            return ResponseEntity.badRequest().build();

        Integer maxPositionInsideList = repo.maxPositionInsideList(card.list.id);
        if(maxPositionInsideList == null) {
            // there are no Cards entities inside that List
            maxPositionInsideList = -1;
        }
        if(card.positionInsideList > maxPositionInsideList + 1) {
            // position sent by the client is invalid
            return ResponseEntity.badRequest().build();
        }

        repo.incrementCardPosition(card.positionInsideList, card.list.id);
        Cards saved = repo.save(card);
        msgs.convertAndSend("/topic/cards", saved);
        return ResponseEntity.ok(saved);
    }

    /**
     * Method for updating the title of a card.
     * A card can only be renamed if it or any of its fields are not null
     * if it already exists in the repo,
     * if it's position is the same as the version of the card in the repo
     * and lastly if the card's list is the same as the list of the card specified in the repo
     * @param card the card whose title is to be renamed
     * @return 200 OK if renaming was successful
     */
    @PostMapping(path = {"/rename","/rename/"})
    public ResponseEntity<Cards> renameCard(@RequestBody Cards card) {

        if(card == null || card.list==null ||
                isNullOrEmpty(card.title) || card.positionInsideList<0){
            return ResponseEntity.badRequest().build();
        }

        if(repo.findById(card.id).isEmpty())
            return ResponseEntity.badRequest().build();

        if(repo.findById(card.id).get().positionInsideList!=card.positionInsideList)
            return ResponseEntity.badRequest().build();

        if(repo.findById(card.id).get().list.id!=card.list.id)
            return ResponseEntity.badRequest().build();

        Cards saved = repo.save(card);
        msgs.convertAndSend("/topic/cards/rename", saved);
        return ResponseEntity.ok(saved);
    }

    /**
     * Method for deleting a card from the repo
     * Whenever a card is removed the positions of the cards with
     * higher position in the same list get decremented by 1
     * @param card the card to be deleted from the repo
     * @return a 200 OK response for a successful http request
     */
    @Transactional
    @PostMapping(path = {"/remove", "/remove/"})
    public ResponseEntity<Cards> removeCard(@RequestBody Cards card) {

        if(card == null){
            return ResponseEntity.badRequest().build();
        }

        if(repo.existsById(card.id)) {
            // only remove and decrement card positions
            // if the entry with the provided id actually exists
            repo.delete(card);
            repo.decrementCardPosition(card.positionInsideList, card.list.id);

            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }

    }

    /**
     * Method for moving a card from one list to another.
     * A card can only be moved to another list if it already exists in the repo.
     * The way it is moved between lists is by first removing the existing card
     * (which has the old list id) from the repo, and later adding the new card
     * (which has the new list id) to the repo. If adding the new card fails,
     * the old one is added back.
     * @param card the card to be moved to another list
     * @return 200 OK if moving the card was successful
     */
    @Transactional
    @PostMapping(path = {"/moveCard","/moveCard/"})
    public ResponseEntity<Cards> moveCardToAnotherList(@RequestBody Cards card) {

        if(repo.findById(card.id).isEmpty())
            return ResponseEntity.badRequest().build();

        if(repo.findById(card.id).get().list.id==card.list.id)
            return ResponseEntity.ok().build();

        Cards oldCard = repo.findById(card.id).get();

        card.id=0;

        ResponseEntity<Cards> addResponse = addCard(card);

        if(addResponse.getStatusCode().is4xxClientError()) {

            return ResponseEntity.badRequest().build();
        }

        card.id = Objects.requireNonNull(addResponse.getBody()).id;

        ResponseEntity<Cards> removeResponse = removeCard(oldCard);

        if(removeResponse.getStatusCode().is4xxClientError()) {
            removeCard(card);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Method for checking whether a String is null or empty
     * @param s the String to be checked
     * @return true if s is null or empty, false otherwise
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
