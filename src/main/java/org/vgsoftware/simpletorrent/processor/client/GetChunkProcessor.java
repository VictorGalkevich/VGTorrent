package org.vgsoftware.simpletorrent.processor.client;

import org.vgsoftware.simpletorrent.connection.Connection;
import org.vgsoftware.simpletorrent.connection.PeerUdpConnection;
import org.vgsoftware.simpletorrent.io.output.UdpChunkUploader;
import org.vgsoftware.simpletorrent.peer.Peer;

import java.io.File;
import java.io.IOException;

public class GetChunkProcessor implements PeerCommunicationProcessor {
    private final UdpChunkUploader udpChunkUploader = new UdpChunkUploader();

    @Override
    public void process(Connection connection, String... args) {
        if (args.length != 3) {
            System.out.printf("INVALID ARGUMENTS, SIZE MUST BE 3, BUT RECEIVED %d ARGUMENTS%n", args.length);
            return;
        }

        String fileName = args[2];
        int chunkIndex;
        try {
            chunkIndex = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("ERROR PARSING CHUNK INDEX: " + args[1]);
            return;
        }

        fileName = Peer.dir() + File.separator + fileName;

        PeerUdpConnection con = (PeerUdpConnection) connection;

        try {
            udpChunkUploader.uploadChunk(con.receiver(), chunkIndex, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
