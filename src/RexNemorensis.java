public class RexNemorensis {
    public static Game game;
    public static boolean replay;

    public static void main(String[] args) {
        replay = true;
        while (replay) {
            game = new Game();
            game.run();
        }
        System.out.println("\nYou defeated " + Game.protagonist.wins + " challengers.");
    }
}