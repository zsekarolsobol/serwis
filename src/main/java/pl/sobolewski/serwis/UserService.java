package pl.sobolewski.serwis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.sobolewski.serwis.tools.ErrorResponse;
import pl.sobolewski.serwis.tools.PasswordGenerator;

import java.time.LocalDateTime;
import java.util.*;

@Component
@ToString
public class UserService extends RuntimeException {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    PasswordGenerator passwordGenerator;
    @Autowired
    ErrorResponse errorResponse;

    public ResponseEntity getUsername(int id) {
        Optional<User> user = userRepository.findById((long) id);
        if (!user.isEmpty()) {
            String username = user.stream()
                    .map(User::getUsername)
                    .findAny().get();
            System.out.println(username);

            return ResponseEntity.ok(username);
        } else {
                errorResponse.setTimestamp(LocalDateTime.now());
                errorResponse.setError("Brak user o podanym id=" +id);
                errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity getUsers() throws JsonProcessingException {
        List<User> allUsers = userRepository.findAll();
        return ResponseEntity.ok(objectMapper.writeValueAsString(allUsers));
    }


    public ResponseEntity addUser(User user) {
        List<User> userFromDb = userRepository.findByUsername(user.getUsername());

        if (!userFromDb.isEmpty()) {
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
}
