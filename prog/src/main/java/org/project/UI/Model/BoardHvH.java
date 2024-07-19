package org.project.UI.Model;

import org.project.Logic.Game.Board;
import org.project.Logic.Game.player.human.PlayerManual;

/**
 * Class that represents the game board.
 */
public class BoardHvH extends Board {


    BoardHvH(PlayerManual[] players) {
        super(players[0], players[1]);
    }


    @Override
    protected Board copy() {
        throw new RuntimeException("Not implemented");
    }
}


