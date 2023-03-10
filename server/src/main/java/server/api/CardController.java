package server.api;

import commons.Cards;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardsRepository;

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

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Cards> addCard(@RequestBody Cards card){

        if(card == null || isNullOrEmpty(card.title) || card.positionInsideList<0){
            return ResponseEntity.badRequest().build();
        }

        Cards saved = repo.save(card);

        return ResponseEntity.ok(saved);
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
