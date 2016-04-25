/* 
* Author: Daniel R Madison
* Copyright (C) 2016
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package battleship.players;

import battleship.Board;
import static battleship.Board.HEIGHT;
import static battleship.Board.WIDTH;
import java.util.ArrayList;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

public class _BattleshipPlayer implements BattleshipPlayer {

    // Create a static map class of ships and their respective sizes. 
    // Allows for cleaner code throughout. This also saves on space 
    // when multiple instances of _BattleshipPlayer are created.
    private static final Map<Character, Integer> ships;
    static { 
        ships = new HashMap<Character, Integer>();
        ships.put('A', 5);
        ships.put('B', 4);
        ships.put('D', 3);
        ships.put('S', 3);
        ships.put('P', 2);
    }

    // Coordinate class. Maintains a row, a column, and the content 
    // at a certain coordinate point on a grid. 
    public class Coordinate {

        public int row;
        public int col;
        public char content;

        // constructor
        Coordinate(int _row, int _col) {
            row = _row;
            col = _col;
        }

        // update the contents at the point.
        public void setContents(char c) {
            content = c;
        }

        public boolean isAHit() { 
            if(content != ' ')
                return true;

            return false;
        }
    }


    private final Random generator = new Random(); 
    boolean tried = false; // need to remember what the previous orientation attempt was.
    private Board[] previousBoards = new Board[100]; // previous placement of my boards.
    private char[][] pBoard = new char[Board.HEIGHT][Board.WIDTH]; // Board maintained as a char matrix
    private ArrayList<Coordinate> previousShots = new ArrayList<>(); // What shots we've made.

    /**
     * hideShips - This method is called once at the beginning of each game when
     * you need to hide your ships.
     *
     * You must return a valid Board object. See that class for details. Note
     * carefully: under *no* circumstances should you return the same board
     * twice in a row; i.e., two successive calls to your hideShips() method
     * must always return *different* answers!
     */
    @Override
    public Board hideShips() {
        boolean good;
        Board mBoard = null; 
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                pBoard[i][j] = ' ';
            }
        }

        for(Map.Entry<Character, Integer> ship : ships.entrySet()) { 
            good = false;
            while(!good) { 
                good = placeShip(ship.getKey());
            }
        }

        try {
            mBoard = new Board(pBoard);
        } catch (Exception ex) {
            System.out.println(toString(pBoard));
        }

        return mBoard;
    }


    /**
     * placeShip - This method is called on a ship to search for and find a
     * random but valid location to place the ship on the board. 
     */
    private boolean placeShip(char ship) {
        int num = ships.get(ship); // get the size of the ship
        int row, col; // init row and col vars
        int o; // orientation. v = 0, h = 1
        char[][] tryBoard = pBoard; // temp board for testing on

        // randomly select an orientation.
        o = generator.nextInt(2);

        // if the orientation is 0 or we've not tried this orientation yet
        // then we go for a vertical placement, otherwise we try horizontal.
        if (o == 0 && !tried) { // set random row/col within vertical bounds
            row = generator.nextInt(WIDTH - num);
            col = generator.nextInt(HEIGHT);
            tried = true;
        } else { // set random row/col within horizontal bounds
            row = generator.nextInt(WIDTH);
            col = generator.nextInt(HEIGHT - num);
            tried = false;
        }


        // depending orientation, we try to place the ship at a point.
        // If that point is anything other then an empty space, then 
        // we clear the current ship from the board and return out to
        // try again. 
        for (int i = 0; i < num; i++) {
            if (o == 0) { // try a vertical placement
                if (tryBoard[row + i][col] != ' ') {
                    tryBoard[row + i][col] = pBoard[row + i][col]; 
                    clearShip(ship);
                    return false;
                }

                tryBoard[row + i][col] = ship; // good placement
            } else { // try a horizontal placement
                if (tryBoard[row][col + i] != ' ') {
                    tryBoard[row][col + i] = pBoard[row][col + i];
                    clearShip(ship);
                    return false;
                }

                tryBoard[row][col + i] = ship; // good placement
            }
        }

        // This will only be reached if the ship has been placed. In this case
        // we update are real board, reset the tried variable, and return true
        tried = false;
        pBoard = tryBoard;
        return true;
    }

    /**
     *  clearShip - This method clears a ship from the board. There was some cases were
     *  not being verbose about this would corrupt the board and would leave attempted
     *  but uncomplete and failed ship placements. 
     */
    public void clearShip(char ship) {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (pBoard[i][j] == ship) {
                    pBoard[i][j] = ' ';
                }
            }
        }
    }

    /**
     * go - This method is called repeatedly throughout the game, every time
     * it's your turn.
     *
     * When it's your turn, and go() is called, you must call fireAt() on the
     * Board object which is passed as a parameter. You must do this exactly
     * *once*: trying to fire more than once during your turn will be detected
     * as cheating.
     */
    @Override
    public void go(Board opponentsBoard) {
        Coordinate guess = null;
        char shot = ' ';
        boolean valid = false;
        int row, col;
        int stuckCounter = 0;



        // This is our first shot, so we take our guess to be the center square. 
        // The center of the board is more then likely to hold a ship then the
        // edges which is why we start here. 
        if (previousShots.isEmpty()) {
            guess = new Coordinate(HEIGHT / 2, WIDTH / 2);
            valid = fire(guess, opponentsBoard);
        } else { // Lets try to make a smart shot. 
            Coordinate lastShot = previousShots.get(previousShots.size() - 1);
            if(previousShots.size() > 1) { 
                Coordinate nextLastShot = previousShots.get(previousShots.size() - 2); 
                if(lastShot.isAHit()) { // The last shot we made was a hit. 
                    do {
                        // check the shot before that. Is there a pattern?
                        if(nextLastShot.isAHit()) { 
                            if(lastShot.row == nextLastShot.row) { // both shots on same row
                                if(lastShot.col > nextLastShot.col) { // look to the right
                                    guess = new Coordinate(lastShot.row, lastShot.col + 1);
                                    if(validShot(guess)) 
                                        valid = fire(guess, opponentsBoard);
                            
                                } else { // look to the left
                                    guess = new Coordinate(lastShot.row, lastShot.col - 1);
                                    if(validShot(guess)) 
                                        valid = fire(guess, opponentsBoard);
                                    
                                }
                            } else { // both shots on same column
                                if(lastShot.row > nextLastShot.row) { // look below
                                    guess = new Coordinate(lastShot.row + 1, lastShot.col);
                                    if(validShot(guess)) 
                                        valid = fire(guess, opponentsBoard);
                                    
                                } else { // look above
                                    guess = new Coordinate(lastShot.row - 1, lastShot.col);
                                    if(validShot(guess)) 
                                        valid = fire(guess, opponentsBoard);
                                    
                                }
                            }

                            if(!valid) {
                                // Handle invalid shot making even if there was a pattern
                                // To do this, we flip and go the opposite direction.
                                if(lastShot.row == nextLastShot.row) { // both shots on same row
                                    if(lastShot.col > nextLastShot.col) { // look to left of nextLastShot
                                        guess = new Coordinate(lastShot.row, nextLastShot.col - 1);
                                        if(validShot(guess)) 
                                            valid = fire(guess, opponentsBoard);
                                        
                                    } else { // look to the right of nextLastShot 
                                        guess = new Coordinate(lastShot.row, nextLastShot.col + 1);
                                        if(validShot(guess)) 
                                            valid = fire(guess, opponentsBoard);
                                        
                                    }
                                } else { // both shots on same column
                                    if(lastShot.row > nextLastShot.row) { // look above nextLastShot
                                        guess = new Coordinate(nextLastShot.row - 1, lastShot.col);
                                        if(validShot(guess)) 
                                            valid = fire(guess, opponentsBoard);
                                        
                                    } else { // look below nextLastShot
                                        guess = new Coordinate(nextLastShot.row + 1, lastShot.col);
                                        if(validShot(guess)) 
                                            valid = fire(guess, opponentsBoard);
                                        
                                    }

                                    // if valid is still false, then we can't make an informed shot.
                                    // So guess randomly.
                                    if(!valid) { valid = randomShot(guess, opponentsBoard); }
                                }
                            }
                        } else {
                            valid = randomShot(guess, opponentsBoard);
                        }

                        stuckCounter++;
                        if(stuckCounter ==5)
                            valid = randomShot(guess, opponentsBoard);

                        if (stuckCounter > 5) {
                            System.out.println("Unable to find a valid shot.");
                            new Exception().printStackTrace();
                            System.exit(0);
                        }


                    } while(valid == false);
                } else { // Didn't find a hit yet. So guess randomly
                    valid = randomShot(guess, opponentsBoard);
                }
            } else { // We havn't made enough shots to discover a pattern.
                do {
                    int dir = generator.nextInt(4);
                    switch (dir) {
                        case 0: // left
                            col = lastShot.col;
                            if ((col - 1) >= 0) { // in bounds 
                                guess = new Coordinate(lastShot.row, col - 1);
                                if (validShot(guess))
                                    valid = fire(guess, opponentsBoard);
                                
                            }
                            break;
                        case 1: // right
                            col = lastShot.col;
                            if ((col + 1) < WIDTH) { // in bounds 
                                guess = new Coordinate(lastShot.row, col + 1);
                                if (validShot(guess)) 
                                    valid = fire(guess, opponentsBoard);
                                
                            }
                            break;
                        case 2: // up
                            row = lastShot.row;
                            if ((row - 1) >= 0) { // in bounds 
                                guess = new Coordinate(row - 1, lastShot.col);
                                if (validShot(guess)) 
                                    valid = fire(guess, opponentsBoard);
                                
                            }
                            break;
                        case 3: // down
                            row = lastShot.row;
                            if ((row + 1) < HEIGHT) { // in bounds 
                                guess = new Coordinate(row + 1, lastShot.col);
                                if (validShot(guess))
                                    valid = fire(guess, opponentsBoard);
                            
                            }
                            break;

                    }

                    // We didn't make a smart shot so make a random selection.
                    if(!valid) { valid = randomShot(guess, opponentsBoard); }
                        
                } while (valid == false);
            }
        }
    }
    
    /** randomShot - A method used to randomly pick a point on the board 
     * to make a shot at.
     *
     * TODO: Make it smarter so it makes shots typically toward the center
     * of the board as this is where ships are more likely to be placed.
     */
    public boolean randomShot(Coordinate guess, Board opponentsBoard) { 
        int row = generator.nextInt(HEIGHT);
        int col = generator.nextInt(WIDTH);
        guess = new Coordinate(row, col);

        if(validShot(guess))
            return fire(guess, opponentsBoard);
        else
            return false;

    }
    /**
     * fire - This method is called when we want to fire a shot. 
     * It handles making the shot and updating the contents of the
     * coordinate we shot at, then adding it back into the stack of
     * shots we've made.
     */
    public boolean fire(Coordinate c, Board b) {
        char mander = b.fireAt(c.row, c.col);
        c.setContents(mander);
        previousShots.add(c);
        return true;
    }

    /** 
     * validShot - This method is called to check to see if the shot you 
     * make is valid or not. The qualifiers to determine if it is a valid
     * shot are checking to see if you have shot within bounds of the board
     * and whether or not you've made a shot at those coordinates before.
     */
    public boolean validShot(Coordinate c) {

        // boundary checking
        if(c.row < 0 || c.row >= HEIGHT)
            return false;

        if(c.col < 0 || c.col >= WIDTH)
            return false;

        // Make sure we havn't tried this shot before.
        for(int i = 0; i < previousShots.size(); i++) {
            Coordinate test = previousShots.get(i);

            if((c.row == test.row ) && (c.col == test.col))
                return false;
        }
        return true;
    }

    /**
     * reset - This method is called when a game has ended and a new game is
     * beginning. It gives you a chance to reset any instance variables you may
     * have created, so that your BattleshipPlayer starts fresh.
     */
    @Override
    public void reset() {
        pBoard = new char[Board.HEIGHT][Board.WIDTH];
        previousShots = new ArrayList<>();
    }

    String toString(char[][] board) {
        String x = "";

        for (int i = 0; i < Board.HEIGHT; i++) {
            for (int j = 0; j < Board.WIDTH; j++) {

                switch (board[i][j]) {
                    case ' ':
                        x += ". ";
                        break;
                    case 'B':
                    case 'A':
                    case 'D':
                    case 'S':
                    case 'P':
                        x += board[i][j] + " ";
                        break;
                    default:
                        x += "?";
                        break;

                }
            }

            x += "\n";
        }
        return x;
    }
}
