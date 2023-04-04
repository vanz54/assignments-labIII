import java.io.*;
import java.net.*;
import java.text.DecimalFormat;

public class PingClient {
    public static void main(String[] args) {
      if (args.length < 2) {
        System.out.println("Usage: java PingClient <hostname> <port>");
        return;
    }
    String hostName = args[0];
    int port = 0;
    try{
      port = Integer.parseInt(args[1]);
      if( port < 1024 || port > 65536 ) {
          System.err.println("ERR -arg 2");
          return;
      }
    } catch (NumberFormatException e) {
        System.err.println("ERR -arg 2");
        return;
    }

        try(DatagramSocket socket = new DatagramSocket(0)){
          InetAddress host = InetAddress.getByName(hostName);
          System.out.println("PingClient online");
          System.out.println("PingClient connesso all'host " + hostName + " sulla porta " + port);
          int packageReceived = 0;
          int packageLost = 0;
          long minRTT = 2000;
          long maxRTT = 0;
          long avgRTT = 0;
          float avgResult = 0;
          socket.setSoTimeout(2000); //se dopo 2 secondi non ricevo risposta

          for(int seqno = 0; seqno < 10 ; seqno++){
            long timestamp = System.currentTimeMillis();

            try {
            byte[] buffer = ("PING " + String.valueOf(seqno) + " " + timestamp).getBytes("US-ASCII");

            DatagramPacket request = new DatagramPacket(buffer, buffer.length, host, port);
            DatagramPacket response = new DatagramPacket(new byte[1024], 1024);

            socket.send(request);
            long rttStart = System.currentTimeMillis();

            socket.receive(response);
            long rttFinish = System.currentTimeMillis();
            long RTT = rttFinish-rttStart;

            //calcolo e tengo traccia di minRTT, maxRTT e avgRTT
            if(minRTT>RTT && maxRTT<minRTT){
              minRTT = RTT;
              maxRTT = RTT;
              avgRTT += RTT;
            } else if(minRTT>RTT){
              minRTT = RTT;
              avgRTT += RTT;
            } else if(maxRTT<RTT){
              maxRTT = RTT;
              avgRTT += RTT;
            }else if(minRTT<RTT && maxRTT>RTT){
              avgRTT += RTT; 
            }

            System.out.println("PING " + String.valueOf(seqno) + " " + timestamp + " RTT: " + (RTT) + " ms");
            packageReceived += 1; 
         
            } catch (SocketTimeoutException s) { //se non ho ricevuto risposta entro 2 secondi
              System.out.println("PING " + String.valueOf(seqno) + " " + timestamp + " RTT: *");
              packageLost += 1;
              continue;
            } catch (IOException e) {
              e.printStackTrace();
              break;
            }
          }

          //creo la media con due cifre dopo la ,
          avgResult = ((float)avgRTT)/((float)packageReceived);
          DecimalFormat dfrmt = new DecimalFormat();
          dfrmt.setMaximumFractionDigits(2);

          System.out.println("---PING STATISTICS---");
          System.out.println("10 packets transmitted, " + packageReceived + " packets received, " + packageLost*10 + "% packet loss");
          System.out.println("round-trip (ms) min/avg/max = " + minRTT + "/" + dfrmt.format(avgResult) + "/" + maxRTT);
        } catch (UnknownHostException e) {
						System.err.println("ERR -arg 1");
						return;
				} catch (IOException e) {
						e.printStackTrace();
				}
    }
}
