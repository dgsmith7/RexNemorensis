import java.util.Scanner;

import static java.lang.Integer.valueOf;

public class Player {
    public Scanner in = new Scanner(System.in);
    public int gamesWon;
    public String name;
    public boolean bot;
    public int positCol;
    public int positRow;
    public int health;
    public int attack;
    public int shield;
    public String weapon;
    public String[] magicItems;
    public int invisibility;
    public int strength;
    public int protection;
    public int restoration;
    public int speed;
    public boolean[] turnCodes;
    public int wins;
    public int damage;

    Player(String _name, boolean _bot) {
//        System.out.println("-------Constructing a player named " + _name + ".");
        this.gamesWon = 0;
        this.name = _name;
        this.bot = _bot;
        if (_bot) {
            this.positCol = 7;//GameMap.mapCol-1;
            this.positRow = 0;//GameMap.mapRow-1;
        } else {
            this.positCol = 0;
            this.positRow = 0;
        }
        this.health = 100;
        this.attack = 2;
        this.damage = 0;
        this.shield = 0;
        this.weapon = "dagger";
        this.magicItems = new String[5];
        this.invisibility = 0;
        this.strength = 0;
        this.protection = 0;
        this.restoration = 0;
        this.speed = 0;
        this.turnCodes = new boolean[6];
        // 0-incTurn 1-invis 2-strength 3-restore 4-protect 5-speed
        this.wins = 0;
     }

    public void showStatus() {
        System.out.println("INVENTORY AND STATUS:");
        System.out.println("Your have " + this.health + " health points.");
        System.out.println("Your strongest weapon is a " + this.weapon + " - each hit removes " + this.attack + " health from your enemy.");
        System.out.println("Your shield (or lack thereof) provides " + this.shield + " protection from any attack.");
        System.out.println("These are your magic items: ");
        boolean none = true;
        for (int i = 0; i < magicItems.length; i++) {
            if (magicItems[i] != null) {
                System.out.println("   Press " + (i+1) + " to activate " + this.magicItems[i]);
                none = false;
            }
        }
        if (none) System.out.println("   none");
        System.out.println("Active magic Items:");
        // figure out index nums for activation here
        if (this.invisibility > 0) System.out.println("   Cloak of invisibility");
        if (this.strength > 0) System.out.println("   Gauntlet of strength");
        if (this.restoration > 0) System.out.println("   Tincture of restoration");
        if (this.protection > 0) System.out.println("   Ring of protection");
        if (this.speed > 0) System.out.println("   Crown of speed");
        int magicSum = this.invisibility + this.strength + this.restoration + this.protection + this.speed;
        if (magicSum == 0) System.out.println("   none");
        System.out.println();
        Game.getReturn();
    }

    public void processMove(String s) {
//        System.out.println("-------The move is being processed for " + name + ".");
        if (name.equals("hero")) {
            System.out.print("You are moving ");
        } else {
            System.out.print("The enemy is moving ");
        }
        if (s.equals("N")) {
            if (Game.gameMap.notWall(this.positCol, this.positRow-1)) {
                this.positRow--;
                System.out.println("North");
            } else {
                System.out.println(" but something blocks the path.");
            }
        }
        if (s.equals("S")) {
            if (Game.gameMap.notWall(this.positCol, this.positRow+1)) {
                this.positRow++;
                System.out.println("South");
            } else {
            System.out.println(" but something blocks the path.");
            }
        }
        if (s.equals("W")) {
            if (Game.gameMap.notWall(this.positCol-1, this.positRow)) {
                this.positCol--;
                System.out.println("West");
            } else {
                System.out.println(" but something blocks the path.");
            }
        }
        if (s.equals("E")) {
            if (Game.gameMap.notWall(this.positCol+1, this.positRow)) {
                this.positCol++;
                System.out.println("East");
            } else {
                System.out.println(" but something blocks the path.");
            }
        }
//        System.out.println("-------" + name + " is now on " + this.positCol + " - " + this.positRow);
        // if you're off the edge fall
        if (this.positRow < 0 || this.positRow > 7 || this.positCol < 0 || this.positCol > 7) {
             if (name.equals("hero")) {
                System.out.println("You fell to your death, you clumsy fool!\n");
            } else {
                System.out.println("You hear sliding gravel, see a blur in the corner of your eye and realize that the enemy fell to their death.  Clumsy fool!\n");
            }
            Game.setState("game-over");
            Game.endName = name;
        } else {
            // if your on a hole fall
            if (GameMap.layout[this.positRow].charAt(this.positCol) == 'H') {
                if (name.equals("hero")) {
                    System.out.println("You fell a great distance down a hot, smelly hole and died a horrible, hot, smelly death!\n");
                } else {
                    System.out.println("You hear the enemy scream as they fall to their hot, smelly death down a hot, smelly hole!\n");
                }
                Game.setState("game-over");
                Game.endName = name;
            }
        }
        System.out.println();
    }

    public void processAttack(){
        // process the attack
        System.out.println("-------Attacking.");
        String damStr = "";
        String missStr = "";
        float prob = (float) Math.floor(Math.random() * 10);
        System.out.println("-------probability." + prob);
        if (prob < 2.0) {
            this.damage = 0;
            damStr = "A swing and a miss!  Hey batter, batter, batter, sssssswingggggg, batterrrrrrr!!";
            missStr = "You swing at nothing and miss - and also manage to look like a total jackass.";
        } else if (prob < 5.0) {
            this.damage = this.attack - 1;
            damStr = "You struck a glancing blow! Your enemy grins.";
            missStr = "You attack the thin air vigorously, making it even thinner - not your best look but at least you got in a quick workout.";
        } else {
            this.damage = this.attack;
            damStr = "A direct hit! The smile leaves your enemy's eyes.";
            missStr = "You swing at what appears to be your own shadow.  Your form was perfect and you looked like a complete badass, except that there is nobody to attack.";
        }
        if (Game.nearEachOther()) {
            System.out.println(damStr);
        } else {
            System.out.println(missStr);
        }
        this.turnCodes[0] = true;
    }

