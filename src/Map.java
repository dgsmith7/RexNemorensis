/* TODO
        make a much larger map with more stuff
 */
public class Map {

    String[] layout;
    int mapCol;
    int mapRow;

    Map () {
        System.out.println("-------Constructing the map.");
        // map dimensions follow
        this.mapCol = 8;
        this.mapRow = 8;
        this.layout = new String[mapRow];
        this.generateLayout();
        this.locateWeaponsAndMagicItems();
        this.displayMap();
    }

    void generateLayout() {
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

    void locateWeaponsAndMagicItems() {
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

    String replaceChar(int i, String s, char c) {
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
        if (this.layout[row].charAt(col) != 'W') {
            return true;
        } else {
            return false;
        }
    }
}
