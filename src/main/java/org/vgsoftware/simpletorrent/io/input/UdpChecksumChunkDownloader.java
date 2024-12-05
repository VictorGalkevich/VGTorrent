package org.vgsoftware.simpletorrent.io.input;

import org.vgsoftware.simpletorrent.encryption.Sha256Util;
import org.vgsoftware.simpletorrent.file.FileChunk;
import org.vgsoftware.simpletorrent.peer.PeerData;
import org.vgsoftware.simpletorrent.request.GetChunkRequest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;

public class UdpChecksumChunkDownloader implements ChunkDownloader {
    private final Sha256Util sha256Util = new Sha256Util();

    @Override
    public FileChunk downloadChunk(GetChunkRequest request, PeerData data, String expected, int expectedSize) throws IOException {
        try (DatagramSocket socket = new DatagramSocket()) {

            byte[] requestMessage = request.requestMessage().getBytes();
            String address = data.address();
            int port = data.port();

            InetAddress byName = Inet4Address.ofLiteral(address);

            DatagramPacket packet = new DatagramPacket(requestMessage, requestMessage.length, byName, port);
            socket.send(packet);

            byte[] chunk = new byte[expectedSize];
            DatagramPacket chunkPacket = new DatagramPacket(chunk, expectedSize);
            socket.receive(chunkPacket);

            String checksum = sha256Util.generate(chunk);
            if (expected.equals(checksum)) {
                return new FileChunk(request.index(), chunk, checksum);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
