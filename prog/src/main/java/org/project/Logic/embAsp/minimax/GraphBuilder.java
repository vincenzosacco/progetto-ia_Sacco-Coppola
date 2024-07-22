package org.project.Logic.embAsp.minimax;



import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import org.project.Logic.LogicSettings;
import org.project.Logic.embAsp.WondevWomanHandler;
import org.project.Logic.embAsp.buildIn;
import org.project.Logic.embAsp.moveIn;
import org.project.UI.Model.BoardAivsAi;

import java.awt.*;
import java.util.*;

import static org.project.UI.Model.BoardAivsAi.BoardCopy;


/**
 * Class used to build a graph of {@link T GridState} starting by a state.
 */

public class GraphBuilder {
    private static MyGraph<Integer> graph ;
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

    static MyGraph<Integer> buildGraph(WondevWomanHandler handler, BoardAivsAi origBoard, int unitCode) throws Exception {
    //--INIT
        init(handler, unitCode);
        BoardCopy myBoard = origBoard.copy(); // Copy the board to avoid changes in the original board

    //--BUILD GRAPH
        T root = new T.RootState(NEXT_ID++, myBoard.getGridState().getPrograms(), myBoard);

        //1) CREATE CHILDREN
            T currentBest = queue.poll();
            ArrayList<T> children =  createChildren(currentBest, currentBest.board);




    //--ADD ALL CHILDREN
//        Queue<T> queue = new PriorityQueue<>(T.heightComparator); // LIFO queue, Comparing by height
//        queue.add(father);








//        while (! queue.isEmpty()) // add children to the graph until the queue is empty
//        {
//        //1) CREATE CHILDREN
//            T currentBest = queue.poll();
//            ArrayList<T> children =  createChildren(currentBest, currentBest.board);
//
//        //2) IF THERE ARE CHILDREN -> ADD CHILDREN
//            if (! children.isEmpty()){
//                addChildrenToGraph(currentBest, children);
//                queue.addAll(children); // if all vertices are expanded, the queue will be empty
//            }
//
//        //4) STOP CONDITION
//            T newBest = queue.peek();
//            if (newBest.moved.getHeight() == 3 ) {
//                graph.setBest(newBest);
//                break;
//            }
//        }

        return graph;

    }




    private static ArrayList<T> createChildren(ASPInputProgram program , BoardAivsAi board) throws Exception {
    //--SET FACTS
        possStateHandler.setFactProgram(program);
        ArrayList<Point> moveableArea = board.moveableArea(myUnitCode);
        if (moveableArea.isEmpty()) return new ArrayList<>(); // optimization

        for (Point p : moveableArea)
            possStateHandler.addFactAsString("moveCell(" + p.x + "," + p.y + "," + board.heightAt(p) + ")");

    //--FOR EACH LEGAL ACTION, CREATE A CHILD
        ArrayList<T> children = new ArrayList<>();
        possStateHandler.startSync();

        for (AnswerSet as : possStateHandler.getAnswerSets().getAnswersets()) {
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

            T child = new T(NEXT_ID++, board.getGridState().getPrograms(), move, build, childBoard);
            children.add(child);
        }

    //--RETURN CHILDREN
        return children;
    }


    private static void addChildrenToGraph(T father, ArrayList<T> children) throws Exception {
        if (! graph.containsVertex(father)) throw new RuntimeException("Father not present in the graph");

        ASPInputProgram facts = new ASPInputProgram();
        for (T child : children) {
        //--SET FACTS
            facts.addObjectInput(child.moved); facts.addObjectInput(child.builded);
            stateValueHandler.setFactProgram(facts);

        //--GET VALUE
            stateValueHandler.startSync();

            // only 1 answer set
            System.out.println(stateValueHandler.getAnswerSets().getAnswerSetsString());

//            graph.addVertex(child); // Add the child to the graph if it is not already present
//            graph.addEdge(father, child); // Add the edge between father and child in the graph if it is not already present
        }

    }



}

