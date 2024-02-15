package main;

import clases.Client;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoTCPServerCentral {

    private static Client[] clientarray = new Client[2];

    private EchoTCPClient ec = new EchoTCPClient();
    
    private Socket connexio;

    public void ConnexioCentral() throws IOException {
        ServerSocket server = new ServerSocket(7879);
        Client c = null;
        // Esperem que es connecti un client
        System.out.println("Esperant connexio...");
        if(clientarray[0] == null){
            ec.ConectarCliente();
        } 
        connexio = server.accept();
        String ip = connexio.getInetAddress().getHostAddress();
        System.out.println("Client " + ip + " connectat.");
        if(clientarray[0] == null){
            c = new Client(1, true, ip);
            clientarray[0] = c;
        } else {
            c = new Client(2, false, ip);
            clientarray[1] = c;
        }
        server.close();
        
        
        
    }

    public Client[] getClientarray() {
        return clientarray;
    }
    
    public void cerrar() throws IOException{
        connexio.close();
        System.out.println("Connexió tancada");
    }

}
