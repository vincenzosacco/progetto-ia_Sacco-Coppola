package org.project.Logic.Game;

import org.project.Logic.embAsp.cell;

import java.awt.Point;
import java.util.*;

import static org.project.Logic.embAsp.cell.UNITCODE_NO_UNIT;
import static org.project.UI.Settings.BOARD_COLS;
import static org.project.UI.Settings.BOARD_ROWS;

/**
 * Class that represents the game board.
 */
public abstract class Board {
    public static final int N_PLAYERS = 2;
    public static final int UNIT_PER_PLAYER = 1;


    public static final int FLOOR_HEIGHT_0= 0;
    public static final int FLOOR_HEIGHT_1= 1;
    public static final int FLOOR_HEIGHT_2= 2;
    public static final int FLOOR_HEIGHT_3= 3;
    public static final int FLOOR_HEIGHT_4= 4;
    public static final int FLOOR_START= FLOOR_HEIGHT_0;
    public static final int FLOOR_REMOVED= FLOOR_HEIGHT_4;

    protected final cell[][] grid;


    protected final List<Player> players ;
    protected boolean win ;


//--CONSTRUCTOR---------------------------------------------------------------------------------------------------------
    protected void spawnUnits() {
        Random rand = new Random();

        for (Player p : players) {
            for (int u = 0; u < UNIT_PER_PLAYER; u++) {
                Point coord = new Point(rand.nextInt(BOARD_ROWS), rand.nextInt(BOARD_COLS));
                while (isOccupied(coord)) {
                    coord = new Point(rand.nextInt(BOARD_ROWS), rand.nextInt(BOARD_COLS));
                }
                addUnit(p, coord);

            }
        }

    }

    /**
     * Constructor for the Board class.<p>
     * Make a copy of the players and initialize the grid.
     * @param player1
     * @param player2
     */
    protected Board(Player player1, Player player2) {
        grid = new cell[BOARD_ROWS][BOARD_COLS];
        players = List.of(player1, player2);

        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                grid[i][j] = new cell(i,j,FLOOR_START,UNITCODE_NO_UNIT);
            }
        }

        spawnUnits();

        win=false;
    }

    protected Board(Board board) {
        players = List.copyOf(board.players);
        grid = new cell[BOARD_ROWS][BOARD_COLS];
        copyGrid(board.grid);
        win=board.win;
    }

    protected abstract Board copy();

    private void copyGrid(cell[][] grid){
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                this.grid[i][j] = new cell(grid[i][j]);
            }
        }
    }


//--GETTERS & SETTERS---------------------------------------------------------------------------------------------------

    /**
     * Get the grid of the game. <p>
     * The grid is a matrix of integers which represent floors height
     * @return the grid of the game
     */
    public cell[][] getGrid() {
        return grid;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean win() {
        return win;
    }
    public void setWin() {
        win = true;
    }


    public int heightAt(int x, int y){
        return grid[x][y].getHeight();
    }
    public int heightAt(Point coord){
        return heightAt(coord.x,coord.y);
    }

//--UNIT METHODS--------------------------------------------------------------------------------------------------------
    /**
     * Check if the cell is occupied by a unit.
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     * @return {@code true} if the cell is occupied, {@code false} otherwise
     */
    public boolean isOccupied(int x, int y) {
        checkCoord(x,y);
        return grid[x][y].getUnitCode() != UNITCODE_NO_UNIT;
    }

    /**
     * Check if the cell is occupied by a unit.
     * @param coord the coordinates of the cell
     * @return {@code true} if the cell is occupied, {@code false} otherwise
     */
    public boolean isOccupied(Point coord) {
        return isOccupied(coord.x,coord.y);
    }

    /**
     * Get the coordinates of the given {@code unitCode}.
     * @param unitCode the code of the unit
     * @return the coordinates of the unit
     * @throws IllegalArgumentException if the unit is not found
     */
    public Point unitCoord(int unitCode){
        checkUnitCode(unitCode);
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                if (grid[i][j].getUnitCode() == unitCode)
                    return new Point(i,j);
            }
        }
        throw new IllegalArgumentException("Unit not found");
    }


    protected int addUnit(Player player, Point coord) {
        checkPlayerCode(player.playerCode);
        checkCoord(coord);
        if (isOccupied(coord)){
            throw new IllegalArgumentException("Cell already occupied");
        }

        int unitCode = players.get(players.indexOf(player)).addUnit(coord);
        grid[coord.x][coord.y].setUnitCode(unitCode);

        return unitCode;

    }

//--CHECK METHODS-------------------------------------------------------------------------------------------------------
    protected void checkCoord(int x, int y){
        if(x < 0 || x >= BOARD_ROWS || y < 0 || y >= BOARD_COLS)
            throw new IllegalArgumentException("Coordinates out of bounds");

    }
    protected void checkCoord(Point coord){
        if (coord == null) throw new IllegalArgumentException("coord cannot be null");
        checkCoord(coord.x,coord.y);
    }

    protected void checkPlayerCode(int playerCode){
        if (playerCode < Player.MIN_PLAYER_CODE() || playerCode > Player.LAST_PLAYER_CODE())
            throw new IllegalArgumentException("Invalid player code");
    }

    protected void checkUnitCode(int unitCode){
        if (unitCode <= UNITCODE_NO_UNIT || unitCode > Player.LAST_UNIT_CODE())
            throw new IllegalArgumentException("Invalid unit code");
    }

