package ch.uzh.helper;

import net.tomp2p.peers.PeerAddress;

/**
 *
 * @author sstephan
 */
public class AudioFrame extends Message{
    private String identifier;
    private byte[] data;
    
    public AudioFrame(String identifier, PeerAddress _senderPeerAddress, String _senderUserID, byte[] _data) {
        super(_senderPeerAddress, _senderUserID, "");
        this.identifier = identifier;
        data = _data;
    }

    public byte[] getData() {
        return data;
    }

}
