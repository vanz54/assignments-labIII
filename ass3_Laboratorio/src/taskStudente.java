public class taskStudente implements Runnable{ 
    public Tutor tutor;
    public int id; //ad es: lo 'studente 5' -> id=5
    public int k_accessi;

    public taskStudente(Tutor tutor, int id, int k_accessi){ //costruttore con tutor, id persona e numero di accessi
        this.tutor = tutor;
        this.id = id;
        this.k_accessi = k_accessi;
    }

    public void run() {

        for (int i = 0; i < this.k_accessi; i++) { //devo fare k accessi

            this.tutor.accediStudente(this.id); //uso il metodo per accedere, che contiene le caratteristiche richieste dall'esercizio
            //è sincronizzato

            try {
                Thread.sleep((long)(Math.random() * 3000)); //distanza in tempo tra due accessi del solito studente ( min 0, max 3 sec)
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}