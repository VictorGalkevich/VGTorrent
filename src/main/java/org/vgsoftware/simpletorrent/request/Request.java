package org.vgsoftware.simpletorrent.request;

public interface Request {
    String requestMessage();

    static String header() {
        return null;
    }
}
