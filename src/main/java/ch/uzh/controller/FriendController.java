package ch.uzh.controller;

import ch.uzh.model.Friend;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by jesus on 11.03.2017.
 */
public class FriendController implements Observer {

    @FXML
    private Label userName;

    @FXML
    private ImageView friendAvatar;

    @FXML
    private AnchorPane rootFriendPane;

    private MainWindowController mainWindowController;


    public FriendController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public void setFriendName(String name){
        userName.setText(name);
        System.err.println("name is set!");
    }

    public void setFriendAvatar(Image img){
        if(img == null){
            return;
        }
        else {
            friendAvatar.setImage(img);
        }
    }



    @FXML
    private void initialize() {
        System.err.println("FriendController is initializing");

        rootFriendPane.setOnMouseClicked((new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent arg0) {

                System.err.println("You clicked: " + userName);
            }

        }));


    }

    public void alive() {
        System.err.println("FriendController is here");


    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Friend) {
            Friend f = (Friend) o;
            //do stuff
        }
    }

}


