package ch.uzh.helper;

import ch.uzh.model.Friend;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.tomp2p.peers.PeerAddress;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jesus on 19.03.2017.
 */
public class User implements Serializable {

    String username;
    String password;
    PeerAddress peerAddress;




    public User(String username, String password, PeerAddress peerAddress) {
        this();
        this.username = username;
        this.password = password;
        this.peerAddress = peerAddress;
    }

    public User() {
        super();
        username = new String();
        password = new String();
        peerAddress = null;
//        friendList = FXCollections.observableList(new ArrayList<Friend>());
//        setFriendRequestStorage(FXCollections.observableList(new ArrayList<FriendRequest>()));
//        chatMessageStorage = FXCollections.observableList(new ArrayList<ChatMessage>());
//        audioInfoStorage = FXCollections.observableList(new ArrayList<AudioInfo>());
//        videoInfoStorage = FXCollections.observableList(new ArrayList<VideoInfo>());
    }



}
