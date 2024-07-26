package org.project.UI.Model;

import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import org.project.Logic.Game.Board;
import org.project.Logic.Game.player.ai.PlayerAi;
import org.project.Logic.embAsp.cell;

import java.awt.*;

import static org.project.UI.Settings.BOARD_COLS;
import static org.project.UI.Settings.BOARD_ROWS;

/**
 * The class is used to manage the game board for Ai players.<p>
 * The main difference with the class Board is the {@code ASPInputProgram gridState} that is used
 * to represent the board state in the ASP logic.
 */
public class BoardAivsAi extends Board {
    private final ASPInputProgram gridState;

    //--CONSTRUCTORS---------------------------------------------------------------------------------------------------------
    private void initGridState() throws Exception {
        gridState.clearAll();
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                gridState.addObjectInput(grid[i][j]);
            }
        }
    }

    /**
     * Constructor for the class BoardAivsAi.
     * Makes a copy of the players and initializes the grid.
     *
     * @param players the players of the game.
     */
    BoardAivsAi(PlayerAi[] players) throws Exception {
        super(players[0], players[1]);
        gridState = new ASPInputProgram();
        initGridState();
    }

    /**
     * Constructor for the class BoardAivsAi.
     * Makes a copy of the players and initializes the grid.
     *
     * @param player1 the first player of the game.
     * @param player2 the second player of the game.
     */
    BoardAivsAi(PlayerAi player1, PlayerAi player2) throws Exception {
        super(player1, player2);
        gridState = new ASPInputProgram();
        initGridState();
    }

    protected BoardAivsAi(BoardAivsAi board) throws Exception {
        super(board);
        gridState = new ASPInputProgram();
        initGridState();
    }

    @Override
    public BoardCopy copy() {
        BoardCopy newBoard = null;
        try {
            newBoard = new BoardCopy(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return newBoard;
    }

//--EMBASP--------------------------------------------------------------------------------------------------------------

    /**
     * Get the current state of the board.<p>
     * Each cell is represented by a {@link cell }object.
     *
     * @return the gridState.
     */
    public ASPInputProgram getGridState() {
        return gridState;
    }

    private void refreshGridState() throws Exception {
        gridState.clearAll();
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                gridState.addObjectInput(grid[i][j]);
            }
        }
    }

    //--GAME METHODS--------------------------------------------------------------------------------------------------------
    boolean moveUnitSafe(int unitCode, Point coord) {
        super._moveUnitSafe(unitCode, coord);
        try {
            refreshGridState();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }


    boolean buildFloorSafe(int unitCode, Point coord) {
        super._buildFloorSafe(unitCode, coord);
        try {
            refreshGridState();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;

    }


//--INNER CLASS---------------------------------------------------------------------------------------------------------

    /**
     * This class cannot be instantiated outside the package, it is used to manage the copy of the board.<p>
     * The difference with the class BoardAivsAi is that the action methods are public and can be used to test the board.
     */
    public static class BoardCopy extends BoardAivsAi {
        BoardCopy(BoardAivsAi board) throws Exception {
            super(board);
        }


        @Override
        public boolean moveUnitSafe(int unitCode, Point coord) {
            return super.moveUnitSafe(unitCode, coord);
        }


        @Override
        public boolean buildFloorSafe(int unitCode, Point coord) {
            return super.buildFloorSafe(unitCode, coord);
        }
    }
}


