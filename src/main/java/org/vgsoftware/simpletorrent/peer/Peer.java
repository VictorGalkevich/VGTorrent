package org.vgsoftware.simpletorrent.peer;

import java.util.Set;

public record Peer(
        PeerData data,
        int maxPeers,
        Set<PeerData> peers) {
    private static String sharedDirectory;

    public static void setDir(String sharedDirectory) {
        Peer.sharedDirectory = sharedDirectory;
    }

    public static String dir() {
        return sharedDirectory;
    }
}
