package org.project.UI.Model;

import org.project.Logic.Game.Board;
import org.project.Logic.Game.player.Player;

/**
 * Class that represents the game board.
 */
public class BoardHvH extends Board {

    BoardHvH() {
        super();
    }

    BoardHvH(Player player1, Player player2) {
        super(player1, player2);
    }
}


