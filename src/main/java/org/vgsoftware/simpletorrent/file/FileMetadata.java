package org.vgsoftware.simpletorrent.file;

import java.util.List;

public record FileMetadata(
        long fileSize,
        int chunkSize,
        int numberOfChunks,
        List<String> chunkChecksums
) {
}
