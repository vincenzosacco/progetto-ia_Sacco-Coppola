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
    private static MyGraph<GridState> graph ;
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

        graph = new MyGraph<>();


    }

    static MyGraph<GridState> buildGraph(WondevWomanHandler handler, BoardAivsAi origBoard, int unitCode) throws Exception {
    //--INIT
        init(handler, unitCode);
        BoardCopy myBoard = origBoard.copy(); // Copy the board to avoid changes in the original board

    //--BUILD GRAPH
        Queue<GridState> queue = new PriorityQueue<>(GridState.ValueComp); // LIFO queue, Comparing by height

        GridState root = new GridState.RootState(NEXT_ID++, myBoard.getGridState().getPrograms(), myBoard);
        queue.add(root);
        graph.addVertex(root);

        int iteration = 0;
        while (! queue.isEmpty()) // add children to the graph until the queue is empty
        {
            //1) CREATE CHILDREN
            GridState currentBest = queue.poll();
            LinkedList<GridState> children = createChildren(currentBest, currentBest.board);

            //2) IF THERE ARE CHILDREN -> ADD CHILDREN
            if (!children.isEmpty()) {
                List<GridState> bestChildren = addBestChildrenToGraph(currentBest , children);

                // If children aren't worse than father, add them to the queue
                if (bestChildren.getFirst().value >= currentBest.value) {
                    queue.addAll(bestChildren); // if all vertices are expanded, the queue will be emptyù
                    //DEBUG: print best children
//                    System.out.println("Best children in iteration  " + ++iteration);
//                    for (GridState b : bestChildren) {
//                        System.out.println(b + " value: " + b.value);
//                    }
                }

            }

            //4) STOP CONDITION
            GridState newBest = queue.peek();
            if (newBest == null) throw new RuntimeException("Queue is empty");

            if (newBest.moved.getHeight() == 3 ) {
                graph.setBest(newBest);
                break;
            }

        }



        return graph;

    }




    private static LinkedList<GridState> createChildren(ASPInputProgram program , BoardAivsAi board) throws Exception {
    //--SET FACTS
        possStateHandler.setFactProgram(program);
        ArrayList<Point> moveableArea = board.moveableArea(myUnitCode);
        if (moveableArea.isEmpty()) return new LinkedList<>(); // optimization

        for (Point p : moveableArea)
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

            if (move == null || build == null || value == null) throw new RuntimeException("Missing move or build or value");

            //MAKE THE ACTION
            BoardCopy childBoard = board.copy(); // Copy the board to avoid changes in the original board
            if (!move.isInvalid()) {
                childBoard.moveUnitSafe(myUnitCode, move.getCoord());
            }
            if (!build.isInvalid()) {
                childBoard.buildFloorSafe(myUnitCode, build.getCoord());
            }

            GridState child = new GridState(NEXT_ID++, board.getGridState().getPrograms(), move, build, value, childBoard);
            children.add(child);
        }

    //--RETURN CHILDREN
        return children;
    }


    /**
     * Add only the best children to the graph.
     * @param father
     * @param children
     * @throws Exception
     */
    private static LinkedList<GridState> addBestChildrenToGraph(GridState father, List<GridState> children) throws Exception {
        LinkedList<GridState> bestChildren = new LinkedList<>();

    //--SEARCH BEST CHILDREN
        for (GridState child : children) {
            int val =  child.value;

            if (bestChildren.isEmpty()) {
                bestChildren.add(child);
            }
            else {
                // if the value is better than the best value -> replace
                if (val > bestChildren.getFirst().value) {
                    bestChildren.clear();
                    bestChildren.add(child);
                }
                // if the value is equal to the best value -> add
                else if (val == bestChildren.getFirst().value) {
                    bestChildren.add(child);
                }
            }
        }


    //--ADD ALL BEST CHILDREN TO GRAPH
        for (GridState best: bestChildren){
            graph.addVertex(best); // Add the child to the graph if it is not already present
            graph.addEdge(father, best); // Add the edge between father and child in the graph if it is not already present
        }

        return bestChildren;

    }



}

