package ch.uzh.controller;

import java.util.Observable;
import java.util.Observer;

import ch.uzh.model.LoginWindow;
import ch.uzh.model.MainWindow;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    private void initialize() {
        System.err.println("LoginWindowController is initializing");

        loginButton.setOnAction((event) -> {
                    System.err.println("CLICK CLICK CLICK");
                    MainWindow mainWindow = new MainWindow();
                    try {
                        System.err.println("1");
                        System.err.println(loginWindow.getStage());
                        System.err.println("2");
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
}


