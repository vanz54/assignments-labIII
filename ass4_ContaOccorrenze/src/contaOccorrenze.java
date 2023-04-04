import java.util.concurrent.*;
import java.io.*;

public class contaOccorrenze {
  public static final int nThread = 6; //imposto numero di thread utili
	public static final int terminationDelay = 5000;
    public static void main(String[] args) throws Exception {
      BlockingQueue<Runnable> coda = new LinkedBlockingQueue<Runnable>(); //implemento una coda ""infinita"" che uso nel tpool
      ConcurrentHashMap<Character, Integer> map = new ConcurrentHashMap<Character, Integer>(); //creo una concurrenthashmapp che associa caratteri (key) a interi (value)
	    ExecutorService pool = new ThreadPoolExecutor( //creazione threadpool
        nThread, 
        nThread, 
        terminationDelay, 
        TimeUnit.MILLISECONDS,
        coda, 
        new ThreadPoolExecutor.AbortPolicy() 
    );
    
    for(int i=0; i<args.length; i++){
      File file_in = new File (args[i]); //scorro le stringhe che passo da linea di comando utilizzando così i file in input
      pool.execute(new taskContatore(file_in,map)); //i file li passo al task assieme alla mappa e mando in esecuzione il tpool
    } 

    pool.shutdown(); //terminazione threadpool
    try {
      if (!pool.awaitTermination(terminationDelay, TimeUnit.MILLISECONDS))
        pool.shutdownNow();
    }
    catch (InterruptedException e) {
      pool.shutdownNow();
    }

    //stampa sul file di output le coppie carattere,occorrenze
    String nome_file_output = "output_occorrenze.txt";
    String encoding = "UTF-8";
      try{
      PrintWriter writer = new PrintWriter(nome_file_output, encoding);
      for(char c: map.keySet())
      writer.println(c + "," + map.get(c));
      writer.close();
      }
      catch (IOException e){
        System.out.println("Errore stampa file.");
        e.printStackTrace();
      }
  }
}