//--ACTIONS-------------------------------------------------------------------------------------------------------------

    protected boolean _moveUnitSafe(int unitCode, Point moveCoord) {
        checkUnitCode(unitCode);
        checkCoord(moveCoord);

        Point currentCoord = unitCoord(unitCode);
        if (canMove(currentCoord,moveCoord)){
            for (Player p : players){
                if ( p.moveUnitSafe(unitCode,moveCoord) ) {
                    //WIN
                    if (grid[moveCoord.x][moveCoord.y].getHeight() == FLOOR_HEIGHT_3)
                        setWin();

                    //GRID
                    grid[currentCoord.x][currentCoord.y].setUnitCode(UNITCODE_NO_UNIT);
                    grid[moveCoord.x][moveCoord.y].setUnitCode(unitCode);

                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Build a floor on a cell. <p>
     * Cells at height 4 are considered removed from game.
     * @param unitCode
     * @param coord
     * @return
     */
    protected boolean _buildFloorSafe(int unitCode, Point coord){
        checkUnitCode(unitCode);
        checkCoord(coord);

        if (canBuild(unitCoord(unitCode),coord)){
            grid[coord.x][coord.y].increaseHeight();
            return true;
        }
        return false;
    }


    public boolean canMove(Point unitCoord, Point toMove){
        /*
        The unit may only move on the same level, step up one level or step down any number of levels.
        After every movement, the unit must be able to build onto an adjacent cell of its new position.
        This causes the cell in question to gain 1 unit of height.
        If the height reaches level 4, the cell is considered removed from play.
        If your unit moves onto a level 3 cell, you win the game.
        */

        if (! canMethods(unitCoord, toMove)) return false;

        //The unit may only move on the same level, step up one level or step down any number of levels.
        if (grid[toMove.x][toMove.y].getHeight() - grid[unitCoord.x][unitCoord.y].getHeight() > 1){
//            System.out.println("NOT VALID! floor too high");
            return false;
        }

        return true;

    }

    public boolean canBuild(Point unitCoord, Point toBuild){
        return canMethods(unitCoord,toBuild);
    }


    boolean canMethods(Point start, Point end){
        int currentX = start.x;
        int currentY = start.y;
        int toX = end.x;
        int toY = end.y;


    //--A unit cannot make action to the same cell it is currently in.
        if (start.equals(end)){
//            System.out.println("NOT VALID! same cell");
            return false;
        }

    //--Can make action to any neighboring cell, including diagonals.
        if( Math.abs(toX -currentX)  > 1 || Math.abs(toY-currentY)  > 1){
//            System.out.println("NOT VALID! too far away");
            return false;
        }

    //--A unit cannot make action to a cell that is occupied by another unit.
        if (isOccupied(toX,toY)){
//            System.out.println("NOT VALID! cell occupied");
            return false;
        }

    //--If the height reaches level 4, the cell is considered removed from play.
        if (grid[toX][toY].getHeight() == FLOOR_REMOVED){
//            System.out.println("NOT VALID! floor removed");
            return false;
        }

        return true;
    }

//--EMBASP--------------------------------------------------------------------------------------------------------------

    /**
     * Get the area around a cell where a unit can legally move. <p>
     * @param unitCode the code of the unit
     * @return the area around the cell where the unit is
     */
    public ArrayList<Point> moveableArea(int unitCode){
        return moveableArea(unitCoord(unitCode));
    }

    /**
     * Get the area around a cell where a unit can legally move. <p>
     * @param unitCoord the cell where the unit is
     * @return the area around the cell where the unit is
     */
    public ArrayList<Point> moveableArea(Point unitCoord){
        ArrayList<Point> area = new ArrayList<>(8);
        initArea(area,unitCoord);
        area.removeIf(toMove -> ! canMove(unitCoord,toMove));
        return area;
    }

    /**
     * Get the area around a cell where a unit can legally Build. <p>
     * @param unitCode the code of the unit
     * @return the area around the cell where the unit is
     */
    public ArrayList<Point> buildableArea(int unitCode){
        return buildableArea(unitCoord(unitCode));
    }

    /**
     * Get the area around a cell where a unit can legally Build. <p>
     * @param unitCoord the cell where the unit is
     * @return the area around the cell where the unit is
     */
    public ArrayList<Point> buildableArea(Point unitCoord){
        ArrayList<Point> area = new ArrayList<>(8);
        initArea(area,unitCoord);
        area.removeIf(toBuild -> ! canBuild(unitCoord,toBuild));

        return area;
    }

    private void initArea( ArrayList<Point> area, Point coord){
        int x,y;

        for (int i = -1; i <= 1; i++){
            for (int j = -1; j <= 1; j++){
                if (i == 0 && j == 0) continue; // skip the cell itself

                x = coord.x + i; y = coord.y + j; // coordinates of the cell around the unit
                if (x >= 0 && x < BOARD_ROWS && y >= 0 && y < BOARD_COLS)
                    area.add(new Point(x,y)); // Each cell around the unit is added to the area
            }
        }
    }

}
