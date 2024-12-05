package org.vgsoftware.simpletorrent.processor.client;

import org.vgsoftware.simpletorrent.connection.Connection;
import org.vgsoftware.simpletorrent.peer.Peer;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class SearchProcessor implements PeerCommunicationProcessor {
    @Override
    public void process(Connection connection, String... args) throws IOException {
        try {
            DataOutputStream dos = new DataOutputStream(connection.os());

            if (args.length == 0) {
                dos.writeUTF("ERROR, not enough arguments");
            }

            String fileName = args[0];

            File[] availableFiles = listFiles();

            boolean exists = exists(availableFiles, fileName);

            dos.writeBoolean(exists);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File[] listFiles() {
        File directory = new File(Peer.dir());

        return directory.listFiles();
    }

    private boolean exists(File[] files, String name) {
        for (File file : files) {
            if (file.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
