import java.util.Scanner;

public class Game {
    public static int turnNum = 1;
    private static String gameState;
    private String title;
    private String backStory;
    private final String continueStory;
    public static String poem;
    public static GameMap gameMap = new GameMap();
    public static Player protagonist = new Player("hero", false);
    public static Player enemy = new Player("enemy", true);
    private static final Scanner in = new Scanner(System.in);
    public static String endName = "none";

    Game() {  // constructor for game object
        setState("init");
        this.title = "\n======================\n";
        this.title += "=   Rex Nemorensis   =\n";
        this.title += "======================\n";
        this.backStory = "You are a mage and warrior.  For your mettle, you have been honored to serve a priesthood for the Godess Diana.  In this role, your final role, you ";
        this.backStory += "have been cast atop the windswept cliffs of The Grove at Nemi.  In this place there are ruins and holes and cliff edges.  Here you are relegated to ";
        this.backStory += "stand an endless guard until you are killed by another exiled sole.  Another like you currently stands guard awaiting your challenge to usurp their reign ";
        this.backStory += "which you shall hold...as long as you live.  For you, a violent death is assured - the question is how soon.  How many battles will you survive if you can ";
        this.backStory += "take the guard?  You are armed with only your fists and a dagger, but there are other, more powerful weapons strewn about the mesa.  There are also ";
        this.backStory += "magical items, each with varying powers. Watch your step - you may fall to your death off the edge of the mesa or into a hole forevermore.  The other ";
        this.backStory += "guard lurks in the grove, awaiting the challengers.\n";
        poem = "       Those trees in whose dim shadow\n";
        poem += "       The ghastly priest doth reign\n";
        poem += "       The priest who slew the slayer,\n";
        poem += "       And shall himself be slain.    -McCaulay\n";
        this.continueStory = "You reign continues.  A new challenger has entered the grove.\n";
    }

    public void run() {
        setState("active");
        showIntro();
         while (getState().equals("active")) {
            gameMap.mapReport(protagonist.positCol, protagonist.positRow);
            String move = getInput("Ponder your next move and press a key: ");
            this.processInput(move, protagonist);
            if (!("QIH".contains(move))) this.advance(move, protagonist.turnCodes, enemy.turnCodes, protagonist, enemy);
//            System.out.println("-----------------------------");
            if (getState().equals("active") && (!"QIH".contains(move))) {
                move = enemy.generateBotMove();
                this.processInput(move, enemy);
                this.advance(move, protagonist.turnCodes, enemy.turnCodes, enemy, protagonist);
 //               System.out.println("-----------------------------");
            }
        }
        int exitCode = this.end(endName);
         switch(exitCode) {
             case 0:
                 this.reset();
                 break;
             case 1:
                 gameMap = new GameMap();
                 protagonist = new Player("hero", false);
                 enemy = new Player("enemy", true);
                 break;
             default :
                 System.out.println("\nYou defeated " + Game.protagonist.wins + " challengers.");
                 break;
         }
    }

    private void reset() {
        setState("active");
        protagonist.positRow = 0;
        protagonist.positCol = 0;
        protagonist.health += 25;
        System.out.println("You make your way back to your camp to sharpen your weapons, grab some grub, and get cleaned up.\n");
        System.out.println("You clean your loin cloth and your health improves by 25 points.\n");
        System.out.println("Another soul has been exiled to the Grove to challenge your reign.\n");
        System.out.println("You almost feel sorry for them, then you remember that poem.\n");
        System.out.println(Game.poem);
        enemy = new Player("enemy", true);
        enemy.health += (5 * protagonist.wins);
        enemy.shield += protagonist.wins;
        System.out.println("The more newly exiled souls you slaughter, the stronger they get, eh?\n");
        System.out.println("Enemy health: " + enemy.health);
        System.out.println("Enemy shield: " + enemy.shield);
   }

    public void showIntro() {
        if (protagonist.wins == 0) {
            System.out.println(this.title);
            System.out.println(this.backStory);
            System.out.println(poem);
            showHelpReport();
            protagonist.showStatus();
        } else {
            System.out.println();
            System.out.println("-----------------------------");
            System.out.println(this.continueStory);
            protagonist.showStatus();
        }
    }

    public static String getState() {
        return gameState;
    }

    public static void setState(String s) {
        gameState = s;
    }

    public void showHelpReport() {
        String helpReport = "-----------------------------HELP----------------------------------\n";
        helpReport += "| These do not cost a turn:     These moves cost one turn:        |\n";
        helpReport += "|  H - Help                      N, S, E, W - move                |\n";
        helpReport += "|  I - Inventory and status      1, 2, 3, 4, 5 - Use Magic Item   |\n";
        helpReport += "|  Q - Quit                      A - Attack with strongest weapon |\n";
        helpReport += "|                                G - get item                     |\n";
        helpReport += "-------------------------------------------------------------------\n";
        System.out.println(helpReport);
        Game.getReturn();
    }

