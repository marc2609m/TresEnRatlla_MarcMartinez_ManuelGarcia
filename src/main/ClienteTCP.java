package main;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClienteTCP {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int CENTRAL_SERVER_PORT = 7879;
    private static final int GAME_SERVER_PORT = 9999;

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            DatagramSocket socket = new DatagramSocket();

            System.out.println("Welcome to Tic Tac Toe!");

            while (true) {
                displayMenu();
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        createGame(socket);
                        break;
                    case 2:
                        joinGame(socket);
                        break;
                    case 3:
                        socket.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayMenu() {
        System.out.println("Menu:");
        System.out.println("1. Create a new game");
        System.out.println("2. Join a game");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void createGame(DatagramSocket socket) throws IOException {
        String message = "CREAR " + GAME_SERVER_PORT;
        sendUDPMessage(socket, message);
        System.out.println("Waiting for opponent...");
        joinGameTCP(socket);
    }

    private static void joinGame(DatagramSocket socket) throws IOException {
        String message = "UNIR-ME";
        sendUDPMessage(socket, message);
        joinGameTCP(socket);
    }

    private static void sendUDPMessage(DatagramSocket socket, String message) throws IOException {
        byte[] sendData = message.getBytes();
        InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, CENTRAL_SERVER_PORT);
        socket.send(sendPacket);

        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println("Server response: " + response);
    }

    private static void joinGameTCP(DatagramSocket socket) throws IOException {
        Socket gameSocket = new Socket(SERVER_ADDRESS, GAME_SERVER_PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(gameSocket.getInputStream()));
        PrintWriter out = new PrintWriter(gameSocket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Connected to game server.");

        while (true) {
            String message = in.readLine();
            if (message.equals("YOUR_TURN")) {
                System.out.println("Your turn. Enter row (0-2) and column (0-2) separated by space:");
                int row = scanner.nextInt();
                int col = scanner.nextInt();
                out.println(row);
                out.println(col);
            } else if (message.equals("MOVE_MADE")) {
                int row = Integer.parseInt(in.readLine());
                int col = Integer.parseInt(in.readLine());
                System.out.println("Opponent moved to: " + row + ", " + col);
            } else if (message.equals("WIN")) {
                System.out.println("Congratulations! You win!");
                break;
            } else if (message.equals("LOSE")) {
                System.out.println("You lose! Better luck next time.");
                break;
            } else if (message.equals("DRAW")) {
                System.out.println("It's a draw!");
                break;
            } else if (message.equals("INVALID_MOVE")) {
                System.out.println("Invalid move. Try again.");
            }
        }

        gameSocket.close();
    }
}

