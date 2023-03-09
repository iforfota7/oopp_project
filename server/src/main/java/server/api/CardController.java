package server.api;

import commons.Cards;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public List<Cards> getAll(){
        return repo.findAll();
    }



}
