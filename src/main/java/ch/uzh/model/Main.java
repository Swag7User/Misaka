package ch.uzh.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main  {

    private static final Logger log = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        LoginWindow loginWindow = new LoginWindow();
        loginWindow.launch(LoginWindow.class, args);
        log.info("~~~~~~~~~~~~~This is the end of the porgram, nyaron~~~~~~~~~~~~~~");
    }
}
