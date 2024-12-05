package org.vgsoftware.simpletorrent.io.output;

import org.vgsoftware.simpletorrent.peer.PeerData;

import java.io.IOException;

public interface ChunkUploader {
    void uploadChunk(PeerData peer, int chunkIndex, String fileName) throws IOException;
}
