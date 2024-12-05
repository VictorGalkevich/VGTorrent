package org.vgsoftware.simpletorrent.app;

import org.vgsoftware.simpletorrent.connection.Connection;
import org.vgsoftware.simpletorrent.connection.PeerTcpConnection;
import org.vgsoftware.simpletorrent.connection.PeerUdpConnection;
import org.vgsoftware.simpletorrent.peer.Peer;
import org.vgsoftware.simpletorrent.peer.PeerData;
import org.vgsoftware.simpletorrent.processor.client.*;
import org.vgsoftware.simpletorrent.request.GetChunkRequest;
import org.vgsoftware.simpletorrent.request.GetMetadataRequest;
import org.vgsoftware.simpletorrent.request.PingRequest;
import org.vgsoftware.simpletorrent.request.SearchRequest;
import org.vgsoftware.simpletorrent.ui.Window;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {
    private static final ExecutorService service = Executors.newFixedThreadPool(4);
    private static ServerSocket serverSocket;
    private static int port = 8080;
    private static Peer peer;
    private static final Map<String, PeerCommunicationProcessor> processors = new HashMap<>();

    static {
        processors.put(GetChunkRequest.header(), new GetChunkProcessor());
        processors.put(GetMetadataRequest.header(), new GetMetadataProcessor());
        processors.put(PingRequest.header(), new PingProcessor());
        processors.put(SearchRequest.header(), new SearchProcessor());
    }

    public static void main(String[] args) throws IOException {
        ejectPort(args);
        PeerData initialPeerData = ejectInitialPeerAddress(args);

        peer = createPeer(initialPeerData);

        serverSocket = new ServerSocket(port);

        new UiThread().start();
        new ClientThread().start();
    }

    public static Peer createPeer(PeerData peerData) {
        Set<PeerData> peers = new HashSet<>();
        peers.add(peerData);
        Peer.setDir("/downloads");
        return new Peer(
                new PeerData("localhost", port),
                0,
                peers
        );
    }

    public static void ejectPort(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("-p".equals(args[i]) && i + 1 < args.length) {
                port = Integer.parseInt(args[i + 1]);
                break;
            }
        }
    }

    public static PeerData ejectInitialPeerAddress(String[] args) {
        String initialHost = "localhost";
        int initialPort = 8081;
        for (int i = 0; i < args.length; i++) {
            if ("-i".equals(args[i]) && i + 2 < args.length) {
                initialHost = args[i + 1];
                initialPort = Integer.parseInt(args[i + 2]);
                break;
            }
        }
        return new PeerData(initialHost, initialPort);
    }

    static class ClientThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Socket accepted = serverSocket.accept();
                    service.submit(
                            () -> handleClientConnection(accepted)
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

        }
    }

    public static void handleClientConnection(Socket socket) {
        while (!socket.isClosed()) {
            try {
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());

                String request = inputStream.readUTF();
                String[] splittedRequest = request.split(" ");

                Connection connection = null;

                if (splittedRequest[0].equals("GET_CHUNK")) {
                    PeerData peerData = new PeerData(socket.getInetAddress().getHostName(), socket.getPort());
                    connection = new PeerUdpConnection(peerData);
                } else {
                    connection = new PeerTcpConnection(outputStream, inputStream);
                }

                processors.get(splittedRequest[0]).process(
                        connection,
                        Arrays.copyOfRange(splittedRequest, 1, splittedRequest.length)
                );

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    static class UiThread extends Thread {

        @Override
        public void run() {
            new Window(peer);
        }
    }
}