    public void processMagic(String s) {
//        System.out.println("-------Once this method is done, I will be:");
// ---->       System.out.println("\nInvoking the magic of the " + Game.protagonist.magicItems[Integer.parseInt(s)]);
        switch(s) {
            case "1": if (this.invisibility == 0 ) {
                this.invisibility = 4;
                this.turnCodes[1] = true;
                if (!this.name.equals("hero")) {
                    System.out.println("You slide the cloak over your shoulders and suddenly disappear (too bad 'cuz your coiffure looks great today). You are safe from attack for now.");
                }
               }
                break;
            case "2": if (this.strength == 0 ) {
                this.strength = 4;
                this.turnCodes[2] = true;
                if (!this.name.equals("hero")) {
                    System.out.println("The gauntlet fits your hand like a........uhhh - gauntlet, and you feel strong enough to pull the ears of a Gundark.");
                }
            }
                break;
            case "3": if (this.restoration == 0 ) {
                this.restoration = 4;
                this.turnCodes[3] = true;
                if (!this.name.equals("hero")) {
                    System.out.println("You drink deeply. This is better than the immuno-boost at Jamba Juice. You health increases.");
                }
            }
                break;
            case "4": if (this.protection == 0 ) {
                this.protection = 4;
                this.shield += 2;
                this.turnCodes[4] = true;
                if (!this.name.equals("hero")) {
                    System.out.println("The ring slides easily onto your hand and you are surrounded by a strange protective aura.");
                }
            }
                break;
            case "5": if (this.speed == 0 ) {
                this.speed = 4;
                this.turnCodes[5] = true;
                if (!this.name.equals("hero")) {
                    System.out.println("As you carefully place the crown atop your head, careful not to mess up your elaborate hairdo, you noticed it is adorned with jeweled wings.  Your hands seem twice as fast as before.");
                }
            }
                break;
        }
    }

    public void getItem() {
        String item = "";
        boolean noItem = false;
        switch (GameMap.layout[this.positRow].charAt(this.positCol)) {
            case '1': item = "Cloak of invisibility: Enemy attack 0 damage for 3 turns when used."; this.magicItems[0] = item; break;
            case '2': item = "Gauntlet of strength: Attack force +3 for 3 turns when used."; this.magicItems[1] = item; break;
            case '3': item = "Tincture of restoration: Health +3 for 3 turns when used."; this.magicItems[2] = item; break;
            case '4': item = "Ring of protection: Shield +2 for 3 turns when used."; this.magicItems[3] = item; break;
            case '5': item = "Crown of speed: 2x attack for three turns when used."; this.magicItems[4] = item; break;
            case 'A': item = "axe. Direct hits will henceforth inflict 4 damage to your enemy."; this.attack = 4; break;
            case 'S': item = "sword. Direct hits will henceforth inflict 3 damage to your enemy."; this.attack = 3; break;
            case 'D': item = "shield. All hits from your enemies will henceforth be reduced by 1 damage."; this.shield = 1; break;
            default: System.out.println("There is nothing to get here."); noItem = true; break;
        }
        if (!noItem) {
            System.out.println("You picked up the " + item);
            this.turnCodes[0] = true;
            GameMap.layout[this.positRow] = GameMap.replaceChar(this.positCol, GameMap.layout[this.positRow], 'B');
        }
    }

//    void updateFromTurn() {
//        if (this.invis) {
//            this.iStart--;
//            if (this.iStart == 0) {
//                this.invis = false;
//            }
//        }
//        if (this.strength) {
//            this.stStart--;
//            if (this.stStart == 0) {
//                this.strength = false;
//            }
//        }
//        if (this.restore) {
//            this.rStart--;
//            this.health += 3;
//            if (this.rStart == 0) {
//                this.restore = false;
//            }
//        }
//        if (this.protect) {
//            this.pStart--;
//            if (this.pStart == 0) {
//                this.protect = false;
//                this.shield -= 2;
//            }
//        }
//        if (this.speed) {
//            this.spStart--;
//            if (this.spStart == 0) {
//                this.speed = false;
//            }
//        }
//    }
//
//    int updateBeforeHitting(int dam) {
//        int newDam = dam;
//        if (this.strength) {
//            newDam += 3;
//        }
//        if (speed) {
//            newDam *= 2;
//        }
//        return newDam;
//    }
//
//    int updateFromHit(int dam) {
//        int newDam = dam;
//        if (this.invis) {
//            newDam = 0;
//        }
//        return newDam;
//    }

    public String generateBotMove() {
//        System.out.println("-------The move was generated for " + name + ".");
        return "W";
        /*
        pick a genreral dir gd

        if item, get
        if enemy if h < 5 escape
                 if h > 5 and < 15 50-50 if magic magic-attack else attack
                 if h > 15 attack
        else move gd if wall new gd
                     if hole 25 new gd no wall

*/
//        return "E";
    }

    public int getPositCol() {
        return this.positCol;
    }
}
