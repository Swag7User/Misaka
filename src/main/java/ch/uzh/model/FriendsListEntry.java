package ch.uzh.model;

import net.tomp2p.peers.PeerAddress;

import java.io.Serializable;

/**
 * An entry in the friends list.
 *
 * @author sstephan
 */
public class FriendsListEntry implements Serializable {
    private final String userID;
    private PeerAddress peerAddress;
    private boolean online = false;
    private boolean waitingForHeartbeat = false;

    public FriendsListEntry(String _userID) {
        userID = _userID;
    }

    /**
     * @return the userID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * @return the peerAddress
     */
    public PeerAddress getPeerAddress() {
        return peerAddress;
    }

    /**
     * @param peerAddress the peerAddress to set
     */
    public void setPeerAddress(PeerAddress peerAddress) {
        this.peerAddress = peerAddress;
    }

    /**
     * @return if the user is online
     */
    public boolean isOnline() {
        return online;
    }


    public void setOnline(boolean online) {
        this.online = online;
    }

    /**
     * @return if entry is wainting for a heartbeat.
     */
    public boolean isWaitingForHeartbeat() {
        return waitingForHeartbeat;
    }


    public void setWaitingForHeartbeat(boolean waitingForHeartbeat) {
        this.waitingForHeartbeat = waitingForHeartbeat;
    }

}
