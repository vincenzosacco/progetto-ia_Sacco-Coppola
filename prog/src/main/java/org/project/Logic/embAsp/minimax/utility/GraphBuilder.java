package org.project.Logic.embAsp.minimax.utility;
//•S 0 : lo stato iniziale, che specifica come viene impostato il gioco
//•Deve_muovere( s): Il giocatore il cui turno è quello di muoversi nello stato s
//•Azioni(s)s): le mosse legali in s
//•Risultato(s, a): Il modello di transizione che definisce lo stato risultante
//dall'azione a nello stato s
//•È Terminale ( s): Un test di terminazione, che è vero in uno stato s quando il
//gioco è finito e falso altrimenti
//•Utilità(s, p): Una funzione di utilità funzione obiettivo o payoff
//che definisce il valore numerico finale per il giocatore p quando il gioco
//termina nello stato terminale s.


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
 * Class used to build a graph of {@link org.project.Logic.embAsp.minimax.utility.GridState GridState} starting by a state.
 */

public class GraphBuilder {
    static final GraphOriented<GridState> graph = new GraphOriented<>();
    static int NEXT_ID = 0;

    private static WondevWomanHandler myHandler;
    private static int myUnitCode;

    public static GraphOriented<GridState> buildGraph(WondevWomanHandler handler, BoardAivsAi origBoard, int unitCode) throws Exception {
    //--INIT
        myHandler = handler;
        myHandler.showAllAnswerSet(true);
        myUnitCode = unitCode;

        BoardCopy myBoard = origBoard.copy(); // Copy the board to avoid changes in the original board


    //--1) ADD FATHER TO GRAPH
        GridState father = new GridState.RootState(NEXT_ID++, myBoard.getGridState().getPrograms(), myBoard);
        graph.addVertex(father);

    //--2) ADD ALL CHILDREN
        Queue<GridState> queue = new PriorityQueue<>(GridState.heightComparator); // LIFO queue, Comparing by height
        queue.add(father);

        while (! queue.isEmpty()) // add children to the graph until the queue is empty
        {
        //1) CREATE CHILDREN
            GridState toExpand = queue.poll();
            ArrayList<GridState> children =  createChildren(toExpand, toExpand.board);

        //2) IF THERE ARE CHILDREN -> ADD CHILDREN
            if (! children.isEmpty()){
                addChildrenToGraph(toExpand, children);
                queue.addAll(children); // if all vertices are expanded, the queue will be empty
//                for (GridState child : children) {
//                    if (! child.isTerminal) queue.add(child);
//                }
            if (queue.peek().moved.getHeight() == 3 )
                break; // optimization
            }

        }

        return graph;
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

