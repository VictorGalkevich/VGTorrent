package org.vgsoftware.simpletorrent.connection;

import java.io.InputStream;
import java.io.OutputStream;

public interface Connection {
    OutputStream os();

    InputStream is();
}
