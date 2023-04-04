import java.util.concurrent.*;


public class UfficioPostale {
	public static final int numSportelli = 4;
	public static final int kPersone = 12; // Numero massimo di persone in salaPiccola
	public static final int totPersone = 100; // Persone totali del giorno che vanno alle poste
	public static final int terminationDelay = 5000; //Tempo massimo di attesa per la terminazione del pool
	public static final int delayTask = ThreadLocalRandom.current().nextInt(100, 500);
	   // Metto un delay pseudorandomico tra i task, cambia ad ogni run del programma

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<Runnable> salaGrande = new LinkedBlockingQueue<Runnable>();
		BlockingQueue<Runnable> salaPiccola = new ArrayBlockingQueue<Runnable>(kPersone); // Capacità massima 12 persone
	    // Creo un pool di thread con al massimo numSportelli thread
	    ExecutorService pool = new ThreadPoolExecutor(
	    		numSportelli, 
	    		numSportelli, 
	    		terminationDelay, 
	    		TimeUnit.MILLISECONDS,
	    		salaPiccola, 
	    		new ThreadPoolExecutor.AbortPolicy() 
	    );

		for (int j = 1; j <= totPersone; j++) 
			salaGrande.put(new PersonaPosta(j));
			// Metto le 100 persone nella coda della salaGrande partendo dalla prima arrivando alla 100esima
            // Parto dal numero i=1 perché non mi piace in questo caso iniziare dalla 'Persona zeresima', tanto cambia poco

	    for(int i = 1; i <= totPersone; i++) { 
	    	try {
				pool.execute(salaGrande.take()); // La TPool prende i task dalla salaGrande e li gestisce autonomamente
			}
	    	catch (RejectedExecutionException e) {
	    		System.err.printf("Persona %d ha trovato la sala piena\n", i);
	    	}
	    	// Attendo un intervallo di tempo prima di creare un nuovo task
	    	try {
				Thread.sleep(delayTask);				
			}
	    	catch (InterruptedException e) {
	    		System.err.println("Interruzione su sleep.");
	    		System.exit(1);
	    	}
	    }

        pool.shutdown(); // Terminazione threadpool
	    try {
	    	if (!pool.awaitTermination(terminationDelay, TimeUnit.MILLISECONDS))
	    		pool.shutdownNow();
	    }
	    catch (InterruptedException e) {pool.shutdownNow();}
		System.err.println("Chusura sportelli, fine giornata alle poste.");
	}
}