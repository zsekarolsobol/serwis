package pl.sobolewski.serwis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.sobolewski.serwis.tools.ErrorResponse;
import pl.sobolewski.serwis.tools.PasswordGenerator;

import java.time.LocalDateTime;
import java.util.*;

@Component
@ToString
@Slf4j

public class UserService extends ResponseEntityExceptionHandler {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    PasswordGenerator passwordGenerator;
    @Autowired
    ErrorResponse errorResponse;

    public String getUsernameById(Integer id) {
        System.out.println("Metoda UserService/getUsernameById -> id " + id);
        if (id == null)  {
            throw new IllegalArgumentException("Id nie może być nullem");
        }
        return userRepository.findById((long) id)
                .map(User::getUsername)
                .orElse(null);
    }

    public ResponseEntity getUserById(int id) throws JsonProcessingException {
        Optional<User> user = userRepository.findById((long) id);
        if (user.isPresent()) {
            return ResponseEntity.ok(objectMapper.writeValueAsString(user));
        } else {
            return new ResponseEntity<>(userNotFoundById(id), HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity getUsers() throws JsonProcessingException {
        List<User> allUsers = userRepository.findAll();
        return ResponseEntity.ok(objectMapper.writeValueAsString(allUsers));
    }


    public ResponseEntity addUser(User user) { // dodac kontrole przed pustym !!
        Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();//  User savedUser = userRepository.save(user);
        }
        String noHashingPassword = user.getPassword(); // pobranie jawnie hasla
        String hashPassword = passwordGenerator.hash(user.getPassword()); // hashowanie hasla
        user.setPassword(hashPassword);

        boolean isCorrect = passwordGenerator.verifyHash(noHashingPassword, hashPassword); // sprawdzenie hasha hasla
        if (isCorrect) {
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(savedUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Transactional
    public ResponseEntity changePassword(int id, @NotNull User updatedUser) {
// MUSZE NA SPOKOJNIE TE WYJATKI POCZYTAC

        Optional<User> exist = userRepository.findById((long) id);
        if (!exist.isEmpty()) {
            try {
                userRepository
                        .findById((long) id)
                        .ifPresentOrElse(user -> {
                                    //user.setUsername(updatedUser.getUsername());
                                    user.setPassword(passwordGenerator.hash(updatedUser.getPassword()));

                                    userRepository.save(user);

                                },
                                () -> {
                                    try {
                                        throw new Exception("Item not found!");
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                        );
            } catch (Exception e) {
                System.out.println(e);
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


        //


    }

    public ErrorResponse userNotFoundById(Integer id) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error("Brak user o podanym id=" + id)
                .status(HttpStatus.NOT_FOUND.value())
                .build();
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleException(IllegalArgumentException exception){
        log.error("Blad poczas pobierania usera: {}" , exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

}
