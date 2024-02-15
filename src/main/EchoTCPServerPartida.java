package main;

import java.io.IOException;
import java.util.Scanner;

public class EchoTCPServerPartida {

    private EchoTCPServerCentral esc = new EchoTCPServerCentral();
    private static char[][] tresenrayarray = new char[3][3];
    private Scanner sc = new Scanner(System.in);

    public EchoTCPServerPartida() {
        //ConnexioPartida();
    }

    public void ConnexioPartida() {
        boolean continuar = true;
        do {
            try {
                esc.ConnexioCentral();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            if (esc.getClientarray()[0] != null && esc.getClientarray()[1] != null) {
                continuar = false;
            }
        } while (continuar);
    }

    public void inicializarArray() {
        for (int i = 0; i < tresenrayarray.length; i++) {
            for (int j = 0; j < tresenrayarray[i].length; j++) {
                tresenrayarray[i][j] = ' ';
            }
        }
    }

    public void inicarPartida() {
        boolean comprobar = true, comprobarTurno;
        int casillas = 0, casillasX = 0, casillasY = 0;
        inicializarArray();
        do {
            comprobarTurno = true;
            System.out.println(tresenrayarray[0][0] + "|" + tresenrayarray[0][1] + "|" + tresenrayarray[0][2]);
            System.out.println(tresenrayarray[1][0] + "|" + tresenrayarray[1][1] + "|" + tresenrayarray[1][2]);
            System.out.println(tresenrayarray[2][0] + "|" + tresenrayarray[2][1] + "|" + tresenrayarray[2][2]);
            do {
                casillas = turno();
                casillasY = obtenerSegundoDigito(casillas);
                casillasX = obtenerPrimerDigito(casillas);
                if (tresenrayarray[casillasX - 1][casillasY - 1] == 'X' || tresenrayarray[casillasX - 1][casillasY - 1] == 'O') {
                    System.out.println("Casilla ocupada");
                } else {
                    if (comprobarTurno()) {
                        tresenrayarray[casillasX - 1][casillasY - 1] = 'X';
                    } else {
                        tresenrayarray[casillasX - 1][casillasY - 1] = 'O';
                    }
                    comprobarTurno = false;
                }
            } while (comprobarTurno);
            for (int i = 0; i < tresenrayarray.length; i++) {
                for (int j = 0; j < tresenrayarray[i].length; j++) {
                    if (tresenrayarray[i][j] == ' ') {
                        comprobar = true;
                        break;
                    } else {
                        comprobar = false;
                    }
                }
            }
        } while (comprobar);
    }

    public int turno() {
        int casillas = 0;
        boolean comprobar = true;
        do {
            System.out.println("Escull las casillas (primer num (columnas 1 - 3), segon num (filas 1 - 3))");
            if (sc.hasNextInt()) {
                casillas = sc.nextInt();
                if (casillas >= 11 && casillas <= 33) {
                    comprobar = false;
                } else {
                    sc.nextLine();
                }
            } else {
                System.out.println("Solo numeros");
                sc.nextLine();
            }
        } while (comprobar);
        return casillas;
    }

    public boolean comprobarTurno() {
        int numX = 0, numO = 0;
        boolean empiezaX = true;
        for (int i = 0; i < tresenrayarray.length; i++) {
            for (int j = 0; j < tresenrayarray[i].length; j++) {
                if (tresenrayarray[i][j] == 'X') {
                    numX++;
                } else if (tresenrayarray[i][j] == 'O') {
                    numO++;
                }
            }
        }
        if (numX > numO) {
            empiezaX = false;
        } else if (numO > numX) {
            empiezaX = true;
        } else {
            empiezaX = true;
        }
        return empiezaX;
    }

    public boolean verificarVictoria() {
        boolean comprobarVictoria = false;

        return comprobarVictoria;
    }

    public boolean comprobarGanador() {
        boolean VictoriaX = false;

        return VictoriaX;
    }

    public void cerrarPartida() {
        try {
            esc.cerrar();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static int obtenerPrimerDigito(int numero) {
        return numero / 10;
    }

    public static int obtenerSegundoDigito(int numero) {
        return numero % 10;
    }

}
