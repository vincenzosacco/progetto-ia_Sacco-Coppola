package org.project.Logic.Game;

import java.awt.Point;
import java.util.Objects;

/**
 * Class used to represent a unit in the game.
 * A unit is defined by a {@code unitCode}, a {@link Player} and a {@link Point coordinate}.
 */
public class Unit {
    final int unitCode;
    final Player player;
    final Point coord;

    /**
     * Constructor by parameters.
     * @param unitCode the code of the unit.
     * @param player {@link Player} of the unit.
     * @param coord {@link Point} of the unit.
     */
     Unit(int unitCode, Player player, Point coord) {
        this.unitCode = unitCode;
        this.player = player;
        this.coord = new Point(coord);
    }

    /**
     * Constructor by copy.
      * @param unit {@link Unit} to be copied.
     */
    Unit(Unit unit) {
        this(unit.unitCode, unit.player, unit.coord);
    }

//--GETTERS-------------------------------------------------------------------------------------------------------------
    public int i() {
        return coord.x;
    }

    public int j() {
        return coord.y;
    }
//---------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return unitCode == unit.unitCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitCode, player, coord);
    }
}
