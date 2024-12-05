package org.vgsoftware.simpletorrent.connection;

import java.io.InputStream;
import java.io.OutputStream;

public record PeerTcpConnection(OutputStream os, InputStream is) implements Connection {
    @Override
    public OutputStream os() {
        return os;
    }

    @Override
    public InputStream is() {
        return is;
    }
}
