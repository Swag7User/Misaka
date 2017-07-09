package ch.uzh.controller;

import ch.uzh.model.Friend;
import ch.uzh.model.MainWindow;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private MainWindowController mainWindowController;


    public FriendController(MainWindowController mainWindowController, MainWindow mainWindow) {
        this.mainWindowController = mainWindowController;
        this.mainWindow = mainWindow;
    }

    public void setFriendName(String name){
        userName.setText(name);
        log.info("name is set!");
    }

    public void setFriendAvatar(Image img){
        if(img == null){
            return;
        }
        else {
            friendAvatar.setImage(img);
        }
    }



    @FXML
    private void initialize() {
        log.info("FriendController is initializing");

        rootFriendPane.setOnMouseClicked((new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent arg0) {
                log.info("You clicked: " + userName);

                mainWindow.setCurrentChatpartner(userName.getText());
                String currentChatPartner = mainWindow.getCurrentChatpartner();
                log.info("Current Chatpartner: " + currentChatPartner);
                mainWindowController.msgWindowController.friendNameTitle.setText(currentChatPartner);
            }

        }));


    }

    public void alive() {
        log.info("FriendController is here");


    }



}


