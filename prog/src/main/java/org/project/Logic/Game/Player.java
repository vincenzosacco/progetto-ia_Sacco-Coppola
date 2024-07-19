package org.project.Logic.Game;


import org.project.Logic.Game.player.ai.actionSet;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import static org.project.UI.Settings.BOARD_COLS;
import static org.project.UI.Settings.BOARD_ROWS;

public abstract class Player implements Callable<actionSet> {
    protected final int playerCode;
    protected final Color color;
    private static int NEXT_UNIT_CODE = -1;
    private static int NEXT_PLAYER_CODE = -1;
    protected final ArrayList<Unit> Units;

    protected Player(Color color) {
        this.playerCode = ++NEXT_PLAYER_CODE;
        this.color = new Color(color.getRGB());
        this.Units = new ArrayList<>();
    }

    protected Player(Player player) {
        playerCode = player.playerCode;
        color = new Color(player.color.getRGB());
        Units = new ArrayList<>();
        for (Unit u : player.Units) {
            Units.add(new Unit(u));
        }
    }

    abstract public Player copy();

//--GETTER--------------------------------------------------------------------------------------------------------------

    /**
     * Get the unit code assigned to the last unit created.
     * @return
     */
    public static int LAST_UNIT_CODE() {
        return NEXT_UNIT_CODE;
    }
    /**
     * Get the minimum value of unit code that can be assigned to a unit.
     * @return
     */
    public static int MIN_UNIT_CODE() {
        return 0;
    }
    /**
     * Get the player code assigned to the last player created.
     * @return
     */
    public static int LAST_PLAYER_CODE() {
        return NEXT_PLAYER_CODE;
    }
    /**
     * Get the minimum value of player code that can be assigned to a player.
     * @return
     */
    public static int MIN_PLAYER_CODE() {
        return 0;
    }

    public int getPlayerCode() {
        return playerCode;
    }

    public Color getColor() {
        return color;
    }

    /**
     * Get the first unit of the player.<p>
     * Is equivalent to call method {@code getUnits().get(0)}
     * @return
     */
    public int getFirstUnitCode() {
        return Units.getFirst().unitCode;
    }

    public ArrayList<Integer> getUnitCodes() {
        ArrayList<Integer> unitCodes = new ArrayList<>();
        for (Unit u : Units) {
            unitCodes.add(u.unitCode);
        }
        return unitCodes;
    }

    public ArrayList<Unit> getUnits() {
        return new ArrayList<>(Units);
    }

//--UNIT METHOD-------------------------------------------------------------------------------------------------------------
     int addUnit(Point coord){
        if (Units.size() == Board.UNIT_PER_PLAYER) {
            throw new IllegalStateException("Player already has the maximum number of units");
        }
        Units.add(new Unit(++NEXT_UNIT_CODE, this, coord));

        return NEXT_UNIT_CODE;
    }

    /**
     * Check if a unit is present at the given coordinates.
     *
     * @param coord the coordinates to check
     * @return true if a unit is present at the given coordinates, false otherwise
     */
    public boolean isUnit(Point coord) {
        checkCoord(coord);

        for (Unit unit : Units) {
            if (unit.coord.equals(coord)) {
                return true;
            }
        }
        return false;
    }

    public boolean isUnit(int x, int y) {
        return isUnit(new Point(x, y));
    }

    /**
     * Get the unit with the given {@code unitCode}.
     * @param unitCode the code of the unit to get
     * @return the unit with the given code, or {@code null} if there is no unit with the given code
     */
    public Unit getUnit(int unitCode) {
        checkUnitCode(unitCode);

        for (Unit u : Units) {
            if (u.unitCode == unitCode) {
                return u;
            }
        }
        return null;
    }

    public boolean containsUnit(Unit unit) {
        return Units.contains(unit);
    }

    public boolean containsUnit(int unitCode) {
        checkUnitCode(unitCode);

        for (Unit u : Units) {
            if (u.unitCode == unitCode) {
                return true;
            }
        }
        return false;
    }

//--MOVE UNIT-----------------------------------------------------------------------------------------------------------

    /**
     * Move a unit to the given coordinates. <p>
     * Before moving, it checks if the unit is present in the player's units.
     * @param unit
     * @param coord
     * @return {@code true} if the unit is present and has been moved, {@code false} otherwise
     */
     boolean moveUnitSafe(Unit unit, Point coord) {
        if (containsUnit(unit)){
            for (Unit u : Units) {
                if (u.unitCode == unit.unitCode) {
                    u.coord.setLocation(coord);
                    return true;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Move a unit to the given coordinates. <p>
     * Before moving, it checks if the unit is present in the player's units.
     * @param unitCode
     * @param coord
     * @return {@code true} if the unit is present and has been moved, {@code false} otherwise
     */
    boolean moveUnitSafe(int unitCode, Point coord) {
        return moveUnitSafe(getUnit(unitCode), coord);
    }

    /**
     * Two players are equal if they have the same playerCode.
     * @param o
     * @return
     */

//--UTILITY-------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return playerCode == player.playerCode;
    }

    @Override
    abstract public int hashCode();

    //--CHECK EXEPTION------------------------------------------------------------------------------------------------------
    protected void checkUnitCode(int unitCode) {
        if (unitCode < MIN_UNIT_CODE() || unitCode > LAST_UNIT_CODE()) {
            throw new IllegalArgumentException("unitCode must be between 1 and " + LAST_UNIT_CODE() + " included");
        }
    }
    protected void checkCoord(Point coord) {
        if (coord == null) throw new IllegalArgumentException("coord cannot be null");
        if (coord.x < 0 || coord.x >= BOARD_ROWS  || coord.y < 0 || coord.y >= BOARD_COLS) {
            throw new IllegalArgumentException("coordinate must be inside the grid");
        }
    }

}