    public String getInput(String prompt) {
        String newInput = "";
        System.out.print(prompt);
        newInput = in.nextLine();
        while (!inputIsValid(newInput)) {
            newInput = getInput(prompt);
        }
        System.out.println();
        return newInput.toUpperCase();
    }

    public static void getReturn() {
        String k = "";
        System.out.print("Press ENTER to continue.\n");
        k = in.nextLine();
        while (!k.equals("")) {
            getReturn();
        }
    }

    public void processInput(String s, Player p) {
//        System.out.println("-------You pressed " + s);
        switch (s) {
            case "H": // help
                if (p.name.equals("hero")) {
                    this.showHelpReport();
                }
                p.turnCodes[0] = false;
                break;
            case "I": // inventory
                if (p.name.equals("hero")) {
                    p.showStatus();
                }
                p.turnCodes[0] = false;
                break;
            case "Q": // quit
                gameState = "game-over";
                endName = "none";
                p.turnCodes[0] = false;
                break;
            case "1":// magic
            case "2":
            case "3":
            case "4":
            case "5":
                p.processMagic(s);
                p.turnCodes[0] = true;
                break;
            case "G": // get
                p.getItem();
                p.turnCodes[0] = true;
                break;
            case "A": // attack
                p.processAttack();
                p.turnCodes[0] = true;
                break;
            case "N":
            case "S":
            case "E":
            case "W": // move
                p.processMove(s);
                p.turnCodes[0] = true;
                break;
        }
    }

    public boolean inputIsValid(String _input) {
        if ("hiqHIQ12345gansweGANSWE".contains(_input) && !_input.equals("")) {
            return true;
        } else {
            System.out.println("That is not a valid move (press H for help).");
            return false;
        }
    }

    public void advance(String m, boolean[] pCodes, boolean[] eCodes, Player pri, Player sec) {
        // calculate and apply damages if an attack
        if (m.equals("A")) {
            int finalDamage = pri.damage;
            String magicStuff = " while using the following magic items: \n";
            if (pri.turnCodes[1]) {  // invis pri
                magicStuff += "The Cloak of Invisibilty (" + pri.invisibility + ") turns remaining.\n";
            }
            if (sec.turnCodes[1]) {  // invis sec
                finalDamage = 0;
            }
            if (pri.turnCodes[2]) {  // strength
                finalDamage += 5;
                magicStuff += "The Gauntlet of Strength (" + pri.strength + ") turns remaining.\n";
            }
            if (pri.turnCodes[3]) {  // restoration
                magicStuff += "The Tincture of Restoration (" + pri.restoration + ") turns remaining.\n";
                // restoration handled below and in player class
            }
            if (pri.turnCodes[4]) {  // protection
                magicStuff += "The Ring of Protection (" + pri.protection + ") turns remaining.\n";
                // shield handled below and in player class
            }
            if (pri.turnCodes[5]) {  // speed
                finalDamage *= 2;
                magicStuff += "The Crown of Speed (" + pri.speed + ") turns remaining.\n";
            }
            if (magicStuff.equals(" while using the following magic items: \n")) {
                if (pri.name.equals("hero")) {
                    magicStuff += "The loin cloth of fortitude (always on - well, almost always).";
                } else {
                    magicStuff += "none.";
                }
            }
            if (Game.nearEachOther()) {
                finalDamage -= sec.shield;
                if (finalDamage < 0) {
                    finalDamage = 0;
                }
                sec.health -= finalDamage;
                // messaging
                String top;
                String bottom;
                if (pri.name.equals("hero")) {
                    top = "You";
                    bottom = "The enemy";
                } else {
                    top = "The enemy";
                    bottom = "You";
                }
                System.out.println(top + " struck with the " + pri.weapon + " inflicting " + finalDamage + " points damage, " + magicStuff);
                System.out.println("Your health is " + protagonist.health + ".  The enemy's health is " + enemy.health + "\n");
            }
        }
        // check for death
        if (protagonist.health <= 0) {
            endName = "hero";
            System.out.println("You hear the tolling of a death knell.");
            setState("game-over");
        } else if (enemy.health <= 0) {
            endName = "enemy";
            System.out.println("You hear the tolling of a death knell.");
            setState("game-over");
        } else {
            // inc the turn num
            if (protagonist.turnCodes[0]) {
                turnNum++;
                protagonist.turnCodes[0] = false;
                // magic depletion
                if (pri.name.equals("hero")) {
                    depleteProtagMagic();
                } else {
                    depleteEnemyMagic();
                }
            }
        }
    }

