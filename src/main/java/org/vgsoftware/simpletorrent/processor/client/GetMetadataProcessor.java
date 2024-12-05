package org.vgsoftware.simpletorrent.processor.client;

import org.vgsoftware.simpletorrent.connection.Connection;
import org.vgsoftware.simpletorrent.encryption.Sha256Util;
import org.vgsoftware.simpletorrent.peer.Peer;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class GetMetadataProcessor implements PeerCommunicationProcessor {
    private final Sha256Util sha256Util = new Sha256Util();

    @Override
    public void process(Connection connection, String... args) throws IOException {
        if (args.length != 2) {
            System.out.printf("INVALID ARGUMENTS, SIZE MUST BE 2, BUT RECEIVED %d ARGUMENTS%n", args.length);
            return;
        }

        String fileName = args[1];
        String dir = Peer.dir();

        File file = new File(dir + File.separator + fileName);

        if (!file.exists()) {
            System.out.printf("FILE %s DOES NOT EXIT%n", fileName);
            return;
        }

        long length = file.length();
        int chunkSize = 256 * 1024;
        int totalChunks = (int) Math.ceil((double) length / chunkSize);
        List<String> checksums = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            int i = 0;
            while (i < totalChunks) {
                long position = (long) i * chunkSize;
                raf.seek(position);

                int bytesToRead = (int) Math.min(chunkSize, length - position);
                byte[] chunk = new byte[bytesToRead];

                raf.readFully(chunk);

                String checksum = sha256Util.generate(chunk);
                checksums.add(checksum);

                i++;
            }

        } catch (IOException e) {
            System.out.println("ERROR READING FILE: " + fileName + ", MESSAGE" + e.getMessage());
            return;
        }

        try {
            DataOutputStream dos = new DataOutputStream(connection.os());
            dos.writeLong(length);
            dos.writeInt(chunkSize);
            dos.writeInt(totalChunks);
            for (String checksum : checksums) {
                dos.writeUTF(checksum);
            }
            dos.flush();
        } catch (IOException e) {
            System.out.println("ERROR WRITING IN PIPE: " + e.getMessage());
        }

    }
}
