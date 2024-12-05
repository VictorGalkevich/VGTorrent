package org.vgsoftware.simpletorrent.processor.ui;

import org.vgsoftware.simpletorrent.peer.Peer;
import org.vgsoftware.simpletorrent.peer.PeerData;

import java.io.IOException;
import java.util.List;

public interface UiProcessor {
    void process(String fileName, Peer requester, List<PeerData> peers) throws IOException;
}
