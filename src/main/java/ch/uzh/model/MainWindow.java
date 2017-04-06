package ch.uzh.model;

import ch.uzh.controller.*;
import ch.uzh.helper.ChatMessage;
import ch.uzh.helper.P2POverlay;
import ch.uzh.helper.PrivateUserProfile;
import ch.uzh.helper.PublicUserProfile;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import net.tomp2p.peers.PeerAddress;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by jesus on 11.03.2017.
 */
public class MainWindow {
    private Stage stage;
    private int id;
    private String ip;
    private String username;
    private String password;
    private boolean bootstrapNode;

    private MainWindowController mainWindowController;
    private MsgWindowController msgWindowController;
    private FriendListController friendListController;
    private MenuOverlayController menuOverlayController;
    private CallWindowController callWindowController;


    private AnchorPane mainPane;
    private AnchorPane msgWindowPane;
    private AnchorPane friendListPane;
    private AnchorPane menuOverlay;
    private AnchorPane callWindow;

    private P2POverlay p2p;

    private PrivateUserProfile userProfile;




    public void draw(Stage stage, int id, String ip, String username, String password,
                     boolean bootstrapNode) throws Exception {
        this.stage = stage;
        this.id = id;
        this.ip = ip;
        this.username = username;
        this.password = password;
        this.bootstrapNode = bootstrapNode;
        drawMainWindow();
    }

    public MainWindow( P2POverlay p2p){
        this.p2p = p2p;
    }

    private void drawMainWindow() throws Exception { //TODO: exception handling
        System.err.println("2222222~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.err.println("FXML resource: " + getClass().getResource("ch/uzh/csg/p2p/screens/MainWindow.fxml"));
        System.err.println("FXML resource: " + getClass().getResource("/MainWindow.fxml"));
        System.err.println("FXML resource: " + getClass().getResource("MainWindow.fxml"));
        System.err.println("FXML resource: " + getClass().getResource("/view/MainWindow.fxml"));
        System.err.println("2222222~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainWindow.fxml"));
        mainWindowController = new MainWindowController(stage, p2p);

        msgWindowController = new MsgWindowController(mainWindowController, this);  // <--- THIS
        mainWindowController.setMsgWindowController(msgWindowController);

        friendListController = new FriendListController(mainWindowController);   // <--- THIS
        mainWindowController.setFriendListController(friendListController);

        menuOverlayController = new MenuOverlayController(mainWindowController);   // <--- THIS
        mainWindowController.setMenuOverlayController(menuOverlayController);

        callWindowController = new CallWindowController(mainWindowController);
        mainWindowController.setCallWindowController(callWindowController);


        loader.setController(mainWindowController);


        mainWindowController.setMainWindow(this);

        mainPane = loader.load();

        drawFriendList();
        drawMsgWindow();
        drawmenuOverlay();
        drawCallWindow();

        mainWindowController.setFriendlistPane(friendListPane);
        mainWindowController.setMsgWindowPane(msgWindowPane);
        mainWindowController.setCallWindowPane(callWindow);
        mainWindowController.setMenuOverlayPane(menuOverlay);


        mainWindowController.setLeftPane(friendListPane);

        mainWindowController.drawMsgPane();

        Scene scene = new Scene(mainPane);

        scene.getStylesheets().add("/css/MainWindow.css");


        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/favicon.png")));
        stage.setTitle("Misaka - Main");
        stage.setScene(scene);

        stage.setMinWidth(800);
        stage.setMinHeight(480);
       // stage.centerOnScreen();

        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            public void handle(WindowEvent event) {
                stage.close();
                System.exit(0);
            }
        });


    }



/*    public void sendChatMessage(String text, String addr) {
        ChatMessage chatMessage = new ChatMessage(p2p.getPeerAddress(), userProfile.getUserID(), text);
        p2p.sendNonBlocking((PeerAddress) addr, chatMessage, false);
    }*/



    private void drawFriendList() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FriendList.fxml"));
        loader.setController(friendListController);
        friendListPane = loader.load();
    }

    private void drawMsgWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MsgWindow.fxml"));
        loader.setController(msgWindowController);
        msgWindowPane = loader.load();
    }

    private void drawCallWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CallWindow.fxml"));
        loader.setController(callWindowController);
        callWindow = loader.load();
    }

    private void drawmenuOverlay() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuOverlay.fxml"));
        loader.setController(menuOverlayController);
        menuOverlay = loader.load();
    }

    public Pair<Boolean, String> createAccount(String userID, String password) {
        // Check if the user is already in the friendslist

        // Check if account exists
        if (p2p.getBlocking(userID) != null) {
            System.err.println("NULL??? WHAT THE SHIT WHY???");
            return new Pair<>(false, "Could not create user account. UserID already taken."); //TODO: LOGIN NOW

        }

        // Create private UserProfile
        userProfile = new PrivateUserProfile(userID, password);

        // TODO: Encrypt it with password
        if (savePrivateUserProfile() == false) {
            return new Pair<>(false, "Error. Could not save private UserProfile");
        }

        // Create public UserProfile
        PublicUserProfile publicUserProfile;
        publicUserProfile = new PublicUserProfile(userID, userProfile.getKeyPair().getPublic(),
                null);

        if (p2p.put(userID, publicUserProfile)) {
            return new Pair<>(true, "User account for user \"" + userID + "\" successfully created");
        } else {
            return new Pair<>(false, "Network DHT error. Could not save public UserProfile");
        }
    }

    public Pair<Boolean, String> login(String userID, String password, String insecurePassword) {


/*        if (BCrypt.checkpw(insecurePassword, hashed))
            System.out.println("It matches");
        else
            System.out.println("It does not match");

*/

        // Get userprofile if password and username are correct
        Object getResult = p2p.getBlocking(userID + password);
        if (getResult == null) {
            return new Pair<>(false, "Login data not valid, Wrong UserID/password?");
        }
        userProfile = (PrivateUserProfile) getResult;

        // Get public user profile
        Object objectPublicUserProfile = p2p.getBlocking(userID);
        if (objectPublicUserProfile == null) {
            return new Pair<>(false, "Could not retrieve public userprofile");
        }
        PublicUserProfile publicUserProfile = (PublicUserProfile) objectPublicUserProfile;



        // Set current IP address in public user profile
        publicUserProfile.setPeerAddress(p2p.getPeerAddress());

        // Save public user profile
        if (p2p.put(userID, publicUserProfile) == false) {
            return new Pair<>(false, "Could not update public user profile");
        }


        return new Pair<>(true, "Login successful");
    }

    private boolean savePrivateUserProfile() {
        // TODO: encrypt before saving

        return p2p.put(userProfile.getUserID() + userProfile.getPassword(), userProfile);
    }

    /**
     *
     * @param userID
     * @return
     */
    public boolean existsUser(String userID) {
        return (p2p.getBlocking(userID) != null);
    }

/*    public boolean addFriend(String userID) {
        // Add to list
        friendsList.add(new FriendsListEntry(userID));
        friendsList.sort(new FriendsListComparator());
        mainController.sortFriendsListView();

        // Send ping
        pingUser(userID, true, true);

        // Save profile
        return savePrivateUserProfile();
    }*/

}
