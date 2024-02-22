package main;

import java.io.*;
import java.net.*;

public class ClienteTCP {

    private static final String SERVER_IP = "192.168.21.2"; // Cambia por la IP del servidor
    private static final int SERVER_PORT = 8888;

    public static void main(String[] args) {
        try ( Socket socket = new Socket(SERVER_IP, SERVER_PORT);  BufferedReader entradaDesdeUsuario = new BufferedReader(new InputStreamReader(System.in));  PrintWriter salidaAServidor = new PrintWriter(socket.getOutputStream(), true);  BufferedReader entradaDesdeServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String mensajeServidor;
            while ((mensajeServidor = entradaDesdeServidor.readLine()) != null) {
                System.out.println("Servidor: " + mensajeServidor);

                if (mensajeServidor.startsWith("Jugador")) {
                    System.out.print("Tu turno: ");
                    String jugada = entradaDesdeUsuario.readLine();
                    salidaAServidor.println(jugada);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
