package org.vgsoftware.simpletorrent.ui;

import org.vgsoftware.simpletorrent.peer.Peer;
import org.vgsoftware.simpletorrent.peer.PeerData;
import org.vgsoftware.simpletorrent.processor.ui.UiDownloadProcessor;
import org.vgsoftware.simpletorrent.processor.ui.UiSearchProcessor;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Window extends JFrame {
    private static final int HEIGHT = 600;
    private static final int WIDTH = 800;

    private static final String DOWNLOAD_MESSAGE = "Downloading file %s, please don't close the application.";
    private static final String SEARCH_MESSAGE = "Looking for file %s, please don't close the application.";
    private static final String SUCCESS_DOWNLOAD = "File downloaded successfully.";
    private static final String FAILURE_DOWNLOAD = "File wasn't downloaded.";
    private static final String SUCCESS_SEARCH = "The file was found successfully.";
    private static final String FAILURE_SEARCH = "The file wasn't found.";

    private static final JButton DOWNLOAD_BUTTON = new JButton("Download");
    private static final JButton SEARCH_BUTTON = new JButton("Search");

    private static final JTextField FILE_NAME_FIELD = new JTextField(5);
    private static final UiDownloadProcessor DOWNLOAD_PROCESSOR = new UiDownloadProcessor();
    private static final UiSearchProcessor SEARCH_PROCESSOR = new UiSearchProcessor();
    private static Peer peer;
    private static List<PeerData> peers;

    public Window(Peer peer) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        Window.peer = peer;
        JPanel upperPanel = new JPanel();
        JPanel lowerPanel = new JPanel();
        upperPanel.setLayout(new GridLayout(2, 1));
        upperPanel.setPreferredSize(new Dimension(WIDTH, (HEIGHT * 2 / 3)));
        lowerPanel.setLayout(new GridLayout(1, 2));

        setSize(WIDTH, HEIGHT);

        upperPanel.add(FILE_NAME_FIELD);
        upperPanel.add(SEARCH_BUTTON);
        lowerPanel.add(DOWNLOAD_BUTTON);

        add(upperPanel, BorderLayout.NORTH);
        add(lowerPanel, BorderLayout.CENTER);

        DOWNLOAD_BUTTON.addActionListener(_ -> {
            String fileName = FILE_NAME_FIELD.getText();

            try {
                DOWNLOAD_PROCESSOR.process(
                        fileName,
                        peer,
                        peers
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        SEARCH_BUTTON.addActionListener(_ -> {
            String fileName = FILE_NAME_FIELD.getText();
            try {
                peers = new ArrayList<>();
                SEARCH_PROCESSOR.process(
                        fileName,
                        peer,
                        peers
                );
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        });

        setVisible(true);
    }
}
