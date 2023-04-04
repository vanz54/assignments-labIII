import java.net.ServerSocket;
import java.util.concurrent.*;
public class Server {
    public static void main(String[] args) throws Exception { //la port sulla quale il client apre la connessione e il server attiva il servizio devono coincidere
        try (ServerSocket listener = new ServerSocket(1609)) { //welcome socket del server, apro listening socket su 1609
            System.out.println("The hero is entering in the Dungeon...");
            ExecutorService pool = Executors.newFixedThreadPool(20);
        while (true) { 
            pool.execute(new ServerRunnable(listener.accept())); 
            //la richiesta di connessione viene accettata e viene creato e restituiro il socket dedicato per l'interazione col client al runnable
            //è bloccante
            }
        }
    }
}