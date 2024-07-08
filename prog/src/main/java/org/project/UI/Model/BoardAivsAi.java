package org.project.UI.Model;

import org.project.Logic.Game.Board;
import org.project.Logic.Game.player.ai.PlayerAi;

/**
 * Class that represents the game board.
 */
public class BoardAivsAi extends Board {
    BoardAivsAi(PlayerAi[] players) {
        super(players[0], players[1]);
    }


    @Override
    public Board copy() {
        BoardAivsAi newBoard = new BoardAivsAi((PlayerAi[]) players);
        newBoard.setGrid(grid);
        newBoard.win = win;
        return newBoard;
    }
}


