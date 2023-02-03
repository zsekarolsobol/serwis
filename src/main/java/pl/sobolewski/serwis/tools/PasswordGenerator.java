package pl.sobolewski.serwis.tools;

import org.springframework.stereotype.Component;

@Component
public interface PasswordGenerator {

    public String hash(String password);
    public boolean verifyHash(String password, String hash);
}
