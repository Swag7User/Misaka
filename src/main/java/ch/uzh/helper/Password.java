package ch.uzh.helper;

/**
 * Created by Stalin on 15/03/2017.
 */

import org.mindrot.jbcrypt.BCrypt;

import java.io.*;

public class Password {

    /**
     * checks password length
     *
     * @param password plaintext string from the password field
     * @return bool, true if long enough false if too short
     */
    public static boolean checkPassword(String password) {
        if (password.length() < 10) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * checks wether the password is in the top 10000 password list
     *
     * @param password plaintext string from the password field
     * @return bool, true if password is uncommon, false if password is common
     * @throws IOException should really not happen as it checks against a hardcoded static file
     */
    public static boolean passwordContainsTop(String password) throws IOException {
        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(Password.class.getClassLoader().getResourceAsStream("misc/10_million_password_list_top_10000.txt")))) {
            String line;
            while ((line = bReader.readLine()) != null) {
                if (line.equals(password)) {
                    return false;
                }
            }
            return true;
        }
    }

}
