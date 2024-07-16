package org.project.Logic.Game;

import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import org.project.Logic.Game.player.Player;
import org.project.Logic.Game.player.Unit;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.project.Logic.embAsp.cell.PLAYERCODE_NO_PLAYER;
import static org.project.Logic.embAsp.minimax.cell.UNITCODE_NO_UNIT;
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

    protected final int[][] grid;
    protected final Player[] players ;
    protected boolean win ;


//--CONSTRUCTOR---------------------------------------------------------------------------------------------------------
    protected void spawnUnits() {
        Random rand = new Random();

        for (int p = 0; p < N_PLAYERS; p++) {
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
        grid = new int[BOARD_ROWS][BOARD_COLS];
        players = new Player [N_PLAYERS];
        this.players[0] = player1.copy();
        this.players[1] = player2.copy();

        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_ROWS; j++) {
                grid[i][j] = FLOOR_START;
            }
        }

        spawnUnits();

        win=false;
    }

    protected Board(){
        grid = new int[BOARD_ROWS][BOARD_COLS];
        players = new Player [N_PLAYERS];
        win=false;
    }

    protected abstract Board copy();

    protected void copyGrid(int[][] grid){
        for (int i = 0; i < BOARD_ROWS; i++) {
            this.grid[i] = Arrays.copyOf(grid[i], grid[i].length);
        }
    }
    protected void copyPlayers(Player[] players){
        this.players[0] = players[0].copy();
        this.players[1] = players[1].copy();
    }

//--GETTERS & SETTERS---------------------------------------------------------------------------------------------------

    /**
     * Get the grid of the game. <p>
     * The grid is a matrix of integers which represent floors height
     * @return
     */
    public int[][] getGrid() {
        return grid;
    }


    public Player[] getPlayers() {
        return players;
    }


    public boolean win() {
        return win;
    }
    public void setWin() {
        win = true;
    }

//--UTILITY--------------------------------------------------------------------------------------------------------------
    /**
     * Check if the cell is occupied by a unit.
     * @param coord
     * @return
     */
    public boolean isOccupied(Point coord) {
        return isOccupied(coord.x,coord.y);
    }

    /**
     * Check if the cell is occupied by a unit.
     * @param x
     * @param y
     * @return {@code true} if the cell is occupied, {@code false} otherwise
     */
    public boolean isOccupied(int x, int y) {
        for (Player p : players){
            if ( p.isUnit(x,y))
                return true;
        }

        return false;
    }

    /**
     * Check if the cell is occupied by a unit.
     * @param x
     * @param y
     * @return {@code Unit} if the cell is occupied, {@code null} otherwise
     */
    public Unit unitAt(int x, int y){
        for (Player p : players){
            if ( p.unitAt(x,y) != null)
                return p.unitAt(x,y);

        }
        return null;
    }

    /**
     * Check if the cell is occupied by a unit.
     * @param coord
     * @return {@code Unit} if the cell is occupied, {@code null} otherwise
     */
    public Unit unitAt(Point coord){
        return unitAt(coord.x,coord.y);
    }

    public int unitCodeAt(int x, int y){
        for (Player p : players){
             Unit u = p.unitAt(x,y);
            if ( u != null)
                return u.unitCode();
        }

        return UNITCODE_NO_UNIT;
    }

    /**
     * Get the player code of the cell.
     * @param x
     * @param y
     * @return the player code of the cell, -1 if there is no player
     */
    public int playerCodeAt(int x, int y){
        for (Player p : players){
            if ( p.isUnit(x,y))
                return p.getPlayerCode();

        }
        return PLAYERCODE_NO_PLAYER;
    }

    /**
     * Get the player on cell. If there is no player return null.
     * @param x
     * @param y
     * @return
     */
     Player playerAt(int x, int y){
        if (x < 0 || x >= BOARD_ROWS || y < 0 || y >= BOARD_COLS)
            throw new IllegalArgumentException("Cell out of bounds");

        for (Player p : players)
            if ( p.isUnit(x,y)) return p;

        return null;
    }

    public int heightAt(Point coord){
        return heightAt(coord.x,coord.y);
    }

    public int heightAt(int x, int y){
        return grid[x][y];
    }
//--EMBASP--------------------------------------------------------------------------------------------------------------
    public ASPInputProgram getGridState(){
         throw new RuntimeException("To implement");
    }

//--GAME----------------------------------------------------------------------------------------------------------------

    int addUnit(int playerCode, Point coord) {
        if(playerCode < 0 || playerCode > N_PLAYERS-1){
            throw new IllegalArgumentException("playerCode must be between 0 and " + (N_PLAYERS-1));
        }

        if (players[playerCode].getUnits().size() > UNIT_PER_PLAYER) {
            throw new IllegalStateException("Player already has the maximum number of units");
        }

        if (isOccupied(coord)){
            throw new IllegalArgumentException("Cell already occupied");
        }

    //--ADD UNIT TO player
        return players[playerCode].addUnit(coord);

    }



