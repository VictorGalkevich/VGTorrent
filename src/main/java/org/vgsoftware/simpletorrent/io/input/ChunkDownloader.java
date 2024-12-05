package org.vgsoftware.simpletorrent.io.input;

import org.vgsoftware.simpletorrent.file.FileChunk;
import org.vgsoftware.simpletorrent.peer.PeerData;
import org.vgsoftware.simpletorrent.request.GetChunkRequest;

import java.io.IOException;

public interface ChunkDownloader {
    FileChunk downloadChunk(GetChunkRequest request, PeerData data, String expected, int expectedSize) throws IOException;
}
