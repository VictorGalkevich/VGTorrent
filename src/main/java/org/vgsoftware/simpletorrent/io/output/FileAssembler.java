package org.vgsoftware.simpletorrent.io.output;

import org.vgsoftware.simpletorrent.file.FileChunk;

import java.io.IOException;
import java.util.List;

public interface FileAssembler {
    void assemble(List<FileChunk> chunks, String outputDirectory) throws IOException;
}
