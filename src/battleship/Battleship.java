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

package battleship;

import battleship.players._BattleshipPlayer;

/**
 * @author Daniel R. Madison
 * @version 1.1
 *
 * The main driver of the program.
 */
public class Battleship {
   public static void main(String[] args) {
        System.out.println("Testing");
        _BattleshipPlayer a = new _BattleshipPlayer();
        _BattleshipPlayer b = new _BattleshipPlayer();
        Board aBoard = a.hideShips();
        System.out.println("Player A's Board");
        System.out.println(aBoard);
        
        
        
        Board bBoard = b.hideShips();
        System.out.println("Player B's Board");
        System.out.println(bBoard);
        //a.reset();
        //b.reset();
        int step = 1;
        while(1 + 1 == 2) {
            System.out.println("STEP " + step + " ================");
            
            System.out.println("Player A's Turn");
            bBoard.firedAtThisRound = false;
            a.go(bBoard);
            
            System.out.println(bBoard);
            System.out.println("Player B's Turn");
            aBoard.firedAtThisRound = false;
            b.go(aBoard);
            System.out.println(aBoard);
            if (aBoard.isComplete()) {
                System.out.println("B Wins!");
                break;
            } else  if (bBoard.isComplete()) {
                System.out.println("A Wins!");
                break;
            }
            step++;
        }
    }
}
