package server.api;

import commons.Boards;
import commons.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository repo;

    /**
     * Constructor method
     * @param repo the user repository
     */
    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    /**
     * A post request sent using a "" or "/" mapping will add a user to the database
     * @param user the user to be added
     * @return response entity of user
     */
    @Transactional
    @PostMapping(path = {"", "/"})
    public ResponseEntity<User> addUser(@RequestBody User user){
        if(user == null || user.username == null || user.username.equals("")){
            return ResponseEntity.badRequest().build();
        }

        repo.save(user);
        return ResponseEntity.ok().build();
    }

    /**
     * A method used to find a user by username
     * @param username the username of the user
     * @return the user if found, otherwise null
     */
    @GetMapping(path = "/find/{username}")
    @ResponseBody
    public User findUser(@PathVariable String username){
        if(repo.findById(username).isEmpty()) return null;
        return repo.findById(username).get();
    }
    /**
     * Update method for the user
     * @param user the user
     * @return a response entity
     */
    @Transactional
    @PostMapping (path = {"/update", "/update/"})
    public User updateUser(@RequestBody User user){
        return repo.save(user);
    }

    /**
     * Change the isAdmin of a user in the database to ture
     * @param user the user to be added
     * @return response entity of user
     */
    @PostMapping(path = {"/refreshAdmin", "/refreshAdmin/"})
    @ResponseBody
    public ResponseEntity<Object> refreshAdmin(@RequestBody User user){
        if (user == null || user.username == null || user.username.equals("")) {
            return ResponseEntity.badRequest().build();
        }
        boolean admin = user.isAdmin;
        user.isAdmin = admin;
        repo.save(user);
        return ResponseEntity.ok().build();
    }

    /**
     * Get the color presets stored by the user in the database
     * @param user current user
     * @return user with new color preset
     */
    @PostMapping(path = {"/setTaskColor", "/setTaskColor/"})
    @ResponseBody
    public ResponseEntity<Object> refreshTaskColor(@RequestBody User user){
        if (user == null || user.username == null || user.username.equals("")) {
            return ResponseEntity.badRequest().build();
        }
        Map<String, String> colorPreset = user.colorPreset;
        user.colorPreset = colorPreset;
        repo.save(user);
        return ResponseEntity.ok().build();
    }

    /**
     * Get all the boards that a user has joined
     * @param username the username of the user
     * @return a list of boards the user has joined
     */
    @GetMapping (path = "/boards/{username}")
    @ResponseBody
    public List<Boards> getAllBoards(@PathVariable String username) {
        return repo.findById(username).get().boards;
    }
}
