public class Player {
    int gamesWon;
    String name;
    boolean bot;

    Player(String _name, boolean _bot) {
        System.out.println("Constructing a player named " + _name + ".");
        this.name = _name;
        this.bot = _bot;
    }
    public void showStatus() {
        System.out.println("The will show the player status report");
    }

    public void processMove() {

    }
}
