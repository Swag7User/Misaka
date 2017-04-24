package ch.uzh.controller;

import ch.uzh.helper.P2POverlay;
import ch.uzh.helper.PublicUserProfile;
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

        addFriendField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
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

		closePane1.setOnMouseClicked((new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent arg0) {

                mainWindowController.hideMenuOverlay();
            }

        }));

        settingsLbl.setOnMouseClicked((new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent arg0) {
                System.err.println( "exists usr misaka: " + mainWindow.existsUser("misaka"));
                System.err.println( "exists usr mikoto: " + mainWindow.existsUser("mikoto"));
                System.err.println( "exists usr test77: " + mainWindow.existsUser("test77"));
                System.err.println( "exists usr test42: " + mainWindow.existsUser("test42"));
                System.err.println( "exists usr test48: " + mainWindow.existsUser("test48"));

            }

        }));

        advLbl.setOnMouseClicked((new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent arg0) {

                p2p.put("test77", ic);
                System.err.println(ic);
                ic++;
                p2p.put("test48", new PublicUserProfile("lolguy", null));
            }

        }));

        aboutLbl.setOnMouseClicked((new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent arg0) {
                // Check if account exists
                if (p2p.getBlocking("test1") != null) {
                    System.err.println("NULL??? WHAT THE SHIT WHY??? this isn't ok");

                }


                int e = 0;
                e = (int) p2p.getBlocking("test1");
                if (p2p.getBlocking("test1") != null) {
                    System.err.println("NULL??? WHAT THE SHIT WHY??? this isn't ok");

                }
                System.err.println("magic happens here : " + e);
            }

        }));

        closePane2.setOnMouseClicked((new EventHandler<MouseEvent>(){

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
