import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ReasonsCounter implements Runnable{
    private String stringaCC;
    public ConcurrentHashMap<String, AtomicInteger> map; 
    public ReasonsCounter(String stringaCC, ConcurrentHashMap<String, AtomicInteger> mappa){
        this.stringaCC = stringaCC;
        this.map = mappa;
    }

    public void run(){
        try (Scanner scanner = new Scanner(stringaCC)) {
            scanner.useDelimiter(" ");
            while(scanner.hasNext()) {
                String myReason = scanner.next();
                try{
                    AtomicInteger myValue = map.get(myReason);
                    myValue.incrementAndGet();
                    map.put(myReason, myValue);
                }catch (NullPointerException e){ 
                    AtomicInteger myValue = new AtomicInteger(1);
                    map.put(myReason, myValue);
                } 
              }  
            }
        }
}
    



