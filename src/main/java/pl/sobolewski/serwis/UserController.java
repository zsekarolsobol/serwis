package pl.sobolewski.serwis;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.sobolewski.serwis.dto.UserDto;
import pl.sobolewski.serwis.dto.UserEmailDto;
import pl.sobolewski.serwis.mapper.UserDtoMapper;

import java.util.List;
import java.util.Optional;

import static pl.sobolewski.serwis.mapper.UserDtoMapper.*;


@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController extends ResponseEntityExceptionHandler {


    private final UserService userService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/email")
    public ResponseEntity getUsersEmails() throws JsonProcessingException {
        List<UserEmailDto> usersEmails = mapUserToUserEmailDto(userService.getUsers());
        if (usersEmails.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(usersEmails);
        }

    }

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
        List<User> userList = userService.getUsers();
        if (userList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(userList);
        }

    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("")
    public ResponseEntity addUser(@RequestBody @Valid UserDto userDto) {
        return userService.addUser(mapToUser(userDto)
        );
    }



    @CrossOrigin(origins = "http://localhost:3000")
    @NonNull
    @PatchMapping("/{id}")
    public ResponseEntity changePassword(@PathVariable("id") int id, @RequestBody @Valid UserDto userDto) {
        User updatedUser = new User();
        updatedUser.setId(id);
        updatedUser.setPassword(userDto.getPassword());
        return userService.changePassword(id, updatedUser);
    }


}
