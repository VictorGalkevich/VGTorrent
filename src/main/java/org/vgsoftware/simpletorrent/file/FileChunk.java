package org.vgsoftware.simpletorrent.file;

import java.io.Serializable;

public record FileChunk(
        int index,
        byte[] data,
        String checksum
) implements Serializable, Comparable<FileChunk> {

    @Override
    public int compareTo(FileChunk o) {
        return index - o.index;
    }
}