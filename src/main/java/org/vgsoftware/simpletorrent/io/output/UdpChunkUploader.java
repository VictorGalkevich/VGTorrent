package org.vgsoftware.simpletorrent.io.output;

import org.vgsoftware.simpletorrent.peer.PeerData;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpChunkUploader implements ChunkUploader {

    @Override
    public void uploadChunk(PeerData peer, int chunkIndex, String fileName) throws IOException {
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("ERROR Файл не найден");
            return;
        }

        long fileSize = file.length();
        int CHUNK_SIZE = 256 * 1024;
        int totalChunks = (int) Math.ceil((double) fileSize / CHUNK_SIZE);

        if (chunkIndex < 0 || chunkIndex >= totalChunks) {
            System.out.println("ERROR Неверный индекс фрагмента");
            return;
        }

        // Открываем файл и читаем нужный фрагмент
        try (RandomAccessFile raf = new RandomAccessFile(file, "r");
             DatagramSocket socket = new DatagramSocket()) {

            // Вычисляем позицию начала фрагмента
            long position = (long) chunkIndex * CHUNK_SIZE;
            raf.seek(position);

            // Вычисляем размер фрагмента
            int bytesToRead = (int) Math.min(CHUNK_SIZE, fileSize - position);
            byte[] buffer = new byte[bytesToRead];

            // Читаем данные фрагмента
            raf.readFully(buffer);

            DatagramPacket packet = new DatagramPacket(buffer, bytesToRead, InetAddress.getByName(peer.address()), peer.port());

            socket.send(packet);

            System.out.println("Отправлен фрагмент " + chunkIndex + " файла " + fileName);
        } catch (IOException e) {
            System.out.println("ERROR Ошибка при чтении файла");
            e.printStackTrace();
        }
    }
}

