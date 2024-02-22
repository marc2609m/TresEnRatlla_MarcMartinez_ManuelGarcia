package main;

import java.net.*;
import java.io.*;
import java.util.*;

public class ServidorUDP {

    private static final int PORT_SERVIDOR_CENTRAL = 7879;
    private static final int MAX_PARTIDES_PENDENTS = 10;

    private Queue<String> partidesNoves = new LinkedList<>();
    private Set<String> partidesRegistrades = new HashSet<>();

    public static void main(String[] args) {
        new ServidorUDP().iniciar();
    }

    public void iniciar() {
        try ( DatagramSocket socket = new DatagramSocket(PORT_SERVIDOR_CENTRAL)) {
            System.out.println("Servidor central iniciado. Esperando conexiones...");

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String mensaje = new String(packet.getData(), 0, packet.getLength());
                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                String[] partes = mensaje.split(" ");
                String accion = partes[0];

                if (accion.equals("CREAR")) {
                    if (partes.length < 2) {
                        enviarMensaje("ERROR Falta el puerto del juego", address, port, socket);
                        continue;
                    }

                    String ipPort = address.getHostAddress() + "::" + partes[1];
                    if (partidesRegistrades.contains(ipPort)) {
                        enviarMensaje("ERROR Ya existe una partida registrada con ese puerto", address, port, socket);
                    } else {
                        if (partidesNoves.size() < MAX_PARTIDES_PENDENTS) {
                            partidesNoves.offer(ipPort);
                            partidesRegistrades.add(ipPort);
                            enviarMensaje("OK", address, port, socket);
                        } else {
                            enviarMensaje("ERROR No se pueden crear más partidas en este momento", address, port, socket);
                        }
                    }
                } else if (accion.equals("UNIR-ME")) {
                    if (!partidesNoves.isEmpty()) {
                        String partida = partidesNoves.poll();
                        enviarMensaje(partida, address, port, socket);
                        partidesRegistrades.remove(partida);
                    } else {
                        // Esperar a que haya partidas disponibles
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enviarMensaje(String mensaje, InetAddress address, int port, DatagramSocket socket) throws IOException {
        byte[] buffer = mensaje.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);
    }
}
