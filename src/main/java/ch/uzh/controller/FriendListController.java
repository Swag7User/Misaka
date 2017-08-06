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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private List<FriendController> FriendControllerList;


    public FriendListController(MainWindowController mainWindowController, P2POverlay p2p, MainWindow mainWindow) {
        this.mainWindowController = mainWindowController;
        this.p2p = p2p;
        this.mainWindow = mainWindow;
        FriendControllerList = new ArrayList<>();
    }

    public void updateFriends() {
        Platform.runLater(new Runnable() {
            public void run() {
                friendListContainer.getChildren().clear();
                FriendControllerList.clear();
                for (FriendsListEntry fr : mainWindow.getFriendsList()) {
                    FXMLLoader loader;
                    loader = new FXMLLoader(MainWindow.class.getResource("/view/Friend.fxml"));

                    FriendController friendController = new FriendController(mainWindowController, mainWindow);
                    loader.setController(friendController);
                    AnchorPane friendobj = new AnchorPane();
                    try {
                        friendobj = loader.load();
                        friendobj.setUserData(fr.getUserID());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    friendController.setFriendName(fr.getUserID());
                    friendController.setFriendAvatar(null);

                    friendListContainer.getChildren().add(friendobj);
                    log.info(fr.getUserID());

                    FriendControllerList.add(friendController);


                }

            }
        });
    }

    public void setGreen(String userId) {
        for (FriendController friendController : FriendControllerList) {
            log.info("setting online user: " + friendController.getUsername());
            if(friendController.getUsername().equals(userId) && mainWindow.getFriendsListEntry(userId).isOnline()){
                friendController.setCircleColorGreen();
            }else{
                friendController.setCircleColorRed();

            }
        }
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
        log.info("FriendListController is initializing");

        menuBtn.setOnAction((event) -> {
                    log.info("CLICK CLICK CLICK MENU BTN LEL");

                    try {
                        log.info("1");
                        updateFriends();
                        mainWindowController.drawMenuOverlay();
                    } catch (Exception e) {
                        log.info("Caught Exception: " + e.getMessage());
                        e.printStackTrace();
                    }

                }

        );


    }

    public void alertNewMsg(String userID){
        for (FriendController friendController : FriendControllerList) {
            log.info("setting Alert for user: " + friendController.getUsername());
            friendController.setNewMsgAlert();
        }
    }


    public void alive() {
        log.info("FriendListController is here");

    }
}


