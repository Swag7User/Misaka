package ch.uzh.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jesus on 11.03.2017.
 */
public class MsgWindowController {
    private MainWindowController mainWindowController;


    public MsgWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }



    @FXML
    private void initialize() {
        System.err.println("MsgWindowController is initializing");

    }

//    public void setFriendList(LoginWindow loginWindow) {
//        this.loginWindow = loginWindow;
//    }

    public void alive() {
        System.err.println("MsgWindowController is here");


    }
}


