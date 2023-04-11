package server.api;

import commons.Boards;
import commons.User;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Random;


@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository repo;
    private final SimpMessagingTemplate msgs;
    private String password;

    /**
     * Constructor method
     * @param repo the user repository
     * @param msgs the messaging template for using websockets
     */
    public UserController(UserRepository repo, SimpMessagingTemplate msgs) {
        this.repo = repo;
        this.msgs = msgs;
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
        msgs.convertAndSend("/topic/users/update", user);
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
        msgs.convertAndSend("/topic/users/refresh", user);

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
        List<Boards> boards = repo.findById(username).get().boards;
        msgs.convertAndSend("/topic/users/boards", "deleted");
        return boards;
    }

    /**
     * Generate a random alphanumerical String of length 15 and print it in the server terminal
     * @return Return the password
     */

    public String passwordCreate(){
        Random random = new Random(100);
        password = random.ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65 && i <= 90 || i >= 97))
                .limit(15)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        // print the password so the admin can see it
        return password;
    }

    /**
     * Displays password
     */
    @GetMapping (path = {"/admin", "/admin/"})
    public void getPassword(){
        System.out.println(passwordCreate());
    }

    /**
     * Check whether the password of the user matches the randomly generated one
     * @param password the password of the user
     * @return null/empty string if the password is wrong, "true" if the password is correct
     */
    @Transactional
    @PostMapping(path = "/admin/password")
    @ResponseBody
    public String checkPassword(@RequestBody String password){
        if(this.password == null || password == null){
            return null;
        }
        if(this.password.equals(password)) return "true";
        return null;
    }
}
