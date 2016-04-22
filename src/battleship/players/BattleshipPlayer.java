
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
     * go - This method is called repeatedly throughout the game, every
     * time it's your turn.
     *
     * When it's your turn, and go() is called, you must call fireAt() on
     * the Board object which is passed as a parameter. You must do this
     * exactly *once*: trying to fire more than once during your turn will
     * be detected as cheating.
     */
    public void go(Board opponentsBoard);

    /**
     * reset - This method is called when a game has ended and a new game
     * is beginning. It gives you a chance to reset any instance variables
     * you may have created, so that your BattleshipPlayer starts fresh.
     */
    public void reset();
}
