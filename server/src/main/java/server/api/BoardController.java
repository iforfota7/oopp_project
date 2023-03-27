package server.api;

import commons.Boards;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BoardsRepository;

import javax.transaction.Transactional;
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

        if(repo.existsById(board.name))
            return ResponseEntity.badRequest().build();

        Boards saved = repo.save(board);

        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
