package server.api;

import commons.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.UserRepository;

import javax.transaction.Transactional;

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
     * Change the isAdmin of a user in the database to ture
     * @param user the user to be added
     * @return response entity of user
     */
    @PostMapping(path = "upAdmin")
    @ResponseBody
    public ResponseEntity<Object> upAdmin(@RequestBody User user){
        if (user == null || user.getUsername() == null || user.getUsername().equals("")) {
            return ResponseEntity.badRequest().build();
        }
        boolean isAdmin = user.isAdmin();
        user.setAdmin(isAdmin);
        repo.save(user);
        return ResponseEntity.ok().build();
    }
}
