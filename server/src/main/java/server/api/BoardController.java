package server.api;

import commons.Boards;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.BoardsRepository;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.function.Consumer;

@RestController
@RequestMapping("api/boards")
public class BoardController {
    private final BoardsRepository repo;
    private final SimpMessagingTemplate msgs;

    /**
     * Constructor method for repository of board
     * @param repo the board repository
     * @param msgs used for websockets
     */
    public BoardController(BoardsRepository repo, SimpMessagingTemplate msgs) {
        this.repo = repo;
        this.msgs = msgs;
    }


    private Map<Object, Consumer<Boards>> listeners = new HashMap<>();

    /** Long Polling request server endpoint.
     * @return nothing if it timeouts, else board for deletion
     */
    @GetMapping(path = {"/longPolling", "/longPolling/"})
    public DeferredResult<ResponseEntity<Boards>> getUpdates() {

        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        var res = new DeferredResult<ResponseEntity<Boards>>(5000L, noContent);

        var key = new Object();
        listeners.put(key, b -> {
            res.setResult(ResponseEntity.ok(b));

        });

        res.onCompletion(()->{
            listeners.remove(key);
        });

        return res;
    }


    /**
     * Get all boards from database
     * @return a list of all boards
     */
    @GetMapping(path = "/all")
    public List<Boards> getAll() {
        return repo.findAll();
    }






    /**
     * Add new board to database
     * @param board the board to be added
     * @return response entity
     */
    @Transactional
    @PostMapping(path={"", "/"})
    public ResponseEntity<Boards> addBoard(@RequestBody Boards board){
        if(board == null || isNullOrEmpty(board.name))
            return ResponseEntity.badRequest().build();

        if(!repo.findByName(board.name).isEmpty())
            return ResponseEntity.badRequest().build();

        Boards saved = repo.save(board);

        return ResponseEntity.ok(saved);
    }

    /**
     * Rename method that saves a board with a new name in the database
     * @param board the board with the new name
     * @return the response entity
     */
    @Transactional
    @PostMapping(path={"/rename", "/rename/"})
    public ResponseEntity<Boards> renameBoard(@RequestBody Boards board){
        if(board == null || isNullOrEmpty(board.name))
            return ResponseEntity.badRequest().build();

        if(!repo.findByName(board.name).isEmpty())
            return ResponseEntity.badRequest().build();

        Boards saved = repo.save(board);

        return ResponseEntity.ok(saved);
    }


    /**
     * Updates information about the board
     * The constraint is that the board cannot be renamed
     *
     * @param board The board to be updated
     * @return The updated board
     */
    @Transactional
    @PostMapping(path={"/update", "/update/"})
    public ResponseEntity<Boards> updateBoard(@RequestBody Boards board) {
        if(board == null || isNullOrEmpty(board.name))
            return ResponseEntity.badRequest().build();

        if(repo.findById(board.id).isEmpty())
            return ResponseEntity.badRequest().build();

        Boards foundBoard = repo.findById(board.id).get();
        if(!board.name.equals(foundBoard.name))
            return ResponseEntity.badRequest().build();

        Boards saved = repo.save(board);
        msgs.convertAndSend("/topic/boards/update", saved);

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

    /**
     * Method that checks whether something is null or empty
     * @param s String to be checked
     * @return true is it is null or empty, otherwise false
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Method that removes board from the database
     * @param boards the board to be deleted
     * @return the response entity
     */
    @Transactional
    @PostMapping(path = {"/remove", "/remove/"})
    public ResponseEntity<Void> removeBoard(@RequestBody Boards boards) {
        if(boards == null)
            return ResponseEntity.badRequest().build();
        String boardName = boards.name;

        if (boardName == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Boards> optionalBoard = repo.findByName(boardName);

        if (optionalBoard.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        listeners.forEach((k, l)->l.accept(boards));

        repo.removeReferenced(boards.id);
        repo.deleteById(boards.id);

        return ResponseEntity.ok().build();
    }
}
