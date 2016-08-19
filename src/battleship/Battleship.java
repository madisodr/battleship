package battleship;

import battleship.players._BattleshipPlayer;

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
