import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class TaskZIP implements Runnable  {
    private File filein;
    private File gzipFile;
    public TaskZIP(File nomefile, File nomezippato) {
        this.filein = nomefile;
        this.gzipFile = nomezippato;
    }

    public void run(){
            Compress(filein, gzipFile); 
            System.out.println("Nome thread : " + Thread.currentThread().getName());
            System.out.println("File zippato.");
    }

    private static void Compress(File fileinput, File filezippato) {
        try {
            FileInputStream fis = new FileInputStream(fileinput);
            FileOutputStream fout = new FileOutputStream(filezippato);
            GZIPOutputStream gzipOS = new GZIPOutputStream(fout);
            byte[] buffer = new byte[1024];
            int len;
            while((len=fis.read(buffer)) != -1){
                gzipOS.write(buffer, 0, len);
            }
            gzipOS.close();
            fout.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

