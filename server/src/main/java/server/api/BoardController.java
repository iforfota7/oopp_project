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

    /**
     * Constructor method for repository of board
     * @param repo the board repository
     */
    public BoardController(BoardsRepository repo) {
        this.repo = repo;
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

    /**
     * Find the specified board by the request information of boardName and return this board.
     * @param boardName Name of the target board.
     * @return Target board.
     */
    @GetMapping(path = {"/get/{boardName}"})
    @ResponseBody
    public Boards getBoard(@PathVariable String boardName) {

        if(repo.findByName(boardName).isEmpty()) return null;
        return repo.findByName(boardName).get();
    }

    /**
     *  By sending the new board from the client,
     *  refresh the color information of the same board in the database and store it
     * @param boards board with new color
     * @return Notification of whether the modification was successful
     */
    @PostMapping(path = {"/setCss","/setCss/"})
    public ResponseEntity<Boards> setCss(@RequestBody Boards boards) {
        Optional<Boards> optionalBoard = repo.findByName(boards.name);
        if (optionalBoard.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Boards savedBoard = optionalBoard.get();
        savedBoard.boardBgColor = boards.boardBgColor;
        savedBoard.boardFtColor = boards.boardFtColor;
        savedBoard.listBgColor = boards.listBgColor;
        savedBoard.listFtColor = boards.listFtColor;
        savedBoard.cardBgColor = boards.cardBgColor;
        savedBoard.cardFtColor = boards.cardFtColor;

        Boards saved = repo.save(savedBoard);
        return ResponseEntity.ok(saved);
    }
}

