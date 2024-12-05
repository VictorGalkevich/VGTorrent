package org.vgsoftware.simpletorrent.processor.client;

import org.vgsoftware.simpletorrent.connection.Connection;

import java.io.IOException;

public interface PeerCommunicationProcessor {
    void process(Connection connection, String... args) throws IOException;
}
