package pl.sobolewski.serwis;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RequestMapping("/users")
@RestController
public class UserController extends ResponseEntityExceptionHandler {

    @Autowired
    UserService userService;

    @GetMapping("/details/{id}")
    public ResponseEntity getUsername(@PathVariable("id") int id) {
        return userService.getUsernameById(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getUsers(@PathVariable("id") int id) throws JsonProcessingException {
        return userService.getUserById(id);
    }

    @GetMapping("")
    public ResponseEntity getUsers() throws JsonProcessingException {
        return userService.getUsers();
    }

    @PostMapping("")
    public ResponseEntity addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @NonNull
    @PatchMapping("/{id}")
    public ResponseEntity changePassword(@PathVariable("id") int id, @RequestBody User updatedUser) {
        return userService.changePassword(id, updatedUser);
    }


}
