import java.util.concurrent.ThreadLocalRandom;

public class Hero {
    private int heroLife;
    private int heroPotion;
    public Hero() {
        this.heroLife = ThreadLocalRandom.current().nextInt(1, 100);;
        this.heroPotion = ThreadLocalRandom.current().nextInt(10, 20);;;
    }
    public int getHeroLife() {
        return this.heroLife;
    }
    public int getHeroPotion() {
        return this.heroPotion;
    }
    public int fightHero(){
        int heroDecreasedX = ThreadLocalRandom.current().nextInt(0, this.heroLife+1); //devo mettere +1 sennò a causa del range di valori in fondo farà 0 danno e non termina
        this.heroLife = this.heroLife - heroDecreasedX;
        return this.heroLife;
    }
        public int drinkPot(){
        int healthRegen = ThreadLocalRandom.current().nextInt(0, this.heroPotion+1);
        this.heroLife = this.heroLife + healthRegen;        
        this.heroPotion = this.heroPotion - healthRegen;
        return this.heroLife;
    }
}
