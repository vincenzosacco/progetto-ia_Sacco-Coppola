package org.project.UI.Model;

import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import org.project.Logic.Game.Board;
import org.project.Logic.Game.player.Unit;
import org.project.Logic.Game.player.ai.PlayerAi;
import org.project.Logic.LogicSettings;
import org.project.Logic.embAsp.MyHandler;
import org.project.Logic.embAsp.minimax.cell;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

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
    BoardAivsAi(PlayerAi[] players) throws Exception {
        super(players[0], players[1]);
        gridState= new ASPInputProgram();
        refreshGridState();
    }

    /**
     * Constructor for the class BoardAivsAi.
     * Makes a copy of the players and initializes the grid.
     * @param player1 the first player of the game.
     * @param player2 the second player of the game.
     */
    BoardAivsAi(PlayerAi player1, PlayerAi player2) throws Exception {
        super(player1, player2);
        gridState= new ASPInputProgram();
        refreshGridState();
    }

    private BoardAivsAi() {
        super();
        gridState= new ASPInputProgram();
    }

    @Override
    public BoardCopy copy() {
        BoardCopy newBoard = new BoardCopy();
        newBoard.copyGrid(grid);
        newBoard.copyPlayers(players);
        newBoard.win = win;

        try {
            newBoard.refreshGridState();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
    void refreshGridState() throws Exception {
        gridState.clearAll();
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                gridState.addObjectInput((new cell(i, j, grid[i][j], unitCodeAt(i, j))));
            }
        }
    }

//--GAME METHODS--------------------------------------------------------------------------------------------------------
    boolean moveUnitSafe(int unitCode, Point coord) {
        boolean toReturn = super._moveUnitSafe(unitCode, coord);
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
     * After the move, the {@code ASPInputProgram gridState } is refreshed.
     * @param unit {@link Unit} to move.
     * @param coord {@link Point} where to move the unit.
     * @return true if the unit is moved, false otherwise.
     */
    boolean moveUnitSafe(Unit unit, Point coord) {
        boolean toReturn = super._moveUnitSafe(unit, coord);
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
     boolean moveUnitSafe(Unit unit, int x, int y) {
        return moveUnitSafe(unit, new Point(x,y));
    }

    /**
     * Build a floor in a cell. If the build is not possible, the floor will not be built.
     * After the build, the {@code ASPInputProgram gridState } is refreshed.
     * @param unit {@link Unit} that builds the floor.
     * @param coord {@link Point} where to build the floor.
     * @return true if the floor is built, false otherwise.
     */
    boolean buildFloorSafe(Unit unit, Point coord) {
        boolean toReturn = super._buildFloorSafe(unit, coord);
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
    boolean buildFloorSafe(Unit unit, int x, int y) {
        return buildFloorSafe(unit, new Point(x,y));
    }

    boolean buildFloorSafe(int unitCode, Point coord) {
        boolean toReturn =  super._buildFloorSafe(unitCode, coord);
        if (toReturn) {
            try {
                refreshGridState();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return toReturn;
    }


//--INNER CLASS---------------------------------------------------------------------------------------------------------
    /**
     * This class cannot be instantiated outside the package, it is used to manage the copy of the board.<p>
     * The difference with the class BoardAivsAi is that the action methods are public and can be used to test the board.
     */
    public static class BoardCopy extends BoardAivsAi{
        private final MyHandler parser;

        BoardCopy() {
            super();
            parser = new MyHandler();
            parser.addEncodingPath(LogicSettings.PATH_ENCOD_PARSER);
        }

        public void setBoardFromGridState(ASPInputProgram gridState) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
//            parser.setFactProgram(gridState);
//            parser.startSync();
//
//            for (Object atom : parser.getAnswerSets().getAnswersets().getFirst().getAtoms()) {
//                if (atom instanceof cell) {
//                    cell c = (cell) atom;
//                    grid[c.getX()][c.getY()] = c.getHeight();
//                    if (c.getUnitCode() != 0) {
//                        Unit unit = players[0].getUnit(c.getUnitCode());
//                        if (unit == null) unit = players[1].getUnit(c.getUnitCode());
//                        if (unit == null) throw new RuntimeException("Unit not found");
//                        unit.coord = new Point(c.getX(), c.getY());
//                    }
//                }
//            }
        }

        @Override
        public boolean moveUnitSafe(int unitCode, Point coord) {
            return super.moveUnitSafe(unitCode, coord);
        }

        @Override
        public boolean moveUnitSafe(Unit unit, Point coord) {
            return super.moveUnitSafe(unit, coord);
        }

        @Override
        public boolean moveUnitSafe(Unit unit, int x, int y) {
            return super.moveUnitSafe(unit, x, y);
        }

        @Override
        public boolean buildFloorSafe(Unit unit, Point coord) {
            return super.buildFloorSafe(unit, coord);
        }

        @Override
        public boolean buildFloorSafe(Unit unit, int x, int y) {
            return super.buildFloorSafe(unit, x, y);
        }

        @Override
        public boolean buildFloorSafe(int unitCode, Point coord) {
            return super.buildFloorSafe(unitCode, coord);
        }
    }
}


