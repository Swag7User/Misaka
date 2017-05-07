/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.uzh.helper;

import net.tomp2p.peers.PeerAddress;

import java.io.Serializable;

/**
 *
 * @author Sebastian
 */
public abstract class Message implements Serializable {
    private String senderUserID;
    private PeerAddress senderPeerAddress;
    private String messageText;
    private static final long serialVersionUID = 43L;
    private long messageTime;



    public Message(PeerAddress _senderPeerAddress, String _senderUserID, String _messageText) {
        senderPeerAddress = _senderPeerAddress;
        senderUserID = _senderUserID;
        messageText = _messageText;
    }
    public Message() {

    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    /**
     * @return the senderUserID
     */
    public String getSenderUserID() {
        return senderUserID;
    }

    /**
     * @return the messageText
     */
    public String getMessageText() {
        return messageText;
    }
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }


    /**
     * @return the senderPeerAddress
     */
    public PeerAddress getSenderPeerAddress() {
        return senderPeerAddress;
    }




}
