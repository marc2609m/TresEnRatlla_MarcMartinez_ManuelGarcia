package main;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class EchoTCPClient {

    Socket connexio;

    public void ConectarCliente() throws UnknownHostException, IOException {
        connexio = new Socket("192.168.21.2", 7879);
        System.out.println("Connexió amb el servidor establerta.");

    }

}
