package server.api;

import commons.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.TagsRepository;

import javax.transaction.Transactional;
import java.util.List;



    @RestController
    @RequestMapping("/api/tags")
    public class TagsController {
        private final TagsRepository repo;

        private final SimpMessagingTemplate msgs;

        public TagsController(TagsRepository repo, SimpMessagingTemplate msgs) {
            this.repo = repo;
            this.msgs = msgs;
        }

        @Transactional
        @PostMapping(path = "/")
        public ResponseEntity<Tags> addTags(@RequestBody Tags tag) {
            if (tag == null || isNullOrEmpty(tag.title)) {
                return ResponseEntity.badRequest().build();
            }

            if (repo.existsById(tag.id))
                return ResponseEntity.badRequest().build();

            Tags saved = repo.save(tag);

            msgs.convertAndSend("/topic/tags/add", saved);
            return ResponseEntity.ok(saved);
        }

        @PostMapping(path = {"/rename", "/rename/"})
        public ResponseEntity<Tags> renameTag(@RequestBody Tags tag) {
            if (tag == null || isNullOrEmpty(tag.title)) {
                return ResponseEntity.badRequest().build();
            }

            if (repo.findById(tag.id).isEmpty())
                return ResponseEntity.badRequest().build();


            if (!repo.existsById(tag.id))
                return ResponseEntity.badRequest().build();

            Tags saved = repo.save(tag);

            msgs.convertAndSend("/topic/tags/rename", saved);
            return ResponseEntity.ok(saved);
        }


        @Transactional
        @PostMapping(path = {"/remove", "/remove/"})
        public ResponseEntity<Tags> removeTags(@RequestBody Tags tag) {
            if (tag == null)
                return ResponseEntity.badRequest().build();

            if(repo.existsById(tag.id)){
                repo.delete(tag);
                msgs.convertAndSend("/topic/tags/remove", tag);
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.badRequest().build();
            }

        }

        @GetMapping(path = "/all/{boardName}")
        public List<Tags> getAllInBoard(@PathVariable long boardName){
            return repo.findAllTagsByBorderID(boardName);
        }
        private static boolean isNullOrEmpty(String s) {
            return s == null || s.isEmpty();
        }

    }
