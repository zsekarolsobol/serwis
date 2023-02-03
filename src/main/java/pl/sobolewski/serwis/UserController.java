package pl.sobolewski.serwis;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public ResponseEntity getUsers() throws JsonProcessingException {
        return userService.getUsers();
    }

    @PostMapping("/users")
    public ResponseEntity addUser(@RequestBody User user) {
        return userService.addUser(user);
    }


}
