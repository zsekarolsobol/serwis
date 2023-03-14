package pl.sobolewski.serwis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser // jeżeli bym korzystał z Spring Security np JWT Token
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    // nie uzaleznia od modelu
    @Test
    void shouldGetUsername() throws Exception {
        //given
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("Sta5")))
        ;
        //then
    }

    // tak sie powinno robić ponieważ jsonPath przekazuje Stringiem Pole - wiec zmiana w modelu powoduje zmiany w testach
    // tutaj testy zaleza od modelu
    // to podejscie łamie zasade nie zaleznosci testów
    @Test
    @Transactional
    // w tej sytuacji wykona sie rollback i dodany rekord nie zaśmieca bazy
    void shouldGetUsernameMoreAssertion() throws Exception {
        //given
        User newUser = new User();
        newUser.setUsername("Karol");
        newUser.setPassword("$2a$10$Lw4C6aDNTdCdQM6HKplRSu7FmIJDgb5bj32/fe8VxB/2EPvsqEm6m");
        newUser.setEmail("zsekarol@o2.pl");
        userRepository.save(newUser);
        //when
        MvcResult mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users/" + newUser.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        //then
        User user = objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(), User.class);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(newUser.getId());
        assertThat(user.getUsername()).isEqualTo(newUser.getUsername());
        assertThat(user.getPassword()).isEqualTo(newUser.getPassword());
    }

}