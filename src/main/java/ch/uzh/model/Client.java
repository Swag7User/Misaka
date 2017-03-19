package ch.uzh.model;

import ch.uzh.helper.User;
import net.tomp2p.connection.Bindings;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.BaseFutureListener;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.replication.IndirectReplication;
import net.tomp2p.rpc.ObjectDataReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;

/**
 * Created by jesus on 19.03.2017.
 */
public class Client extends Observable {

    protected static final int MAX_TRIES = 0;
    protected static final long TRY_AGAIN_TIME_WINDOW = 7000;
    protected final String BOOTSTRAPNODE = "Bootstrapnode";
    private final int DEFAULTPORT = 54000;
    private final long TIMERDELAY = 10000;
    private boolean running = true;

    private Logger log;
    private User user;
    private Timer onlineStatusTaskTimer;
    protected PeerDHT peer;


    public Client(int nodeId, String ip, String username, String password, boolean bootstrapNode, Observer nodeReadyObserver) throws IOException, LineUnavailableException, ClassNotFoundException {
        log = LoggerFactory.getLogger("Node of user " + username);
        user = new User(username, password, null);
        int id = ((Long) System.currentTimeMillis()).intValue();

        if (nodeReadyObserver != null) {
            addObserver(nodeReadyObserver);
        }
        // if not a BootstrapNode
        createPeerAndInitiateUser(id, username, password, bootstrapNode, ip);
    }

    protected void createPeerAndInitiateUser(int nodeId, String username, String password, boolean bootstrapNode, String ip) throws IOException, ClassNotFoundException, LineUnavailableException {
        Bindings b = new Bindings().listenAny();
        peer = new PeerBuilderDHT(
                new PeerBuilder(new Number160(nodeId)).ports(getPort()).bindings(b).start())
                .start();

        if (bootstrapNode) {
            // Bootstrapnode
            BootstrapRequest request = new BootstrapRequest(RequestType.STORE);
            request.setBootstrapNodeIP(ip);
            final long time = System.currentTimeMillis();
            BaseFutureListener<FuturePut> futurePutListener = new BaseFutureListener<FuturePut>() {
                @Override
                public void operationComplete(FuturePut future) throws Exception {
                    if (future.isSuccess()) {
                        peer.peer().objectDataReply(new ObjectDataReply() {
                            public Object reply(PeerAddress peerAddress, Object object)
                                    throws Exception {
                                return handleReceivedData(peerAddress, object);
                            }
                        });
                        // TODO: Indirect Replication or direct? Replication factor?
                        new IndirectReplication(peer).autoReplication(true).replicationFactor(3)
                                .start();

                        initiateUser(username, password);
                    } else {
                        long timeNow = System.currentTimeMillis();
                        if (timeNow - time < TRY_AGAIN_TIME_WINDOW) {
                            tryAgain(request, this);
                        }
                    }
                }

                @Override
                public void exceptionCaught(Throwable t) throws Exception {
                    log.error(t.getMessage());
                }

            };
            RequestHandler.handleRequest(request, this, futurePutListener);
        } else {
            peer.peer().objectDataReply(new ObjectDataReply() {
                public Object reply(PeerAddress peerAddress, Object object) throws Exception {
                    return handleReceivedData(peerAddress, object);
                }
            });
            new IndirectReplication(peer).autoReplication(true).replicationFactor(3).start();

            connectToNode(ip, username, password);
        }
    }

    private void connectToNode(String knownIP, String username, String password)
            throws ClassNotFoundException, IOException, LineUnavailableException {
        BootstrapRequest request =
                new BootstrapRequest(RequestType.SEND, user.getPeerAddress(), knownIP);
        final long time = System.currentTimeMillis();
        BaseFutureListener<FutureBootstrap> futureBootstrapListener =
                new BaseFutureListener<FutureBootstrap>() {

                    @Override
                    public void operationComplete(FutureBootstrap future) throws Exception {

                        log.info(getUser().getUsername() + " knows: "
                                + getPeer().peerBean().peerMap().all() + " unverified: "
                                + getPeer().peerBean().peerMap().allOverflow());
                        log.info("Waiting for maintenance ping");
                        if (future.isSuccess()) {
                            initiateUser(username, password);
                        } else {
                            long timeNow = System.currentTimeMillis();
                            if (timeNow - time < TRY_AGAIN_TIME_WINDOW) {
                                tryAgain(request, this);
                            }
                        }
                    }

                    @Override
                    public void exceptionCaught(Throwable t) throws Exception {
                        log.error(t.getMessage());
                    }

                };
        RequestHandler.handleRequest(request, this, futureBootstrapListener);
    }


