package ch.uzh.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jesus on 11.03.2017.
 */
public class MsgWindowController {
    private MainWindowController mainWindowController;

    @FXML
    GridPane gridMSG;

    public MsgWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }



    @FXML
    private void initialize() {
        System.err.println("MsgWindowController is initializing");

        gridMSG.setVgap(0);

    }

//    public void setFriendList(LoginWindow loginWindow) {
//        this.loginWindow = loginWindow;
//    }

    public void alive() {
        System.err.println("MsgWindowController is here");


    }
}


