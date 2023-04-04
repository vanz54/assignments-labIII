import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class MainCopieBuffer2048 {
    public static void main (String args[]) throws IOException {
        if (args.length != 1) {
           System.err.println("Passa il file da copiare");
           return;
           }
        String fileToCopy = args[0];

        //FileChannel con buffer indiretti ----------------------------------------------------------------------------
        long start1 = System.currentTimeMillis();
        ReadableByteChannel source = Channels.newChannel(new FileInputStream(args[0]));
        WritableByteChannel destination =  Channels.newChannel(new FileOutputStream((args[0]) + "_copia_indirectBuffer"));
        channelCopyIndirect(source,destination);
        source.close();
        destination.close();
        long end1 = System.currentTimeMillis();
        System.out.println("Il metodo di copia del file " + args[0] + " con BUFFER INDIRETTI ci impiega " + (end1-start1) + " ms");

        //FileChannel con buffer diretti ------------------------------------------------------------------------------
        long start2 = System.currentTimeMillis();
        ReadableByteChannel src = Channels.newChannel(new FileInputStream(args[0]));
        WritableByteChannel dest =  Channels.newChannel(new FileOutputStream((args[0]) + "_copia_directBuffer"));
        channelCopyDirect(src,dest);
        src.close();
        dest.close();
        long end2 = System.currentTimeMillis();
        System.out.println("Il metodo di copia del file " + args[0] + " con BUFFER DIRETTI ci impiega " + (end2-start2) + " ms");

        //FileChannel utilizzando l'operazione transferTo() -----------------------------------------------------------
        long start3 = System.currentTimeMillis();
        RandomAccessFile fromFile = new RandomAccessFile(fileToCopy, "r");
        FileChannel fromChannel = fromFile.getChannel();
        RandomAccessFile toFile = new RandomAccessFile(args[0] + "_copia_toTransfer", "rw");
        FileChannel toChannel = toFile.getChannel();
        long position = 0;
        long count = fromChannel.size();
        toChannel.transferFrom(fromChannel, position, count);
        fromFile.close();
        toFile.close();
        long end3 = System.currentTimeMillis();
        System.out.println("Il metodo di copia del file " + args[0] + " con TRANSFER_TO ci impiega " + (end3-start3) + " ms");

        //Buffered Stream di I/O --------------------------------------------------------------------------------------
        long start4 = System.currentTimeMillis();
        BufferedInputStream fin = new BufferedInputStream(new FileInputStream(fileToCopy));
        BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(args[0] + "_copia_bufferedStream"));
        int i;
        do {
          i = fin.read();
          if (i != -1)
            fout.write(i);
        } while (i != -1);
        fin.close();
        fout.close();
        long end4 = System.currentTimeMillis();
        System.out.println("Il metodo di copia del file " + args[0] + " con BUFFERED STREAM ci impiega " + (end4-start4) + " ms");

        //Stream letto in un byte-array gestito dal programmatore -----------------------------------------------------
        long start5 = System.currentTimeMillis();
        InputStream inputStream = new FileInputStream(args[0]);
        OutputStream outputStream = new FileOutputStream(args[0]+"_copia_StreamWithByteArray");
        byte[] buffer = new byte[2048];
        while (inputStream.read(buffer) != -1) {
            int k=0;
            while (k<buffer.length){
                outputStream.write(buffer[k]);
                k=k+1;
            }
            k=0;
        }
        inputStream.close();
        outputStream.close();
        long end5 = System.currentTimeMillis();
        System.out.println("Il metodo di copia del file " + args[0] + " con STREAM IN BYTE ARRAY ci impiega " + (end5-start5) + " ms");

    }
//-------------------------------------------------------------------------------------------------------------------
//metodo per copiare con i buffer indiretti con buffer di 2048 byte
private static void channelCopyIndirect (ReadableByteChannel src, WritableByteChannel dest) throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate (2048);
    while (src.read (buffer) != -1) {
     buffer.flip();
     while (buffer.hasRemaining()) {
     dest.write (buffer); }
     buffer.clear();
    }
}

