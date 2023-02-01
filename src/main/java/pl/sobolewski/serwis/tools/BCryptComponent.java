package pl.sobolewski.serwis.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;


public class BCryptComponent implements HashingPassword {
    /*
    private static final Logger log = LoggerFactory.getLogger(BCryptComponent.class);
    private final int logRounds;

    public BCryptComponent(int logRounds) {
        this.logRounds = logRounds;
    }

     */

    public BCryptComponent() {
    }

    @Override
    public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
