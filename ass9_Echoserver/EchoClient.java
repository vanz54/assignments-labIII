import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/*
 * Echo CLient per trasmissione di messaggi, ottiene risposte dal server
 */
public class EchoClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        String myLocalHost = "127.0.0.1";
        //mi connetto al server porta 1609
        try (SocketChannel myClient = SocketChannel.open(new InetSocketAddress(myLocalHost, 1609));) {
        print("Connecting to EchoServer...");
        
        Scanner sc = new Scanner(System.in); //per leggere input dalla tastiera
        boolean end = false; //flag di terminazione, in caso inserissi un 'exit'
        while(!end){
        print("\nEnter something from keyboard, 'exit' to end connection\n-> ");  
        String str = sc.nextLine(); 
        if(str.equals("exit")) end=true;

        //manda al server la stringa letta da tastiera
        //(devo mandarlo comunque al server il msg=exit altrimenti darebbe errore
        //lato server perché leggerebbe da un buffer vuoto)
        byte[] message = new String(str).getBytes(); //trasforma la stringa di input in byte array
        ByteBuffer buffer = ByteBuffer.wrap(message); //wrappa byte array in un buffer
        myClient.write(buffer); //scrive nel channel dal buffer
        print("sending: " + str);
        buffer.clear(); //libera il buffer dopo averlo mandato
        Thread.sleep(1500);
        
        //riceve risposta dal server echoata, che deve essere != exit 
        if(!str.equals("exit")){
        ByteBuffer reply = ByteBuffer.allocate(1024); //buffer di ricezione del client
        myClient.read(reply); //legge dal channel in un buffer reply
        reply.flip();
        print("receiving : " + new String(reply.array()).trim());
        reply.clear();
        }
    }
    print("Client closing..."); //ho ricevuto in input un exit quindi chiudo tutto
        myClient.close();
        sc.close();
    
    } catch (IOException e) {
          e.printStackTrace();
    }
    }


    private static void print(String str) {
        System.out.println(str);
    }

}