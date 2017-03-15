package ch.uzh.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import ch.uzh.model.LoginWindow;
import ch.uzh.model.MainWindow;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ch.uzh.helper.Password;

/**
 * Created by jesus on 11.03.2017.
 */
public class LoginWindowController {

    private LoginWindow loginWindow;
    private String username;
    private String password;

    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    @FXML
    private void initialize() {
        System.err.println("LoginWindowController is initializing");

        loginButton.setOnAction((event) -> {
                    if (Password.checkPassword(passwordField.getText()) == false) {
                        errorLabel.setText("Password too short, please chose at least 10 characters");
                        return;
                    }
                    try {
                        if (Password.passwordContainsTop(passwordField.getText()) == false) {
                            errorLabel.setText("Password too common, please chose another one");
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Aw shit, password check borkered");
                    }

                    System.err.println("CLICK CLICK CLICK");
                    MainWindow mainWindow = new MainWindow();
                    try {
                        username = usernameField.getText();
                        password = Password.hashPassword(passwordField.getText());
                        System.err.println("username:" + username);
                        System.err.println("hashed password:" + password);
                        mainWindow.draw(loginWindow.getStage());
                    } catch (Exception e) {
                        System.err.println("Caught Exception: " + e.getMessage());
                        e.printStackTrace();
                    }

                }

        );
    }

    public void setLoginWindow(LoginWindow loginWindow) {
        this.loginWindow = loginWindow;
    }

    public void alive() {
        System.err.println("LoginWindowController is here");
    }

    public void login(){

    }
}


