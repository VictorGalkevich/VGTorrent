package org.vgsoftware.simpletorrent.io.input;

import org.vgsoftware.simpletorrent.file.FileMetadata;
import org.vgsoftware.simpletorrent.peer.PeerData;
import org.vgsoftware.simpletorrent.request.GetMetadataRequest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TcpMetadataDownloader implements MetadataDownloader {

    @Override
    public FileMetadata downloadMetadata(PeerData peer, String fileName) {
        try (Socket socket = new Socket(peer.address(), peer.port());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             DataInputStream dis = new DataInputStream(socket.getInputStream())) {

            GetMetadataRequest gmr = new GetMetadataRequest(fileName);

            dos.writeUTF(gmr.requestMessage());
            dos.flush();

            long fileSize = dis.readLong();
            int chunkSize = dis.readInt();
            int numberOfChunks = dis.readInt();

            List<String> chunkChecksums = new ArrayList<>();
            for (int i = 0; i < numberOfChunks; i++) {
                String checksum = dis.readUTF();
                chunkChecksums.add(checksum);
            }

            return new FileMetadata(fileSize, chunkSize, numberOfChunks, chunkChecksums);

        } catch (Exception e) {
            //TODO: IMPLEMENT
            e.printStackTrace();
            return null;
        }
    }
}
