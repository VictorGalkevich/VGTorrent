package org.vgsoftware.simpletorrent.processor.ui;

import org.vgsoftware.simpletorrent.peer.Peer;
import org.vgsoftware.simpletorrent.peer.PeerData;
import org.vgsoftware.simpletorrent.request.SearchRequest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UiSearchProcessor implements UiProcessor {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void process(String fileName, Peer requester, List<PeerData> peers) throws UnknownHostException {
        Set<PeerData> neighbours = requester.peers();

        SearchRequest request = new SearchRequest(fileName);


        for (PeerData neighbour : neighbours) {
            InetAddress address = InetAddress.getByName(neighbour.address());
            int port = neighbour.port();
            try (Socket socket = new Socket(address, port)) {
                executorService.submit(() -> {
                    boolean result = search(request, socket);
                    if (result) {
                        peers.add(neighbour);
                    }
                });
            } catch (IOException e) {
                System.out.println("Error searching for peer " + neighbour.address() + " " + e.getMessage());
            }
        }
    }

    private boolean search(SearchRequest request, Socket socket) {
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            dos.writeUTF(request.requestMessage());

            return dis.readBoolean();
        } catch (IOException e) {
            System.out.println("Error searching for file " + request.toString() + " " + e.getMessage());
            return false;
        }
    }
}