    protected Object handleReceivedData(PeerAddress peerAddress, Object object)
            throws IOException, LineUnavailableException, ClassNotFoundException {

        log.info("Received message: " + object.toString() + " from: " + peerAddress.toString());

        if (object instanceof Message) {
            MessageRequest messageRequest = new MessageRequest();
            messageRequest.setType(RequestType.RECEIVE);
            messageRequest.setMessage((Message) object);
            RequestHandler.handleRequest(messageRequest, this);
        } else if (object instanceof Request) {
            Request r = (Request) object;
            r.setType(RequestType.RECEIVE);
            RequestHandler.handleRequest(r, this);
        } else {
            System.out.println("else");
        }

        return 0;
    }

    protected void tryAgain(Request request, BaseFutureListener baseFutureListener)
            throws LineUnavailableException, InterruptedException {
        Thread.sleep(500);
        log.debug("Node had unsuccessful Request, Try again...");
        log.debug(
                "Sender: " + request.getSenderName() + ", Receiver: " + request.getReceiverName());
        log.debug(" Request: " + request.getClass() + " Type: " + request.getType());
        RequestHandler.tryAgain(request, this, baseFutureListener);
    }


    private void initiateUser(final String username, final String password)
            throws ClassNotFoundException, LineUnavailableException, IOException {
        Node node = this;
        final long time = System.currentTimeMillis();
        BaseFutureListener<FutureGet> userExistsListener = new BaseFutureListener<FutureGet>() {
            @Override
            public void operationComplete(FutureGet futureGet) throws Exception {
                if (futureGet != null && futureGet.isSuccess() && futureGet.data() != null) {
                    // user already exist --> only update address
                    LoginHelper.updatePeerAddress(node, username);
                } else {
                    long timeNow = System.currentTimeMillis();
                    if (timeNow - time < TRY_AGAIN_TIME_WINDOW) {
                        tryAgainRetrieveUserInfo(username, node, this);
                    } else {
                        // user does not exist --> add user
                        LoginHelper.saveUsernamePassword(node, username, password);
                        UserInfo newUser =
                                new UserInfo(node.getPeer().peerAddress(), username, password);
                        node.setUserInfo(newUser);
                    }
                }
                nodeReady();
            }

            @Override
            public void exceptionCaught(Throwable t) throws Exception {}
        };

        LoginHelper.retrieveUserInfo(username, this, userExistsListener);
    }


    public void shutdown() {
        log.info("Shutting down gracefully.");
        running = false;
        if (peer != null) {
            stopOnlineStatusTask();
            try {
                announceChangedToOfflineStatus();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    protected void stopOnlineStatusTask() {
        if (onlineStatusTaskTimer != null) {
            onlineStatusTaskTimer.cancel();
        }
    }

    private int getPort() {
        int port = DEFAULTPORT;
        while (portIsOpen("127.0.0.1", port, 200)) {
            port++;
        }
        log.info("Free port found, port is: " + port);
        return port;
    }

    private boolean portIsOpen(String ip, int port, int timeout) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), timeout);
            socket.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private void announceChangedToOfflineStatus() throws LineUnavailableException {
//        if (user.getFriendList().isEmpty()) {
//            peer.shutdown();
//        } else {
//            int i = 1;
//            for (Friend f : user.getFriendList()) {
//                OnlineStatusRequest request = new OnlineStatusRequest(f.getPeerAddress(),
//                        peer.peerAddress(), user.getUsername(), f.getName(), RequestType.SEND);
//                // act like we received an OnlineStatusRequest and answer with Aborted so Status
//                // goes to
//                // offline
//                request.setOnlineStatus(OnlineStatus.OFFLINE);
//                request.setStatus(RequestStatus.ABORTED);
//                final boolean lastInLine = (i == user.getFriendList().size());
//                final long time = System.currentTimeMillis();
//                BaseFutureListener<FutureDirect> futureDirectListener =
//                        new BaseFutureListener<FutureDirect>() {
//                            @Override
//                            public void operationComplete(FutureDirect future) throws Exception {
//                                if (future.isSuccess()) {
//                                    if (lastInLine) {
//                                        peer.shutdown();
//                                    }
//                                } else {
//                                    long timeNow = System.currentTimeMillis();
//                                    if (timeNow - time < TRY_AGAIN_TIME_WINDOW) {
//                                        tryAgain(request, this);
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void exceptionCaught(Throwable t) throws Exception {
//                                log.error(t.getMessage());
//                            }
//                        };
//                RequestHandler.handleRequest(request, this, futureDirectListener);
//                i++;
//            }
//        }
    }



}
