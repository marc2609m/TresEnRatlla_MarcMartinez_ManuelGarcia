package clases;


public class Client {
    
    private int id;
    private boolean host;
    private String ip;

    public Client(int id, boolean host, String ip) {
        this.id = id;
        this.host = host;
        this.ip = ip;
    }

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isHost() {
        return host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", host=" + host + ", ip=" + ip + '}';
    }

    
    
    
    
}