//metodo per copiare con i buffer diretti con buffer di 2048 byte
private static void channelCopyDirect (ReadableByteChannel src, WritableByteChannel dest) throws IOException {
    ByteBuffer buffer = ByteBuffer.allocateDirect (2048);
    while (src.read (buffer) != -1) {
     buffer.flip();
     while (buffer.hasRemaining()) {
     dest.write (buffer); }
     buffer.clear();
    }
}

}

//COMMENTO OUTPUT:
/*
--------------DIMENSIONE BUFFER = 2048--------------
-file da 10M
Il metodo di copia del file .\file di test\file_10M con BUFFER INDIRETTI ci impiega 482 ms
Il metodo di copia del file .\file di test\file_10M con BUFFER DIRETTI ci impiega 89 ms
Il metodo di copia del file .\file di test\file_10M con TRANSFER_TO ci impiega 11 ms
Il metodo di copia del file .\file di test\file_10M con BUFFERED STREAM ci impiega 404 ms
Il metodo di copia del file .\file di test\file_10M con STREAM IN BYTE ARRAY ci impiega 63520 ms

-file da 10K
Il metodo di copia del file .\file di test\file_10K con BUFFER INDIRETTI ci impiega 101 ms
Il metodo di copia del file .\file di test\file_10K con BUFFER DIRETTI ci impiega 1 ms
Il metodo di copia del file .\file di test\file_10K con TRANSFER_TO ci impiega 10 ms
Il metodo di copia del file .\file di test\file_10K con BUFFERED STREAM ci impiega 9 ms
Il metodo di copia del file .\file di test\file_10K con STREAM IN BYTE ARRAY ci impiega 65 ms

-file da 1M
Il metodo di copia del file .\file di test\file_1M con BUFFER INDIRETTI ci impiega 136 ms
Il metodo di copia del file .\file di test\file_1M con BUFFER DIRETTI ci impiega 9 ms
Il metodo di copia del file .\file di test\file_1M con TRANSFER_TO ci impiega 11 ms
Il metodo di copia del file .\file di test\file_1M con BUFFERED STREAM ci impiega 68 ms
Il metodo di copia del file .\file di test\file_1M con STREAM IN BYTE ARRAY ci impiega 5900 ms

-file da 1K
Il metodo di copia del file .\file di test\file_1K con BUFFER INDIRETTI ci impiega 95 ms
Il metodo di copia del file .\file di test\file_1K con BUFFER DIRETTI ci impiega 4 ms
Il metodo di copia del file .\file di test\file_1K con TRANSFER_TO ci impiega 8 ms
Il metodo di copia del file .\file di test\file_1K con BUFFERED STREAM ci impiega 0 ms
Il metodo di copia del file .\file di test\file_1K con STREAM IN BYTE ARRAY ci impiega 24 ms

-file da 80M
Il metodo di copia del file .\file di test\file_80M con BUFFER INDIRETTI ci impiega 964 ms
Il metodo di copia del file .\file di test\file_80M con BUFFER DIRETTI ci impiega 875 ms
Il metodo di copia del file .\file di test\file_80M con TRANSFER_TO ci impiega 115 ms
Il metodo di copia del file .\file di test\file_80M con BUFFERED STREAM ci impiega 3091 ms
Il metodo di copia del file .\file di test\file_80M con STREAM IN BYTE ARRAY ci impiega 1607112 ms

-file da 40M
Il metodo di copia del file .\file di test\file_40M con BUFFER INDIRETTI ci impiega 612 ms
Il metodo di copia del file .\file di test\file_40M con BUFFER DIRETTI ci impiega 430 ms
Il metodo di copia del file .\file di test\file_40M con TRANSFER_TO ci impiega 57 ms
Il metodo di copia del file .\file di test\file_40M con BUFFERED STREAM ci impiega 2414 ms
Il metodo di copia del file .\file di test\file_40M con STREAM IN BYTE ARRAY ci impiega 452284 ms

*/

