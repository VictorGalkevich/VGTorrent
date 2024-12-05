package org.vgsoftware.simpletorrent.io.output;

import org.vgsoftware.simpletorrent.file.FileChunk;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class SimpleFileAssembler implements FileAssembler {
    @Override
    public void assemble(List<FileChunk> chunks, String outputDirectory) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputDirectory)) {
            for (FileChunk chunk : chunks) {
                fos.write(chunk.data());
            }
        }
    }
}
