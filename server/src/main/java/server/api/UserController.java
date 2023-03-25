package server.api;

import commons.User;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.UserRepository;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository repo;
    private final SimpMessagingTemplate msgs;

    public UserController(UserRepository repo, SimpMessagingTemplate msgs) {
        this.repo = repo;
        this.msgs = msgs;
    }

    @Transactional
    @PostMapping(path = {"", "/"})
    public ResponseEntity<User> addUser(@RequestBody User user){
        if(user == null || user.username == null || user.username.equals("")){
            return ResponseEntity.badRequest().build();
        }

        msgs.convertAndSend("/topic/user/add", repo.save(user));
        return ResponseEntity.ok().build();
    }
}
