package ch.uzh.controller;

import ch.uzh.helper.P2POverlay;
import ch.uzh.model.FriendsListEntry;
import ch.uzh.model.MainWindow;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuOverlayController {

    private static final Logger log = LoggerFactory.getLogger(MenuOverlayController.class);


    @FXML
    private Pane closePane1;

    @FXML
    private Pane closePane2;

    @FXML
    private TextField addFriendField;

    @FXML
    private Label settingsLbl;

    @FXML
    private Label aboutLbl;

    @FXML
    private Label advLbl;

    private Controller mainWindowController;
    private P2POverlay p2p;
    private MainWindow mainWindow;
    int ic = 1;

    public MenuOverlayController(Controller mainWindowController, P2POverlay p2p, MainWindow mainWindow) {
        this.mainWindowController = mainWindowController;
        this.p2p = p2p;
        this.mainWindow = mainWindow;
    }

    @FXML
    private void initialize() {
        log.info("FriendListController is initializing");

        addFriendField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    String userID = addFriendField.getText();
                    log.info("existsUser " + userID + " ????????????????????????????????????? \n");
                    log.info(Boolean.toString(mainWindow.existsUser(userID)));
                    Pair<Boolean, String> result = mainWindow.sendFriendRequest(userID, "hi, pls accept");

                    if (result.getKey() == true) {
                        log.info("friend request sent");
                    } else {
                        log.info("friend request ERROR");
                    }

                    addFriendField.clear();
                }
            }
        });

        closePane1.setOnMouseClicked((new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {

                mainWindowController.hideMenuOverlay();
            }

        }));

        settingsLbl.setOnMouseClicked((new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                mainWindow.savePrivateUserProfileNonBlocking();


            }

        }));

        advLbl.setOnMouseClicked((new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {

                for (FriendsListEntry e : mainWindow.getUserProfile().getFriendsList()) {
                    log.info("mainWindow.getUserProfile().getFriendsList(): " + e.getUserID());
                }

            }

        }));

        aboutLbl.setOnMouseClicked((new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {


                for (FriendsListEntry e : mainWindow.getFriendsList()) {
                    log.info("mainWindow.getFriendsList(): " + e.getUserID());
                }
            }

        }));

        closePane2.setOnMouseClicked((new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {

                mainWindowController.hideMenuOverlay();
            }

        }));


    }

}
