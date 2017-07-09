package ch.uzh.controller;

import ch.uzh.helper.FriendRequestMessage;
import ch.uzh.helper.P2POverlay;
import ch.uzh.model.Friend;
import ch.uzh.model.FriendsListEntry;
import ch.uzh.model.LoginWindow;
import ch.uzh.model.MainWindow;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by jesus on 11.03.2017.
 */
public class FriendListController {

    private static final Logger log = LoggerFactory.getLogger(FriendListController.class);

    @FXML
    private Button menuBtn;

    @FXML
    private VBox friendListContainer;

    private MainWindowController mainWindowController;
    private P2POverlay p2p;
    private MainWindow mainWindow;


    public FriendListController(MainWindowController mainWindowController, P2POverlay p2p, MainWindow mainWindow) {
        this.mainWindowController = mainWindowController;
        this.p2p = p2p;
        this.mainWindow = mainWindow;
    }

    public void updateFriends(){
        Platform.runLater(new Runnable() {
            public void run() {
                friendListContainer.getChildren().clear();
                for(FriendsListEntry fr : mainWindow.getFriendsList()){
                    FXMLLoader loader;
                    loader = new FXMLLoader(MainWindow.class.getResource("/view/Friend.fxml"));

                    FriendController friendController = new FriendController(mainWindowController, mainWindow);
                    loader.setController(friendController);
                    AnchorPane friendobj = new AnchorPane();
                    try {
                        friendobj = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    friendController.setFriendName(fr.getUserID());
                    friendController.setFriendAvatar(null);

                    friendListContainer.getChildren().add(friendobj);
                    log.info(fr.getUserID());

                }

            }
        });
    }


    public static void showIncomingFriendRequest(FriendRequestMessage requestMessage) {
        // Show notification
        String message = "User " + requestMessage.getSenderUserID() + " wants to add you: \n" + requestMessage.getMessageText();
        log.info(message);
    }

    public void initFriendlist() {
        Platform.runLater(new Runnable() {
            public void run() {
                updateFriends();
            }
        });
    }


    @FXML
    private void initialize() {
        System.err.println("FriendListController is initializing");

        menuBtn.setOnAction((event) -> {
                    System.err.println("CLICK CLICK CLICK MENU BTN LEL");
                    try {
                        System.err.println("1");
                        updateFriends();
                        mainWindowController.drawMenuOverlay();
                    } catch (Exception e) {
                        System.err.println("Caught Exception: " + e.getMessage());
                        e.printStackTrace();
                    }

                }

        );


    }

    public void addFriendToList() {

    }

//    public void setFriendList(LoginWindow loginWindow) {
//        this.loginWindow = loginWindow;
//    }

    public void alive() {
        System.err.println("FriendListController is here");


    }
}


