import java.io.PrintWriter;
import java.net.Socket;          
import java.util.Scanner;        
public class Client {
   public static void main(String[] args) throws Exception { 
 Scanner inputTastiera=null;
 Scanner inputFromServer=null;  
 int port = 1609;
 String host = "localhost";

 //try with resources sulla socket
 try (Socket clientSocket = new Socket(host, port)) { //client crea socket verso server, richiesta connessione e 3way handshake
    
  System.out.println("Enter numbers to continue with the game: | 1 -> hero fights the dragon | | 2 -> hero drinks a potion of regeneration | | 3 -> hero exits the dungeon |");
    
  inputTastiera = new Scanner(System.in); //scanner che scannerizza input da tastiera
  inputFromServer = new Scanner(clientSocket.getInputStream()); //ciò che riceve dal server
  PrintWriter outputToServer = new PrintWriter(clientSocket.getOutputStream(), true); //ciò che manderà al server
  
    boolean end=false;
    while (!end) {
      String lineUser = inputTastiera.nextLine(); // Prendo input da tastiera
      if (lineUser.contentEquals("exit")) { // Se inserisco exit esce
        outputToServer.println(lineUser);
        end=true; 
        continue;
      }
      outputToServer.println(lineUser); // La mando al server
      if(inputFromServer.hasNextLine()){
        while(inputFromServer.hasNextLine()) {
          String inpuString = inputFromServer.nextLine(); // Risposta del server
          System.out.println(inpuString);
        }
        continue;
      }
    }
   }
   finally {inputTastiera.close(); inputFromServer.close();
   }
 }
}


/* while con due+ stringhe
      System.out.println(input);
      String lineUser = scanner.nextLine(); // Prendo input da tastiera
      out.println(lineUser); // La mando al server
      while(in.hasNextLine()) {
        String inputfromServer = in.nextLine(); // Risposta del server
        System.out.println(inputfromServer);
 */

/*
 vecchio while
     while (!end) {
      String line = scanner.nextLine();  //mette su line ciò che legge
      if (line.contentEquals("3")) { //se inserisco 3 esce
          end=true; 
      }
      out.println(line); //se non avevo inserito 3 mando la linea letta sull'output verso il server
      String input = in.nextLine(); //input=ciò che ricevo dal server
      if (input.contentEquals("The hero LOST")){ //se l'eroe ha perso termina
          end = true;
      }
    }
 */