package server.api;

import commons.Boards;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardsRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("api/boards")
public class BoardController {
    private final BoardsRepository repo;

    public BoardController(BoardsRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = "/all")
    public List<Boards> getAll() {
        return repo.findAll();
    }

    @Transactional
    @PostMapping(path={"", "/"})
    public ResponseEntity<Boards> addBoard(@RequestBody Boards board){
        if(board == null || isNullOrEmpty(board.getName()))
            return ResponseEntity.badRequest().build();

        if(!repo.findByName(board.name).isEmpty())
            return ResponseEntity.badRequest().build();

        Boards saved = repo.save(board);

        return ResponseEntity.ok(saved);
    }



    /**
     * A method used to find a board by its ID
     * @param boardName the ID of the board
     * @return the board corresponding to the specified ID if it exists, otherwise null
     */
    @GetMapping(path = {"/find/{boardName}"})
    @ResponseBody
    public Boards findBoard(@PathVariable String boardName) {

        if(repo.findByName(boardName).isEmpty()) return null;
        return repo.findByName(boardName).get();
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
    @Transactional
    @PostMapping(path = {"/remove", "/remove/"})
    public ResponseEntity<Void> removeBoard(@RequestBody Boards boards) {
        String boardName = boards.name;
        System.out.println(boardName);
        if (boardName == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Boards> optionalBoard = repo.findByName(boardName);

        if (optionalBoard.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        repo.deleteById(boards.id);

        return ResponseEntity.ok().build();
    }
}
