package org.vgsoftware.simpletorrent.request;

public record GetMetadataRequest(
        String fileName
) implements Request {

    private static final String HEADER = "GET_METADATA";

    @Override
    public String requestMessage() {
        return HEADER + " " + fileName;
    }

    public static String header() {
        return HEADER;
    }
}
