public class taskProfessore implements Runnable{
    public Tutor tutor; 
    public int id;
    public int k_accessi;

    public taskProfessore(Tutor tutor, int id, int k_accessi){
        this.tutor = tutor;
        this.id = id;
        this.k_accessi = k_accessi;
    }

    public void run() {

        for (int i = 0; i < this.k_accessi; i++) {

            this.tutor.accediProfessore(this.id);

            try {
                Thread.sleep((long)(Math.random() * 3000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}