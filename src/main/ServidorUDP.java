package main;

import java.net.*;
import java.io.*;
import java.util.*;

public class ServidorUDP {

    private static final int UDP_PORT = 7879;
    private static final int TCP_PORT = 8888;

    private static Queue<String> gameQueue = new LinkedList<>();
    private static Set<String> gameSet = new HashSet<>();

    public static void main(String[] args) {
        try {
            DatagramSocket udpSocket = new DatagramSocket(UDP_PORT);
            System.out.println("Central server started...");

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                udpSocket.receive(packet);

                String request = new String(packet.getData(), 0, packet.getLength());
                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();

                processRequest(request, clientAddress, clientPort);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processRequest(String request, InetAddress clientAddress, int clientPort) throws IOException {
        String[] parts = request.split(" ");
        String response = "";

        if (parts[0].equals("CREAR")) {
            String gameKey = clientAddress.getHostAddress() + "::" + parts[1];
            if (!gameSet.contains(gameKey)) {
                gameQueue.offer(gameKey);
                gameSet.add(gameKey);
                response = "OK";
            } else {
                response = "ERROR Joc ja registrat";
            }
        } else if (parts[0].equals("UNIR-ME")) {
            if (!gameQueue.isEmpty()) {
                String gameKey = gameQueue.poll();
                response = gameKey;
                gameSet.remove(gameKey);
            } else {
                response = "WAIT";
            }
        }

        sendResponse(response, clientAddress, clientPort);
    }

    private static void sendResponse(String response, InetAddress clientAddress, int clientPort) throws IOException {
        DatagramSocket udpSocket = new DatagramSocket();
        byte[] sendData = response.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
        udpSocket.send(sendPacket);
        udpSocket.close();
    }
}
