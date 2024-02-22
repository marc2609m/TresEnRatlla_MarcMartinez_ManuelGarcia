package main;

import java.io.*;
import java.net.*;

public class ServidorTCP {
    
    private static final int PORT = 9999;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Game server started...");

            while (true) {
                Socket player1Socket = serverSocket.accept();
                Socket player2Socket = serverSocket.accept();

                JuegoTresEnRaya game = new JuegoTresEnRaya(player1Socket, player2Socket);
                game.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
