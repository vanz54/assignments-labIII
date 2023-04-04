public class Laboratorio { //in questo file viene attuata la gestione del laboratorio con le regole fornite dall'esercizio

    public static boolean isNumber(String str) { //metodo per verificare se un numero è effettivamente un numero
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException { //main del programma
        //passo il numero di studenti, tesisti e professori dalla linea di comando per iniziare
        
        //facendo dei controlli per evitare la generazione di errori:
        //se non passo il numero esatto di parametri
        if (args.length != 3) {
            System.out.println("Errore in input: uso corretto ---> n_studenti n_tesisti n_professori");
            System.exit(1);
        }
        //se non passo numeri
        if ( (!isNumber(args[0])) || (!isNumber(args[1])) || (!isNumber(args[2])) ) {
            System.out.println("Inserire tre valori interi come parametri");
            System.exit(1);
        }

        int n_studenti = Integer.parseInt(args[0]); //numero studenti passato come primo parametro
        int n_tesisti = Integer.parseInt(args[1]); //numero tesisti passato come secondo parametro
        int n_professori = Integer.parseInt(args[2]); //numero professori passato come terzo parametro

        int n_utenti = n_studenti + n_tesisti + n_professori; //utenti totali, ne voglio almeno 1 sennò non ha senso
        if (n_utenti <= 1){
            System.out.println("Troppa poca gente in laboratorio");
            System.exit(1);
        } 

        //genero un numero casuale per indicare gli accessi effettuati dagli utenti, da 1 a 10 per avere senso ed evitare n grossi
        int k_accessi = (int) (Math.random() * (10 - 1)) + 1;
        System.out.println("Apre il laboratorio!");
        System.out.println("Gli utenti eseguiranno " + k_accessi + " accessi");
        //creo il tutor con 20 computer come da richiesta dell'esercizio
        Tutor tutor = new Tutor(20);

        //mi creo tre array diversi di thread per ogni rispettiva classe di persone passando quante ce ne sono e riempendoli col for
        //impostando la priorità dei thread, dopo avergli passato il task, come studente < tesista < professore in ordine crescente
        //poi faccio partire il thread avendo prima settato la priorità

        Thread[] studenti = new Thread[n_studenti]; 
        for (int i = 0; i < n_studenti; i++) {
            studenti[i] = new Thread(new taskStudente(tutor, i, k_accessi));
            studenti[i].setPriority(Thread.MIN_PRIORITY); //priorità minima
            studenti[i].start();
        }

        Thread[] tesisti = new Thread[n_tesisti];
        for (int i = 0; i < n_tesisti; i++) {
            tesisti[i] = new Thread(new taskTesista(tutor, n_studenti + i, k_accessi));
            tesisti[i].setPriority(Thread.NORM_PRIORITY); //priorità media
            tesisti[i].start();
        }

        Thread[] professori = new Thread[n_professori];
        for (int i = 0; i < n_professori; i++) {
            professori[i] = new Thread(new taskProfessore(tutor, n_studenti + n_tesisti + i, k_accessi));
            professori[i].setPriority(Thread.MAX_PRIORITY); //priorità massima
            professori[i].start();
        }

        //aspetto che terminino tutti i thread che ho creato e inizializzato utilizzando join()

        for (int i = 0; i < n_studenti; i++)
            studenti[i].join();

        for (int i = 0; i < n_tesisti; i++)
            tesisti[i].join();

        for (int i = 0; i < n_professori; i++)
            professori[i].join();

        System.out.println("I " + n_utenti + " utenti hanno eseguito i " + k_accessi + " accessi!");
        System.out.println("Chiude il laboratorio!");
        System.exit(0);

    }

}