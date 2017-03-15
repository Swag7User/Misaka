package ch.uzh.helper;

/**
 * Created by Stalin on 15/03/2017.
 */

import org.mindrot.jbcrypt.BCrypt;

import java.io.*;

public class Password {

    public static String hashPassword(String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        return hashed;
    }

    public static boolean checkPassword(String password) {
        if (password.length() < 10) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean passwordContainsTop(String password) throws IOException{
        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(Password.class.getClassLoader().getResourceAsStream("misc/10_million_password_list_top_10000.txt")))) {
            String line;
            while ((line = bReader.readLine()) != null) {
                System.err.println(line + " = " + password);
                if (line.equals(password)){
                    return false;
                }
            }
            return true;
        }
    }

}
