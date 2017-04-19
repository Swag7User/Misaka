/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uzh.helper;

import ch.uzh.model.MainWindow;
import javafx.application.Platform;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

import java.math.BigInteger;


/**
 *
 * @author Sebastian
 */
public class ObjectReplyHandler implements ObjectDataReply {

    private MainWindow mainWindow;

    public ObjectReplyHandler(MainWindow _mainWindow) {
        mainWindow = _mainWindow;
    }
    public String toHex(String arg) {
        return String.format("%x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
    }

    @Override
    public Object reply(PeerAddress pa, Object o) throws Exception {
        System.err.println("ObjectReplyhandler");
        if (o instanceof FriendRequestMessage) {
            Runnable task = () -> {
                System.err.println("HEX HEX ~~~~~~~~~~~~~~~INCOMING~~~~~~~~~~~~~ HEX HEX");
                System.err.println("HEX username:" + toHex(((FriendRequestMessage) o).getSenderUserID()));
                System.err.println("HEX unhashed password:" + toHex(((FriendRequestMessage) o).getSenderUserID()));
                mainWindow.handleIncomingFriendRequest((FriendRequestMessage) o);
            };
            Platform.runLater(task);
        } else if (o instanceof OnlineStatusMessage) {
            Runnable task = () -> {
                mainWindow.handleIncomingOnlineStatus((OnlineStatusMessage) o);
            };
            Platform.runLater(task);
        } else if (o instanceof ChatMessage) {
            Runnable task = () -> {
                ChatMessage msg = (ChatMessage) o;
                mainWindow.handleIncomingChatMessage(msg);
            };
            Platform.runLater(task);
        } /*else if (o instanceof CallRequestMessage) {
            Runnable task = () -> {
                CallRequestMessage msg = (CallRequestMessage) o;
                mainWindow.handleIncomingCallRequestMessage(msg);
            };
            Platform.runLater(task);
        } else if (o instanceof CallAcceptMessage) {
            Runnable task = () -> {
                CallAcceptMessage msg = (CallAcceptMessage) o;
                mainWindow.handleIncomingCallAcceptMessage(msg);
            };
            Platform.runLater(task);
        } else if (o instanceof AudioFrame) {
            AudioFrame msg = (AudioFrame)o;
            mainWindow.handleIncomingAudioFrame(msg);
        }*/
        return null;
    }
}
