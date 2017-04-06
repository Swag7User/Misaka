package ch.uzh.controller;

import ch.uzh.helper.P2POverlay;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class MenuOverlayController {

    @FXML
    private Pane closePane1;

    @FXML
    private Pane closePane2;

	private Controller mainWindowController;
	private P2POverlay p2p;

	public MenuOverlayController(Controller mainWindowController, P2POverlay p2p) {
		this.mainWindowController = mainWindowController;
		this.p2p = p2p;
	}

	@FXML
	private void initialize() {
		System.err.println("FriendListController is initializing");

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
