import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class MainZIP {
    public static void main(String[] args){
        ExecutorService service = Executors.newFixedThreadPool(10);
        for(String dirName : args){
            File f = new File(dirName);
            File[] directoryListing = f.listFiles(); 
            if(directoryListing != null) {
                for(File file_figlio : directoryListing) {
                  String path_file_input = file_figlio.getAbsolutePath();
                  File file_output = new File(path_file_input + ".gz");
                  service.execute(new TaskZIP(file_figlio, file_output));
                }
            }
        }
    }
}
