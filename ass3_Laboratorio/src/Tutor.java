public class Tutor {
    public int n_computer; //numero postazioni nel laboratorio che si gestiranno in concorrenza
    public int utenti_on;  //numero degli utenti presenti, quelli collegati alla postazione
    public Computer[] laboratorio;  //array di computer, ossia il laboratorio stesso
    public int n_professori;  //numero professori presenti nel laboratorio

    private static final Object myLock = new Object(); //devo utilizzare una mia Lock in quanto altrimenti avrei dovuto fare i metodi degli utenti
    //tutti synchronized non riuscendo ad implementare bene la sincronizzazione, in quanto mettendo synchronized solo un thread alla volta avrebbe potuto accedere a tutto il 
    //metodo, permettendo quindi magari a un solo studente di accedere anche se le posizione fossero state di più
    
    public Tutor(int n_computer){ //gli passo le postazioni che devo gestire in concorrenza (20 nel mio esercizio)
        this.n_computer = n_computer;
        this.utenti_on = 0; //parto con 0 utenti e 0 professori nel laboratorio
        this.n_professori =0;
        laboratorio = new Computer[n_computer]; //creo array di computer lungo quanti sono i computer stessi
        for (int i = 0; i < this.laboratorio.length; i++) {
            this.laboratorio[i] = new Tutor.Computer(i); //per ogni elemento dell'array laboratorio creo un nuovo computer iesimo
        }                                   //usando la class Computer sotto

    }

    public void accediStudente(int userID) { //metodo per far accedere lo studente al laboratorio
        int n_postazione = 0; //inizializzo la postazione

        synchronized (myLock) { //blocco sincronizzato eseguibile uno alla volta dai thread
            while (this.n_professori > 0 || this.utenti_on == this.laboratorio.length) {
                try { //finchè non ci sono le condizioni per accedere come studento mi sospendo sulla mia Lock
                    myLock.wait();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
            this.utenti_on++;
            for (int i = 0; i < this.laboratorio.length; i++) {
                if (this.laboratorio[i].isFree()) { //appena trovo una postazione libera piazzo lo studente
                    this.laboratorio[i].aggiungiProprietario(userID);
                    n_postazione = this.laboratorio[i].id;
                    break;
                }
            }
            myLock.notifyAll();
        }

        System.out.println("-> Lo studente " + userID + " ha preso il pc " + this.laboratorio[n_postazione].id);

        try {
            Thread.sleep((long)(Math.random() * 5000)); //randomizzo quanto 'sta' uno studente nel laboratorio, da 0 a max 5 secondi
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        System.out.println("<- Lo studente " + userID + " ha lasciato libero il pc " + this.laboratorio[n_postazione].id);


        synchronized (myLock) {
            this.laboratorio[n_postazione].rimuoviProprietario();
            this.utenti_on--; //quando ha finito ce lo levo
            myLock.notifyAll();
        }

    }

    public void accediTesista(int userID) {
        int n_postazione = 0;

        synchronized (myLock) {
            while (this.n_professori > 0 || !this.laboratorio[userID % this.n_computer].isFree()) {
                try {
                    myLock.wait();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
            this.utenti_on++;
            this.laboratorio[userID % this.n_computer].aggiungiProprietario(userID);
            n_postazione = userID % this.n_computer;
            myLock.notifyAll();
        }

        System.out.println("--> Il tesista " + userID + " ha preso il pc " + n_postazione);

        try {
            Thread.sleep((long)(Math.random() * 4000)); //il tesista sta dentro massimo 4 secondi
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        System.out.println("<-- Il tesista " + userID + " ha liberato il pc " + n_postazione);

        synchronized (myLock) {
            this.laboratorio[userID % this.n_computer].rimuoviProprietario();
            this.utenti_on--;
            myLock.notifyAll();
        }

    }

    public void accediProfessore(int userID) {
        synchronized (myLock) {
            while (this.utenti_on > 0) {
                try {
                    myLock.wait();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
            this.n_professori++;
            myLock.notifyAll();
        }


        System.out.println("---> Il professore " + userID + " e' entrato nel laboratorio");

        try {
            Thread.sleep((long)(Math.random() * 3000)); //il prof sta dentro massimo 3 secondi
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("<--- Il professore " + userID + " e' uscito dal laboratorio");

        synchronized (myLock) {
            this.n_professori--;
            myLock.notifyAll();
        }

    }

    public static class Computer { //singolo oggetto Computer
        public boolean libero; //questo computer libero oppure occupato
        public int utente; //id possessore del computer stesso
        public int id;  //identificativo pc

        public Computer(int id){ //gli passo un identificatore
            this.id = id;
            this.libero = true;  //il pc 'nasce' libero
            this.utente = -1;
        }

        public boolean isFree() {  //metodo per vedere se è libero, libero->true, occupato->false
            if (this.libero) return true;
            return false;
        }

        public void aggiungiProprietario(int newPossessor){ //metodo per dare un nuovo proprietario al pc
            assert (this.libero);
            this.libero = false;
            this.utente = newPossessor; //utete passa da -1 al nuovo utente
        }

        public void rimuoviProprietario(){ //metodo per rimuovere il proprietario
            this.libero = true;
            this.utente = -1;
        }
    }
}