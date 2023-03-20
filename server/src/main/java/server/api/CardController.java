package server.api;

import commons.Cards;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardsRepository;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardsRepository repo;

    /**
     * Constructor for CardController
     * @param repo - Repository for cards entities
     */
    public CardController(CardsRepository repo) {
        this.repo = repo;
    }

    /**
     * Method for retrieving all cards in the repo
     * @return all cards that are stored in repo
     */
    @GetMapping(path = {"", "/"})
    public List<Cards> getAllCards(){
        return repo.findAll();
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

        repo.incrementListPosition(card.positionInsideList, card.list.id);
        Cards saved = repo.save(card);

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
    public ResponseEntity<Cards> removeCard(@RequestBody Cards card){

        if(card==null || isNullOrEmpty(card.title) || card.positionInsideList<0){
            return ResponseEntity.badRequest().build();
        }

        repo.delete(card);

        repo.decrementCardPosition(card.positionInsideList, card.list.id);

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