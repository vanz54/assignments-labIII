import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class TimeClient {
    public static void main (String[] args) {
        //riceve data e ora dal gruppo di multicast
        try {
            //crea il gruppo multicast
            MulticastSocket socket = new MulticastSocket(5555);
            InetAddress group = InetAddress.getByName("226.226.226.226");
            InetSocketAddress address = new InetSocketAddress(group, 5555);
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(group);
            socket.joinGroup(address, networkInterface);

            for (int i = 0; i < 10; i++) { //riceve solo 10 volte la data
                DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
                socket.receive(response);
                String date = new String(response.getData(), 0, response.getLength());
                System.out.println("Pacchetto " + i + ": Data: " + date);

            }
            System.out.println("10 Pacchetti Data ricevuti.");
            //lascia gruppo e chiude le risorse
            socket.leaveGroup(address, networkInterface);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
