package org.vgsoftware.simpletorrent.util;

import org.vgsoftware.simpletorrent.peer.Peer;

import java.net.UnknownHostException;

public interface Stabilizer {
    void stabilize(Peer peer) throws UnknownHostException;
}
