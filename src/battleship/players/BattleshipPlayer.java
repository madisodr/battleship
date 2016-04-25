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

public interface BattleshipPlayer {

    /**
     * hideShips - This method is called once at the beginning of each game
     * when you need to hide your ships.
     *
     * You must return a valid Board object. See that class for details.
     * Note carefully: under *no* circumstances should you return the same
     * board twice in a row; i.e., two successive calls to your hideShips()
     * method must always return *different* answers!
     */
    public Board hideShips();
   
   /**
    * placeShip - This method is called on a ship to search for and find a
    * random but valid location to place the ship on the board. 
    */
    public boolean placeShip(char ship);

    /**
    *  clearShip - This method clears a ship from the board. There was some cases were
    *  not being verbose about this would corrupt the board and would leave attempted
    *  but uncomplete and failed ship placements. 
    */ 
    public void clearShip(char ship);
    
    /**
     * go - This method is called repeatedly throughout the game, every
     * time it's your turn.
     *
     * When it's your turn, and go() is called, you must call fireAt() on
     * the Board object which is passed as a parameter. You must do this
     * exactly *once*: trying to fire more than once during your turn will
     * be detected as cheating.
     */
    public void go(Board opponentsBoard);

    /** randomShot - A method used to randomly pick a point on the board 
     * to make a shot at.
     *
     * TODO: Make it smarter so it makes shots typically toward the center
     * of the board as this is where ships are more likely to be placed.
     */
    public boolean randomShot(Coordinate guess, Board opponentsBoard);


    /**
     * fire - This method is called when we want to fire a shot. 
     * It handles making the shot and updating the contents of the
     * coordinate we shot at, then adding it back into the stack of
     * shots we've made.
     */
    public boolean fire(Coordinate c, Board b);

    /** 
     * validShot - This method is called to check to see if the shot you 
     * make is valid or not. The qualifiers to determine if it is a valid
     * shot are checking to see if you have shot within bounds of the board
     * and whether or not you've made a shot at those coordinates before.
     */
    public boolean validShot(Coordinate c);


   
    /**
     * reset - This method is called when a game has ended and a new game
     * is beginning. It gives you a chance to reset any instance variables
     * you may have created, so that your BattleshipPlayer starts fresh.
     */
    public void reset();
}
