import java.net.*;

public class taskWebLog implements Runnable{
    private String lineaWeblog;
    public taskWebLog(String linea){
        this.lineaWeblog = linea;
    }
    public void run() {
    String[] parts = lineaWeblog.split(" - - ");
        try {
            InetAddress addr = InetAddress.getByName(parts[0]);
            System.out.println(addr.getHostName() + " - - " + parts[1]);  
        }
        catch (UnknownHostException e) {
            System.out.println(e);
        }
    }
}
