package server.api;

import commons.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.ListsRepository;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class ListController {
    private final ListsRepository repo;

    /**
     * Constructor for ListController
     * @param repo - Repository for lists entities
     */
    public ListController(ListsRepository repo) {
        this.repo = repo;
    }

    /**
     * Method for retrieving all lists in the repo
     * @return all lists that are stored in repo
     */
    @GetMapping(path = {"", "/"})
    public List<Lists> getAll(){
        return repo.findAll();
    }

}
