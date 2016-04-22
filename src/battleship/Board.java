
package battleship;

public class Board {

    // The height and width of the board, in squares. Note that this may
    // change without notice!
    public static final int HEIGHT = 10;
    public static final int WIDTH = 10;

    // Cheating detector, with package visibility so mischievous students
    // can't tamper with it.
    boolean firedAtThisRound;

    // Change this to "true" only if you're using Linux and the command
    // line.
    public static boolean GOOD_OPERATING_SYSTEM = false;

    /**
     * Constructor that takes a 2-dimensional array of characters as an
     * argument. The array *must* represent a legitimate board, otherwise
     * an Exception will be thrown. A "legitimate" board is one that has
     * exactly five overlapping ships of the proper length (A - length 5,
     * B - length 4, S - length 3, D - length 3, P - length 2) and has all
     * other squares set to ' ' (space.)
     */
    public Board(char[][] contents) throws Exception {

        squares = contents;
        if (!legit()) {
            throw new Exception("Bad board!\n" + toString(0));
        }
        for (int i=0; i<HEIGHT; i++) {
            for (int j=0; j<WIDTH; j++) {
                firedSquares[i][j] = false;
            }
        }
    }

    /**
     * fireAt - Call this method once per round to fire at a square on this
     * board. The contents of the square will be revealed in the return
     * value.
     */
    public char fireAt(int row, int col) {
        if (firedAtThisRound) {
            System.out.println("you CHEATED!");
            new Exception().printStackTrace();
            System.exit(1);
        }
        firedAtThisRound = true;
        firedSquares[row][col] = true;
        return squares[row][col];
    }

    /**
     * toString - You can use this during debugging to dump the contents of
     * the board to the screen.
     */
    public String toString() {
        return toString(0);
    }



    private char squares[][];
    private boolean firedSquares[][] = new boolean[HEIGHT][WIDTH];

    private boolean legit() {
        if (hasShip('B', 4) &&
            hasShip('A', 5) &&
            hasShip('S', 3) &&
            hasShip('D', 3) &&
            hasShip('P', 2) &&
            hasOnlyLegalCharacters()) {
            return true;
        }
        return false;
    }

    private boolean hasOnlyLegalCharacters() {
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                char x = squares[i][j];
                if (x != 'A' && 
                    x != 'B' && 
                    x != 'D' && 
                    x != 'S' && 
                    x != 'P' && 
                    x != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasShip(char ship, int num) {
        if (countSquares(ship) != num) {
            return false;
        }
        if (existsContiguousShip(ship,num)) {
            return true;
        }
        return false;
    }

    private int countSquares(char ship) {
        int total = 0;
        for (int i=0; i<HEIGHT; i++) {
            for (int j=0; j<WIDTH; j++) {
                if (squares[i][j] == ship) {
                    total++;
                }
            }
        }
        return total;
    }

    private boolean existsContiguousShip(char ship, int num) {
        int firstRow = -1, firstCol = -1;
        for (int i=0; i<HEIGHT; i++) {
            for (int j=0; j<WIDTH; j++) {
                if (squares[i][j] == ship  &&  firstRow == -1  &&
                    firstCol == -1) {
                    firstRow = i;
                    firstCol = j;
                }
            }
        }
        if (firstRow == -1 || firstCol == -1) {
            return false;
        }

        if (existsHorizontalShipStartingAt(firstRow,firstCol,num) ||
            existsVerticalShipStartingAt(firstRow,firstCol,num)) {
            return true;
        }
        return false;
    }

    private boolean existsHorizontalShipStartingAt(int r, int c, int n) {
        char ship = squares[r][c];
        if (WIDTH - c < n) {
            return false;
        }
        for (int i=1; i<n; i++) {
            if (squares[r][c+i] != ship) {
                return false;
            }
        }
        return true;
    }

    private boolean existsVerticalShipStartingAt(int r, int c, int n) {
        char ship = squares[r][c];
        if (HEIGHT - r < n) {
            return false;
        }
        for (int i=1; i<n; i++) {
            if (squares[r+i][c] != ship) {
                return false;
            }
        }
        return true;
    }

    boolean isComplete() {
        for (int i=0; i<HEIGHT; i++) {
            for (int j=0; j<WIDTH; j++) {
                if (firedSquares[i][j] == false  &&
                        squares[i][j] != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    String toString(int rightPad) {
        String x = "";
        if (GOOD_OPERATING_SYSTEM) {
            x += "[" + rightPad + "C";
        }
        for (int i=0; i<HEIGHT; i++) {
            for (int j=0; j<WIDTH; j++) {
                if (firedSquares[i][j]) {
                    if(squares[i][j] == ' ')
                        x += "\u25ae\t";
                    else
                        x += "x\t";
                }
                else {
                    switch (squares[i][j]) {
                    case ' ':
                        x += ".\t";
                        break;
                    case 'B':
                    case 'A':
                    case 'D':
                    case 'S':
                    case 'P':
                        x += squares[i][j] + "\t";
                        break;
                    default:
                        x += "?";
                        break;
                    }
                }
            }
            if (GOOD_OPERATING_SYSTEM) {
                x += "\n[" + rightPad + "C";
            } else {
                x += "\n";
            }
        }
        return x;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Board)) {
            return false;
        }
        Board b = (Board) o;
        for (int i=0; i<HEIGHT; i++) {
            for (int j=0; j<HEIGHT; j++) {
                if (b.squares[i][j] != squares[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
