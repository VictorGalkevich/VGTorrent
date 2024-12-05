package org.vgsoftware.simpletorrent.peer;

import java.io.Serializable;

public record PeerData(
        String address,
        int port
) implements Serializable {
}
