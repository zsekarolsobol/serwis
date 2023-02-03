package pl.sobolewski.serwis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.sobolewski.serwis.tools.PasswordGenerator;

import java.util.List;

@Component
@ToString
public class UserService extends RuntimeException {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    PasswordGenerator passwordGenerator;


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
// Jak tutaj sterowac przeplywem jak nie znajdzie?

        try {
            userRepository
                    .findById((long) id)
                    .ifPresentOrElse(user -> {
                                //user.setUsername(updatedUser.getUsername());
                                user.setPassword(passwordGenerator.hash(updatedUser.getPassword()));

                                userRepository.save(user);
                                ResponseEntity status = ResponseEntity.status(HttpStatus.OK).build();

                            },
                            () -> {
                                ResponseEntity status = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                            }

                    );
        } catch (Exception e) {
            System.out.println(e);
        }
// narazie zwraca zawsze ok bo nie wiem ja sterowac

        return ResponseEntity.status(HttpStatus.OK).build();
        //


    }
}
