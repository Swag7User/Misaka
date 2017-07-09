package ch.uzh.controller;

import ch.uzh.helper.P2POverlay;
import ch.uzh.helper.PublicUserProfile;
import ch.uzh.model.FriendsListEntry;
import ch.uzh.model.MainWindow;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

public class MenuOverlayController {

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
        System.err.println("FriendListController is initializing");

        addFriendField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    String userID = addFriendField.getText();
                    System.err.println("existsUser " + userID + " ????????????????????????????????????? \n");
                    System.err.println(mainWindow.existsUser(userID));
                    Pair<Boolean, String> result = mainWindow.sendFriendRequest(userID, "hi, pls accept");

                    if (result.getKey() == true) {
                        System.err.println("friend request sent");
                    } else {
                        System.err.println("friend request ERROR");
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
                    System.err.println("mainWindow.getUserProfile().getFriendsList(): " + e.getUserID());
                }

            }

        }));

        aboutLbl.setOnMouseClicked((new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {


                for (FriendsListEntry e : mainWindow.getFriendsList()) {
                    System.err.println("mainWindow.getFriendsList(): " + e.getUserID());
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


//	public void makeDialog(String dialogText, EventHandler<ActionEvent> acceptHandler,
//			EventHandler<ActionEvent> rejectHandler) {
//		mainWindowController.drawMenuOverlay();
//		requestPaneLabel.setText(dialogText);
//		requestPaneAcceptBtn.setOnAction(new EventHandler<ActionEvent>() {
//			public void handle(ActionEvent event) {
//				mainWindowController.hideMenuOverlay();
//				acceptHandler.handle(event);
//			}
//		});
//		requestPaneRejectBtn.setOnAction(new EventHandler<ActionEvent>() {
//
//			public void handle(ActionEvent event) {
//				mainWindowController.hideMenuOverlay();
//				rejectHandler.handle(event);
//			}
//		});
//	}

//	public void inform(String information) {
//		mainWindowController.showInformOverlay();
//		informPaneLabel.setText(information);
//		informPaneOkBtn.setOnAction(new EventHandler<ActionEvent>() {
//			public void handle(ActionEvent event) {
//				mainWindowController.hideInformOverlay();
//			}
//		});
//	}

}
