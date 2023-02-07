package pl.sobolewski.serwis.tools;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BCryptComponentTest {

    private final BCryptComponent bCryptComponent = new BCryptComponent();

    @Test
    void shouldHashPassword() {
        //given
        String password = "abc";

        //when
        String hash = bCryptComponent.hash(password);

        //then
        Assertions.assertThat(hash).isNotBlank();
    }
}