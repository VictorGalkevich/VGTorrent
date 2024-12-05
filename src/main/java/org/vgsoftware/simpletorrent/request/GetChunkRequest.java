package org.vgsoftware.simpletorrent.request;

public record GetChunkRequest(
        String filename,
        int index
) implements Request {

    private static final String HEADER = "GET_CHUNK";

    @Override
    public String requestMessage() {
        return HEADER + " " + index + " " + filename;
    }

    public static String header() {
        return HEADER;
    }
}
