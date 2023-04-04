import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class webLogMT {
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(args[0]));
        ExecutorService Multitp = Executors.newFixedThreadPool(8);
        String line = bufferedReader.readLine();
        long start = System.nanoTime();

        while(line!=null){
        line = bufferedReader.readLine();
        Multitp.submit(new taskWebLog(line));
        }

        Multitp.shutdown();
        
        boolean finished = Multitp.awaitTermination(5, TimeUnit.MINUTES);
        if(finished)
            System.out.println("programma thread pool ci mette : " + (System.nanoTime() - start) + " nanosec");

        bufferedReader.close();
    }   
}
