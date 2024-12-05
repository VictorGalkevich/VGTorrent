package org.vgsoftware.simpletorrent.request;

public record PingRequest() implements Request {

    private static final String HEADER = "PING";

    @Override
    public String requestMessage() {
        return HEADER;
    }

    public static String header() {
        return HEADER;
    }
}
