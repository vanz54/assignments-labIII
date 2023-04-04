import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class MainCompressione {
    public static void main(String[] args){
        ExecutorService service = Executors.newFixedThreadPool(10);
        for(String dirName : args){
            File f = new File(dirName);
            File[] directoryListing = f.listFiles(); 
            if(directoryListing != null) {
                for(File file_figlio : directoryListing) {
                    String filePath = file_figlio.getAbsolutePath();
                    Path pathinput = Paths.get(filePath);
                    Path pathoutput = Paths.get(filePath + ".gz");
                  service.execute(new TaskZippatore(pathinput, pathoutput));
                }
            }
        }
    }
}