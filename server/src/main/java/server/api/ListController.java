package server.api;

import commons.Lists;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    /**
     * Method for adding a list to the repo
     * @param list the list to be added to the repo
     * @return a 200 OK response for a successful http request
     */
    @PostMapping(path={"", "/"})
    public ResponseEntity<Lists> addList(@RequestBody Lists list) {

        if(isNullOrEmpty(list.title) || list.positionInsideBoard<0)
            return ResponseEntity.badRequest().build();

        Lists saved = repo.save(list);
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
