package ch.uzh.controller;

import ch.uzh.helper.P2POverlay;
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

	private Controller mainWindowController;
	private P2POverlay p2p;
	private MainWindow mainWindow;

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

                    userID = null;
                }
            }
        });

		closePane1.setOnMouseClicked((new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent arg0) {

                mainWindowController.hideMenuOverlay();
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
