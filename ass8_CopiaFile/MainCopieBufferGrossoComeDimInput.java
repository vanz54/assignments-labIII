import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class MainCopieBufferGrossoComeDimInput {
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
        long fileInDimension = ((FileChannel) source).size();
        channelCopyIndirect(source,destination,fileInDimension);
        source.close();
        destination.close();
        long end1 = System.currentTimeMillis();
        System.out.println("Il metodo di copia del file " + args[0] + " con BUFFER INDIRETTI ci impiega " + (end1-start1) + " ms");

        //FileChannel con buffer diretti ------------------------------------------------------------------------------
        long start2 = System.currentTimeMillis();
        ReadableByteChannel src = Channels.newChannel(new FileInputStream(args[0]));
        WritableByteChannel dest =  Channels.newChannel(new FileOutputStream((args[0]) + "_copia_directBuffer"));
        long fileInDim = ((FileChannel) src).size();
        channelCopyDirect(src,dest,fileInDim);
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
        int length = inputStream.available();
        byte[] buffer = new byte[length];
        while (inputStream.read(buffer) != -1) { };
        for(int j=0; j<buffer.length; j++){
            outputStream.write(buffer[j]);
        }
        inputStream.close();
        outputStream.close();
        long end5 = System.currentTimeMillis();
        System.out.println("Il metodo di copia del file " + args[0] + " con STREAM IN BYTE ARRAY ci impiega " + (end5-start5) + " ms");

    }
//-------------------------------------------------------------------------------------------------------------------
//metodo per copiare con i buffer indiretti
private static void channelCopyIndirect (ReadableByteChannel src, WritableByteChannel dest, long fileDimension) throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate ((int)fileDimension);
    while (src.read (buffer) != -1) {
     buffer.flip();
     while (buffer.hasRemaining()) {
     dest.write (buffer); }
     buffer.clear();
    }
}

//metodo per copiare con i buffer diretti
private static void channelCopyDirect (ReadableByteChannel src, WritableByteChannel dest, long fileDimension) throws IOException {
    ByteBuffer buffer = ByteBuffer.allocateDirect ((int)fileDimension);
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
--------------DIMENSIONE BUFFER = DIMENSIONE INPUT--------------
-file da 10M
Il metodo di copia del file .\file di test\file_10M con BUFFER INDIRETTI ci impiega 16 ms          
Il metodo di copia del file .\file di test\file_10M con BUFFER DIRETTI ci impiega 16 ms                                          Il metodo di copia del file .\file di test\file_10M con BUFFER DIRETTI ci impiega 16 ms
Il metodo di copia del file .\file di test\file_10M con TRANSFER_TO ci impiega 13 ms
Il metodo di copia del file .\file di test\file_10M con BUFFERED STREAM ci impiega 286 ms
Il metodo di copia del file .\file di test\file_10M con STREAM IN BYTE ARRAY ci impiega 45231 ms

-file da 10K
Il metodo di copia del file .\file di test\file_10K con BUFFER INDIRETTI ci impiega 101 ms   
Il metodo di copia del file .\file di test\file_10K con BUFFER DIRETTI ci impiega 0 ms                                               Il metodo di copia del file .\file di test\file_10K con BUFFER DIRETTI ci impiega 0 ms
Il metodo di copia del file .\file di test\file_10K con TRANSFER_TO ci impiega 11 ms
Il metodo di copia del file .\file di test\file_10K con BUFFERED STREAM ci impiega 3 ms
Il metodo di copia del file .\file di test\file_10K con STREAM IN BYTE ARRAY ci impiega 50 ms

-file da 1M
Il metodo di copia del file .\file di test\file_1M con BUFFER INDIRETTI ci impiega 357 ms
Il metodo di copia del file .\file di test\file_1M con BUFFER DIRETTI ci impiega 3 ms
Il metodo di copia del file .\file di test\file_1M con TRANSFER_TO ci impiega 4 ms
Il metodo di copia del file .\file di test\file_1M con BUFFERED STREAM ci impiega 39 ms
Il metodo di copia del file .\file di test\file_1M con STREAM IN BYTE ARRAY ci impiega 4512 ms

-file da 1K
Il metodo di copia del file .\file di test\file_1K con BUFFER INDIRETTI ci impiega 363 ms
Il metodo di copia del file .\file di test\file_1K con BUFFER DIRETTI ci impiega 11 ms
Il metodo di copia del file .\file di test\file_1K con TRANSFER_TO ci impiega 4 ms
Il metodo di copia del file .\file di test\file_1K con BUFFERED STREAM ci impiega 1 ms
Il metodo di copia del file .\file di test\file_1K con STREAM IN BYTE ARRAY ci impiega 3 ms

-file da 80M
Il metodo di copia del file .\file di test\file_80M con BUFFER INDIRETTI ci impiega 1024 ms
Il metodo di copia del file .\file di test\file_80M con BUFFER DIRETTI ci impiega 449 ms
Il metodo di copia del file .\file di test\file_80M con TRANSFER_TO ci impiega 114 ms
Il metodo di copia del file .\file di test\file_80M con BUFFERED STREAM ci impiega 1923 ms
Il metodo di copia del file .\file di test\file_80M con STREAM IN BYTE ARRAY ci impiega 404178 ms  

-file da 40M
Il metodo di copia del file .\file di test\file_40M con BUFFER INDIRETTI ci impiega 516 ms
Il metodo di copia del file .\file di test\file_40M con BUFFER DIRETTI ci impiega 175 ms
Il metodo di copia del file .\file di test\file_40M con TRANSFER_TO ci impiega 28 ms
Il metodo di copia del file .\file di test\file_40M con BUFFERED STREAM ci impiega 1001 ms
Il metodo di copia del file .\file di test\file_40M con STREAM IN BYTE ARRAY ci impiega 206229 ms

*/


