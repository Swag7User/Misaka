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
public class AudioFrame extends Message{
    private byte[] data;
    
    public AudioFrame(PeerAddress _senderPeerAddress, String _senderUserID, byte[] _data) {
        super(_senderPeerAddress, _senderUserID, "");
        data = _data;
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

}
