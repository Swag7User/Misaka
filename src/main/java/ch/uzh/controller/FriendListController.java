package ch.uzh.controller;

import ch.uzh.helper.FriendRequestMessage;
import ch.uzh.helper.FriendStuff;
import ch.uzh.helper.P2POverlay;
import ch.uzh.model.Friend;
import ch.uzh.model.FriendsListEntry;
import ch.uzh.model.LoginWindow;
import ch.uzh.model.MainWindow;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import net.tomp2p.holep.DuplicatesHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jesus on 11.03.2017.
 */
public class FriendListController {

    @FXML
    private Button menuBtn;

    @FXML
    private VBox friendListContainer;

    private MainWindowController mainWindowController;
    private P2POverlay p2p;
    private MainWindow mainWindow;

    private FriendStuff friendStuff;
    private ListChangeListener<FriendsListEntry> listChangeListener;
    //private List<Friend> friendList;
    //public Map<String, FriendController> friendControllerList;


    public FriendListController(MainWindowController mainWindowController, P2POverlay p2p, MainWindow mainWindow) {
        this.mainWindowController = mainWindowController;
        this.p2p = p2p;
        this.mainWindow = mainWindow;
        //this.friendList = new ArrayList<Friend>();

/*        friendList.add(new Friend("R2D2"));
        friendList.add(new Friend("Lenin"));
        friendList.add(new Friend("hi 2 u"));
        friendList.add(new Friend());*/
        //initFriendlist();
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
                    //friend.addObserver(friendController);
                    System.err.println(fr.getUserID());
                    System.err.println(friendController);
                    System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    //friendControllerList.put(friend.getName(), friendController);
                }

            }
        });
    }

/*    public void addFriend(FriendsListEntry f) {
        final FriendsListEntry friend = f;
        Platform.runLater(new Runnable() {
            public void run() {
                FXMLLoader loader;
                loader = new FXMLLoader(MainWindow.class.getResource("/view/Friend.fxml"));

                FriendController friendController = new FriendController(mainWindowController);
                loader.setController(friendController);

                AnchorPane friendobj = new AnchorPane();
                try {
                    friendobj = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                friendController.setFriendName(friend.getUserID());
                friendController.setFriendAvatar(null);

                friendListContainer.getChildren().add(friendobj);
                //friend.addObserver(friendController);
                System.err.println(friend.getUserID());
                System.err.println(friendController);
                System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                //friendControllerList.put(friend.getName(), friendController);
            }
        });
    }*/

    public static void showIncomingFriendRequest(FriendRequestMessage requestMessage) {
        // Show notification
        String message = "User " + requestMessage.getSenderUserID() + " wants to add you: \n" + requestMessage.getMessageText();
        System.err.println(message);


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


