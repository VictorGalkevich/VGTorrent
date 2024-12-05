package org.vgsoftware.simpletorrent.request;

public record SearchRequest(String fileName) implements Request {
    private static final String HEADER = "SEARCH";

    @Override
    public String requestMessage() {
        return HEADER + " " + fileName;
    }

    public static String header() {
        return HEADER;
    }
}
