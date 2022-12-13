package RexNem;

public class GameMap {
    public static String[] layout;
    public static int mapCol;
    public static int mapRow;

    GameMap() {
        mapCol = 8; // map dimensions
        mapRow = 8; // map dimensions
        layout = new String[mapRow];
        this.generateLayout();
        this.locateWeaponsAndMagicItems();
    }

    public void generateLayout() {
        // if you make a bigger map, adjust mapCol and mapRow above appropriately
        // map codes for initial build: B - bare, W-wall, H-Hole
//        layout[0] = "12345ASD";  // for testing
//        layout[1] = "HWBBBBHB";  // for testing
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
        // map codes for item pass: A - axe, S - sword, D - shield, 12345 - magical items
        for (int i = 0; i < 6; i++) { // add 6 items
            int col = (int) (Math.random() * mapCol);
            int row = (int) (Math.random() * mapRow);
            while (layout[row].charAt(col) != 'B') {  //  find a bare spot on map - no hole, no wall, no items
                col = (int) (Math.random() * mapCol);
                row = (int) (Math.random() * mapRow);
            }
            if (i == 0) { // axe
                layout[row] = replaceChar(col, layout[row], 'A');
            } else if (i <= 2) {  // 2 swords
                layout[row] = replaceChar(col, layout[row], 'S');
            } else if (i == 3) {  // shield
                layout[row] = replaceChar(col, layout[row], 'D');
            } else { // 2 random magic items
                layout[row] = replaceChar(col, layout[row], String.valueOf(Math.floor(Math.random() * 5 + 1)).charAt(0));
            }
        }
    }

    public void displayMap() {  //  draws the map - happens unannounced only at start - kind of an easter egg
        for (int row = 0; row < mapRow; row++) {
            for (int col = 0; col < mapCol; col++) {
                System.out.print(layout[row].charAt(col) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public String oneLine(int row) {
        String line = "";
        for (int col = 0; col < mapCol; col++) {
            line += layout[row].charAt(col) + " ";
        }
        return line;
    }

    public static String replaceChar(int i, String s, char c) {  // utility used when doing weapons pass on build
        String newStr = "";
        for (int j = 0; j < 8; j++) {
            if (j == i) {
                newStr += c;
            } else {
                newStr += s.charAt(j);
            }
        }
        return newStr;
    }

    public boolean notWall(int col, int row) {  // check to make sure there is no wall at passed cords
        if (col < 0 || row < 0 || col > GameMap.mapCol - 1 || row > GameMap.mapRow - 1) { // no wall, but ledge
            return true;
        } else {
            return layout[row].charAt(col) != 'W';
        }
    }

    public void mapReport(int col, int row) {  // report on map status - happens each turn
        String report = "You are located in a grove on a mesa on row " + Game.protagonist.positRow + " - column " + Game.protagonist.positCol + "\n";
        if (Game.enemy.invisibility == 0) {
            report += "You spot your enemy on row " + Game.enemy.positRow + " - column " + Game.enemy.positCol + "\n";
        } else {
            report += "The enemy is invisible at the moment.  Sneaky!\n";
        }
        char nChar = 'L';
        char sChar = 'L';
        char wChar = 'L';
        char eChar = 'L';
        boolean signif = false;
        // look in each direction for reportable info
        if (col != 0) {
            wChar = layout[row].charAt(col - 1);
        }
        if (col != 7) {
            eChar = layout[row].charAt(col + 1);
        }
        if (row != 0) {
            nChar = layout[row - 1].charAt(col);
        }
        if (row != 7) {
            sChar = layout[row + 1].charAt(col);
        }
        // holes
        if (nChar == 'H' || sChar == 'H' || wChar == 'H' || eChar == 'H') {
            report += "A foul, hot stench rises from a nearby hole in the ground.\n";
        }
        // walls
        if (nChar == 'W') report += "A raven circles high above the wall of a ruin to the North.\n";
        if (sChar == 'W') report += "You feel the cool moisture of a stone wall to your South.\n";
        if (wChar == 'W') report += "A granite boulder to the West blocks out the light.\n";
        if (eChar == 'W') report += "The east is impassable due to a barrier.\n";
        // edges
        if (row == 0) {
            report += "The view is great, but you feel your stomach tighten as you slip on some gravel near the Northern precipice.\n";
        }
        if (row == 7) {
            report += "Something deep below your guts begins to pucker because your are near the high Southern rim.\n";
        }
        if (col == 0) {
            report += "Your head feels light and your vision grows dim near the Western cliff-edge of the grove.\n";
        }
        if (col == 7) {
            report += "Your knees tremble as you peer over the Eastern ledge at the gorgeous vista of Lake Nemi.\n";
        }
        // magic items
        if (layout[row].charAt(col) == '1') {
            report += "You see a scarlet cloak among a pile of bones here.\n";
            signif = true;
        }
        if (layout[row].charAt(col) == '2') {
            report += "A metal glove hangs from a golden bough of a nearby dead tree.\n";
            signif = true;
        }
        if (layout[row].charAt(col) == '3') {
            report += "There is a delicate clay bottle containing an iridescent liquid atop an altar.\n";
            signif = true;
        }
        if (layout[row].charAt(col) == '4') {
            report += "A ring with an inscription, smelt of rare metal, that is laying at your feet, begins to vibrate.\n";
            signif = true;
        }
        if (layout[row].charAt(col) == '5') {
            report += "An ornate crown fit for a king glows brightly as you approach.\n";
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
        if (signif) report += "Press 'g' to get this item.\n";
        // enemy
        if ((Math.abs(Game.protagonist.positCol - Game.enemy.positCol) <= 1) && (Math.abs(Game.protagonist.positRow - Game.enemy.positRow) <= 1)) {
            report += "Your adreneline surges as you sense the closeness of your enemy.\n";
            signif = true;
        }
        if (!signif) {
            report += "There is nothing else to see here.\n";
        }
        System.out.println(report);
    }
}