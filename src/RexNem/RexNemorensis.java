package RexNem;

public class RexNemorensis {
    public static Game game;
    public static boolean replay;

    public static void main(String[] args) {
        replay = true;  // repeat game until lose or quit

        while (replay) {
            game = new Game();
            game.run();
        }
    }
}

/*
TODO
    Add a probability - "you dropped youre weapon, buttefingers" when on an edge so players are encouraged to enter middle of board.
    Draw the map really nicely once at the beginning instead of lame one
    Draw an ascii image at the beginning
    Add a 'draw map' command or add the map as an item to find
    Add some more scenery in useless or plain areas instead of 'nothing to see here'
    Add some probabilty-driven variations in common text blurbs to reduce repetition
    Consider making into a website just for fun in the vein of ZORK, look-and-feel design-wise
 */