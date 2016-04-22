/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleship;

import battleship.players.zholt_BattleshipPlayer;

/**
 *
 * @author sada
 */
public class Battleship {

    /**
     * @param args the command line arguments
     */
   public static void main(String[] args) {
        System.out.println("Testing");
        zholt_BattleshipPlayer a = new zholt_BattleshipPlayer();
        zholt_BattleshipPlayer b = new zholt_BattleshipPlayer();
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
