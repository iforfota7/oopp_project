package server.api;

import commons.Boards;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardsRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@RestController
@RequestMapping("api/boards")
public class BoardController {
    private final BoardsRepository repo;

    public BoardController(BoardsRepository repo) {
        this.repo = repo;
    }

    @Transactional
    @PostMapping(path={"", "/"})
    public ResponseEntity<Boards> addBoard(@RequestBody Boards board){
        if(board == null || isNullOrEmpty(board.getName()))
            return ResponseEntity.badRequest().build();

        if(repo.existsById(board.name))
            return ResponseEntity.badRequest().build();

        Boards saved = repo.save(board);

        return ResponseEntity.ok(saved);
    }

    /**
     * A method used to find a board by its ID
     * @param boardID the ID of the board
     * @return the board corresponding to the specified ID if it exists, otherwise null
     */
    @GetMapping(path = {"/find/{boardID}"})
    @ResponseBody
    public Boards findBoard(@PathVariable String boardID) {

        if(repo.findById(boardID).isEmpty()) return null;
        return repo.findById(boardID).get();
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
    @Transactional
    @DeleteMapping("/removeBoard")
    public ResponseEntity<Void> removeBoard(@PathVariable String boardName) {
        if (boardName == null || boardName.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Boards> optionalBoard = repo.findById(boardName);

        if (!optionalBoard.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        repo.deleteById(boardName);

        return ResponseEntity.noContent().build();
    }
}
