package ch.uzh.helper;

import net.tomp2p.peers.PeerAddress;


public class OnlineStatusMessage extends Message {
    private String identifier;
    final private boolean onlineStatus;
    final private boolean replyPongExpected;

    public OnlineStatusMessage(String identifier, PeerAddress _senderPeerAddress, String _senderUserID, boolean _onlineStatus, boolean _pongRequested) {
        super(_senderPeerAddress, _senderUserID, "");
        this.identifier = identifier;
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
