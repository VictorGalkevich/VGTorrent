package org.vgsoftware.simpletorrent.encryption;

public interface ChecksumGenerator {
    String generate(byte[] data);
}
