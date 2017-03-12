package ch.uzh.controller;

import ch.uzh.model.LoginWindow;
import ch.uzh.model.MainWindow;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Created by jesus on 11.03.2017.
 */
public class FriendListController {

    @FXML
    private Button menuBtn;

    private MainWindowController mainWindowController;


    public FriendListController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }



    @FXML
    private void initialize() {
        System.err.println("FriendListController is initializing");

        menuBtn.setOnAction((event) -> {
                    System.err.println("CLICK CLICK CLICK MENU BTN LEL");
                    try {
                        System.err.println("1");
                    } catch (Exception e) {
                        System.err.println("Caught Exception: " + e.getMessage());
                        e.printStackTrace();
                    }

                }

        );


    }

//    public void setFriendList(LoginWindow loginWindow) {
//        this.loginWindow = loginWindow;
//    }

    public void alive() {
        System.err.println("FriendListController is here");


    }
}


