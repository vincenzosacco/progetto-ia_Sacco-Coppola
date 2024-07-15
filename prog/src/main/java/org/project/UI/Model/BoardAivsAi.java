package org.project.UI.Model;

import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import org.project.Logic.Game.Board;
import org.project.Logic.Game.player.Unit;
import org.project.Logic.Game.player.ai.PlayerAi;
import org.project.Logic.embAsp.cell;

import java.awt.*;
import java.util.ArrayList;

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
    /**
     * Constructor for the class BoardAivsAi.
     * Makes a copy of the players and initializes the grid.
     * @param players the players of the game.
     */
    BoardAivsAi(PlayerAi[] players) {
        super(players[0], players[1]);
        gridState= new ASPInputProgram();
    }

    /**
     * Constructor for the class BoardAivsAi.
     * Makes a copy of the players and initializes the grid.
     * @param player1 the first player of the game.
     * @param player2 the second player of the game.
     */
    BoardAivsAi(PlayerAi player1, PlayerAi player2) {
        super(player1, player2);
        gridState= new ASPInputProgram();
    }

    private BoardAivsAi(){
        super();
        gridState= new ASPInputProgram();
    }

    @Override
    public Board copy() {
        BoardAivsAi newBoard = new BoardAivsAi();
        newBoard.copyGrid(grid);
        try {
            refreshGridState();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        newBoard.copyPlayers(players);
        newBoard.win = win;
        return newBoard;
    }

//--GETTERS-------------------------------------------------------------------------------------------------------------
    /**
     * Get the current state of the board.<p>
     * Each cell is represented by a {@link cell }object.
     * @return the gridState.
     */
    public ASPInputProgram getGridState() {
        return gridState;
    }

//--EMBASP--------------------------------------------------------------------------------------------------------------
    private void refreshGridState() throws Exception {
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                gridState.addObjectInput((new cell(i, j, grid[i][j], playerCodeAt(i, j))));
            }
        }
    }

//--GAME METHODS--------------------------------------------------------------------------------------------------------
    /**
     * Move a unit to a new cell. If the move is not possible, the unit will not move.
     * After the move, the {@code ASPInputProgram gridState } is refreshed.
     * @param unit {@link Unit} to move.
     * @param coord {@link Point} where to move the unit.
     * @return true if the unit is moved, false otherwise.
     */
    @Override
    public boolean moveUnitSafe(Unit unit, Point coord) {
        boolean toReturn = super.moveUnitSafe(unit, coord);
        if (toReturn) {
            try {
                refreshGridState();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return toReturn;
    }

    /**
     * Move a unit to a new cell. If the move is not possible, the unit will not move.
     * @param unit {@link Unit} to move.
     * @param x coordinate where to move the unit.
     * @param y coordinate where to move the unit.
     * @return true if the unit is moved, false otherwise.
     */
    @Override
    protected boolean moveUnitSafe(Unit unit, int x, int y) {
        return moveUnitSafe(unit, new Point(x,y));
    }

    /**
     * Build a floor in a cell. If the build is not possible, the floor will not be built.
     * After the build, the {@code ASPInputProgram gridState } is refreshed.
     * @param unit {@link Unit} that builds the floor.
     * @param coord {@link Point} where to build the floor.
     * @return true if the floor is built, false otherwise.
     */
    @Override
    public boolean buildFloor(Unit unit, Point coord) {
        boolean toReturn =  super.buildFloor(unit, coord);
        if (toReturn) {
            try {
                refreshGridState();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return toReturn;
    }

    /**
     * Build a floor in a cell. If the build is not possible, the floor will not be built.
     * @param unit {@link Unit} that builds the floor.
     * @param x coordinate where to build the floor.
     * @param y coordinate where to build the floor.
     * @return true if the floor is built, false otherwise.
     */
    @Override
    protected boolean buildFloor(Unit unit, int x, int y) {
        return buildFloor(unit, new Point(x,y));
    }

}


