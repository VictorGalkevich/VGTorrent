package org.vgsoftware.simpletorrent.io.input;

import org.vgsoftware.simpletorrent.file.FileMetadata;
import org.vgsoftware.simpletorrent.peer.PeerData;

public interface MetadataDownloader {
    FileMetadata downloadMetadata(PeerData peer, String fileName);
}
