package pl.sobolewski.serwis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.sobolewski.serwis.tools.HashingPassword;

import java.util.List;

@Component
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    HashingPassword hashingPassword;


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
        String hashPassword = hashingPassword.hash(user.getPassword()); // hashowanie hasla
        user.setPassword(hashPassword);

        boolean itsOk = hashingPassword.verifyHash(noHashingPassword, hashPassword); // sprawdzenie hasha hasla
        if (itsOk) {
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(savedUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


    }
}
