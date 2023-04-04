import java.util.concurrent.ThreadLocalRandom;

public class Dragon {
    private int dragonLife;
    public Dragon() {
        this.dragonLife = ThreadLocalRandom.current().nextInt(30, 80);;;
    }
    public int getDragonLife(){
        return this.dragonLife;
    }
    public int fightDragon(){
        int dragonDecreasedK = ThreadLocalRandom.current().nextInt(0, this.dragonLife+1);;
        this.dragonLife = this.dragonLife - dragonDecreasedK;
        return this.dragonLife;
    }
}
