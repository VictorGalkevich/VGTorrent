package org.vgsoftware.simpletorrent.io.input;

import org.vgsoftware.simpletorrent.peer.PeerData;

import java.io.IOException;
import java.util.Queue;

public interface FileDownloader {
    void download(String fileName, Queue<PeerData> peers) throws IOException;
}
