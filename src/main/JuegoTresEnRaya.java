package main;

import java.io.*;
import java.net.*;

public class JuegoTresEnRaya {
    private char[][] tablero;
    private char jugadorActual;
    private Socket jugador1Socket;
    private Socket jugador2Socket;
    private PrintWriter salidaJugador1;
    private PrintWriter salidaJugador2;
    private BufferedReader entradaJugador1;
    private BufferedReader entradaJugador2;

    public JuegoTresEnRaya(Socket jugador1Socket, Socket jugador2Socket) {
        tablero = new char[3][3];
        jugadorActual = 'X'; // Comienza el jugador X
        this.jugador1Socket = jugador1Socket;
        this.jugador2Socket = jugador2Socket;

        try {
            salidaJugador1 = new PrintWriter(jugador1Socket.getOutputStream(), true);
            salidaJugador2 = new PrintWriter(jugador2Socket.getOutputStream(), true);
            entradaJugador1 = new BufferedReader(new InputStreamReader(jugador1Socket.getInputStream()));
            entradaJugador2 = new BufferedReader(new InputStreamReader(jugador2Socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        inicializarTablero();
    }

    private void inicializarTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[i][j] = '-';
            }
        }
    }

    public void imprimirTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void enviarMensaje(String mensaje, PrintWriter salida) {
        salida.println(mensaje);
    }

    public boolean jugar(int fila, int columna) {
        if (fila >= 0 && fila < 3 && columna >= 0 && columna < 3 && tablero[fila][columna] == '-') {
            tablero[fila][columna] = jugadorActual;
            return true;
        }
        return false;
    }

    public boolean comprobarGanador() {
        return (comprobarFilas() || comprobarColumnas() || comprobarDiagonales());
    }

    private boolean comprobarFilas() {
        for (int i = 0; i < 3; i++) {
            if (tablero[i][0] == tablero[i][1] && tablero[i][1] == tablero[i][2] && tablero[i][0] != '-') {
                return true;
            }
        }
        return false;
    }

    private boolean comprobarColumnas() {
        for (int i = 0; i < 3; i++) {
            if (tablero[0][i] == tablero[1][i] && tablero[1][i] == tablero[2][i] && tablero[0][i] != '-') {
                return true;
            }
        }
        return false;
    }

    private boolean comprobarDiagonales() {
        return ((tablero[0][0] == tablero[1][1] && tablero[1][1] == tablero[2][2] && tablero[0][0] != '-')
                || (tablero[0][2] == tablero[1][1] && tablero[1][1] == tablero[2][0] && tablero[0][2] != '-'));
    }

    public boolean tableroLleno() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    public void cambiarJugador() {
        jugadorActual = (jugadorActual == 'X') ? 'O' : 'X';
    }

    public void iniciar() {
        enviarMensaje("¡Bienvenido al Tres en Raya!", salidaJugador1);
        enviarMensaje("¡Bienvenido al Tres en Raya!", salidaJugador2);

        while (!comprobarGanador() && !tableroLleno()) {
            enviarMensaje("TABLERO", salidaJugador1);
            enviarMensaje("TABLERO", salidaJugador2);
            enviarMensaje("Jugador " + jugadorActual + ", es tu turno. Introduce la fila y la columna (ejemplo: 1 1): ", jugadorActual == 'X' ? salidaJugador1 : salidaJugador2);

            try {
                String jugada = jugadorActual == 'X' ? entradaJugador1.readLine() : entradaJugador2.readLine();
                String[] coordenadas = jugada.split(" ");
                int fila = Integer.parseInt(coordenadas[0]);
                int columna = Integer.parseInt(coordenadas[1]);

                if (!jugar(fila, columna)) {
                    enviarMensaje("Movimiento inválido. Inténtalo de nuevo.", jugadorActual == 'X' ? salidaJugador1 : salidaJugador2);
                    continue;
                }

                if (comprobarGanador()) {
                    enviarMensaje("Ganador: Jugador " + jugadorActual, salidaJugador1);
                    enviarMensaje("Ganador: Jugador " + jugadorActual, salidaJugador2);
                } else if (tableroLleno()) {
                    enviarMensaje("Empate. El juego ha terminado.", salidaJugador1);
                    enviarMensaje("Empate. El juego ha terminado.", salidaJugador2);
                }

                cambiarJugador();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            jugador1Socket.close();
            jugador2Socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
