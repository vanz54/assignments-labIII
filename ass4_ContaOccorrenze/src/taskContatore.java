import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

public class taskContatore implements Runnable {
    public File filein;
    public ConcurrentHashMap<Character, Integer> map; 
 public taskContatore(File nomefile, ConcurrentHashMap<Character, Integer> mappa){
   this.filein = nomefile;  //costruttore a cui passo file di input dove andrà a leggere i caratteri
   this.map = mappa;        //e la concurrenthashmap
 }

 public void run() {
    try{  //metto tutto in try catch, istanzio il file dove andrò a leggere i caratteri passandogli i file di input
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filein))) {
            char[] arr; 
            String line = bufferedReader.readLine(); //leggo la linea dal file di testo finché non incontra un 'a capo' o finisce il file
            while(line != null){ //finché c'è qualcosa da leggere va avanti
                String temp = line.toLowerCase(); //si crea una stringa temporanea della linea letta mettendo tutti i caratteri minuscoli
                //almeno evito mi faccia poi differenze nella mappa tra minuscole e maiuscole e l'output viene come vuole la consegna
                arr = temp.toCharArray(); //con l'array di caratteri inizializzato prima crea appunto un array di caratteri
                //mettendoci dentro i singoli caratteri c spezzettati dalla linea letta messa precedentemente in minuscolo
                for(char c:arr){ //scorro i caratteri dell'array<->linea_lette
                    if((c >= 'a' && c <= 'z')) //se sono all'interno dell'alfabeto
                    try{
                        map.put(c,map.get(c)+1); //se la chiave è già nella mappa incremento il valore di 1 
                    //uso la try catch perché se non fosse nella mappa darebbe errore che catcho e allora lo inizializzo a 1 il rispettivo valore
                    }catch (NullPointerException e){
                        map.put(c, 1); //se arrivo qua vuol dire che la chiave non era nella mappa
                    }   //e quindi inizializzo il valore a 1
                }
                line = bufferedReader.readLine();
            }
        }
    }catch(FileNotFoundException e){
            e.printStackTrace();
    } catch(IOException e){
        e.printStackTrace();
    }

 }
}