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
    private final String senderUserID;
    private final PeerAddress senderPeerAddress;
    protected final String messageText;
    private static final long serialVersionUID = 43L;


    
    public Message(PeerAddress _senderPeerAddress, String _senderUserID, String _messageText) {
        senderPeerAddress = _senderPeerAddress;
        senderUserID = _senderUserID;
        messageText = _messageText;
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

    /**
     * @return the senderPeerAddress
     */
    public PeerAddress getSenderPeerAddress() {
        return senderPeerAddress;
    }
    
}
