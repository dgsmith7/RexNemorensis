package RexNem;

import java.util.Scanner;

public class Player {
    public Scanner in = new Scanner(System.in);
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

    Player(String _name, boolean _bot) {  // constructor with identifiers
        this.name = _name;
        this.bot = _bot;
        if (_bot) {  // enemy in lower right (SE)
            this.positCol = GameMap.mapCol - 1;
            this.positRow = GameMap.mapRow - 1;
        } else {  // hero in upper left (NW)
            this.positCol = 0;
            this.positRow = 0;
        }
        this.health = 100;
        this.attack = 10; // fists-5 dagger-10 sword-15 axe-20
        this.damage = 0;
        this.shield = 0;
        this.weapon = "dagger";
        this.magicItems = new String[5];
        this.invisibility = 0;
        this.strength = 0;
        this.protection = 0;
        this.restoration = 0;
        this.speed = 0;
        this.turnCodes = new boolean[6];  // 0-incTurn 1-invis 2-strength 3-restore 4-protect 5-speed
        this.wins = 0;
    }

    public void showStatus() {  // report on inventory and status
        System.out.println("INVENTORY AND STATUS:");
        System.out.println("Your have " + this.health + " health points.");
        System.out.println("Your strongest weapon is a " + this.weapon + " - each direct hit removes " + this.attack + " health from your enemy.");
        System.out.println("Your shield (or lack thereof) provides " + this.shield + " protection from any attack.");
        System.out.println("These are your magic items: ");
        boolean none = true;
        for (int i = 0; i < magicItems.length; i++) {
            if (magicItems[i] != null) {
                System.out.println("   Press " + (i + 1) + " to activate " + this.magicItems[i]);
                none = false;
            }
        }
        if (none) System.out.println("   none");
        System.out.println("Active magic Items:");
        if (this.invisibility > 0) System.out.println("   Cloak of invisibility");
        if (this.strength > 0) System.out.println("   Gauntlet of strength");
        if (this.restoration > 0) System.out.println("   Tincture of restoration");
        if (this.protection > 0) System.out.println("   Ring of protection");
        if (this.speed > 0) System.out.println("   Crown of speed");
        int magicSum = this.invisibility + this.strength + this.restoration + this.protection + this.speed;
        if (magicSum == 0) System.out.println("   none");
        System.out.println("You have defeated " + wins + " challengers.");
        if (Math.random() < 0.5) {
            System.out.println("Your loin cloth flaps with the wind.  You blush.");
        }
        System.out.println();
        Game.getReturn();
    }

