import java.util.concurrent.*;

public class PersonaPosta implements Runnable { // Task ufficio postale
	// Tempo minimo di attesa per le operazioni del PersonaPosta. Operazione postale più corta possibile.
	public final int minDelay = 0;
	// Tempo massimo di attesa per le operazioni del PersonaPosta. Operazione postale più lunga possibile.
	public final int maxDelay = 1000;
	// Identificativo del PersonaPosta.
	public final int id;

	public PersonaPosta(int id) {this.id = id;} // Costruttore

	public void run() {
		System.out.printf("Persona %d va allo sportello per l'operazione postale\n", id); 
        // Delay pseudocasuale che assegno per l'operazione della persona, ossia quanto ci mette una persona a fare l'operazione
		int delay = ThreadLocalRandom.current().nextInt(minDelay, maxDelay + 1);
	    try {
			Thread.sleep(delay);
		}
	    catch (InterruptedException e) {
	    	System.err.println("Interruzione su sleep.");
	    }
	    System.out.printf("---Persona %d ha terminato l'operazione allo sportello mettendoci %d ms---\n", id, delay);
	}
}
