package server.api;

import commons.Subtask;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.SubtaskRepository;

import javax.transaction.Transactional;

@RestController
@RequestMapping("api/subtask")
public class SubtaskController {
    private final SubtaskRepository repo;

    /**
     * Constructor method for repository of subtask
     * @param repo the subtask repository
     */
    public SubtaskController(SubtaskRepository repo){
        this.repo = repo;
    }
    /**
     * Add a new subtask to database
     * @param subtask the subtask to be added
     * @return response entity
     */
    @Transactional
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Subtask> addSubtask(@RequestBody Subtask subtask){
        if(subtask == null || isNullOrEmpty(subtask.title) || subtask.position < 0){
            return ResponseEntity.badRequest().build();
        }

        if(repo.existsById(subtask.id)){
            return ResponseEntity.badRequest().build();
        }

        Subtask saved = repo.save(subtask);
        return ResponseEntity.ok(saved);
    }

    /**
     * Method to remove a subtask from the repo if the subtask exists
     * @param subtask the subtask to be removed
     * @return a 200 OK response for a successful http request
     */
    @Transactional
    @PostMapping(path = {"/remove", "/remove/"})
    public ResponseEntity<Subtask> removeSubtask(@RequestBody Subtask subtask){
        if(subtask == null){
            return ResponseEntity.badRequest().build();
        }

        if(repo.existsById(subtask.id)){
            repo.delete(subtask);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
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
