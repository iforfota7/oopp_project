package server.api;

import commons.Lists;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ListsRepository;

import javax.transaction.Transactional;
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
     * Method for retrieving all lists in the repo, sorted by their position inside the board
     * @return all lists that are stored in repo
     */
    @GetMapping(path = {"", "/"})
    public List<Lists> getAll(){
        return repo.findAllByOrderByPositionInsideBoardAsc();
    }

    /**
     * Method for adding a list to the repo
     * @param list the list to be added to the repo
     * @return a 200 OK response for a successful http request
     */
    @Transactional
    @PostMapping(path={"", "/"})
    public ResponseEntity<Lists> addList(@RequestBody Lists list) {
        if(isNullOrEmpty(list.title) || list.positionInsideBoard<0)
            return ResponseEntity.badRequest().build();

        // if the instance exists in the repository, the client gets returned a bad request
        if(repo.existsById(list.id))
            return ResponseEntity.badRequest().build();

        Integer maxPositionInsideBoard = repo.maxPositionInsideBoard();
        if(maxPositionInsideBoard == null) {
            // there are no Lists entities
            maxPositionInsideBoard = -1;
        }
        if(list.positionInsideBoard > maxPositionInsideBoard + 1) {
            // position sent by the client is invalid
            return ResponseEntity.badRequest().build();
        }

        repo.incrementListPosition(list.positionInsideBoard);
        Lists saved = repo.save(list);
        return ResponseEntity.ok(saved);
    }

    /**
     * Method for removing a list from the repo
     * Note that when removing the list, only its primary key is taken into account
     * If there is no list that matches the given's list ID, the method does nothing
     * The lists that had a position inside board greater than the given list have their positions decreased by 1
     *
     * @param list the list to be removed
     * @return 200 OK if the request is successful
     *         400 Bad Request if the provided list in invalid
     */

    @Transactional
    @PostMapping(path={"/remove", "/remove/"})
    public ResponseEntity<Lists> removeList(@RequestBody Lists list) {
        if(list == null || isNullOrEmpty(list.title) || list.positionInsideBoard < 0)
            return ResponseEntity.badRequest().build();

        if(repo.existsById(list.id)) {
            // only remove and decrement list positions if the entry with the provided id actually exists
            repo.delete(list);
            repo.decrementListPositions(list.positionInsideBoard);
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
