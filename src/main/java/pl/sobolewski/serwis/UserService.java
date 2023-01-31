package pl.sobolewski.serwis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.sobolewski.serwis.tools.MD5;

import java.util.List;

@RestController
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MD5 md5;


    @GetMapping("/users")
    public ResponseEntity getUsers() throws JsonProcessingException {
        List<User> allUsers = userRepository.findAll();
        return ResponseEntity.ok(objectMapper.writeValueAsString(allUsers));
    }

    @PostMapping("/users")
    public ResponseEntity addUser(@RequestBody User user) {
        List<User> userFromDb = userRepository.findByUsername(user.getUsername());

        if(!userFromDb.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();//  User savedUser = userRepository.save(user);
        }
        String hashPassword = md5.getMd5(user.getPassword());
        user.setPassword(hashPassword);

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);


    }
}
