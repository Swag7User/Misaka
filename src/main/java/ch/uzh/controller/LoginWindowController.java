package ch.uzh.controller;

import java.io.IOException;


import ch.uzh.helper.Encryption;
import ch.uzh.helper.P2POverlay;
import ch.uzh.model.LoginWindow;
import ch.uzh.model.MainWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ch.uzh.helper.Password;
import javafx.util.Pair;


/**
 * Created by jesus on 11.03.2017.
 */
public class LoginWindowController {

    private static final Logger log = LoggerFactory.getLogger(LoginWindowController.class);


    private LoginWindow loginWindow;
    private String username;
    private String password;
    private String insecurePassword;
    private P2POverlay p2p;


    private int clientId;
    private String clientIP;


    @FXML
    private Button loginButton;
    @FXML
    private Button dbg;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private MenuItem bootstrapOpt;
    @FXML
    private MenuItem close;
    @FXML
    private TextField ipTextField;

    @FXML
    private void initialize() {

        log.info("LoginWindowController is initializing");
        usernameField.setText("misaka");
        passwordField.setText("1234qwertyuniqueshit");

        bootstrapOpt.setOnAction((event) -> {
                    String ip = ipTextField.getText();
                    log.info("CLICK CLICK CLICK");
                    if (ip != null || !ip.equals("")) {
                        loginWindow.changeP2P(ip);
                    }
                }
        );

        close.setOnAction((event) -> {
                    p2p.shutdown();
                    System.exit(0);
                }
        );

        dbg.setOnAction((event) -> {
                    log.info("CLICK CLICK CLICK");
                    if (loginCheck() == false) {
                        return;
                    } else {
                        reg();
                    }
                }
        );

        loginButton.setOnAction((event) -> {
                    log.info("CLICK ");
                    if (loginCheck() == false) {
                        return;
                    } else {
                        int id = getId();
                        login(id, null);
                    }
                }
        );
    }

    public void getP2P() {
        this.p2p = loginWindow.getP2p();
    }

    public void setLoginWindow(LoginWindow loginWindow) {
        this.loginWindow = loginWindow;
    }

    public void alive() {
        log.info("LoginWindowController is here");
    }

    public void login(final int id, final String ip) {
        MainWindow mainWindow = new MainWindow(p2p);
        try {
            username = usernameField.getText();
            password = Encryption.sha256(username + passwordField.getText());
            log.info("username:" + username);
            log.info("unhashed password:" + password);

            this.clientIP = ip;
            this.clientId = id;

            Pair<Boolean, String> result = mainWindow.login(username, password);

            if (result.getKey() == false) {
                log.info("NOT Loged in successfully, SOMETHING BROKE");

            } else {
                log.info("Logged in A-Okay");

            }
            mainWindow.draw(loginWindow.getStage(), 1, null, username, password, false);


        } catch (Exception e) {
            log.info("Caught Exception: " + e.getMessage());
            e.printStackTrace();
        }

    }


    /**
     * Login function, loads next window
     */
    public void reg() {
        MainWindow mainWindow = new MainWindow(p2p);
        try {
            username = usernameField.getText();
            insecurePassword = passwordField.getText();
            password = Encryption.sha256(username + passwordField.getText());
            log.info("username: " + username);
            log.info("hashed password: " + password);


            Pair<Boolean, String> result = mainWindow.createAccount(username, password);

            if (result.getKey() == true) {
                log.info("Account creation OK");

            } else {
                log.info("Account creation FUCKED UP, OH NOEZ");

            }

            mainWindow.draw(loginWindow.getStage(), 1, null, username, password, false);
        } catch (Exception e) {
            log.info("Caught Exception: " + e.getMessage());

            e.printStackTrace();
        }
    }

    /**
     * checks for valid login password
     * returns false if password is invalid
     * returns true if password is OK
     *
     * @return bool if login is ok or not
     */
    public boolean loginCheck() {
        if (usernameField.getText().length() < 3) {
            errorLabel.setText("Username too short, at least 3 characters");
            return false;
        }
        if (Password.checkPassword(passwordField.getText()) == false) {
            errorLabel.setText("Password too short, please chose at least 10 characters");
            return false;
        }
        try {
            if (Password.passwordContainsTop(passwordField.getText()) == false) {
                errorLabel.setText("Password too common, please chose another one");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("Aw shit, password check borkered");

        }
        return true;
    }

    private int getId() {
        int id = ((Long) System.currentTimeMillis()).intValue();
        return id;
    }


}


