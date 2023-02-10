package pl.sobolewski.serwis;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController extends ResponseEntityExceptionHandler {


    private final UserService userService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/details/{id}")
    public ResponseEntity getUsername(@PathVariable("id") Integer id) {
        String username = userService.getUsernameById(id);

        if (username == null) {
            return new ResponseEntity<>(userService.userNotFoundById(id), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(username);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity getUsers(@PathVariable("id") int id) throws JsonProcessingException {
        Optional<User> userById = userService.getUserById(id);

        if (!userById.isPresent()) {
            return new ResponseEntity<>(userService.userNotFoundById(id), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userById);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("")
    public ResponseEntity getUsers() throws JsonProcessingException {
        return userService.getUsers();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("")
    public ResponseEntity addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @NonNull
    @PatchMapping("/{id}")
    public ResponseEntity changePassword(@PathVariable("id") int id, @RequestBody User updatedUser) {
        return userService.changePassword(id, updatedUser);
    }


}
