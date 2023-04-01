package server.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-connection")
public class TestConnectionController {

    /**
     * Checks whether a connection was successful
     * @return a string if the connection was successful, otherwise
     *          it throws an error
     */
    @GetMapping(path = { "", "/" })
    public String testConnection() {
        return "Client successfully connected!";
    }
}
