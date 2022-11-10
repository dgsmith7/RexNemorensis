public class Game {
    int turnNum;
    String gameState;
    String title;
    String backStory;
    Map map;
    Player protag;
    Player enemy;

    Game () {
        System.out.println("Constructing the Game.");
        this.turnNum = 1;
        this.gameState= "init";
        this.title = "\n======================\n";
        this.title += "=   Rex Nemorensis   =\n";
        this.title += "======================\n";
        this.backStory =  "You are a mage and warrior.  For your mettle, you have been honored to serve a priesthood for the Godess Diana.  In this role, your final role, you\n";
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
        this.map = new Map();
        this.protag = new Player("hero", false);
        this.enemy = new Player("villian", true);
    }

    public void run() {
        this.gameState = "active";
        showIntro();
        showHelpReport();
        protag.showStatus();
        while (this.gameState == "active") {
            getInput();
            protag.processMove();
            enemy.processMove();
        }
    }

    public void showIntro() {
        System.out.println(this.title);
        System.out.println(this.backStory);
    }
    public String getState() {
        return this.gameState;
    }

    public void showHelpReport() {
        String helpReport = "These do not cost a turn:               These moves cost one turn:\n";
        helpReport += "H - Help                                N, S, E, W - move\n";
        helpReport += "I - Inventory and status                1, 2, 3, 4, 5 - Use Magic Item\n";
        helpReport += "                                        A - Attack with strongest weapon\n";
        helpReport += "                                        G - get item\n";
        System.out.println(helpReport);
    }

    public void getInput() {}

    public boolean inputIsValid(String _input) {
        return true;
    }

    public void processOneTurnEach() {
        turnNum++;
    }

    void end() {
        System.out.println("Game over.\n");
        System.exit(0);
    }
}
