package main;

import java.io.*;
import java.net.*;

public class JuegoTresEnRaya extends Thread{
    private Socket player1Socket;
    private Socket player2Socket;

    public JuegoTresEnRaya(Socket player1Socket, Socket player2Socket) {
        this.player1Socket = player1Socket;
        this.player2Socket = player2Socket;
    }

    public void run() {
        try {
            BufferedReader player1In = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            PrintWriter player1Out = new PrintWriter(player1Socket.getOutputStream(), true);
            BufferedReader player2In = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
            PrintWriter player2Out = new PrintWriter(player2Socket.getOutputStream(), true);

            char[][] board = new char[3][3];
            initializeBoard(board);

            int currentPlayer = 1;
            boolean gameEnd = false;
            int moves = 0;

            while (!gameEnd && moves < 9) {
                int row, col;
                if (currentPlayer == 1) {
                    player1Out.println("YOUR_TURN");
                    row = Integer.parseInt(player1In.readLine());
                    col = Integer.parseInt(player1In.readLine());
                } else {
                    player2Out.println("YOUR_TURN");
                    row = Integer.parseInt(player2In.readLine());
                    col = Integer.parseInt(player2In.readLine());
                }

                if (isValidMove(board, row, col)) {
                    char symbol = (currentPlayer == 1) ? 'X' : 'O';
                    board[row][col] = symbol;
                    moves++;

                    if (checkWin(board, symbol)) {
                        gameEnd = true;
                        if (currentPlayer == 1) {
                            player1Out.println("WIN");
                            player2Out.println("LOSE");
                        } else {
                            player1Out.println("LOSE");
                            player2Out.println("WIN");
                        }
                    } else if (moves == 9) {
                        player1Out.println("DRAW");
                        player2Out.println("DRAW");
                    } else {
                        currentPlayer = (currentPlayer == 1) ? 2 : 1;
                    }
                } else {
                    if (currentPlayer == 1) {
                        player1Out.println("INVALID_MOVE");
                    } else {
                        player2Out.println("INVALID_MOVE");
                    }
                }
            }

            player1Socket.close();
            player2Socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeBoard(char[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }
    }

    private boolean isValidMove(char[][] board, int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == '-';
    }

    private boolean checkWin(char[][] board, char symbol) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) {
                return true; // Horizontal win
            }
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) {
                return true; // Vertical win
            }
        }
        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) {
            return true; // Diagonal win
        }
        if (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol) {
            return true; // Diagonal win
        }
        return false;
    }
}
