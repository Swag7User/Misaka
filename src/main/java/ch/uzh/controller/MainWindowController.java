package ch.uzh.controller;

import ch.uzh.helper.ChatMessage;
import ch.uzh.helper.P2POverlay;
import ch.uzh.helper.PrivateUserProfile;
import ch.uzh.model.MainWindow;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.tomp2p.peers.PeerAddress;

/**
 * Created by jesus on 11.03.2017.
 */
public class MainWindowController implements Controller{

    private MainWindow mainWindow;
    private Stage stage;

    public MsgWindowController msgWindowController;
    public CallWindowController callWindowController;
    public FriendListController friendListController;
    public MenuOverlayController menuOverlayController;

    private AnchorPane friendListPane;
    private AnchorPane msgWindowPane;
    private AnchorPane callWindowPane;
    private AnchorPane menuOverlay;

    private PrivateUserProfile userProfile;

    private P2POverlay p2p;

    @FXML
    private Button menuBtn;

    @FXML
    private AnchorPane leftPane;

    @FXML
    private AnchorPane rightBottomPane;

    @FXML
    private AnchorPane rightTopPane;

    @FXML
    private AnchorPane modalOverlayPane;

    @FXML
    private void initialize() {
        System.err.println("MainWindowController is initializing");

    }

    public MainWindowController(Stage stage, P2POverlay p2p) {
        this.stage = stage;
        this.p2p = p2p;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void setMsgWindowController(MsgWindowController msgWindowController) {
        this.msgWindowController = msgWindowController;
    }

    public void setCallWindowController(CallWindowController callWindowController) {
        this.callWindowController = callWindowController;
    }

    public void setFriendListController(FriendListController friendListController) {
        this.friendListController = friendListController;
    }

    public void setMenuOverlayController(MenuOverlayController menuOverlayController) {
        this.menuOverlayController = menuOverlayController;
    }

    public void setFriendlistPane(AnchorPane friendListPane) {
        this.friendListPane = friendListPane;
    }

    public void setMsgWindowPane(AnchorPane msgWindowPane) {
        this.msgWindowPane = msgWindowPane;
    }

    public void setCallWindowPane(AnchorPane callWindowPane) {
        this.callWindowPane = callWindowPane;
    }

    public void setMenuOverlayPane(AnchorPane menuOverlay) {
        this.menuOverlay = menuOverlay;
    }

    public void setLeftPane(AnchorPane anchorPane) {
        System.err.println("11");
        System.err.println(leftPane);
        System.err.println("22");
        leftPane.getChildren().clear();
        System.err.println("33");
        if (anchorPane != null) {
            leftPane.getChildren().add(anchorPane);
        }

    }

    public void setRightBottomPane(AnchorPane anchorPane) {
        rightBottomPane.getChildren().clear();
        if (anchorPane != null) {
            rightBottomPane.getChildren().add(anchorPane);
        }
    }

    public void setRightTopPane(AnchorPane anchorPane) {
        rightTopPane.getChildren().clear();
        if (anchorPane != null) {
            rightTopPane.getChildren().add(anchorPane);
        }
    }

    public void drawCallPane() {
        setRightTopPane(callWindowPane);
        setRightBottomPane(msgWindowPane);
     //   showChatBtns("video");
      //  stage.setWidth(1333);
      //  stage.setHeight(768);
        stage.centerOnScreen();



    }

    public void drawMsgPane() {
        setRightTopPane(null);
        setRightBottomPane(msgWindowPane);
        stage.setMinWidth(1024);
        stage.setMinHeight(640);
        stage.centerOnScreen();
    }

    public void alive(){
        System.err.println("MainWindowController is here");


    }

    public void drawMenuOverlay() {
        modalOverlayPane.setVisible(true);
        modalOverlayPane.getChildren().clear();
        modalOverlayPane.getChildren().add(menuOverlay);
    }

    public void hideMenuOverlay() {
        modalOverlayPane.setVisible(false);
    }




}
