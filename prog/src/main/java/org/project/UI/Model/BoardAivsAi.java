package org.project.UI.Model;

import org.project.Logic.Game.Board;
import org.project.Logic.Game.player.ai.PlayerAi;

/**
 * Class that represents the game board.
 */
public class BoardAivsAi extends Board {

    /**
     * Constructor for the class BoardAivsAi.
     * Makes a copy of the players and initializes the grid.
     * @param players the players of the game.
     */
    BoardAivsAi(PlayerAi[] players) {
        super(players[0], players[1]);
    }
    /**
     * Constructor for the class BoardAivsAi.
     * Makes a copy of the players and initializes the grid.
     * @param player1 the first player of the game.
     * @param player2 the second player of the game.
     */
    BoardAivsAi(PlayerAi player1, PlayerAi player2) {
        super(player1, player2);
    }

    private BoardAivsAi(){
        super();

    }
    @Override
    public Board copy() {
        BoardAivsAi newBoard = new BoardAivsAi();
        newBoard.copyGrid(grid);
        newBoard.copyPlayers(players);

        newBoard.win = win;
        return newBoard;
    }
}


