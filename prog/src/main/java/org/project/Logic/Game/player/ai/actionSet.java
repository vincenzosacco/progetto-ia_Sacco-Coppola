package org.project.Logic.Game.player.ai;

import org.project.Logic.Game.Player;

import java.awt.*;
import java.util.Objects;

import static org.project.UI.Settings.BOARD_COLS;
import static org.project.UI.Settings.BOARD_ROWS;

public class actionSet {
    private final Player player;
    private final int unitCode;
    private final Point move;
    private final Point build;
//    private boolean nullAction = false;

//--CONSTRUCTOR---------------------------------------------------------------------------------------------------------

    /**
     * Construct a non {@code nullAction} actionSet.
     *
     * @param player
     * @param move
     * @param build
     */
    public actionSet(Player player, int unitCode, Point move, Point build) {
        if (player == null || move == null || build == null) {
            String nullArgs = "";
            if (player == null) nullArgs += " unit ";
            if (move == null) nullArgs += " move ";
            if (build == null) nullArgs += " build ";

            throw new NullPointerException("null values are not allowed, Null arguments: " + nullArgs);
        }
        if (unitCode < Player.MIN_UNIT_CODE() || unitCode > Player.LAST_UNIT_CODE()) {
            throw new IllegalArgumentException("unitCode must be" + Player.MIN_UNIT_CODE() + "and" + Player.LAST_UNIT_CODE());
        }
        if (player.getPlayerCode() < PlayerAi.MIN_PLAYER_CODE() || player.getPlayerCode() > Player.LAST_PLAYER_CODE()) {
            throw new IllegalArgumentException("playerCode must be" + PlayerAi.MIN_PLAYER_CODE() + "and" + Player.LAST_PLAYER_CODE());
        }
        if (move.x < 0 || move.x > BOARD_ROWS - 1 || move.y < 0 || move.y > BOARD_COLS - 1) {
            throw new IllegalArgumentException("move coordinates must be between 0" + (BOARD_ROWS - 1) + "and" + (BOARD_COLS - 1));
        }
        if (build.x < 0 || build.x > BOARD_ROWS - 1 || build.y < 0 || build.y > BOARD_COLS - 1) {
            throw new IllegalArgumentException("build coordinates must be between 0" + (BOARD_ROWS - 1) + "and" + (BOARD_COLS - 1));
        }

        this.unitCode = unitCode;
        this.player = player;
        this.move = move;
        this.build = build;
    }


//--GETTERS & SETTERS---------------------------------------------------------------------------------------------------

    public int unitCode() {
        return unitCode;
    }

    public Player player() {
        return player;
    }

    public Point move() {
        return move;
    }

    public Point build() {
        return build;
    }


//--UTILITY-------------------------------------------------------------------------------------------------------------

    public String display() {
        return "\nPlayer " + player.getPlayerCode() + " moves unit " + unitCode + " to (" + move.x + "," + move.y + ") and builds at (" + build.x + "," + build.y + ")";
    }

    @Override
    public String toString() {
        return " actionSet(" +
                player.getPlayerCode() +
                ", " +
                "(" + move.x + "," + move.y + ")" +
                "," +
                "(" + build.x + "," + build.y + ")" +
                ") ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        actionSet that = (actionSet) o;
        return player.equals(that.player) && move.equals(that.move) && build.equals(that.build);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, move, build);
    }
}
