package org.project.Logic.embAsp.minimax;

import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import org.project.Logic.embAsp.buildIn;
import org.project.Logic.embAsp.moveIn;

import java.util.Comparator;
import java.util.Objects;
import  static org.project.UI.Model.BoardAivsAi.BoardCopy;

public class GridState extends ASPInputProgram {
    final String name;
    final moveIn moved;
    final buildIn builded;
    final boolean isTerminal;
    final BoardCopy board ; // reference to the board


    public static Comparator<GridState> heightComparator = (s1, s2) -> Integer.compare(s2.moved.getHeight(), s1.moved.getHeight());

    private GridState(int id, String initial_program, boolean isTerminal, BoardCopy board) {
        super(initial_program);
        name = "state_" + id  ;
        this.isTerminal = isTerminal;
        moved = null;
        builded = null;
        this.board = board;
    }

    public GridState(int id, String initial_program, moveIn moved, buildIn builded, BoardCopy board) {
        super(initial_program);
        this.moved = moved;
        this.builded = builded;
        this.board = board;


        if (moved.getHeight() == 3 || builded.isInvalid() || moved.isInvalid()) {
            name = "Tstate_" + id;
            isTerminal = true;
        } else {
            name = "state_" + id;
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

    /**
     * Two GridState are equals if they have the same move and build.
     * @param o the object to compare.
     * @return true if the two GridState are equals, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GridState that = (GridState) o;
        return moved.equals(that.moved) && builded.equals(that.builded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moved, builded, isTerminal, board);
    }

    //--INNER CLASSES-------------------------------------------------------------------------------------------------------
    public static class RootState extends GridState {
        public RootState(int id, String initial_program, BoardCopy board) {
            super(id, initial_program, false, board);
        }
    }

//    public static class TerminalState extends GridState {
//        public TerminalState(String initial_program, BoardCopy board) {
//            super(id, initial_program, true, board);
//        }
//    }
}
