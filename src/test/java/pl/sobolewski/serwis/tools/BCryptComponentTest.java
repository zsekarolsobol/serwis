package pl.sobolewski.serwis.tools;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BCryptComponentTest {
    /*
    sekcje given-when-then to tylko konwencja, która pochodzi z testów pisanych w Groovy, ale fajnie się sprawdza
    i twrozy podział testu na wyraźne sekcje.
     */
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

    //Do asercji używam org.assertj.core.api, ale nic nie stoi na przeszkodzie, aby używać czegoś innego np. z Junit
    @Test
    void shouldThrowExceptionWhenPasswordIsNull() {
        Assertions.assertThatThrownBy(() -> bCryptComponent.hash(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessageContaining("because \"password\" is null");
    }

    @Test
    void shouldVerifyHash() {
        //given
        String password = "abc";
        String hash = bCryptComponent.hash(password);

        //when
        boolean verified = bCryptComponent.verifyHash(password, hash);

        //then
        Assertions.assertThat(verified).isTrue();
    }

    @Test
    void shouldNotVerifyHashIfPasswordAreDifferent() {
        //given
        String oldPassword = "abc";
        String newPassword = "xxx";
        String hash = bCryptComponent.hash(oldPassword);

        //when
        boolean verified = bCryptComponent.verifyHash(newPassword, hash);

        //then
        Assertions.assertThat(verified).isFalse();
    }
}