package org.project.Logic.embAsp.minimax;



import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import org.project.Logic.embAsp.WondevWomanHandler;
import org.project.Logic.embAsp.buildIn;
import org.project.Logic.embAsp.moveIn;
import org.project.UI.Model.BoardAivsAi;

import java.awt.*;
import java.util.*;

import static org.project.UI.Model.BoardAivsAi.BoardCopy;


/**
 * Class used to build a graph of {@link GridState GridState} starting by a state.
 */

public class GraphBuilder {
    private static MyGraph graph ;
    static int NEXT_ID = 0;
    private static WondevWomanHandler myHandler;
    private static int myUnitCode;

    static MyGraph buildGraph(WondevWomanHandler handler, BoardAivsAi origBoard, int unitCode) throws Exception {
    //--INIT
        myHandler = handler;
        myHandler.showAllAnswerSet(true);
        myUnitCode = unitCode;
        graph = new MyGraph();

        BoardCopy myBoard = origBoard.copy(); // Copy the board to avoid changes in the original board


    //--ADD FATHER TO GRAPH
        GridState father = new GridState.RootState(NEXT_ID++, myBoard.getGridState().getPrograms(), myBoard);
        graph.addVertex(father);

    //--ADD ALL CHILDREN
        Queue<GridState> queue = new PriorityQueue<>(GridState.heightComparator); // LIFO queue, Comparing by height
        queue.add(father);

        while (! queue.isEmpty()) // add children to the graph until the queue is empty
        {
        //1) CREATE CHILDREN
            GridState currentBest = queue.poll();
            ArrayList<GridState> children =  createChildren(currentBest, currentBest.board);

        //2) IF THERE ARE CHILDREN -> ADD CHILDREN
            if (! children.isEmpty()){
                addChildrenToGraph(currentBest, children);
                queue.addAll(children); // if all vertices are expanded, the queue will be empty
            }

        //4) STOP CONDITION
            GridState newBest = queue.peek();
            if (newBest.moved.getHeight() == 3 ) {
                graph.setBest(newBest);
                break;
            }
        }

        return graph;

//        throw new RuntimeException("No terminal state found, TO IMPLEMENT");
    }



    private static ArrayList<GridState> createChildren(ASPInputProgram program , BoardAivsAi board) throws Exception {
    //--SET FACTS
        myHandler.setFactProgram(program);
        ArrayList<Point> moveableArea = board.moveableArea(myUnitCode);
        if (moveableArea.isEmpty()) return new ArrayList<>(); // optimization

        for (Point p : moveableArea)
            myHandler.addFactAsString("moveCell(" + p.x + "," + p.y + "," + board.heightAt(p) + ")");

    //--FOR EACH LEGAL ACTION, CREATE A CHILD
        ArrayList<GridState> children = new ArrayList<>();
        myHandler.startSync();

        for (AnswerSet as : myHandler.getAnswerSets().getAnswersets()) {
            moveIn move = null;
            buildIn build = null;
            for (Object atom : as.getAtoms()) {
                if (atom instanceof moveIn) move = (moveIn) atom;
                else if (atom instanceof buildIn) build = (buildIn) atom;
                else throw new RuntimeException("Unexpected atom: " + atom);
            }
            if (move == null || build == null) throw new RuntimeException("Missing move or build");

            //MAKE THE ACTION
            BoardCopy childBoard = board.copy(); // Copy the board to avoid changes in the original board
            if (!move.isInvalid()) {
                childBoard.moveUnitSafe(myUnitCode, move.getCoord());
            }
            if (!build.isInvalid()) {
                childBoard.buildFloorSafe(myUnitCode, build.getCoord());
            }

            GridState child = new GridState(NEXT_ID++, board.getGridState().getPrograms(), move, build, childBoard);
            children.add(child);
        }

    //--RETURN CHILDREN
        return children;
    }

    private static void addChildrenToGraph(GridState father, ArrayList<GridState> children) {
        if (! graph.containsVertex(father)) throw new RuntimeException("Father not present in the graph");

        for (GridState child : children) {
            graph.addVertex(child); // Add the child to the graph if it is not already present
            graph.addEdge(father, child); // Add the edge between father and child in the graph if it is not already present
        }

    }

}

