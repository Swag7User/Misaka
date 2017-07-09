package ch.uzh.helper;


import net.tomp2p.peers.PeerAddress;

import java.io.Serializable;

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
    }


}
