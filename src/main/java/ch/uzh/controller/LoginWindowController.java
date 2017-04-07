package ch.uzh.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Observable;
import java.util.Observer;

import ch.uzh.helper.P2POverlay;
import ch.uzh.model.LoginWindow;
import ch.uzh.model.MainWindow;
import ch.uzh.model.UserInfo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ch.uzh.helper.Password;
import javafx.util.Pair;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.futures.BaseFutureListener;
import org.mindrot.jbcrypt.BCrypt;

import javax.sound.sampled.LineUnavailableException;

/**
 * Created by jesus on 11.03.2017.
 */
public class LoginWindowController {

    private final String StartUpNode = "startupClient";
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
    private void initialize() {
        System.err.println("LoginWindowController is initializing");
        usernameField.setText("Test_user_42");
        passwordField.setText("1234qwertyuniqueshit");

        dbg.setOnAction((event) -> {
                    System.err.println("CLICK CLICK CLICK");
                    if (loginCheck() == false){
                        return;
                    }
                    else{
                        reg();
                    }
                }
        );

        loginButton.setOnAction((event) -> {
                    System.err.println("CLICK ");
                    if (loginCheck() == false){
                        return;
                    }
                    else{
                        int id = getId();
                        login(id, null);
                    }
                }
        );
    }

    public void getP2P(){
        this.p2p = loginWindow.getP2p();
    }

    public void setLoginWindow(LoginWindow loginWindow) {
        this.loginWindow = loginWindow;
    }

    public void alive() {
        System.err.println("LoginWindowController is here");
    }

    public void login(final int id, final String ip) {
        MainWindow mainWindow = new MainWindow(p2p);
        try {
            username = usernameField.getText();
            password = passwordField.getText();
            System.err.println("username:" + username);
            System.err.println("unhashed password:" + password);
            this.clientIP = ip;
            this.clientId = id;

            Pair<Boolean, String> result = mainWindow.login(username, password);

            if (result.getKey() == false) {
                System.err.println("NOT Loged in successfully, SOMETHING BROKE");
            } else {
                System.err.println("Logged in A-Okay");
            }
            mainWindow.draw(loginWindow.getStage(),1,null,"debug","123", false);




        } catch (Exception e) {
            System.err.println("Caught Exception: " + e.getMessage());
            e.printStackTrace();
        }
//        try {
//            MainWindow mainWindow = new MainWindow();
//            mainWindow.draw(loginWindow.getStage());
//
//        } catch (Exception e) {
//            System.err.println("Caught Exception: " + e.getMessage());
//            e.printStackTrace();
//
//        }
    }


    /**
     * Login function, loads next window
     */
    public void reg() {
        MainWindow mainWindow = new MainWindow(p2p);
        try {

            username = usernameField.getText();
            insecurePassword = passwordField.getText();
            password = passwordField.getText();
            System.err.println("username: " + username);
            System.err.println("hashed password: " + password);

            Pair<Boolean, String> result = mainWindow.createAccount(username, password);

            if (result.getKey() == true) {
                System.err.println("Account creation OK");
            } else {
                System.err.println("Account creation FUCKED UP, OH NOEZ");
            }

            mainWindow.draw(loginWindow.getStage(),1,null,"debug","123", false);
        } catch (Exception e) {
            System.err.println("Caught Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * checks for valid login password
     * returns false if password is invalid
     * returns true if password is OK
     * @return bool if login is ok or not
     */
    public boolean loginCheck() {
        if (usernameField.getText().length() < 3){
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
            System.err.println("Aw shit, password check borkered");
        }
        return true;
    }

    private int getId() {
        int id = ((Long) System.currentTimeMillis()).intValue();
        return id;
    }



}


