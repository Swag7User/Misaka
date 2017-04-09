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
public class OnlineStatusMessage extends Message {
    final private boolean onlineStatus;
    final private boolean replyPongExpected;

    public OnlineStatusMessage(PeerAddress _senderPeerAddress, String _senderUserID, boolean _onlineStatus, boolean _pongRequested) {
        super(_senderPeerAddress, _senderUserID, "");
        onlineStatus = _onlineStatus;
        replyPongExpected = _pongRequested;
    }

    /**
     * @return the onlineStatus
     */
    public boolean isOnline() {
        return onlineStatus;
    }

    /**
     * @return the replyPongExpected
     */
    public boolean isReplyPongExpected() {
        return replyPongExpected;
    }
}
