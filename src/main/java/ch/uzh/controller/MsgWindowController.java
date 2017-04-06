package ch.uzh.controller;

import ch.uzh.helper.P2POverlay;
import ch.uzh.model.Main;
import ch.uzh.model.MainWindow;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jesus on 11.03.2017.
 */
public class MsgWindowController {
    private MainWindowController mainWindowController;
    private MainWindow mainWindow;
    private P2POverlay p2p;

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

    @FXML
    TextField messageText;

    public MsgWindowController(MainWindowController mainWindowController, MainWindow mainWindow, P2POverlay p2p) {
        this.mainWindowController = mainWindowController;
        this.mainWindow = mainWindow;
        this.p2p = p2p;
    }


    @FXML
    private void initialize() throws Exception {
        System.err.println("MsgWindowController is initializing");
        p2p.put("lol","hello");


        gridMSG.setVgap(0);

        sendMessage.setOnAction((event) -> {
            String addr = messageText.getText();
            System.err.println("existsUser " + addr + " ????????????????????????????????????? \n");
            System.err.println(mainWindow.existsUser(addr));
           // mainWindow.sendChatMessage("chello my friend");
                    System.err.println("CLICK CLICK CLICK");
                }
        );
        startVideoChat.setOnAction((event) -> {
                    System.err.println("CLICK CLICK CLICK 22");
                    mainWindowController.drawCallPane();
                    try {
                        wait(1000);
                    } catch(Exception e){

                    }
                    try {
                    //    mainWindowController.callWindowController.startVideoHandler();
                      //  mainWindowController.callWindowController.startVideoCall();
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


