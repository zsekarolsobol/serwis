package pl.sobolewski.serwis.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import pl.sobolewski.serwis.User;
import pl.sobolewski.serwis.UserRepository;
import pl.sobolewski.serwis.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceTest {


  //  @InjectMocks
    @Autowired
    private UserService userService;

    @Mock
    private UserRepository userRepository;



    @Test
    void shouldGetUserById() throws JsonProcessingException {
        //give
        //when
        Optional<User> user = userService.getUserById(1);
        Stream<Integer> userId = user.stream().map(User::getId);     // tak dla siebie
        //then
        assertThat(user).isNotNull();
        assertThat(user.get().getId()).isEqualTo(1);
        assertThat(userId).isNotNull();

    }

    @Test
    void shouldUserNotFoundByIdNotNull() {
        //given
        Integer id = 5;

        //when
        ErrorResponse errorResponse = userService.userNotFoundById(id);

        //then
        assertThat(errorResponse).isNotNull();
    }

    @Test
    void shouldReturnNullIfIdIsNull() {
        //given
        Integer id = null;

        //when
        ErrorResponse errorResponse = userService.userNotFoundById(id);

        //then
        assertThat(errorResponse).isNull();
    }

    @Test
    void shouldReturnUsernameWhenIdIsNotNull() {
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User("Sta5", "haslo")));

        //when
        String username = userService.getUsernameById(1);

        //then
        assertEquals("Sta5", username);
    }

    @Test
    void shouldThrowExceptionWhenIdIsNullGetUsernameById() {
        //given
        Integer id = null;

        Assertions.assertThatThrownBy(() -> userService.getUserById(id))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessageContaining("because \"id\" is null");

    }

    @Test
    void shouldGetAllUsers() throws JsonProcessingException {
        //given
      // when(userRepository.findAll()).thenReturn(List<User> user));
     //   User newUser = new User("Karol", "kkk");
        //then
   //     List<User> users = userService.getUsers();
   //     users.add(newUser);


    //    assertEquals(1, users.size());


    }

    @Test
        // nie dziala
    void shouldUserListIsEmpty() throws JsonProcessingException {
        //when
        //   OngoingStubbing<ResponseEntity> notFound = when(userService.getUsers()).thenReturn(new ResponseEntity<>(
        //             "Nie znaleziono",
        //            HttpStatus.NOT_FOUND));

        //then
        //     assertEquals(HttpStatus.NOT_FOUND, notFound);

    }


}
