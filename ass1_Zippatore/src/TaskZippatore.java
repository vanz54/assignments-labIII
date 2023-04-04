import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;

public class TaskZippatore implements Runnable {
    private Path fileinput; 
    private Path fileoutput; 

    public TaskZippatore(Path filein, Path fileout) {
        this.fileinput = filein;
        this.fileoutput = fileout;
    }

    public void run() {
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(Files.newOutputStream(fileoutput))) {
            byte[] allBytes = Files.readAllBytes(fileinput);
            gzipOutputStream.write(allBytes);
            System.out.println("Nome thread : " + Thread.currentThread());
            System.out.println("File zippato.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

