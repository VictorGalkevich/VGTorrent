package org.vgsoftware.simpletorrent.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.vgsoftware.simpletorrent.encryption.EncryptionAlgorithms.SHA_256;

public class Sha256Util implements ChecksumGenerator, ChecksumVerifier {
    @Override
    public String generate(byte[] data) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(SHA_256);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] digest = md.digest(data);
        return bytesToHex(digest);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @Override
    public boolean verify(byte[] data, String expected) {
        String actualChecksum = generate(data);
        return actualChecksum.equals(expected);
    }
}
