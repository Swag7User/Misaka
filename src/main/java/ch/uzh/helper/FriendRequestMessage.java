package ch.uzh.helper;

import net.tomp2p.peers.PeerAddress;


public class FriendRequestMessage extends Message {
    private String identifier;

    
    public FriendRequestMessage(String identifier, PeerAddress _senderPeerAddress, String _senderUserID, String _messageText) {
        super(_senderPeerAddress, _senderUserID, _messageText);
        this.identifier = identifier;
    }
   
}