//--ACTIONS-------------------------------------------------------------------------------------------------------------
protected boolean _moveUnitSafe(int unitCode, Point coord){
        Unit toMove = null;
        for (Player p : players) {
            toMove = p.getUnit(unitCode);
            if (toMove != null) break;
        }

        if (toMove == null) throw new IllegalArgumentException("Unit not found");
        return _moveUnitSafe(toMove,coord);

    }

    /**
     * Move a unit to a new position. <p>
     * @param unit
     * @param coord
     * @return
     */
    protected boolean _moveUnitSafe(Unit unit, Point coord){
         if (unit == null || !isOccupied(unit.coord())) throw new IllegalArgumentException("Unit not found");

         if (canMove(unit,coord)){
            Player p = playerAt(unit.x(), unit.y());
            if (p == null) return false;

            if (! p.moveUnitSafe(unit,coord))
                throw new RuntimeException("Qualcosa non va ");

            //--WIN
            if (grid[coord.x][coord.y] == FLOOR_HEIGHT_3)
                setWin();

            return true;
        }

        return false;
    }

    /**
     * Move a unit to a new position. <p>
     * @param unit
     * @param x
     * @param y
     * @return
     */
     boolean _moveUnitSafe(Unit unit, int x, int y){
        return _moveUnitSafe(unit, new Point(x,y));
    }

    /**
     * Build a floor on a cell. <p>
     * Cells at height 4 are considered removed from game.
     * @param unitCode
     * @param coord
     * @return
     */
    protected boolean _buildFloorSafe(int unitCode, Point coord){
        Unit unit = null;
        for (Player p : players) {
            unit = p.getUnit(unitCode);
            if (unit != null) break;
        }

        if (unit == null) throw new IllegalArgumentException("Unit not found");
        return _buildFloorSafe(unit,coord);
    }

    /**
     * Build a floor on a cell. <p>
     * Cells at height 4 are considered removed from game.
     * @param unit
     * @param coord
     * @return
     */
    protected boolean _buildFloorSafe(Unit unit, Point coord) {
        if (unit == null || !isOccupied(unit.coord())) throw new IllegalArgumentException("Unit not found");

        if (canBuild(unit, coord)){
            grid[coord.x][coord.y]++;
            return true;
        }
        return false;
    }

    /**
     * Build a floor on a cell. <p>
     * Cells at height 4 are considered removed from game.
     * @param unit
     * @param x
     * @param y
     * @return
     */
    boolean _buildFloorSafe(Unit unit, int x, int y) {
        return _buildFloorSafe(unit, new Point(x,y));
    }

    public boolean canMove(Point unitCoord, Point toMove){
//    The unit may only move on the same level, step up one level or step down any number of levels.
//    After every movement, the unit must be able to build onto an adjacent cell of its new position.
//    This causes the cell in question to gain 1 unit of height.
//    If the height reaches level 4, the cell is considered removed from play.
//    If your unit moves onto a level 3 cell, you win the game.

        if (! canMethods(unitCoord, toMove)) return false;

        //The unit may only move on the same level, step up one level or step down any number of levels.
        if (grid[toMove.x][toMove.y] - grid[unitCoord.x][unitCoord.y] > 1){
//            System.out.println("NOT VALID! floor too high");
            return false;
        }

        return true;

    }
    public boolean canMove(Unit unit, Point coord){
        return canMove(unit.coord(),coord);
    }

    boolean canMethods(Point start, Point end){

        int currentX = start.x;
        int currentY = start.y;
        int toX = end.x;
        int toY = end.y;

        //--Can't make action outside the grid
        if (toX < 0 || toX >= BOARD_ROWS || toY< 0 || toY >= BOARD_COLS)
            return false;

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
        if (grid[toX][toY] == FLOOR_REMOVED){
//            System.out.println("NOT VALID! floor removed");
            return false;
        }

        return true;
    }


    public boolean canBuild(Point unitCoord, Point toBuild){
        return canMethods(unitCoord,toBuild);
    }
    public boolean canBuild(Unit unit , Point coord) {
        return canBuild(unit.coord(),coord);
    }




//--EMBASP--------------------------------------------------------------------------------------------------------------

    public ArrayList<Point> moveableArea(int unitCode){
        Unit unit = null;
        for (Player p : players) {
            unit = p.getUnit(unitCode);
            if (unit != null) break;
        }

        if (unit == null) throw new IllegalArgumentException("Unit not found");
        return moveableArea(unit);

    }
    /**
     * Get the area around a cell where a unit can legally move. <p>
     * @param unitCoord the cell where the unit is
     * @return
     */
    public ArrayList<Point> moveableArea(Point unitCoord){
        ArrayList<Point> area = new ArrayList<>(8);
        initArea(area,unitCoord);
        area.removeIf(toMove -> ! canMove(unitCoord,toMove));

        return area;
    }

    /**
     * Get the area around a cell where a unit can legally move. <p>
     * @param unit
     * @return
     */
    public ArrayList<Point> moveableArea(Unit unit){
        return moveableArea(unit.coord());
    }


    /**
     * Get the area around a cell where a unit can legally Build. <p>
     * @param unitCoord the cell where the unit is
     * @return
     */
    public ArrayList<Point> buildableArea(Point unitCoord){
        ArrayList<Point> area = new ArrayList<>(8);
        initArea(area,unitCoord);
        area.removeIf(toBuild -> ! canBuild(unitCoord,toBuild));

        return area;
    }

    /**
     * Get the area around a cell where a unit can legally Build. <p>
     * @param unit
     * @return
     */
    public ArrayList<Point> buildableArea(Unit unit){
        return buildableArea(unit.coord());
    }



    private void initArea( ArrayList<Point> area, Point coord){
        int x = coord.x;
        int y = coord.y;

        //up
        area.add(new Point(x-1,y));
        //up right
        area.add(new Point(x-1,y+1));
        // right
        area.add( new Point(x,y+1));
        // down right
        area.add( new Point(x+1,y+1));
        // down
        area.add(new Point(x+1,y));
        // down left
        area.add( new Point(x+1,y-1));
        // left
        area.add(new Point(x,y-1));
        // up left
        area.add(new Point(x-1,y-1));

    }
//--VIEW

    /**
     * @return all the units on the board
     */
    public ArrayList<Unit> getUnits(){
        ArrayList<Unit> units = new ArrayList<>();
        for (Player p : players){
            units.addAll(p.getUnits());
        }
        return units;
    }

}
