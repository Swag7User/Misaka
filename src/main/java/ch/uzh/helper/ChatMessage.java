package ch.uzh.helper;

import net.tomp2p.peers.PeerAddress;


public class ChatMessage extends Message {
    private String identifier;
    private Status messageStatus;
    private UserType userType;


    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }


    public ChatMessage(String identifier, PeerAddress _senderPeerAddress, String _senderUserID, String _messageText) {
        super(_senderPeerAddress, _senderUserID, _messageText);
        this.identifier = identifier;
    }

    public ChatMessage() {
        super();
        this.identifier = identifier;
    }

    public Status getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(Status messageStatus) {
        this.messageStatus = messageStatus;
    }


}

