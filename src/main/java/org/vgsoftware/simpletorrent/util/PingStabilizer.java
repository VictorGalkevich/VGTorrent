package org.vgsoftware.simpletorrent.util;

import org.vgsoftware.simpletorrent.peer.Peer;
import org.vgsoftware.simpletorrent.peer.PeerData;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PingStabilizer implements Stabilizer {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void stabilize(Peer peer) throws UnknownHostException {
        Set<PeerData> peers = peer.peers();
        List<PeerData> todelete = new ArrayList<>();
        for (PeerData peerData : peers) {
            InetAddress address = Inet4Address.getByName(peerData.address());
            int port = peerData.port();
            try (Socket socket = new Socket(address, port)) {

            } catch (IOException e) {
                todelete.add(peerData);
            }
        }
        for (PeerData peerx : todelete) {
            peers.remove(peerx);
        }
    }
}
