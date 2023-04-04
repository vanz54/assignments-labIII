import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;

public class TimeServer {
    public static void main(String[] args) throws InterruptedException {
        //manda data e ora ogni intervallo regolare
        try (MulticastSocket ms = new MulticastSocket();) {
            //metto ms nel tryw/res perchè dava warning
            InetAddress group = InetAddress.getByName("226.226.226.226"); //usa 226.226.226.226

            int delay = 3000; //intervallo di 3 secondi
            while (true) {
                //prende la data e la converte in una stringa e poi in un buffer da mandare nel pacchetto
                Date date = new Date();
                String dateString = date.toString();
                byte[] buf = dateString.getBytes();

                DatagramPacket datePacket = new DatagramPacket(buf, buf.length, group, 5555);
                ms.send(datePacket);

                //stampa la data inviata e aspetta l'intervallo
                System.out.println(dateString);
                Thread.sleep(delay);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}