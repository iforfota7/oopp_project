package server.api;

import commons.Cards;
import commons.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.CardsRepository;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardsRepository repo;
    private final SimpMessagingTemplate msgs;
    /**
     * Constructor for CardController
     * @param repo - Repository for cards entities
     * @param msgs - used for websockets
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
        msgs.convertAndSend("/topic/client/refresh", saved);
        return ResponseEntity.ok(saved);
    }

    /**
     * Method for updating information of a card that is related to card details.
     * A card can only be updated if it or any of its fields are not null
     * if it already exists in the repo,
     * if it's position is the same as the version of the card in the repo
     * and lastly if the card's list is the same as the list of the card specified in the repo
     * @param card the card whose title is to be renamed
     * @return 200 OK if renaming was successful
     */
    @PostMapping(path = {"/rename","/rename/"})
    public ResponseEntity<Cards> renameCard(@RequestBody Cards card) {

        if(card == null || card.list==null
                || isNullOrEmpty(card.title) || card.positionInsideList<0){
            return ResponseEntity.badRequest().build();
        }

        if(repo.findById(card.id).isEmpty())
            return ResponseEntity.badRequest().build();

        if(repo.findById(card.id).get().positionInsideList!=card.positionInsideList)
            return ResponseEntity.badRequest().build();

        if(repo.findById(card.id).get().list.id!=card.list.id)
            return ResponseEntity.badRequest().build();

        Cards saved = repo.save(card);
        msgs.convertAndSend("/topic/client/refresh", saved);
        msgs.convertAndSend("/topic/card/rename", saved);
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
            msgs.convertAndSend("/topic/client/refresh", card);
            msgs.convertAndSend("/topic/cards/remove", card);
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }

    }

    /**
     * Method for moving cards to a different position in a (possibly different) list
     * If one of the 2 requests fails, the state of the repository is maintained
     * @param card the card to be moved to another list
     * @return 200 OK if moving the card was successful
     */
    @Transactional
    @PostMapping(path = {"/move","/move/"})
    public ResponseEntity<Cards> moveCard(@RequestBody Cards card) {

        if(repo.findById(card.id).isEmpty())
            return ResponseEntity.badRequest().build();

        Cards oldCard = repo.findById(card.id).get();
        removeCard(oldCard);

        ResponseEntity<Cards> addResponse = addCard(card);
        if(addResponse.equals(ResponseEntity.badRequest().build())) {
            throw new RuntimeException("Failed to add card");
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Given a tag, it removes all references from cards
     * to that specific tag
     *
     * @param tag The given tag object
     * @return OK iff the operation was successfully performed
     */
    @Transactional
    @PostMapping(path = {"/removeTag", "/removeTag/"})
    public ResponseEntity<Tags> removeTagFromCards(@RequestBody Tags tag) {
        if(tag == null)
            return ResponseEntity.badRequest().build();

        List<Cards> cardsList = repo.findAll();
        for(Cards card : cardsList) {
            card.tags.removeIf(tag::equals);
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
