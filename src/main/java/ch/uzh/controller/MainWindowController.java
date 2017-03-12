package ch.uzh.controller;

import ch.uzh.model.MainWindow;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Created by jesus on 11.03.2017.
 */
public class MainWindowController {

    private MainWindow mainWindow;
    private Stage stage;

    public MsgWindowController msgWindowController;
    public FriendListController friendListController;

    private AnchorPane friendListPane;
    private AnchorPane msgWindowPane;

    @FXML
    private AnchorPane leftPane;

    @FXML
    private AnchorPane rightBottomPane;

    @FXML
    private AnchorPane rightTopPane;

    @FXML
    private void initialize() {
        System.err.println("MainWindowController is initializing");
    }

    public MainWindowController(Stage stage) {
        this.stage = stage;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void setMsgWindowController(MsgWindowController msgWindowController) {
        this.msgWindowController = msgWindowController;
    }

    public void setFriendListController(FriendListController friendListController) {
        this.friendListController = friendListController;
    }

    public void setFriendlistPane(AnchorPane friendListPane) {
        this.friendListPane = friendListPane;
    }

    public void setMsgWindowPane(AnchorPane msgWindowPane) {
        this.msgWindowPane = msgWindowPane;
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


}
