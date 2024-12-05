package org.vgsoftware.simpletorrent.encryption;

public interface ChecksumVerifier {
    boolean verify(byte[] data, String expected);
}
