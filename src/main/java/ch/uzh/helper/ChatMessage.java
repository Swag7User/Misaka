/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uzh.helper;

import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author sstephan
 */
public class ChatMessage extends Message {

    public ChatMessage(PeerAddress _senderPeerAddress, String _senderUserID, String _messageText) {
        super(_senderPeerAddress, _senderUserID, _messageText);
    }
}
