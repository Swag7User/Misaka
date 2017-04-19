/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.uzh.helper;

import javafx.util.Pair;
import net.tomp2p.dht.*;
import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Basically a wrapper for TomP2P for the needs of this application.
 * @author Sebastian
 */
public class P2POverlay {

    private Peer peer;
    private PeerDHT peerDHT;
    private static Random rnd = new Random();

    public PeerAddress getPeerAddress() {
        return peer.peerAddress();
    }

    public boolean put(String key, Object value) {
        Data data;
        try {
            data = new Data(value);
        } catch (IOException ex) {
            return false;
        }

        FuturePut futurePut = peerDHT.put(Number160.createHash(key)).data(data).start().awaitUninterruptibly();

        return futurePut.isSuccess();
    }

    public Object getBlocking(String key) {
        FutureGet futureGet = peerDHT.get(Number160.createHash(key)).start().awaitUninterruptibly();

        if (futureGet.isSuccess()) {
            try {
                return futureGet.data().object();
            } catch (Exception ex) {
                System.err.println("MY ARCH NEMESIS");
                ex.printStackTrace();
                return null;
            }
        } else {
            System.err.println("MY ARCH NEMESIS TOO TBH");
            return null;
        }
    }

    public boolean sendBlocking(PeerAddress recipient, Object o) {
        FutureDirect futureDirect = peer.sendDirect(recipient)
                .object(o).start().awaitUninterruptibly();

        return futureDirect.isSuccess();
    }

    public void sendNonBlocking(PeerAddress recipient, Object o, boolean forceUDP) {
        FutureDirect futureDirect = peer.sendDirect(recipient)
                .object(o).forceTCP(forceUDP).start();
    }

    public void getNonBLocking(String key, BaseFutureAdapter<FutureGet> baseFutureAdapter) {
        FutureGet futureGet = peerDHT.get(Number160.createHash(key)).start();
        futureGet.addListener(baseFutureAdapter);
    }

    public Pair<Boolean, String> bootstrap(String bootstrapIP) {
        int port = 4001;

        // Create TomP2P peer
        boolean peerCreated = false;
        do {
            try {
                // Create new peer
                peer = new PeerBuilder(new Number160(rnd)).ports(port++).start();
                peerDHT = new PeerBuilderDHT(peer).start();
                peerCreated = true;
            } catch (IOException ex) {
                System.out.println("Port already in use. " + ex.getMessage());
            }
        } while (!peerCreated && port < 4010);

        if (!peerCreated) {
            return new Pair<>(false, "Could not find any unused port");
        }

        try {
            peer.discover().inetAddress(InetAddress.getByName(bootstrapIP)).ports(4001).start().awaitUninterruptibly();
            FutureBootstrap futureBootstrap = peer.bootstrap().inetAddress(InetAddress.getByName(bootstrapIP)).ports(4001).start();
            futureBootstrap.awaitUninterruptibly();
            if (futureBootstrap.isSuccess()) {
                return new Pair<>(true, "Bootstrap successful");
            } else {
                return new Pair<>(false, "Could not bootstrap to well known peer");
            }
        } catch (UnknownHostException ex) {
            return new Pair<>(false, "Unknown bootstrap host. (UnknownHostException)");
        }

    }

    public void setObjectDataReply(ObjectDataReply replyHandler) {
        peer.objectDataReply(replyHandler);
    }

    public void shutdown() {
        if (peer != null) {
            BaseFuture shutdownBuilder = peer.shutdown();
            shutdownBuilder.awaitUninterruptibly();
            peer = null;
        }
    }
}
