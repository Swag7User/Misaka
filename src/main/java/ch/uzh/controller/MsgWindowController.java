package ch.uzh.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @FXML
    Button sendMessage;

    @FXML
    Button startVideoChat;

    @FXML
    Button startAudioChat;

    @FXML
    Label friendNameTitle;

    public MsgWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }


    @FXML
    private void initialize() throws Exception {
        System.err.println("MsgWindowController is initializing");

        gridMSG.setVgap(0);

        sendMessage.setOnAction((event) -> {
                    System.err.println("CLICK CLICK CLICK");
                }
        );
        startVideoChat.setOnAction((event) -> {
                    System.err.println("CLICK CLICK CLICK 22");
                    mainWindowController.drawCallPane();
                    try {
                    //    mainWindowController.callWindowController.startVideoHandler();
                        mainWindowController.callWindowController.startVideoCall();
                    } catch(Exception e){
                        e.printStackTrace();
                        System.err.println("Shit, video borkered");

                    }

                }
        );
        startAudioChat.setOnAction((event) -> {
                    System.err.println("CLICK CLICK CLICK 33");
                    mainWindowController.drawCallPane();
                }
        );

    }

//    public void setFriendList(LoginWindow loginWindow) {
//        this.loginWindow = loginWindow;
//    }

    public void alive() {
        System.err.println("MsgWindowController is here");


    }
}