    public void processMove(String s) { // act on instruction entered or generated
        if (name.equals("hero")) {
            System.out.print("You are moving ");
        } else {
            if (Game.enemy.invisibility == 0) System.out.print("The enemy is moving ");
        }
        if (s.equals("N")) {
            if (Game.gameMap.notWall(this.positCol, this.positRow - 1)) {
                this.positRow--;
                if (Game.enemy.invisibility == 0 || name.equals("hero")) System.out.println("North.");
            } else {
                if (Game.enemy.invisibility == 0 || name.equals("hero"))
                    System.out.println("but something blocks the path.");
            }
        }
        if (s.equals("S")) {
            if (Game.gameMap.notWall(this.positCol, this.positRow + 1)) {
                this.positRow++;
                if (Game.enemy.invisibility == 0 || name.equals("hero")) System.out.println("South.");
            } else {
                if (Game.enemy.invisibility == 0 || name.equals("hero"))
                    System.out.println("but something blocks the path.");
            }
        }
        if (s.equals("W")) {
            if (Game.gameMap.notWall(this.positCol - 1, this.positRow)) {
                this.positCol--;
                if (Game.enemy.invisibility == 0 || name.equals("hero")) System.out.println("West.");
            } else {
                if (Game.enemy.invisibility == 0 || name.equals("hero"))
                    System.out.println("but something blocks the path.");
            }
        }
        if (s.equals("E")) {
            if (Game.gameMap.notWall(this.positCol + 1, this.positRow)) {
                this.positCol++;
                if (Game.enemy.invisibility == 0 || name.equals("hero")) System.out.println("East.");
            } else {
                if (Game.enemy.invisibility == 0 || name.equals("hero"))
                    System.out.println("but something blocks the path.");
            }
        }
        // if you're off the edge, fall
        if (this.positRow < 0 || this.positRow > 7 || this.positCol < 0 || this.positCol > 7) {
            if (name.equals("hero")) {
                System.out.println("You fell to your death, you clumsy fool!\n");
            } else {
                System.out.println("You hear sliding gravel, see a blur in the corner of your eye and realize that the enemy fell to their death.  Clumsy fool!\n");
            }
            Game.setState("game-over");
            Game.endName = name;
        } else {
            // if your on a hole, fall
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

    public void processAttack() {  // if an attack was ordered, process it
        String damStr = ""; // damage message
        String missStr = ""; // you missed message
        float prob = (float) Math.floor(Math.random() * 10); // dice?
        if (prob < 1.2) { // 12% chance of a clean miss
            this.damage = 0;
            damStr = "A swing and a miss!  Your enemy grins.  Hey batter, batter, batter, sssssswingggggg, batterrrrrrr!!";
            missStr = "You swing at nothing and miss - and also manage to look like a total jackass.";
        } else if (prob < 3.8) { // 26% chance of a glancing blow
            this.damage = this.attack - 5;
            damStr = "You struck a glancing blow! Your enemy grunts and furrows their brow a bit.";
            missStr = "You attack the thin air vigorously, making it even thinner - not your best look but at least you got in a quick workout.";
        } else {  // 62% chance direct hit at full strength
            this.damage = this.attack;
            damStr = "A direct hit! The smile leaves your enemy's eyes as they stumble back.";
            missStr = "You swing at what appears to be your own shadow.  Your form was perfect and you looked like a complete badass, except that there is nobody to attack.";
        }
        if (this.name.equals("hero")) {  // only show hit messages if its hero and enemy not "invisible"
            if (Game.nearEachOther() && Game.enemy.invisibility == 0) {
                System.out.println(damStr);
            } else {
                System.out.println(missStr);
            }
            this.turnCodes[0] = true;
        }
    }

    public void processMagic(String s) {  //  process any magical incantations
        switch (s) {
            case "1":
                if (this.invisibility == 0 && this.magicItems[0] != null) {
                    this.magicItems[0] = null;
                    this.invisibility = 4;
                    this.turnCodes[1] = true;
                    if (this.name.equals("hero")) {
                        System.out.println("You slide the cloak over your shoulders and suddenly disappear (too bad 'cuz your coiffure looks great today). You are safe from attack for now.");
                    } else {
                        System.out.println("The enemy vanished into thin air.");
                    }
                } else {
                    System.out.println("Good try, Rockstar, but you don't own that item.");
                }
                break;
            case "2":
                if (this.strength == 0 && this.magicItems[1] != null) {
                    this.magicItems[1] = null;
                    this.strength = 4;
                    this.turnCodes[2] = true;
                    if (this.name.equals("hero")) {
                        System.out.println("The gauntlet fits your hand like a.....uhhh - gauntlet, and you feel strong enough to pull the ears of a Gundark.");
                    } else {
                        System.out.println("The enemy donned the Gauntlet of Strength.");
                    }
                } else {
                    System.out.println("I don't think so, mate. You don't own that item.");
                }
                break;
            case "3":
                if (this.restoration == 0 && this.magicItems[2] != null) {
                    this.magicItems[2] = null;
                    this.restoration = 4;
                    this.turnCodes[3] = true;
                    if (this.name.equals("hero")) {
                        System.out.println("You drink deeply. This is better than the immuno-boost at Jamba Juice. You health begins to build.");
                    } else {
                        System.out.println("The enemy chugged a vile of The Tincture of Restoration.");
                    }
                } else {
                    System.out.println("As if you own that item or something.");
                }
                break;
            case "4":
                if (this.protection == 0 && this.magicItems[3] != null) {
                    this.magicItems[3] = null;
                    this.protection = 4;
                    this.shield += 5;
                    this.turnCodes[4] = true;
                    if (this.name.equals("hero")) {
                        System.out.println("The ring slides easily onto your hand and you are surrounded by a strange protective aura.");
                    } else {
                        System.out.println("The enemy said 'I do' to the Ring of Protection. I bet that honeymoon 'll suck!");
                    }
                } else {
                    System.out.println("Uhhhhh, you need to get one to wear one, Dufus.");
                }
                break;
            case "5":
                if (this.speed == 0 && this.magicItems[4] != null) {
                    this.magicItems[4] = null;
                    this.speed = 4;
                    this.turnCodes[5] = true;
                    if (this.name.equals("hero")) {
                        System.out.println("As you carefully place the crown atop your head, careful not to mess up your elaborate hairdo, you noticed it is adorned with jeweled wings.  Your hands seem twice as fast as before.  Hmmmmm, you ponder, I wonder if I could attack twice in the same amount of time it takes to attack once.");
                    } else {
                        System.out.println("The enemy messed up their do while donning the Crown of Speed.");
                    }
                } else {
                    System.out.println("Negative, Ghost-rider, the pattern is full.  You don't own that item.");
                }
                break;
        }
    }

    public void getItem() {  // pick stuff up
        String item = "";
        boolean noItem = false;
        switch (GameMap.layout[this.positRow].charAt(this.positCol)) {
            case '1':
                item = "Cloak of invisibility: Incoming attack 0 damage for 3 turns when used.  Press '1' to use.";
                this.magicItems[0] = item;
                break;
            case '2':
                item = "Gauntlet of strength: Attack force +5 for 3 turns when used.  Press '2' to use.";
                this.magicItems[1] = item;
                break;
            case '3':
                item = "Tincture of restoration: Health +5 for 3 turns when used.  Press '3' to use.";
                this.magicItems[2] = item;
                break;
            case '4':
                item = "Ring of protection: Shield +5 for 3 turns when used.  Press '4' to use.";
                this.magicItems[3] = item;
                break;
            case '5':
                item = "Crown of speed: 2x attack for three turns when used.  Press '5' to use.";
                this.magicItems[4] = item;
                break;
            case 'A':
                item = "axe. Direct hits will henceforth inflict 20 damage.";
                this.weapon = "axe";
                this.attack = 20;
                break;
            case 'S':
                if (weapon.equals("sword") && name.equals("hero")) {  // hero already has one
                    System.out.println("You already have a sword, Greed-o.");
                } else if (!weapon.equals("axe")) {  // enemy or hero does not have one
                    item = "sword. Direct hits will henceforth inflict 15 damage.";
                    this.weapon = "sword";
                    this.attack = 15;
                } else {  // hero already has a better weapon
                    noItem = true;
                    if (name.equals("hero")) {
                            System.out.println("You decide to forgo the sword for the superior axe in hand.");
                        }
                    turnCodes[0] = false;
                }
                break;
            case 'D':
                item = "shield. All hits from enemies will henceforth be reduced by 5 damage.";
                this.shield = 5;
                break;
            default:  // user pressed g when there is nothing to pick up
                System.out.println("There is nothing to get here, pal.");
                noItem = true;
                break;
        }
        if (!noItem) {
            if (name.equals("hero")) {
                System.out.println("You picked up the " + item + " Press I for inventory.");
            } else {
                System.out.println("The enemy picked up the " + item + ".  Oh, snap!");
            }
            this.turnCodes[0] = true;
            GameMap.layout[this.positRow] = GameMap.replaceChar(this.positCol, GameMap.layout[this.positRow], 'B');
        }
    }

    public String generateBotMove() {  // figure out an input for the bot
        String dir = getSafeDir();
        if (Game.nearEachOther()) {  // if enemy in strike distance
            if (this.health >= 50) { // attack if health above 50
                return "A";  // attack
            } else if (this.health >= 20) { // if between 20 and 50 use magic first then attack
                if (this.magicItems[0] != null) {
                    return "1";
                } else if (this.magicItems[1] != null) {
                    return "2";
                } else if (this.magicItems[2] != null) {
                    return "3";
                } else if (this.magicItems[3] != null) {
                    return "4";
                } else if (this.magicItems[4] != null) {
                    return "5";
                } else {
                    return "A";  // no more magic to invoke - start attacking again
                }
            } else { // if lower than 20 run away
                return dir;
            }
            // or if there is nobody to attack and there is something to pick up, do it
        } else if ("ASD12345".contains(String.valueOf(GameMap.layout[this.positRow].charAt(this.positCol)))) { // something to get
            return "G";
        } else { // otherwise just move
            return dir;
        }
    }

    public boolean edgeFall(String dir) {  // bot checking to see if it will fall off edge
        return (this.positCol == 0 && (dir.equals("W")) ||
                (this.positCol == GameMap.mapCol - 1 && dir.equals("E")) ||
                (this.positRow == 0 && dir.equals("N")) ||
                (this.positRow == GameMap.mapRow - 1 && dir.equals("S")));
    }

    public boolean edgeWall(String dir) {  // bot checking for walls when at edge of mesa - good Gawd!
        // the edges of the map will never have walls, BTW
        /*   Explanations for hairy booleans created to keep nesting hell down
        if col is eastmost and dir is west and w != wall then move is safe
        if col is westmost and dir is east and e != wall then move is safe
        if row is southmost and dir is north and n != wall then move is safe
        if row is northmost and dir is south and s != wall then move is safe
         and
        if you are not on east edge and (dir is e and the string is not Wall) then move is safe
        or if you are not on west edge and (dir is w and the string in not wall) then move is safe
        or if you are not on the south edge and (dir is s and the string is not wall) then move is safe
        or if you are not on the north edge and (dir is n and the string is not wall) then move is safe
         */
        return (((this.positCol == GameMap.mapCol - 1 && (dir.equals("W") && !String.valueOf(GameMap.layout[positRow].charAt(positCol - 1)).equals("W"))) ||
                (this.positCol == 0 && (dir.equals("E") && !String.valueOf(GameMap.layout[positRow].charAt(positCol + 1)).equals("W"))) ||
                (this.positRow == GameMap.mapRow - 1 && (dir.equals("N") && !String.valueOf(GameMap.layout[positRow - 1].charAt(positCol)).equals("W"))) ||
                (this.positRow == 0 && (dir.equals("S") && !String.valueOf(GameMap.layout[positRow + 1].charAt(positCol)).equals("W")))))
                ||
                (((this.positCol != GameMap.mapCol - 1 && (dir.equals("E") && !String.valueOf(GameMap.layout[positRow].charAt(positCol + 1)).equals("W"))) ||
                        (this.positCol != 0 && (dir.equals("W") && !String.valueOf(GameMap.layout[positRow].charAt(positCol - 1)).equals("W"))) ||
                        (this.positRow != GameMap.mapRow - 1 && (dir.equals("S") && !String.valueOf(GameMap.layout[positRow + 1].charAt(positCol)).equals("W"))) ||
                        (this.positRow != 0 && (dir.equals("N") && !String.valueOf(GameMap.layout[positRow - 1].charAt(positCol)).equals("W")))));
    }

    public boolean nonEdgeWall(String dir) {  // bot checking for walls when not at edge of mesa
        String n = String.valueOf(GameMap.layout[positRow - 1].charAt(positCol));
        String e = String.valueOf(GameMap.layout[positRow].charAt(positCol + 1));
        String s = String.valueOf(GameMap.layout[positRow + 1].charAt(positCol));
        String w = String.valueOf(GameMap.layout[positRow].charAt(positCol - 1));
        return !((n.equals("W") && dir.equals("N")) ||
                (s.equals("W") && dir.equals("S")) ||
                (e.equals("W") && dir.equals("E")) ||
                (w.equals("W") && dir.equals("W")));

    }

    public String getSafeDir() {  // bot getting direction to try and checking it out
        /*  psuedo code for algo
        get a random dir
        safe = false
        while not safe
            get a random dir
            if its on the edge
                check for a fall - probability under threshold? check if fall mark unsafe else dont check
                check for a wall - if wall mark unsafe
            else...not on an edge
                check for a wall - if wall mark unsafe
         */
        String dir = "N";
        dir = getRandomDir(dir);
        boolean safe = false;
        while (!safe) {
            safe = true;  // assume its a good direction unless proven otherwise below
            dir = getRandomDir(dir);
            if (positCol == 0 || positCol == (GameMap.mapCol - 1) || positRow == 0 || (positRow == GameMap.mapRow - 1)) {// on a cliff edge
                float probSure = (float) (Math.random() * 10);  // determine if I should check if Im near a ledge
                if (probSure < 9.8) {// check for a potential fall only 98 % of the time - sometimes enemy falls off
                    safe = (!edgeFall(dir));
                }
                safe = safe && edgeWall(dir);  // combine the two checks above without overriding one
            } else { // not on an edge
                safe = nonEdgeWall(dir);
            }
        }
        return dir;
    }

    public String getRandomDir(String dir) {  // bot getting initial direction during move process
        switch ((int) (Math.random() * 4)) {
            case 0:
                dir = "N";
                break;
            case 1:
                dir = "E";
                break;
            case 2:
                dir = "S";
                break;
            case 3:
                dir = "W";
                break;
        }
        return dir;
    }
}