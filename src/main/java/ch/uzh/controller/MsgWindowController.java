package ch.uzh.controller;

import ch.uzh.helper.P2POverlay;
import ch.uzh.model.MainWindow;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Created by jesus on 11.03.2017.
 */
public class MsgWindowController {
    private static final Logger log = LoggerFactory.getLogger(MsgWindowController.class);

    private MainWindowController mainWindowController;
    private MainWindow mainWindow;
    private P2POverlay p2p;

    @FXML
    private GridPane gridMSG;

    @FXML
    private Button sendMessage;

    @FXML
    private Button startVideoChat;

    @FXML
    private Button startAudioChat;

    @FXML
    private Button inviteFriend;

    @FXML
    public Label friendNameTitle;

    @FXML
    private VBox messagesVBox;

    @FXML
    private TextField messageText;

    public MsgWindowController(MainWindowController mainWindowController, MainWindow mainWindow, P2POverlay p2p) {
        this.mainWindowController = mainWindowController;
        this.mainWindow = mainWindow;
        this.p2p = p2p;
    }


    @FXML
    private void initialize() throws Exception {
        log.info("MsgWindowController is initializing");

        gridMSG.setVgap(0);

        sendMessage.setOnAction((event) -> {
                    mainWindow.sendChatMessage(messageText.getText(), mainWindow.getFriendsListEntry(mainWindow.getCurrentChatpartner()));
                    mainWindowController.msgWindowController.addChatBubble(messageText.getText(), "Me ", true);
                    messageText.clear();
                    log.info("CLICK CLICK CLICK");

                }
        );
        startVideoChat.setOnAction((event) -> {
                    log.info("CLICK CLICK CLICK 22");

                    mainWindowController.drawCallPane();
                    try {
                        wait(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        //    mainWindowController.callWindowController.startVideoHandler();
                        //  mainWindowController.callWindowController.startVideoCall();
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.info("Shit, video borkered");


                    }

                }
        );
        startAudioChat.setOnAction((event) -> {
                    log.info("CLICK CLICK CLICK 33");

                    mainWindowController.callWindowController.setFriendsListEntry(mainWindow.getFriendsListEntry(mainWindow.getCurrentChatpartner()));
                    mainWindowController.drawCallPane();
                    mainWindowController.callWindowController.startTransmitting();
                }
        );
        inviteFriend.setOnAction((event) -> {
                    log.info("CLICK CLICK CLICK 44");

                    mainWindowController.callWindowController.stopTransmitting();
                    mainWindowController.drawMsgPane();
                }
        );

    }

    public void addChatBubble(final String message, final String sender, final boolean fromMe) {
        Platform.runLater(new Runnable() {
            public void run() {
                FXMLLoader loader;
                if (fromMe) {
                    loader = new FXMLLoader(MainWindow.class.getResource("/view/ChatBubbleMe.fxml"));
                } else {
                    loader = new FXMLLoader(MainWindow.class.getResource("/view/ChatBubblePeer.fxml"));
                }
                ChatBubbleController chatBubbleController = new ChatBubbleController();
                loader.setController(chatBubbleController);
                AnchorPane chatBubble = new AnchorPane();
                try {
                    chatBubble = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                chatBubbleController.setMessage(message);
                if (!fromMe) {
                    chatBubbleController.setSender(sender);
                }
                chatBubbleController.setDateTime();

                if (fromMe) {
                    messagesVBox.getChildren().add(chatBubble);
                } else {
                    messagesVBox.getChildren().add(chatBubble);
                }
            }
        });
    }

    public void alive() {
        log.info("MsgWindowController is here");
    }
}


