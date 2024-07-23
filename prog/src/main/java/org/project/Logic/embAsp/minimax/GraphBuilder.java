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
import java.util.List;

import static org.project.UI.Model.BoardAivsAi.BoardCopy;


/**
 * Class used to build a graph of {@link GridState GridState} starting by a state.
 */

public class GraphBuilder {
    private static MyGraph graph ;
    static int NEXT_ID = 0;
    private static WondevWomanHandler possStateHandler, stateValueHandler;
    private static int myUnitCode;

    static {
        stateValueHandler = new WondevWomanHandler();
        ASPInputProgram stateValue = new ASPInputProgram();
        stateValue.addFilesPath(LogicSettings.PATH_ENCOD_MINIMAX + "/state_value.asp");
        stateValueHandler.setEncoding(stateValue);
    }

    private static void init(WondevWomanHandler handler, int unitCode)  {
        possStateHandler = handler;
        possStateHandler.showAllAnswerSet(true);
        myUnitCode = unitCode;

        graph = new MyGraph();


    }

    static MyGraph buildGraph(WondevWomanHandler handler, BoardAivsAi origBoard, int unitCode) throws Exception {
    //--INIT
        init(handler, unitCode);
        BoardCopy myBoard = origBoard.copy(); // Copy the board to avoid changes in the original board

        Queue<GridState> queue = new PriorityQueue<>(GridState.ValueComp); // LIFO queue, Comparing by height

        GridState root = new GridState.RootState(NEXT_ID++, myBoard.getGridState().getPrograms(), myBoard);
        queue.add(root);
        graph.addVertex(root);

//        int iteration = 0;

    //--BUILD GRAPH
        while (! queue.isEmpty()) // add children to the graph until the queue is empty
        {
            //1) CREATE AND ADD CHILDREN
            GridState currentBest = queue.poll();
            LinkedList<GridState> bestChildren = addBestChildren(currentBest, currentBest.board);

            if (!bestChildren.isEmpty()) {

            //2) If children aren't worse than father, add them to the queue
                if (bestChildren.getFirst().value >= currentBest.value) {
                    queue.addAll(bestChildren); // if all vertices are expanded, the queue will be empty
                    //DEBUG: print best children
//                    System.out.println("Best children in iteration  " + ++iteration);
//                    for (GridState b : bestChildren) {
//                        System.out.println(b + " value: " + b.value);
//                    }
                }

            }

            //4) STOP CONDITION
//            GridState newBest = queue.peek();
//            if (newBest == null)
//                throw new RuntimeException("Queue is empty");
//
//            if (newBest.moved.getHeight() == 3 ) {
//                continue;
//            }

        }



        return graph;

    }




    private static LinkedList<GridState> addBestChildren(GridState father, BoardAivsAi board) throws Exception {
        if (father == null || board == null) throw new IllegalArgumentException("Father or board cannot be null");
        if (! graph.containsVertex(father)) throw new IllegalArgumentException("Father is not in the graph");

        //--STOP CONDITION
        if (father.isTerminal())
            return new LinkedList<>();

    //--SET FACTS
        possStateHandler.setFactProgram(father);

        for (Point p : board.moveableArea(myUnitCode))
            possStateHandler.addFactAsString("moveCell(" + p.x + "," + p.y + "," + board.heightAt(p) + ")");

    //--FOR EACH LEGAL ACTION, CREATE A CHILD
        LinkedList<GridState> children = new LinkedList<>();
        possStateHandler.startSync();

        for (AnswerSet as : possStateHandler.getAnswerSets().getAnswersets()) {
            moveIn move = null; buildIn build = null; Integer value = null;
            for (Object atom : as.getAtoms()) {
                if (atom instanceof moveIn) move = (moveIn) atom;
                else if (atom instanceof buildIn) build = (buildIn) atom;
                else if (atom instanceof value v) value = v.getN();
            }

            if (move == null || build == null || value == null) throw new RuntimeException("Missing move or build or value"); //TODO: REMOVE

            if(move.isInvalid() || build.isInvalid()) //TODO : REMOVE
                throw new RuntimeException("Invalid move or build");


        //--MAKE THE ACTION
            BoardCopy childBoard = board.copy(); // Copy the board to avoid changes in the original board

            // here move and build are always valid because STOP CONDITION is checked before
            childBoard.moveUnitSafe(myUnitCode, move.getCoord());
            childBoard.buildFloorSafe(myUnitCode, build.getCoord());


    //--CREATE ONLY BEST CHILDREN

            if (children.isEmpty()) {
                GridState child = new GridState(NEXT_ID++, board.getGridState().getPrograms(), move, build, value, childBoard);
                children.add(child);
            }
            else if (value > children.getFirst().value) {
                children.clear();
                GridState child = new GridState(NEXT_ID++, board.getGridState().getPrograms(), move, build, value, childBoard);
                children.add(child);
            }
            else if (value == children.getFirst().value) {
                GridState child = new GridState(NEXT_ID++, board.getGridState().getPrograms(), move, build, value, childBoard);
                children.add(child);

            }

        }
    //--ADD THE BEST CHILDREN TO GRAPH
        LinkedList<GridState> toRemove = new LinkedList<>();
        for (GridState best: children){
            if (! graph.addVertex(best)) // Add the child to the graph if it is not already present
                toRemove.add(best);
            graph.addEdge(father, best); // Add the edge between father and child in the graph if it is not already present
        }

    //--RETURN CHILDREN
        // Remove the child from the list if it is already present in the graph
        children.removeAll(toRemove);

        return children;
    }






}

