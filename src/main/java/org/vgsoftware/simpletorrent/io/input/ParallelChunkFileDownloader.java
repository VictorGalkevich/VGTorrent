package org.vgsoftware.simpletorrent.io.input;

import org.vgsoftware.simpletorrent.file.FileChunk;
import org.vgsoftware.simpletorrent.file.FileMetadata;
import org.vgsoftware.simpletorrent.io.output.SimpleFileAssembler;
import org.vgsoftware.simpletorrent.peer.PeerData;
import org.vgsoftware.simpletorrent.request.GetChunkRequest;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelChunkFileDownloader implements FileDownloader {
    private final TcpMetadataDownloader tcpMetadataDownloader;
    private final UdpChecksumChunkDownloader udpChecksumDownloader;
    private final SimpleFileAssembler simpleFileAssembler;
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public ParallelChunkFileDownloader(TcpMetadataDownloader tcpMetadataDownloader, UdpChecksumChunkDownloader udpChecksumDownloader, SimpleFileAssembler simpleFileAssembler) {
        this.tcpMetadataDownloader = tcpMetadataDownloader;
        this.udpChecksumDownloader = udpChecksumDownloader;
        this.simpleFileAssembler = simpleFileAssembler;
    }

    @Override
    public void download(String fileName, Queue<PeerData> peers) throws IOException {
        FileMetadata metadata = tcpMetadataDownloader.downloadMetadata(peers.peek(), fileName);

        if (metadata == null) {
            System.err.println("Unable to retrieve metadata for file: " + fileName);
            return;
        }

        final List<FileChunk> chunks = new CopyOnWriteArrayList<>();
        final Queue<Integer> chunkNumbers = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < metadata.numberOfChunks(); i++) {
            chunkNumbers.add(i);
        }
        while (!chunkNumbers.isEmpty()) {
            int i = chunkNumbers.poll();
            int expectedSize = (int) Math.min(metadata.chunkSize(), metadata.fileSize()) - i * metadata.chunkSize();
            String expectedChecksum = metadata.chunkChecksums().get(i);

            PeerData peer = peers.poll();

            GetChunkRequest request = new GetChunkRequest(
                    fileName,
                    i
            );
            executorService.submit(() -> {
                try {
                    FileChunk fc = udpChecksumDownloader.downloadChunk(request, peer, expectedChecksum, expectedSize);
                    chunks.add(fc);
                    peers.add(peer);
                } catch (IOException e) {
                    chunkNumbers.add(i);
                }
            });

        }

        simpleFileAssembler.assemble(chunks, "downloads/" + fileName);
    }
}
