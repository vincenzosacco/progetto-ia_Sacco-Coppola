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
    private static MyGraph graph;

    private static WondevWomanHandler possStateHandler, stateValueHandler;

    static {
        stateValueHandler = new WondevWomanHandler();
        ASPInputProgram stateValue = new ASPInputProgram();
        stateValue.addFilesPath(LogicSettings.PATH_ENCOD_MINIMAX + "/state_value.asp");
        stateValueHandler.setEncoding(stateValue);
    }


    static MyGraph MaxGraph(WondevWomanHandler handler, BoardAivsAi origBoard, int maxUnitCode, int minUnitCode) throws Exception {
    //--INIT
        possStateHandler = handler;
        possStateHandler.showAllAnswerSet(true);
        graph = new MyGraph();
        BoardCopy myBoard = origBoard.copy(); // Copy the board to avoid changes in the original board

        Point rootCoord = origBoard.unitCoord(maxUnitCode);
        moveIn rootMove = new moveIn(rootCoord.x, rootCoord.y, myBoard.heightAt(rootCoord));
        buildIn rootBuild = new buildIn(rootCoord.x, rootCoord.y, myBoard.heightAt(rootCoord));
        GridState root = new GridState("ROOT", myBoard, rootMove, rootBuild, 0);
        graph.addVertex(root);

    //--BUILD GRAPH
        // MAX
        addChildren(root, maxUnitCode,true, true);
        // MIN
        addMin(graph, minUnitCode);

        return graph;

    }

    /**
     * For each leaf node in the graph, add all min children
     * @param maxGraph
     * @return
     * @throws Exception
     */
    private static void addMin(MyGraph maxGraph, int minUnitCode ) throws Exception {
        for (GridState leaf : maxGraph.getLeaves()) {
            LinkedList<GridState> minChildren = addChildren(leaf, minUnitCode, false, false);
            for (GridState minChild : minChildren) {
                graph.addVertex(minChild);
                graph.addEdge(leaf, minChild);
            }
        }

    }

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


    private static LinkedList<GridState> addChildren(GridState father, int unitCode, boolean isMax, boolean betterThanFather) throws Exception {
        LinkedList<GridState> children = childrenFromAsp(father, unitCode, isMax);

        if (! children.isEmpty()) {

            //If betterThanFather -> ADD ONLY BEST CHILDREN
            if (betterThanFather) {
                // get max value in children collection
                int max = children.stream().max(Comparator.comparingInt(c -> c.value)).get().value;
                children.removeIf(c -> c.value < max || c.value < father.value);
            }

    //--ADD CHILDREN TO THE GRAPH
            for (GridState child : children) {
                graph.addVertex(child);
                graph.addEdge(father, child);
            }
        }

    //--RETURN CHILDREN
        return children;
    }

    private static LinkedList<GridState> childrenFromAsp(GridState father, int unitCode, boolean isMax) throws Exception {
        if (father == null ) throw new IllegalArgumentException("father cannot be null");
        if (!graph.containsVertex(father)) throw new IllegalArgumentException("father is not in the graph");

        BoardAivsAi board = father.board; // reference to the board, don't make actions on it

    //--STOP CONDITION
        if (father.isTerminal())
            return new LinkedList<>();

    //--SET FACTS
        possStateHandler.setFactProgram(father);

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

            if (move == null || build == null || value == null)
                throw new RuntimeException("Missing move or build or value"); //TODO: REMOVE

            if (move.isInvalid() || build.isInvalid()) //TODO : REMOVE
                throw new RuntimeException("Invalid move or build");


            //  MAKE THE ACTION
            BoardCopy childBoard = board.copy(); // Copy the board to avoid changes in the original board

            // here move and build are always valid because STOP CONDITION is checked before
            childBoard.moveUnitSafe(unitCode, move.getCoord());
            childBoard.buildFloorSafe(unitCode, build.getCoord());

            // CREATE CHILD
            String name ;
            if (isMax) name = "MAX";
            else name = "MIN";

            GridState child = new GridState(name, childBoard, move, build, value);
            children.add(child);
        }


    //--RETURN
        return children;
    }
}

