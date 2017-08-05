package ch.uzh.controller;

import ch.uzh.helper.ChatMessage;
import ch.uzh.model.Friend;
import ch.uzh.model.MainWindow;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Observable;

/**
 * Created by jesus on 11.03.2017.
 */
public class FriendController {

    private static final Logger log = LoggerFactory.getLogger(FriendController.class);

    MainWindow mainWindow;

    @FXML
    private Label userName;

    @FXML
    private ImageView friendAvatar;

    @FXML
    private AnchorPane rootFriendPane;

    @FXML
    private Circle statusCircle;

    @FXML
    private Text newMsgAlert;

    private MainWindowController mainWindowController;


    public FriendController(MainWindowController mainWindowController, MainWindow mainWindow) {
        this.mainWindowController = mainWindowController;
        this.mainWindow = mainWindow;
    }

    public void setFriendName(String name) {
        userName.setText(name);
        log.info("name is set!");
    }

    public String getUsername(){
        return userName.getText();
    }

    public void setFriendAvatar(Image img) {
        if (img == null) {
            return;
        } else {
            friendAvatar.setImage(img);
        }
    }


    @FXML
    private void initialize() {
        log.info("FriendController is initializing");

        rootFriendPane.setOnMouseClicked((new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                log.info("You clicked: " + userName);
                removeNewMsgAlert();

                if(mainWindow.getCurrentChatpartner() == userName.getText()){
                    log.info("chat already open");
                    return;
                }

                mainWindow.setCurrentChatpartner(userName.getText());
                log.info("Current Chatpartner: " + mainWindow.getCurrentChatpartner());
                mainWindowController.getMsgWindowController().friendNameTitle.setText(mainWindow.getCurrentChatpartner());

                log.info("removing old chat ");
                mainWindowController.getMsgWindowController().removeChatBubbles();
                List<ChatMessage> newChat = mainWindow.getMessagesFrom(mainWindow.getCurrentChatpartner());
                if(newChat == null){
                    log.info("no chat messages there yet ");
                }
                else {
                    for (ChatMessage msg : newChat) {
                        mainWindowController.getMsgWindowController().addChatBubble(msg.getMessageText(), msg.getSenderUserID(), false);
                    }
                }

            }

        }));

    }

    public void setNewMsgAlert(){
        newMsgAlert.setOpacity(1);
    }
    public void removeNewMsgAlert(){
        newMsgAlert.setOpacity(0);
    }

    public void setCircleColorGreen(){
        statusCircle.setFill(Color.GREEN);
    }
    public void setCircleColorGrey(){
        statusCircle.setFill(Color.GREY);
    }
    public void setCircleColorRed(){
        statusCircle.setFill(Color.RED);
    }

    public void alive() {
        log.info("FriendController is here");
    }

}


