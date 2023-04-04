import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
/*
 * Echo Server per ritrasmissione dei messaggi
 */
public class EchoServer {
    public static void main(String[] args) throws IOException {

    ServerSocketChannel serverChannel = ServerSocketChannel.open();
    serverChannel.socket().bind(new InetSocketAddress(1609)); //apro e ricavo il socket associato
    serverChannel.configureBlocking(false);

        Selector selector = Selector.open(); //apro selettore e registro il server channel per accettare richieste
        serverChannel.register(selector, SelectionKey.OP_ACCEPT,null);

        print("EchoServer online");
        Integer connectionsActive = 0; //tengo traccia delle connessioni e disconnessioni

        while (true) { //pattern generale slide 45
           int readyChannels = selector.selectNow();
           if(readyChannels==0) continue;
           
           Set<SelectionKey> selectedKeys = selector.selectedKeys();
           Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

           while(keyIterator.hasNext()){ //finché c'è una chiave
            SelectionKey key = keyIterator.next(); //la prende
            keyIterator.remove();
            try{
            if(key.isAcceptable()){ //va nel readySet delle keys e verifica se c'è un evento di accettazione della connessione
                ServerSocketChannel server = (ServerSocketChannel) key.channel(); //channel per accettare connessione
                SocketChannel channelClient = server.accept(); //accetta una connessione fatta da un client
                channelClient.configureBlocking(false);
                channelClient.register(selector, SelectionKey.OP_READ); //registra questo channel appena accettato in modalità lettura
                print("Connection accepted: " + channelClient.getRemoteAddress());
                print("Connection active: " + (connectionsActive+=1).toString());
                
            } else if (key.isReadable()) { //leggo se c'è un channel pronto in lettura
                SocketChannel serverIn = (SocketChannel) key.channel(); //channel che ci fornirà la parola
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                serverIn.read(buffer); //leggo la parola mandata dal client nel buffer del server
                buffer.flip();
                byte[] array = new byte[buffer.limit()]; //byte array fin dove ho dati "utili"
                buffer.get(array); //trasferisco i byte nell'"array" di destinazione
                String msg = new String(array); //messaggio letto dal client
                print("received from EchoClient " + serverIn.getRemoteAddress() + " : " + msg);
                
                if(msg.equals("exit")) {
                    key.cancel();
                    serverIn.close(); //se il msg=exit chiudo tutto e diminuisco il numero di connessioni
                    print("Connection active: " + (connectionsActive-=1).toString());
                }
                else { //se !=exit mando il channel appena usato in lettura registrandolo per la scrittura
                       //con attachment il messaggio che devo rimandare al client 
                    serverIn.register(selector, SelectionKey.OP_WRITE, msg + " echoed by server ");
                }

            } else if (key.isWritable()) {  //scrivo al client se c'è un channel pronto in scrittura
                SocketChannel serverOut = (SocketChannel) key.channel(); 
                String reply = (String) key.attachment(); //la risposta sarà direttamente l'attachment del channel grazie all'operazione fatta prima
                ByteBuffer bbEchoAnsw = ByteBuffer.wrap(reply.getBytes()); //wrappa il byte array in un buffer
                serverOut.write(bbEchoAnsw); //scrive i byte del buffer nel channel, li manda al client
                print("sending to EchoClient " + serverOut.getRemoteAddress() + " : " + reply);
                if (!bbEchoAnsw.hasRemaining()) {
                    bbEchoAnsw.clear();
                    serverOut.register(selector, SelectionKey.OP_READ); //lo riutilizzo in lettura una volta scritto
                }
            }  

            } catch (IOException e) {   
                e.printStackTrace();
                key.channel().close();
                key.cancel();
            }
           }
        }
    }

    private static void print(String str) {    
        System.out.println(str);
    }

}