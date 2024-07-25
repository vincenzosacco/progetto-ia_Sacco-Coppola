package org.project.Logic.embAsp.minimax;



import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import org.project.Logic.LogicSettings;
import org.project.Logic.embAsp.WondevWomanHandler;
import org.project.Logic.embAsp.buildIn;
import org.project.Logic.embAsp.minimax.atoms.value;
import org.project.Logic.embAsp.moveIn;
import org.project.UI.Model.BoardAivsAi;

import java.awt.*;
import java.util.*;

import static org.project.UI.Model.BoardAivsAi.BoardCopy;

/**
 * Class used to build a graph of {@link GridState GridState} starting by a state.
 */

public class GraphBuilder {
    private static NewGraph graph;

    private final static WondevWomanHandler possStateHandler, stateValueHandler;

    static {
        stateValueHandler = new WondevWomanHandler();
        ASPInputProgram stateValue = new ASPInputProgram();
        stateValue.addFilesPath(LogicSettings.PATH_ENCOD_MINIMAX + "/search_MinMax.asp");
        stateValueHandler.setEncoding(stateValue);

        possStateHandler = new WondevWomanHandler();
        ASPInputProgram possState = new ASPInputProgram();
        possState.addFilesPath(LogicSettings.PATH_ENCOD_MINIMAX + "/possible_state.asp");
        possStateHandler.setEncoding(possState);
        possStateHandler.showAllAnswerSet(true);

    }


//    static NewGraph MaxGraph(BoardAivsAi origBoard, int maxUnitCode, int minUnitCode) throws Exception {
//    //--INIT
//        graph = new NewGraph();
//        BoardCopy myBoard = origBoard.copy(); // Copy the board to avoid changes in the original board
//
//        Point rootCoord = origBoard.unitCoord(maxUnitCode);
//        moveIn rootMove = new moveIn(rootCoord.x, rootCoord.y, myBoard.heightAt(rootCoord));
//        buildIn rootBuild = new buildIn(rootCoord.x, rootCoord.y, myBoard.heightAt(rootCoord));
//        GridState root = new GridState(Type.ROOT, myBoard, rootMove, rootBuild, 0);
//
//        graph.addVertex(root);
//
//    //--BUILD GRAPH
//        // MAX
//        addChildren(root, maxUnitCode, Type.MAX,true);
//        // MIN
//        addMin(graph, minUnitCode);
//
//        return graph;
//
//    }

    /**
     * For each leaf node in the graph, add all min children
     * @param maxGraph
     * @return
     * @throws Exception
     */
//    private static void addMin(NewGraph maxGraph, int minUnitCode) throws Exception {
//        for (GridState leaf : maxGraph.getLeaves()) {
//            LinkedList<GridState> minChildren = addChildren(leaf, minUnitCode, Type.MIN, false);
//            for (GridState minChild : minChildren) {
//                graph.addVertex(minChild);
//                graph.addEdge(leaf, minChild);
//            }
//        }
//
//    }

//    static MyGraph buildGraph(WondevWomanHandler handler, BoardAivsAi origBoard, int unitCode) throws Exception {
//        //--INIT
//        init(handler, unitCode);
//        BoardCopy myBoard = origBoard.copy(); // Copy the board to avoid changes in the original board
//
//        Queue<GridState> queue = new PriorityQueue<>(GridState.ValueComp); // LIFO queue, Comparing by height
//
//        GridState root = new GridState.RootState(NEXT_ID++, myBoard.getGridState().getPrograms(), myBoard);
//        queue.add(root);
//        graph.addVertex(root);
//
//        int iteration = 0;
//
//        //--BUILD GRAPH
//        while (!queue.isEmpty()) // add children to the graph until the queue is empty
//        {
//            //1) CREATE AND ADD CHILDREN
//            GridState currentBest = queue.poll();
//            LinkedList<GridState> bestChildren = addChildren(currentBest, currentBest.board, true);
//
//            if (!bestChildren.isEmpty()) {
//
//                queue.addAll(bestChildren); // if all vertices are expanded, the queue will be empty
//                //DEBUG: print best children
////                System.out.println("Best children in iteration  " + ++iteration);
////                for (GridState b : bestChildren) {
////                    System.out.println(b + " value: " + b.value);
////                }
//
//            }
//
//
//        }
//
//
//        return graph;
//
//    }


    static LinkedList<GridState> childrenOf(GridState father, int unitCode, boolean betterThanFather) throws Exception {
        LinkedList<GridState> children = childrenFromAsp(father, unitCode);

        if (!children.isEmpty() && betterThanFather) {
            //If betterThanFather -> ADD ONLY BEST CHILDREN
            // get max value in children collection
            int max = children.stream().max(Comparator.comparingInt(c -> c.value)).get().value;
            children.removeIf(c -> c.value < max || c.value < father.value);
        }

    //--RETURN CHILDREN
        return children;

    }

    private static LinkedList<GridState> childrenFromAsp(GridState father, int unitCode) throws Exception {
        if (father == null ) throw new IllegalArgumentException("father cannot be null");

        BoardAivsAi board = father.board; // reference to the board, don't make actions on it

    //--STOP CONDITION
        if (father.isTerminal())
            return new LinkedList<>();

    //--SET FACTS
        possStateHandler.setFactProgram(father);
        possStateHandler.addFactAsString("unit(" + unitCode + ")");
        for (Point p : board.moveableArea(unitCode))
            possStateHandler.addFactAsString("moveCell(" + p.x + "," + p.y + "," + board.heightAt(p) + ")");


    //--FOR EACH LEGAL ACTION-> CREATE A CHILD
        LinkedList<GridState> children = new LinkedList<>();
        possStateHandler.startSync();

        // FOR EACH ANSWER SET
        for (AnswerSet as : possStateHandler.getAnswerSets().getAnswersets()) {
            moveIn move = null;
            buildIn build = null;
            Integer value = null;
            for (Object atom : as.getAtoms()) {
                if (atom instanceof moveIn) move = (moveIn) atom;
                else if (atom instanceof buildIn) build = (buildIn) atom;
                else if (atom instanceof value v) value = v.getN();
            }

            if (move == null)  throw new RuntimeException("Missing move"); //TODO: REMOVE
            if (build == null) throw new RuntimeException("Missing build"); //TODO: REMOVE
            if (value == null) throw new RuntimeException("Missing value"); //TODO: REMOVE

            if (move.isInvalid() || build.isInvalid()) //TODO : REMOVE
                throw new RuntimeException("Invalid move or build");


            //  MAKE THE ACTION
            BoardCopy childBoard = board.copy(); // Copy the board to avoid changes in the original board

            // here move and build are always valid because STOP CONDITION is checked before
            childBoard.moveUnitSafe(unitCode, move.getCoord());
            childBoard.buildFloorSafe(unitCode, build.getCoord());

            // CREATE CHILD
            GridState child = new GridState(childBoard, move, build, value);
            children.add(child);
        }


    //--RETURN
        return children;
    }
}

