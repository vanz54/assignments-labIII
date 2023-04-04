import java.io.*;
import java.net.*;
import java.util.Random;

public class PingServer {
    public static void main(String[] args) throws InterruptedException { 
        int port = 0;
        int optionalSeed = 0;
        if (args.length < 1) {
            System.out.println("Usage: java PingServer <port> <[seed]>");
            return;
        }
        try { //controllo che la porta sia nel range corretto
            port = Integer.parseInt(args[0]);
            if( port < 1024 || port > 65536 ) {
                System.err.println("ERR -arg 1");
                return;
            }
        } catch (NumberFormatException e) { //controllo che la porta non sia una stringa a caso
            System.err.println("ERR -arg 1");
            return;
        }

        if (args.length > 1) { //se ho il parametro opzionale
            try{
                optionalSeed = Integer.parseInt(args[1]); //riassegna il seed
            } catch (NumberFormatException e) { 
                System.err.println("ERR -arg 2"); 
                return;
            }
        }

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("PingServer online");
            while (true) {
                try {
                    DatagramPacket request = new DatagramPacket(new byte[1024], 1024);
                    socket.receive(request); 

                    //il server determina se ignorare il pacchetto (perdita al 25%) o effettuarne l'eco
                    boolean probabilityLoss = new Random().nextInt(4)==0; //genera numero da 0 a 3
                    if(probabilityLoss) { //se il numero è 0 (probabilità 1/4) aspetta più di 2sec per far scattare timeout nel client e non mandare la richiesta
                        Thread.sleep(2222);
                        System.out.println(new String(request.getData()) + " ACTION: not sent"); 
                        continue;
                    }
                    else {
                    byte[] data = "serverResponse".getBytes("US-ASCII");
                    DatagramPacket response = new DatagramPacket(data, data.length, request.getAddress(), request.getPort());
                    //se decide di effettuare l'eco del PING (75%), il server attende un intervallo di tempo 
                    //casuale per simulare la latenza di rete
                    long delay = (long)(Math.random() * 100) + optionalSeed;
                    Thread.sleep(delay);
                    socket.send(response);
                    System.out.println(new String(request.getData()) + " ACTION: delayed "+ delay + " ms");                    
 
                    }
                } catch (IOException ex) { ex.printStackTrace(); } 
            } 
        } catch (IOException ex) { ex.printStackTrace(); }     
    }   
} 