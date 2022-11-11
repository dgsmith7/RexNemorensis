public class GameMap {
/* TODO
    make a much larger map with more stuff
 */

    public static String[] layout;
    public static int mapCol;
    public static int mapRow;

    GameMap () {
        System.out.println("-------Constructing the map.");
        // map dimensions follow
        mapCol = 8;
        mapRow = 8;
        layout = new String[mapRow];
        this.generateLayout();
        this.locateWeaponsAndMagicItems();
        this.displayMap();
    }

    public void generateLayout() {
        // adjust this.mapCol and mapRow appropriately
        // B - bare, W-wall, H-Hole
        layout[0] = "BBBBBBBB";
        layout[1] = "BWBBBBHB";
        layout[2] = "BWWWWBBB";
        layout[3] = "BWBBWBBB";
        layout[4] = "BBBBWBWB";
        layout[5] = "BBBBBBWB";
        layout[6] = "BBBWWWWB";
        layout[7] = "BBBBBBBB";
    }

    public void locateWeaponsAndMagicItems() {
        // A - axe, S - sword, D - shield, 12345 - magical item
        for (int i = 0; i < 6; i ++) {
            int col = (int)(Math.random() * mapCol);
            int row = (int)(Math.random() * mapRow);
            while (layout[row].charAt(col) != 'B') {
                col = (int)(Math.random() * mapCol);
                row = (int)(Math.random() * mapRow);
            }
            if (i == 0) { // axe
                layout[row] = replaceChar(col, layout[row], 'A');
            } else if (i <= 2) {
                layout[row] = replaceChar(col, layout[row], 'S');
            } else if (i == 3) {
                layout[row] = replaceChar(col, layout[row], 'D');
            } else {
                layout[row] = replaceChar(col, layout[row], String.valueOf(Math.floor(Math.random()  * 5 + 1)).charAt(0));
            }
        }
    }
    public void displayMap() {
        for (int row = 0; row < mapRow; row ++) {
            for (int col = 0 ; col < mapCol; col ++) {
                System.out.print(layout[row].charAt(col) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public String replaceChar(int i, String s, char c) {
        String newStr = "";
        for (int j = 0; j < 8; j ++) {
            if (j == i) {
                newStr += c;
            } else {
                newStr += s.charAt(j);
            }
        }
        return newStr;
    }

    public boolean noWall(int col, int row) {
        if (layout[row].charAt(col) != 'W') {
            return true;
        } else {
            return false;
        }
    }

    public String mapReport(int col, int row) {
        String report = "";
        char nChar = 'L';
        char sChar = 'L';
        char wChar = 'L';
        char eChar = 'L';
        boolean signif = false;
        System.out.println();
        System.out.println("You are on col " + col + " row " + row + ". The map code is " + layout[row].charAt(col));
        System.out.println("Its turn number " + Game.turnNum);
        // look in each direction for stuff
        if (col != 0) {
            wChar = layout[row].charAt(col-1);
        }
        if (col != 7) {
            eChar = layout[row].charAt(col+1);
        }
        if (row != 0) {
            nChar = layout[row-1].charAt(col);
        }
        if (row != 7) {
            sChar = layout[row+1].charAt(col);
        }
        // holes
        if (nChar == 'H' || sChar == 'H' || wChar == 'H' || eChar == 'H') {
            report += "A foul, hot stench rises from a nearby hole in the ground.\n";
        }
        // walls
        if (nChar == 'W') report += "A raven circle high above the wall of a ruin to the North.\n";
        if (sChar == 'W') report += "You feel the cool moisture of a stone wall to your South.\n";
        if (wChar == 'W') report += "A granite boulder to the West blocks out the light.\n";
        if (eChar == 'W') report += "The east is impassable due to a barrier.\n";
        // edges
        if (row == 0) {
            report += "You feel your bile rising as you stand near the Northern precipice.\n";
        }
        if (row == 7) {
            report += "Something deep below your guts begins to pucker because your are near the high Southern rim.\n";
        }
        if (col == 0) {
            report += "Your head feels light and your vision grows dim near the Western cliff-edge of the grove.\n";
        }
        if (col == 7) {
            report += "Your knees tremble as you peer over the Eastern ledge.\n";
        }
        // magic items
        if (layout[row].charAt(col) == '1') {
            report += "You see a scarlet cloak among a pile of bones here.\n";
            signif = true;
        }
        if (layout[row].charAt(col) == '2') {
            report += "A metal glove hangs from a golden bough of a nearby dead tree.";
            signif = true;
        }
        if (layout[row].charAt(col) == '3') {
            report += "There is a delicate clay bowl containg an irridescent liquid atop an altar.";
            signif = true;
        }
        if (layout[row].charAt(col) == '4') {
            report += "A ring smelt of rare metal with an inscription lies at your feet";
            signif = true;
        }
        if (layout[row].charAt(col) == '5') {
            report += "An ornate crown fit for a king glows brightly as you approach.";
            signif = true;
        }
        // weapons
        if (layout[row].charAt(col) == 'A') {
            report += "A heavy axe with a keen edge is stuck in a nearby stump.\n";
            signif = true;
        }
        if (layout[row].charAt(col) == 'S') {
            report += "You hear a low hum coming from a well-crafted sword on the ground.\n";
            signif = true;
        }
        if (layout[row].charAt(col) == 'D') {
            report += "You see a sturdy shield propped on a rock.\n";
            signif = true;
        }
        // enemy
        if ((Math.abs(Game.protagonist.getPositCol() - Game.enemy.positCol) <= 1) && (Math.abs(Game.protagonist.positRow - Game.enemy.positRow) <= 1)) {
            report += "Your adreneline surges as you sense the closeness of your enemy.\n";
            signif = true;
        }
        if (!signif) {
            report += "There is nothing else to see here.\n";
        }
        return report;
    }
}
