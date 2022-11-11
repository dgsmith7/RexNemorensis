import java.util.Scanner;

public class Game {
    public static int turnNum = 1;
    private static String gameState;
    private String title;
    private String backStory;
    public static GameMap gameMap = new GameMap();
    public static Player protagonist = new Player("hero", false);
    public static Player enemy = new Player("villian", true);
    private final Scanner in;

    Game() {
        System.out.println("-------Constructing the Game.");
        this.in = new Scanner(System.in);
//        this.turnNum = 1;
        this.setState("init");
        this.title = "\n======================\n";
        this.title += "=   Rex Nemorensis   =\n";
        this.title += "======================\n";
        this.backStory = "You are a mage and warrior.  For your mettle, you have been honored to serve a priesthood for the Godess Diana.  In this role, your final role, you\n";
        this.backStory += "have been cast atop the windswept cliffs of The Grove at Nemi.  In this place there are ruins and holes and cliff edges.  Here you are relegated to\n";
        this.backStory += "stand an endless guard until you are killed by another exiled sole.  Another like you currently stands guard awaiting your challenge to usurp their reign\n";
        this.backStory += "which you shall hold...as long as you live.  For you, a violent death is assured - the question is how soon.  How many battles will you survive if you can\n";
        this.backStory += "take the guard?  You are armed with only your fists and a dagger, but there are other, more powerful weapons strewn about the mesa.  There are also \n";
        this.backStory += "magical items, each with varying powers. Watch your step - you may fall to your death off the edge of the mesa or into a hole forevermore.  The other\n";
        this.backStory += "guard lurks in the grove, awaiting the challengers.\n\n";
        this.backStory += "       Those trees in whose dim shadow\n";
        this.backStory += "       The ghastly priest doth reign\n";
        this.backStory += "       The priest who slew the slayer,\n";
        this.backStory += "       And shall himself be slain.    -McCaulay\n";
     }

    public void run() {
        this.gameState = "active";
        showIntro();
        showHelpReport();
        protagonist.showStatus();
        while (this.getState().equals("active")) {
            System.out.println(gameMap.mapReport(protagonist.positCol, protagonist.positRow));
            String move = getInput();
            this.processInput(move);
            protagonist.processMove(move);
            enemy.processMove(move);
            this.advance(protagonist.turnCodes, enemy.turnCodes);
        }
        this.end();
    }

    public void showIntro() {
        System.out.println(this.title);
        System.out.println(this.backStory);
    }

    public String getState() {
        return gameState;
    }

    public void setState (String s) {
        gameState = s;
    }
    public void showHelpReport() {
        String helpReport = "These do not cost a turn:               These moves cost one turn:\n";
        helpReport += "H - Help                                N, S, E, W - move\n";
        helpReport += "I - Inventory and status                1, 2, 3, 4, 5 - Use Magic Item\n";
        helpReport += "Q - Quit                                A - Attack with strongest weapon\n";
        helpReport += "                                        G - get item\n";
        System.out.println(helpReport);
    }

    public String getInput() {
        String newInput = "";
        System.out.print("Ponder your next move and press a key: ");
        newInput = in.nextLine();
        while (!inputIsValid(newInput)) {
            newInput = getInput();
        }
        return newInput.toUpperCase();
    }

    public void processInput(String s) {
        System.out.println("You pressed " + s);
        switch (s) {
            case "H": // help
                this.showHelpReport();
                protagonist.turnCodes[0] = false;
                break;
            case "I": // inventory
                protagonist.showStatus();
                protagonist.turnCodes[0] = false;
                break;
            case "Q": // quit
                this.gameState = "game-over";
                protagonist.turnCodes[0] = false;
                break;
            case "1":// magic
            case "2":
            case "3":
            case "4":
            case "5":
                  protagonist.processMagic(s);
//                println(this.game.map.mapReport(this.game.protagonist.positCol, this.game.protagonist.positRow));
                protagonist.turnCodes[0] = true;
                break;
            case "G": // get
                protagonist.getItem();
//                println(this.game.map.mapReport(this.game.protagonist.positCol, this.game.protagonist.positRow));
                protagonist.turnCodes[0] = true;
                break;
            case "A": // attack
//                this.game.protagonist.attack();
//                println(this.game.map.mapReport(this.game.protagonist.positCol, this.game.protagonist.positRow));
                protagonist.turnCodes[0] = true;
                break;
            case "N":
            case "S":
            case "E":
            case "W": // move
//                if (this.game.map.moveIsValid()) {
//                    this.game.protagonist.move(key);
//                    println(this.game.map.mapReport(this.game.protagonist.positCol, this.game.protagonist.positRow));
//                }
                protagonist.turnCodes[0] = true;
                break;
        }
    }

    public boolean inputIsValid(String _input) {
        if ("hiqHIQ12345gansweGANSWE".contains(_input)) {
            return true;
        } else {
            System.out.println("That is not a valid move (press H for help).");
            return false;
        }
    }

    public void advance(boolean[] pCodes, boolean[] eCodes) {
        if (pCodes[0] ) {  // I might need to check both, not sure yet
            turnNum++;
        }
    }

    void end() {
        System.out.println("Game over.\n");
        System.exit(0);
    }
}
