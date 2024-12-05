package org.vgsoftware.simpletorrent.connection;

import org.vgsoftware.simpletorrent.peer.PeerData;

import java.io.InputStream;
import java.io.OutputStream;

public record PeerUdpConnection(PeerData receiver) implements Connection {

    @Override
    public OutputStream os() {
        return null;
    }

    @Override
    public InputStream is() {
        return null;
    }
}
