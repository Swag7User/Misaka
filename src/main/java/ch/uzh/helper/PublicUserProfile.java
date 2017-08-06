package ch.uzh.helper;

import net.tomp2p.peers.PeerAddress;

import java.io.Serializable;
import java.util.ArrayList;


public class PublicUserProfile implements Serializable {
    private String userID;
    private String eMail;
    private byte[] publicKeySerialized;
    private PeerAddress peerAddress;
    private ArrayList<String> pendingFriendRequests;
    private static final long serialVersionUID = 42L;
    private String version;


    public PublicUserProfile(String _userID, PeerAddress _peerAddress, byte[] _publicKeySerialized) {
        userID = _userID;
        peerAddress = _peerAddress;
        pendingFriendRequests = new ArrayList<>();
        publicKeySerialized = _publicKeySerialized;
    }
    public PublicUserProfile(String _userID, PeerAddress _peerAddress, byte[] _publicKeySerialized, String version) {
        userID = _userID;
        peerAddress = _peerAddress;
        pendingFriendRequests = new ArrayList<>();
        publicKeySerialized = _publicKeySerialized;
        this.version = version;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
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
     * @return the pendingFriendRequests
     */
    public ArrayList<String> getPendingFriendRequests() {
        return pendingFriendRequests;
    }


}
