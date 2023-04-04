import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class webLogST {
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(args[0]));
        ExecutorService Singletp = Executors.newSingleThreadExecutor();
        String line = bufferedReader.readLine();
        long start = System.nanoTime();
       
        while(line!=null){
        line = bufferedReader.readLine();
        Singletp.submit(new taskWebLog(line));
        }
        Singletp.shutdown();
        
        boolean finished = Singletp.awaitTermination(5, TimeUnit.MINUTES);
        if(finished)
            System.out.println("programma single threaded ci mette : " + (System.nanoTime() - start) + " nanosec");
        bufferedReader.close();
    }
}
    
