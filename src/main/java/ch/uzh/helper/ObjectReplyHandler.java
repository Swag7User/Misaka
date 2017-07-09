package ch.uzh.helper;

import ch.uzh.model.MainWindow;
import com.google.gson.*;
import javafx.application.Platform;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sebastian
 */
public class ObjectReplyHandler implements ObjectDataReply {

    private static final Logger log = LoggerFactory.getLogger(ObjectReplyHandler.class);


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
        log.info("ObjectReplyhandler");
        log.info(jsonReply);
        String identifier;
        try {
            identifier = parse(jsonReply);
            log.info("identifier is: " + identifier);
        } catch (Exception e) {
            log.info("Nope, didn't work to parse this json");
            e.printStackTrace();
            identifier = "shit happens";
        }

        if (identifier.equals("FriendRequestMessage")) {
            log.info("~~~~~~~~~~~~~~~FriendRequest message incomming~~~~~~~~~~~~~");
            Runnable task = () -> {
                log.info("~~~~~~~~~~~~~~~FriendRequest message handling~~~~~~~~~~~~~");
                mainWindow.handleIncomingFriendRequest(gsonReply.fromJson(jsonReply, FriendRequestMessage.class));

            };
            Platform.runLater(task);
        } else if (identifier.equals("shit happens")) {
            log.info("~~~~~~~~~~~~~~~error handling~~~~~~~~~~~~~");
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
            log.info("~~~~~~~~~~~~~~~all has failed~~~~~~~~~~~~~");

        }
        log.info(" ~~~~~~~~~~~~~~~end of objectreplyhandler~~~~~~~~~~~~~ ");

        return null;
    }


}
