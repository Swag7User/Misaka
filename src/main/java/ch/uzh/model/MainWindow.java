package ch.uzh.model;

import ch.uzh.controller.*;
import ch.uzh.helper.*;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.peers.PeerAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.NoSuchPaddingException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by jesus on 11.03.2017.
 */
public class MainWindow /*implements CallBack*/ {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MainWindow.class);

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

    public static boolean futurputSuccess = false;


    private AnchorPane mainPane;
    private AnchorPane msgWindowPane;
    private AnchorPane friendListPane;
    private AnchorPane menuOverlay;
    private AnchorPane callWindow;

    private P2POverlay p2p;
    private List<FriendsListEntry> friendsList;
    private ObservableList<FriendRequestMessage> friendRequestsList;
    private ScheduledExecutorService scheduler;

    private String currentChatPartner;

    private Key publicKey;
    private Key privateKey;

    byte[] publicKeySerialized;
    byte[] privateKeySerialized;

    private HashMap<String, List<ChatMessage>> messages;

    private PrivateUserProfile userProfile;
    private EncryptedPrivateUserProfile encrypteduserProfile;

    public String getCurrentChatpartner() {
        return currentChatPartner;
    }

    public PrivateUserProfile getUserProfile() {
        return userProfile;
    }

    public void setCurrentChatpartner(String userID) {
        currentChatPartner = userID;
    }

    public void sendVideoFrame(BufferedImage image, FriendsListEntry friendsListEntry) throws IOException {
        ByteArrayOutputStream compressed = new ByteArrayOutputStream();
        ImageOutputStream outputStream = ImageIO.createImageOutputStream(compressed);
        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();

// Configure JPEG compression: 70% quality
        ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
        jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpgWriteParam.setCompressionQuality(0.7f);

// Set your in-memory stream as the output
        jpgWriter.setOutput(outputStream);

// Write image as JPEG w/configured settings to the in-memory stream
// (the IIOImage is just an aggregator object, allowing you to associate
// thumbnails and metadata to the image, it "does" nothing)
        jpgWriter.write(null, new IIOImage(image, null, null), jpgWriteParam);

// Dispose the writer to free resources
        jpgWriter.dispose();

// Get data for further processing...
        byte[] jpegData = compressed.toByteArray();
        VideoFrame vidFrame = new VideoFrame("VideoFrame", jpegData);

        String VideoFrameJson = GsonHelper.createJsonString(vidFrame);
        log.info("VideoFrame json: " + VideoFrameJson);
        p2p.sendNonBlocking(friendsListEntry.getPeerAddress(), VideoFrameJson, false);

    }

    public void handleIncomingVideoFrame(VideoFrame vidFrame){
        log.info("VideoFrame handling beegins now: ");
        callWindowController.showVideo(vidFrame);

    }


    public void draw(Stage stage, int id, String ip, String username, String password,
                     boolean bootstrapNode) throws Exception {
        this.stage = stage;
        this.id = id;
        this.ip = ip;
        this.username = username;
        this.password = password;
        this.bootstrapNode = bootstrapNode;
        drawMainWindow();
        messages = new HashMap<String, List<ChatMessage>>();
    }

    public MainWindow(P2POverlay p2p) {
        this.p2p = p2p;
    }

    public HashMap<String, List<ChatMessage>> getMessages() {
        return messages;
    }

    public List<ChatMessage> getMessagesFrom(String userID) {
        return messages.get(userID);
    }

    public void setMessages(HashMap<String, List<ChatMessage>> messages) {
        this.messages = messages;
    }


    private void drawMainWindow() throws Exception {
        stage.setOnCloseRequest(e -> {
            shutdown();
        });
        log.info("2222222~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        log.info("FXML resource: " + getClass().getResource("ch/uzh/csg/p2p/screens/MainWindow.fxml"));
        log.info("FXML resource: " + getClass().getResource("/MainWindow.fxml"));
        log.info("FXML resource: " + getClass().getResource("MainWindow.fxml"));
        log.info("FXML resource: " + getClass().getResource("/view/MainWindow.fxml"));
        log.info("2222222~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainWindow.fxml"));
        mainWindowController = new MainWindowController(stage, p2p);

        msgWindowController = new MsgWindowController(mainWindowController, this, p2p);
        mainWindowController.setMsgWindowController(msgWindowController);

        friendListController = new FriendListController(mainWindowController, p2p, this);
        mainWindowController.setFriendListController(friendListController);

        menuOverlayController = new MenuOverlayController(mainWindowController, p2p, this);
        mainWindowController.setMenuOverlayController(menuOverlayController);

        callWindowController = new CallWindowController(mainWindowController, p2p, this);
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


        stage.show();


        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            public void handle(WindowEvent event) {
                stage.close();
                System.exit(0);
            }
        });

        setCurrentChatpartner("nullPerson42203SKDC");


    }

    public void acceptFriendRequest(FriendRequestMessage message) {
        addFriend(message.getSenderUserID());
        friendRequestsList.remove(message);

        // Save User Profile
        savePrivateUserProfileNonBlocking();
        log.info("userid: " + message.getSenderUserID() + "messagetxt: " + message.getMessageText() + "peeraddress: " + message.getSenderPeerAddress());
        FriendsListEntry newFriend = new FriendsListEntry(message.getSenderUserID());
        newFriend.setPeerAddress(message.getSenderPeerAddress());
        friendListController.updateFriends();

    }

    public void declineFriendRequest(FriendRequestMessage message) {
        friendRequestsList.remove(message);
        savePrivateUserProfileNonBlocking();
    }

    public void handleIncomingFriendRequest(FriendRequestMessage requestMessage) {
        // Ignore requests from users already in the list
        if (userProfile.isFriendsWith(requestMessage.getSenderUserID())) {
            return;
        }

        friendRequestsList.add(requestMessage);

        // Save the change
        boolean isSaved = savePrivateUserProfileNonBlocking();
        if (isSaved == true) {
            log.info("saved succesfully");
        } else {
            log.info("saved UNsuccesfully");

        }

        FriendListController.showIncomingFriendRequest(requestMessage);
        acceptFriendRequest(requestMessage);
    }

    public boolean sendFriendRequest(String userID, String messageText) {
        // Check if user already exists in friends list
        if (getFriendsListEntry(userID) != null) {
            log.info("User already in friendslist");
            return false;
        }

        // Check if user exists in the network
        if (!existsUser(userID)) {
            log.info("User was not found");
            return false;
        }

        // Get public profile of friend we want to add
        String jsonFriendProfile = (String) p2p.getBlocking(userID);
        Gson gson = new Gson();
        PublicUserProfile friendProfile = gson.fromJson(jsonFriendProfile, PublicUserProfile.class);

        FriendRequestMessage friendRequestMessage = new FriendRequestMessage("FriendRequestMessage", p2p.getPeerAddress(), userProfile.getUserID(), messageText);
        String jsonFriendRequest = GsonHelper.createJsonString(friendRequestMessage);

        // Try to send direct friend request first, (in case user is online)
        boolean sendDirect = false;
        if (friendProfile.getPeerAddress() != null) {
            sendDirect = p2p.sendBlocking(friendProfile.getPeerAddress(), jsonFriendRequest);
        }

        // If that failed, or other has no peer address, append to pub profile of other friend
        if (sendDirect == false) {
            // Friend is not online, append to public profile
            friendProfile.getPendingFriendRequests().add(jsonFriendRequest);
            boolean now = p2p.putNonBlocking(userID, friendProfile);

            while (!futurputSuccess) {
                donothing();
            }
            futurputSuccess = false;

            if (now == false) {
                log.info("Error sending friend request");
                return false;
            }
        }

        // Addd as friend
        if (addFriend(userID) == false) {
            log.info("Error, adding the friend");
            return false;
        }
        log.info("Friend request to " + userID + " was sent");
        return true;
    }

    public void donothing() {
        log.info("nothing");
    }

    public FriendsListEntry getFriendsListEntry(String userID) {
        if (friendsList == null) {
            return null;
        }
        for (FriendsListEntry e : friendsList) {
            if (e.getUserID().equals(userID)) {
                return e;
            }
        }
        return null;
    }

    public List<FriendsListEntry> getFriendsList() {
        if (friendsList == null) {
            return null;
        } else {
            return friendsList;
        }
    }

    public boolean addFriend(String userID) {
        log.info("ADD THIS BFF: " + userID);
        friendsList.add(new FriendsListEntry(userID));

        // Send ping
        pingUser(userID, true, true);
        friendListController.updateFriends();

        return savePrivateUserProfileNonBlocking();
    }

    private void pingUser(String userID, boolean onlineStatus, boolean replyPongExpected) {
        p2p.getNonBLocking(userID, new BaseFutureAdapter<FutureGet>() {
            @Override
            public void operationComplete(FutureGet f) throws Exception {
                FriendsListEntry friendsListEntry = getFriendsListEntry(userID);
                assert (friendsListEntry != null);
                if (f.isSuccess()) {
                    Gson publicUserProfileGson = new Gson();
                    String publicUserProfileJson = (String) f.data().object();

                    PublicUserProfile publicUserProfile = publicUserProfileGson.fromJson(publicUserProfileJson, PublicUserProfile.class);
                    // Set peer address in friendslist
                    PeerAddress peerAddress = publicUserProfile.getPeerAddress();
                    friendsListEntry.setPeerAddress(peerAddress);

                    // Send ping
                    if (peerAddress != null) {
                        pingAddress(publicUserProfile.getPeerAddress(), onlineStatus, replyPongExpected);
                    }
                } else {
                    // Can't find other peer, maybe he deleted his account? -> show offline
                    log.info("User " + userID + " doesnt seem to exist anymore");
                    friendsListEntry.setOnline(false);
                    friendsListEntry.setPeerAddress(null);
                }
            }
        });
    }

    public void pingAddress(PeerAddress pa, boolean onlineStatus, boolean replyPongExpected) {
        // Send ping
        OnlineStatusMessage msg = new OnlineStatusMessage("OnlineStatusMessage", p2p.getPeerAddress(), userProfile.getUserID(), onlineStatus, replyPongExpected);
        Gson onlineStatusMessageGson = new Gson();
        String onlineStatusMessageJson = onlineStatusMessageGson.toJson(msg);

        p2p.sendNonBlocking(pa, onlineStatusMessageJson, false);
    }

    public void logout() {
        // Tell "friends" that i'm going offline
        pingAllFriends(false);
        log.info("Told everyone I'm offline");

        // Set PeerAddress in public Profile to null
        Object objectPublicUserProfile = p2p.getBlocking(userProfile.getUserID());
        if (objectPublicUserProfile == null) {
            log.info("Could not retrieve public userprofile");
            return;
        }

        Gson publicUserprofileGson = new Gson();
        String publicUserProfileJson = (String) objectPublicUserProfile;


        PublicUserProfile publicUserProfile = publicUserprofileGson.fromJson(publicUserProfileJson, PublicUserProfile.class);
        publicUserProfile.setPeerAddress(null);

        String newPublicUserProfileJson = GsonHelper.createJsonString(publicUserProfile);


        boolean now = p2p.putNonBlocking(userProfile.getUserID(), newPublicUserProfileJson);

        while (!futurputSuccess) {
            donothing();
        }
        futurputSuccess = false;

        if (now == false) {
            log.info("Could not update peer address in public user profile");
            return;
        }

        savePrivateUserProfileNonBlocking();

        p2p.setObjectDataReply(null);

        userProfile = null;
        friendsList = null;
        shutdown();


    }

    private void pingAllFriends(boolean onlineStatus) {
        if (friendsList == null) {
            return;
        }
        for (FriendsListEntry entry : friendsList) {
            String userID = entry.getUserID();

            // For friends that are online, send direct to their PeerAddress
            if (entry.isOnline()) {
                OnlineStatusMessage ping = new OnlineStatusMessage("OnlineStatusMessage", p2p.getPeerAddress(), userProfile.getUserID(),
                        onlineStatus, onlineStatus);
                Gson pingAllFriendsGson = new Gson();
                String pingAllFriendsJson = pingAllFriendsGson.toJson(ping);

                p2p.sendNonBlocking(entry.getPeerAddress(), pingAllFriendsJson, false);
            } // For friends that are offline and in the case that we want to tell
            // them we're coming online, use pingUser method to first check for
            // their peerAddress (if any).
            else if (!entry.isOnline() && onlineStatus == true) {
                pingUser(userID, onlineStatus, onlineStatus);

            }
        }
    }

    public void shutdown() {
        log.info("logout ");
        if (userProfile != null) {
            logout();
        }
        log.info("exit ");

        // Shutdown Tom P2P stuff
        p2p.shutdown();
        Platform.exit();
        System.exit(0);
    }

    public void handleIncomingOnlineStatus(OnlineStatusMessage msg) {
        synchronized (this) {
            FriendsListEntry e = getFriendsListEntry(msg.getSenderUserID());
            log.info("Heartbeat received: " + msg.getSenderUserID() + " " + msg.getMessageText() + " " + msg.isOnline() + "   " + msg.getSenderPeerAddress());

            // If friend is in friendslist
            if (e != null) {

                // Set online/offline
                e.setOnline(msg.isOnline());
                e.setPeerAddress(msg.getSenderPeerAddress());
                e.setWaitingForHeartbeat(false);
                friendListController.setGreen(msg.getSenderUserID());

                // Send pong back if wanted
                if (msg.isReplyPongExpected()) {
                    pingAddress(msg.getSenderPeerAddress(), true, false);
                }
            }
        }
    }

    public String getUserID() {
        return (userProfile != null) ? userProfile.getUserID() : "error";
    }

    public void sendChatMessage(String text, FriendsListEntry friendsListEntry) {
        ChatMessage chatMessage = new ChatMessage("ChatMessage", p2p.getPeerAddress(), userProfile.getUserID(), text);
        log.info("SENDING THIS: peeraddress: " + p2p.getPeerAddress() + " userID: " + userProfile.getUserID() + " text: " + text);
        log.info("SENDING TO: peeraddress: " + friendsListEntry.getPeerAddress() + " userID: " + friendsListEntry.getUserID() + " text: " + text);

        String ChatMessageJson = GsonHelper.createJsonString(chatMessage);
        p2p.sendNonBlocking(friendsListEntry.getPeerAddress(), ChatMessageJson, false);
    }

    public void handleIncomingChatMessage(ChatMessage msg) {
        FriendsListEntry e = getFriendsListEntry(msg.getSenderUserID());

        // If friend is in friendslist
        if (e != null) {
            log.info("Message received from: " + msg.getSenderUserID() + " Messagetext: " + msg.getMessageText());
            log.info("current chat partner: " + currentChatPartner + "message userID: " + msg.getSenderUserID());

            if (currentChatPartner.equals(msg.getSenderUserID())) {
                addMsgToHashMap(msg);
                msgWindowController.addChatBubble(msg.getMessageText(), msg.getSenderUserID(), false);
                log.info("works????????? ");
            } else {
                addMsgToHashMap(msg);
                friendListController.alertNewMsg(msg.getSenderUserID());
            }
            //msgWindowController.addChatBubble(msg.getMessageText(), msg.getSenderUserID(), false);
        } else {
            log.info("That's my purse, i don't know you!");
        }
    }

    public void addMsgToHashMap(ChatMessage msg) {
        if (messages.containsKey(msg.getSenderUserID())) {
            messages.get(msg.getSenderUserID()).add(msg);
        } else {
            List<ChatMessage> newChat = new ArrayList<ChatMessage>();
            newChat.add(msg);
            messages.put(msg.getSenderUserID(), newChat);
        }
    }

    public void addSelfMessageToChat(String userID, ChatMessage msg) {
        if (messages.containsKey(userID)) {
            messages.get(userID).add(msg);
        }
        List<ChatMessage> newChat = new ArrayList<ChatMessage>();
        newChat.add(msg);
        messages.put(userID, newChat);
    }

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

    public void generateRSAKeys() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.genKeyPair();
            publicKeySerialized = kp.getPublic().getEncoded();
            privateKeySerialized = kp.getPrivate().getEncoded();
            publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeySerialized));
            privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeySerialized));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public Pair<Boolean, String> createAccount(String userID, String password) {
        // Check if account exists
        if (p2p.getBlocking(userID) != null) {
            log.info("Could not create user account. UserID already taken.");
            return new Pair<>(false, "Could not create user account. UserID already taken."); //TODO: LOGIN NOW
        }

        generateRSAKeys();

        // Create private UserProfile
        userProfile = new PrivateUserProfile(userID, password, privateKeySerialized);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Encryption.encrypt(password, (Serializable) userProfile, baos);
            byte[] encryptedArray = baos.toByteArray();
            encrypteduserProfile = new EncryptedPrivateUserProfile(encryptedArray);
            baos.close();
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }

        boolean saving = savePrivateUserProfileNonBlocking();
        log.info("saving is: " + saving);

        // Create public UserProfile
        PublicUserProfile publicUserProfile;
        publicUserProfile = new PublicUserProfile(userID, null, publicKeySerialized, "wl1.0");
        String jsonPublic = GsonHelper.createJsonString(publicUserProfile);

        boolean now = p2p.putNonBlocking(userID, jsonPublic);

        while (!futurputSuccess) {
            donothing();
        }
        futurputSuccess = false;

        if (now) {
            login(userID, password);
        } else {
            log.info("Network DHT error. Could not save public UserProfile");
        }

        return new Pair<>(true, "ok for now");
    }


    private void pingAllOnlineFriends() {
        for (FriendsListEntry entry : friendsList) {
            if (entry.isOnline()) {
                // If friend din't reply since last call, set him offline
                if (entry.isWaitingForHeartbeat()) {
                    entry.setOnline(false);
                }

                // Flag friend until he replies
                entry.setWaitingForHeartbeat(true);

                OnlineStatusMessage ping = new OnlineStatusMessage("OnlineStatusMessage", p2p.getPeerAddress(), userProfile.getUserID(),
                        true, true);
                Gson pingAllOnlineFriendsGson = new Gson();
                String pingAllOnlineFriendsJson = pingAllOnlineFriendsGson.toJson(ping);

                p2p.sendNonBlocking(entry.getPeerAddress(), pingAllOnlineFriendsJson, false);
            }
        }
    }

    public Pair<Boolean, String> login(String userID, String password) {

        // Get userprofile if password and username are correct
        Object getResult = p2p.getBlocking(userID + password);
        if (getResult == null) {
            return new Pair<>(false, "Login data not valid, Wrong UserID/password?");
        }

        log.info("Whatdo we have here?");
        log.info(getResult.toString());
        Gson gson = new Gson();
        encrypteduserProfile = gson.fromJson((String) getResult, EncryptedPrivateUserProfile.class);
        log.info("Whatdo we have here FO REAL?");
        log.info(encrypteduserProfile.getEncryptedProfile().toString());
        ByteArrayInputStream bais = new ByteArrayInputStream(encrypteduserProfile.getEncryptedProfile());
        try {
            userProfile = (PrivateUserProfile) Encryption.decrypt(password, bais);
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        log.info("userID: " + userProfile.getUserID());
        log.info("pw: " + userProfile.getPassword());
        if (!userProfile.getFriendsList().isEmpty()) {
            log.info("friends: " + userProfile.getFriendsList().get(0));
        }

        // Get public user profile
        Object objectPublicUserProfile = p2p.getBlocking(userID);
        if (objectPublicUserProfile == null) {
            log.info("Could not retrieve public userprofile");
            return new Pair<>(false, "Could not retrieve public userprofile");
        }

        PublicUserProfile publicUserProfile = gson.fromJson((String) objectPublicUserProfile, PublicUserProfile.class);

        // **** FRIENDS LIST ****
        // Reset all friends list entries to offline and unkown peer address
        for (FriendsListEntry e : userProfile.getFriendsList()) {
            e.setOnline(false);
            e.setPeerAddress(null);
            e.setWaitingForHeartbeat(false);
        }

        friendsList = new ArrayList<>();
        friendsList = userProfile.getFriendsList();

        friendRequestsList = FXCollections.observableList(userProfile.getFriendRequestsList());


        // Set current IP address in public user profile
        publicUserProfile.setPeerAddress(p2p.getPeerAddress());
        String jsonPublic = GsonHelper.createJsonString(publicUserProfile);

        // Save public user profile
        boolean now = p2p.putNonBlocking(userID, jsonPublic);

        while (!futurputSuccess) {
            donothing();
        }
        futurputSuccess = false;

        if (now == false) {
            log.info("Could not update public user profile");
            return new Pair<>(false, "Could not update public user profile");
        }

        // Set reply handler
        p2p.setObjectDataReply(new ObjectReplyHandler(this));

        // Send out online status to all friends
        pingAllFriends(true);

        // Schedule new thread to check periodically if friends are still online
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            pingAllFriends(true);
        }, 10, 10, SECONDS);

        log.info("Login successful");
        return new Pair<>(true, "Login successful");
    }

    public boolean savePrivateUserProfileNonBlocking() {
        Gson gson = new Gson();
        String json = gson.toJson(encrypteduserProfile);
        log.info("IMMA GONNA PRINT MY JSON NON BLOCKING");
        log.info(json);
        log.info("PRINTED MY JSON");

        boolean now = p2p.putNonBlocking(userProfile.getUserID() + userProfile.getPassword(), json);

        while (!futurputSuccess) {
            donothing();
        }
        futurputSuccess = false;

        return now;
    }

    public void handleIncomingAudioFrame(AudioFrame frame) {
        if (true) {
            callWindowController.handleIncomingAudioFrame(frame);
        }
    }

    public boolean existsUser(String userID) {
        return (p2p.getBlocking(userID) != null);
    }

}
