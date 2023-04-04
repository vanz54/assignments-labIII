import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.stream.JsonReader;

public class MainBanca {
    public static final int terminationDelay = 5000;
    public static void main(String[] args) {
    //inizializzo tpool e mappa 
    ExecutorService pool = Executors.newFixedThreadPool(10);
    ConcurrentHashMap<String, AtomicInteger> map = new ConcurrentHashMap<String, AtomicInteger>();
    //leggo un contocorrente e lo passo ad un thread della tpool
    JsonReader reader;
    try {
    reader = new JsonReader(new FileReader("accounts.json"));
    reader.beginArray(); //entro nell'array esterno che contiene tutti i conti correnti
    while(reader.hasNext()){ //finché c'è un cc
        reader.beginObject(); //gli dico che inizia il contocorrente
        String stringacontocorrente = "";
        while(reader.hasNext()){ 
        String nameORrecord = reader.nextName();
        if("owner".equals(nameORrecord)){
            reader.skipValue();
        } else if("records".equals(nameORrecord)){
                reader.beginArray();
                while(reader.hasNext()){
                    reader.beginObject();
                    while(reader.hasNext()){
                        String dateORtransaction = reader.nextName();
                        if("date".equals(dateORtransaction)){
                            reader.skipValue();
                        } else if("reason".equals(dateORtransaction)){
                            stringacontocorrente = stringacontocorrente + " " + reader.nextString(); 
                        }
                    }
                    reader.endObject();
                }
                reader.endArray();
         } 
        }
        reader.endObject();
        pool.execute(new ReasonsCounter(stringacontocorrente, map));
    }
    reader.endArray();
    reader.close();
    } catch (FileNotFoundException e) { System.err.print(e.getMessage());
    } catch (IOException e) { System.err.print(e.getMessage());}

    pool.shutdown(); //terminazione threadpool
    try {
      if (!pool.awaitTermination(terminationDelay, TimeUnit.MILLISECONDS))
        pool.shutdownNow();
    }
    catch (InterruptedException e) {
      pool.shutdownNow();
    }

    //stampa sul file di output le coppie causale : numero causali
    
    String nome_file_output = "quantitativoCausali.txt";
    String encoding = "UTF-8";
      try{
      PrintWriter writer = new PrintWriter(nome_file_output, encoding);
      for(String c: map.keySet())
      writer.println(c + " : " + map.get(c));
      writer.close();

      }
      catch (IOException e){
        System.out.println("Errore stampa file.");
        e.printStackTrace();
      }
    }
}
/*
 *   map.put("BONIFICO",0);
  map.put("ACCREDITO",0);
  map.put("BOLLETTINO",0);
  map.put("F24",0);
  map.put("PAGOBANCOMAT",0);
 */