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

/*
TODO
Comments
Magic items messaging and efficacy - all five
Add a probability - "you dropped youre weapon, buttefingers" when on an edge.
Draw the map really nicely once at the beginning
Add a draw map command?
Refactor slop
Consider making into a website just for fun in the vein of ZORK, design-wise
 */