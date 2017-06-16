/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uzh.helper;

import ch.uzh.model.MainWindow;
import com.google.gson.*;
import javafx.application.Platform;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

import java.math.BigInteger;


/**
 * @author Sebastian
 */
public class ObjectReplyHandler implements ObjectDataReply {

    private MainWindow mainWindow;

    public ObjectReplyHandler(MainWindow _mainWindow) {
        mainWindow = _mainWindow;
    }

    public String parse(String jsonLine) {
        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject jobject = jelement.getAsJsonObject();
        String result = jobject.get("identifier").toString();
        result = result.replace("\"", "");
        return result;
    }

    @Override
    public Object reply(PeerAddress pa, Object o) throws Exception {
        Gson gsonReply = new Gson();
        String jsonReply = (String) o;
        System.err.println("ObjectReplyhandler");
        System.err.println(jsonReply);
        String identifier;
        try {
            identifier = parse(jsonReply);
            System.err.println("identifier is: " + identifier);
        } catch (Exception e) {
            System.err.println("Nope, didn't work to parse this json");
            e.printStackTrace();
            identifier = "shit happens";
        }

        if (identifier.equals("FriendRequestMessage")) {
            System.err.println("~~~~~~~~~~~~~~~FriendRequest message incomming~~~~~~~~~~~~~");
            Runnable task = () -> {
                System.err.println("~~~~~~~~~~~~~~~FriendRequest message handling~~~~~~~~~~~~~");
                mainWindow.handleIncomingFriendRequest(gsonReply.fromJson(jsonReply, FriendRequestMessage.class));

            };
            Platform.runLater(task);
        } else if (identifier.equals("shit happens")) {
            System.err.println("~~~~~~~~~~~~~~~error handling~~~~~~~~~~~~~");
            Runnable task = () -> {
                mainWindow.donothing();
            };
            Platform.runLater(task);
        } else if (identifier.equals("ChatMessage")) {
            Runnable task = () -> {
                ChatMessage msg = gsonReply.fromJson(jsonReply, ChatMessage.class);
                mainWindow.handleIncomingChatMessage(msg);
            };
            Platform.runLater(task);
        } else if (identifier.equals("OnlineStatusMessage")) {
            Runnable task = () -> {
                mainWindow.handleIncomingOnlineStatus(gsonReply.fromJson(jsonReply, OnlineStatusMessage.class));
            };
            Platform.runLater(task);
        } else if(identifier.equals("AudioFrame")){
            Runnable task = () -> {
                mainWindow.handleIncomingAudioFrame(gsonReply.fromJson(jsonReply, AudioFrame.class));
            };
            Platform.runLater(task);
        }else {
            System.err.println("~~~~~~~~~~~~~~~all has failed~~~~~~~~~~~~~");

        }



       /* if (o instanceof FriendRequestMessage) {
            System.err.println(" ~~~~~~~~~~~~~~~We shouldn't be here tbh~~~~~~~~~~~~~ ");

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
        } else if (o instanceof CallRequestMessage) {
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
        System.err.println(" ~~~~~~~~~~~~~~~end of objectreplyhandler~~~~~~~~~~~~~ ");

        return null;
    }


}
