import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
 
public class ServerRunnable implements Runnable {
    private Hero hero;
    private Dragon dragon;
    private Socket socket;

    public ServerRunnable (Socket socket) {
        this.hero = new Hero();
        this.dragon = new Dragon();
        this.socket = socket;
    }
    //inputstream dal client e outputstream verso client 
    public void run () {
        System.out.println("The key to access the dungeon is: " + socket);
        try (
            Scanner in = new Scanner(socket.getInputStream()); //recepisce le linee dal client tramite socket
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true) //restituisce il risultato sull'out
            ) {
                while (in.hasNextLine()) {
                    switch (in.nextLine()) {
                        case "1":
                        int dragonLife = dragon.fightDragon();
                        int heroLife = hero.fightHero();
                            if(heroLife <= 0 && dragonLife <= 0) {
                                out.println("Tied match, press NG+ to play again");
                            }
                            else if(heroLife <= 0) {
                                out.println("The hero LOST");
                            }
                            else if (dragonLife <= 0) {
                                out.println("The hero WON, to fight another dragon enter NG+");;
                            }
                            else {
                                out.println("| Hero health: " + Integer.toString(heroLife) + " HP |" + "| Dragon health: " + Integer.toString(dragonLife) + " HP |" + "| Potion left: " + Integer.toString(hero.getHeroPotion()) + " ml |");
                                out.println("SECONDA STRINGA");
                                out.println("A\nB\nC\nD\nE\nF\nG STRINGA");
                                }
                            break;
                        case "2":
                            int heroLifeafterPot = hero.drinkPot();
                            if(hero.getHeroPotion() == 0) {
                            out.println("No more healing, you must fight! -> | Hero health: " + Integer.toString(heroLifeafterPot) + " HP |" + "| Potion left:" + Integer.toString(hero.getHeroPotion()) + " ml |");
                            break;
                            }
                            out.println("You gain energy from the potion! -> | Hero health: " + Integer.toString(heroLifeafterPot) + " HP |" + "| Potion left:" + Integer.toString(hero.getHeroPotion()) + " ml |");
                            break;
                        case "3":
                            out.println("You ran away from the fight");
                            break;
                        case "NG+":
                            hero = new Hero();
                            dragon = new Dragon();
                            out.println("A new hero appeared!");
                            break;
                        default:
                            out.println("Enter 1, 2, 3 or NG+");
                            break;
                    }
                }
            } catch (Exception e) { System.out.println("Error:" + socket);
        }
    }
}