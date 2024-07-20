package org.project.Logic.embAsp.minimax.utility;

import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import org.project.Logic.embAsp.buildIn;
import org.project.Logic.embAsp.moveIn;

import java.util.Objects;

public class GridState extends ASPInputProgram {
    final String name;
    final moveIn move;
    final buildIn build;
    final boolean isTerminal;

    private static int counter = 0;

    private GridState(String initial_program, boolean isTerminal) {
        super(initial_program);
        name = "state_" + counter++;
        this.isTerminal = isTerminal;
        move = null;
        build = null;
    }

    public GridState(String initial_program, moveIn move, buildIn build) {
        super(initial_program);
        this.move = move;
        this.build = build;
        if (move.getHeight() == 3 || build.isInvalid() || move.isInvalid()) {
            name = "Tstate_" + counter++;
            isTerminal = true;
        } else {
            name = "state_" + counter++;
            isTerminal = false;
        }

    }

    public boolean isTerminal() {
        return isTerminal;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GridState that = (GridState) o;
        return isTerminal == that.isTerminal && move.equals(that.move) & build.equals(that.build);
    }

    @Override
    public int hashCode() {
        return Objects.hash(move, build, isTerminal);
    }

    //--INNER CLASSES-------------------------------------------------------------------------------------------------------
    public static class RootState extends GridState {
        public RootState(String initial_program) {
            super(initial_program, false);
        }
    }

    public static class TerminalState extends GridState {
        public TerminalState(String initial_program) {
            super(initial_program, true);
        }
    }
}
