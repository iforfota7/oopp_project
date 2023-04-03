package server.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-connection")
public class TestConnectionController {
    @GetMapping(path = { "", "/" })
    public String testConnection() {
        return "Client successfully connected!";
    }
}
