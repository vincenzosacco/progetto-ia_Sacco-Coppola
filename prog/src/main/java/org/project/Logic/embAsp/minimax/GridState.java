package org.project.Logic.embAsp.minimax;

import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import org.project.Logic.embAsp.buildIn;
import org.project.Logic.embAsp.moveIn;
import org.project.UI.Model.BoardAivsAi;

import java.util.Comparator;
import java.util.Objects;
import  static org.project.UI.Model.BoardAivsAi.BoardCopy;

public class GridState extends ASPInputProgram {
    private static int NEXT_ID = 0;
    final int id;
    final moveIn moved;
    final buildIn builded;
    final int value;
    final boolean isTerminal;
    final BoardAivsAi board ; // reference to the board

    public static Comparator<GridState> ValueComp = (s1, s2) -> Integer.compare(s2.value, s1.value);

//    private GridState(String name, BoardCopy board, boolean isTerminal) throws Exception {
//        super(board.getGridState().getPrograms());
//        this.name = name + "_" + NEXT_ID++;
//        this.isTerminal = isTerminal;
//        moved = null;
//        builded = null;
//        value = 0; // TODO: verificare che vada bene come default
//        this.board = board;
//    }

    public GridState(BoardCopy board, moveIn moved, buildIn builded, int value) throws Exception {
        super(board.getGridState().getPrograms());

        if (moved.getHeight() == 3 || builded.isInvalid() || moved.isInvalid()) {
            isTerminal = true;
        } else {
            isTerminal = false;
        }

        id = NEXT_ID++;
        this.moved = moved;
        this.builded = builded;
        this.value = value;

        this.board = board;
    }



    public boolean isTerminal() {
        return isTerminal;
    }
    public boolean isWinning() { return  moved.getHeight() == 3; }

//    @Override
//    public String toString() {
//        return type.toString() + "_" + id;
//    }

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
//            return moved.equals(that.moved) && builded.equals(that.builded) && isTerminal == that.isTerminal && board.equals(that.board);
            return moved.equals(that.moved) && builded.equals(that.builded) && value == that.value;
        }

    @Override
    public int hashCode() {
//        return Objects.hash(moved, builded, isTerminal, board);
        return Objects.hash(moved, builded, value);
    }

    //--INNER CLASSES-------------------------------------------------------------------------------------------------------
//    public static class RootState extends GridState {
//        public RootState(int id, BoardCopy board) throws Exception {
//            super(id, board, false);
//        }
//    }

//    public static class TerminalState extends GridState {
//        public TerminalState(String initial_program, BoardCopy board) {
//            super(id, initial_program, true, board);
//        }
//    }
}
