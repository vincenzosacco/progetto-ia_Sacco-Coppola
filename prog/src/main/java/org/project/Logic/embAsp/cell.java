package org.project.Logic.embAsp;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;
import org.project.Logic.Game.Board;
import org.project.Logic.Game.Player;

import java.util.Objects;

/**
 * The class is used to represent a cell of the board in the ASP logic for EmbASP.
 * Each cell gives information about the coordinates in the grid, the height of the floor and the {@code unitCode}.
 * The {@code unitCode} is -1 if there is no unit in the cell.
 */
@Id("cell") //%cell(X,Y,H,U))
public class cell {
    @Param(0)
    private int x;
    @Param(1)
    private int y;
    @Param(2)
    private int height;
    @Param(3)
    private int unitCode;

    public static final int UNITCODE_NO_UNIT = -1;

    public cell() {
    }

    public cell(cell c) {
        this.x = c.x;
        this.y = c.y;
        this.height = c.height;
        this.unitCode = c.unitCode;
    }

    public cell(int x, int y, int height, int unitCode) throws IllegalArgumentException {
        this.x = x;
        this.y = y;
        this.height = height;
        this.unitCode = unitCode;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        if (height < Board.FLOOR_START || height > Board.FLOOR_REMOVED)
            throw new IllegalArgumentException("Invalid height, must be between 0 and 4");
        this.height = height;
    }

    public boolean increaseHeight() {
        if (height < Board.FLOOR_REMOVED){
            height++;
            return true;
        }
        return false;
    }

    public int getUnitCode() {
        return unitCode;
    }

    /**
     * Set the player code of the cell.
     * @param unitCode the player code of the cell, must be -1 if there is no player
     */
    public void setUnitCode(int unitCode) throws IllegalArgumentException {
        if(unitCode < UNITCODE_NO_UNIT || unitCode > Player.LAST_UNIT_CODE())
            throw new IllegalArgumentException("Invalid unit code: " + unitCode);
        this.unitCode = unitCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        cell cell = (cell) o;
        return x == cell.x && y == cell.y && height == cell.height && unitCode == cell.unitCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, height, unitCode);
    }

    @Override
    public String toString() {
        return " cell(" +
                x +
                "," + y +
                "," + height +
                "," + unitCode +
                ") ";
    }



}
