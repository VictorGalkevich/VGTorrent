package org.vgsoftware.simpletorrent.processor.ui;

import org.vgsoftware.simpletorrent.io.input.FileDownloader;
import org.vgsoftware.simpletorrent.io.input.ParallelChunkFileDownloader;
import org.vgsoftware.simpletorrent.io.input.TcpMetadataDownloader;
import org.vgsoftware.simpletorrent.io.input.UdpChecksumChunkDownloader;
import org.vgsoftware.simpletorrent.io.output.SimpleFileAssembler;
import org.vgsoftware.simpletorrent.peer.Peer;
import org.vgsoftware.simpletorrent.peer.PeerData;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UiDownloadProcessor implements UiProcessor {
    private final FileDownloader fileDownloader;

    public UiDownloadProcessor() {
        TcpMetadataDownloader downloader = new TcpMetadataDownloader();
        UdpChecksumChunkDownloader chunkDownloader = new UdpChecksumChunkDownloader();
        SimpleFileAssembler simpleFileAssembler = new SimpleFileAssembler();
        this.fileDownloader = new ParallelChunkFileDownloader(
                downloader,
                chunkDownloader,
                simpleFileAssembler
        );
    }

    @Override
    public void process(String fileName, Peer requester, List<PeerData> peers) throws IOException {
        Queue<PeerData> queue = new ConcurrentLinkedQueue<>(peers);
        fileDownloader.download(fileName, queue);
    }
}
