package org.vgsoftware.simpletorrent.processor.client;

import org.vgsoftware.simpletorrent.connection.Connection;

import java.io.DataOutputStream;
import java.io.IOException;

public class PingProcessor implements PeerCommunicationProcessor {
    @Override
    public void process(Connection connection, String... args) {
        try {
            DataOutputStream dos = new DataOutputStream(connection.os());
            dos.writeUTF("PONG");
        } catch (IOException e) {
            System.out.println("CONNECTION ERROR: " + e.getMessage());
        }
    }
}
