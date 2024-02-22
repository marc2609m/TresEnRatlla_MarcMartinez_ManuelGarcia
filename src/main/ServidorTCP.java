package main;

import java.io.*;
import java.net.*;

public class ServidorTCP {
    
    private static final int PORT = 8888;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor de partida iniciado. Esperando jugadores...");

            while (true) {
                Socket jugador1Socket = serverSocket.accept();
                Socket jugador2Socket = serverSocket.accept();

                System.out.println("Jugador 1 y Jugador 2 conectados.");

                JuegoTresEnRaya juego = new JuegoTresEnRaya(jugador1Socket, jugador2Socket);
                juego.iniciar();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