    void depleteProtagMagic() {
        if (protagonist.invisibility > 0) {
            protagonist.invisibility--;
            if (protagonist.invisibility == 0) {
                protagonist.turnCodes[1] = false;
                System.out.println("You suddenly fade back into the realm of the visible as the cloak loses its power.");
                protagonist.magicItems[0] = null;
            } else {
                System.out.println("You magic invisibility will be gone in " + protagonist.invisibility + " turns.");
            }
        }
        if (protagonist.strength > 0) {
            protagonist.strength--;
            if (protagonist.strength == 0) {
                protagonist.turnCodes[2] = false;
                System.out.println("You weapon feels heavier once again.");
                protagonist.magicItems[1] = null;
            } else {
                System.out.println("You magic strength will be gone in " + protagonist.strength + " turns.");
            }
        }
        if (protagonist.restoration > 0) {
            protagonist.restoration--;
            protagonist.health += 5;
            System.out.println("Ahhhhh! The tincture kicked in, increasing your health is now " + protagonist.health);
            if (protagonist.restoration == 0) {
                protagonist.turnCodes[3] = false;
                System.out.println("You look around for a place to nap and recharge as your magic health dissipates.");
                protagonist.magicItems[2] = null;
            } else {
                System.out.println("You magic restoration will be gone in " + protagonist.restoration + " turns.");
            }
        }
        if (protagonist.protection > 0) {
            protagonist.protection--;
            if (protagonist.protection == 0) {
                protagonist.shield -= 5;
                protagonist.turnCodes[4] = false;
                System.out.println("The aura that once enveloped you swirls away.  You feel nearly naked without your magic shield.");
                protagonist.magicItems[3] = null;
            } else {
                System.out.println("You magic protection will be gone in " + protagonist.protection + " turns.");
            }
        }
        if (protagonist.speed > 0) {
            protagonist.speed--;
            if (protagonist.speed == 0) {
                protagonist.turnCodes[5] = false;
                System.out.println("You take a practice swing of your weapon and it feels like its moving in slow motion as your magic speed wanes like the moon.");
                protagonist.magicItems[4] = null;
            } else {
                System.out.println("You magic speed will be gone in " + protagonist.speed + " turns.");
            }
        }    }

    void depleteEnemyMagic() {
        if (enemy.invisibility > 0) {
            enemy.invisibility--;
            if (enemy.invisibility == 0) {
                enemy.turnCodes[1] = false;
//                enemy.magicItems[0] = null;
            }
        }
        if (enemy.strength > 0) {
            enemy.strength--;
            if (enemy.strength == 0) {
                enemy.turnCodes[2] = false;
//                enemy.magicItems[1] = null;
            }
        }
        if (enemy.restoration > 0) {
            enemy.restoration--;
            if (enemy.restoration == 0) {
                enemy.turnCodes[3] = false;
//                enemy.magicItems[2] = null;
            }
        }
        if (enemy.protection > 0) {
            enemy.protection--;
            if (enemy.protection == 0) {
                enemy.turnCodes[4] = false;
//                enemy.magicItems[3] = null;
            }
        }
        if (enemy.speed > 0) {
            enemy.speed--;
            if (enemy.speed == 0) {
                enemy.turnCodes[5] = false;
//                enemy.magicItems[4] = null;
            }
        }
    }

    public int end(String n) {
//        System.out.println("-------exit code " + n);
        System.out.println("Game over.\n");
        if (n.equals("enemy")) {
            System.out.println("You are victorious. ");
            protagonist.wins++;
            System.out.println("You have defeated " + protagonist.wins + " challengers.");
            System.out.println();
            return 0;
        } else if (n.equals("hero")) {
            System.out.println("You are defeated. A haloed child touches your head. You hear their whisper from behind, 'All glory is fleeting...'");
            System.out.println();
            RexNemorensis.replay = checkReplay().startsWith("Y");
            return 1;
        } else {
            RexNemorensis.replay = checkReplay().startsWith("Y");
            return 2;
        }
    }

    public String checkReplay() {
        String check = "";
        System.out.print("Would you like to play again? ");
        check = in.nextLine().toUpperCase();
        while (!(check.startsWith("Y") || check.startsWith("N"))) {
            check = checkReplay();
        }
        return check;
    }

    public static boolean nearEachOther() {
        return (Math.abs(protagonist.positCol - enemy.positCol) <= 1) && (Math.abs(protagonist.positRow - enemy.positRow) <= 1);
    }
}