package org.project.Logic.Game.player;

import org.project.Logic.Game.player.human.PlayerManual;
import org.project.Logic.Game.player.ai.PlayerAi;
import java.awt.*;
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
     * Constructor by parameters .<p></p> Makes a copy of the 3 parameters.
     * @param unitCode the code of the unit.
     * @param player {@link Player} of the unit.
     * @param coord {@link Point} of the unit.
     */
    public Unit(int unitCode, Player player, Point coord) {
        this.unitCode = unitCode;
        this.player = player.copy();
        this.coord = new Point(coord);
    }

    /**
     * Constructor by copy.<p></p> Makes a copy of the unit.
      * @param unit {@link Unit} to be copied.
     */
    public Unit(Unit unit) {
        this(unit.unitCode, unit.player, unit.coord);
    }


    public int unitCode() {
        return unitCode;
    }

    /**
     * Get a copy of the player of the unit.
     * @return
     */
    public Player player() {
        if (player instanceof PlayerAi) {
            return ((PlayerAi) player).copy();
        }
        else {
            return ((PlayerManual) player).copy();
        }
    }

    /**
     * Get a copy of the coordinate of the unit.
     */
    public Point coord() {
        return new Point(coord);
    }
    public int x() {
        return coord.x;
    }
    public int y() {
        return coord.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return unitCode == unit.unitCode && player.equals(unit.player) && coord.equals(unit.coord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitCode, player, coord);
    }
}
