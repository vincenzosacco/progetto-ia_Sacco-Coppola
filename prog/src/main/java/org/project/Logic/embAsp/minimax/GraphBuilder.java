package org.project.Logic.embAsp.minimax;
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


import it.unical.mat.embasp.languages.asp.AnswerSet;
import org.project.Logic.embAsp.WondevWomanHandler;
import org.project.Logic.embAsp.buildIn;
import org.project.Logic.embAsp.minimax.utility.Graph;
import org.project.Logic.embAsp.minimax.utility.GridState;
import org.project.Logic.embAsp.moveIn;
import org.project.UI.Model.BoardAivsAi;

import java.awt.*;
import java.util.ArrayList;

/**
 * Class used to build a graph of {@link org.project.Logic.embAsp.minimax.utility.GridState GridState} starting by a state.
 */

class GraphBuilder {
    private static final Graph<GridState> graph = new Graph<>();
    private static WondevWomanHandler myHandler;
    private static BoardAivsAi.BoardCopy myBoard;
    private static int myUnitCode;

    static Graph<GridState> buildGraph(WondevWomanHandler handler, BoardAivsAi board, int unitCode) throws Exception {
    //--INIT
        myHandler = handler;
        myHandler.showAllAnswerSet(true);
        myBoard = board.copy();
        myUnitCode = unitCode;

    //--BUILD GRAPH


        //1)ADD CURRENT STATE AS FATHER
        GridState father = new GridState.RootState(myBoard.getGridState().getPrograms());
        graph.addVertex(father);
        //2)ADD CHILDREN
        addChildren(father);
        return graph;
    }


    private static void addChildren(GridState father) throws Exception {

    //--SET FACTS
        myHandler.setFactProgram(father);
        ArrayList<Point> moveableArea = myBoard.moveableArea(myUnitCode);
        for (Point p : moveableArea) {
            myHandler.addFactAsString("moveCell(" + p.x + "," + p.y + "," + myBoard.heightAt(p) + ")");
    //--FOR EACH LEGAL ACTION
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

            //1)MAKE THE ACTION
                myBoard.moveUnitSafe(myUnitCode, move.getCoord());
                myBoard.buildFloorSafe(myUnitCode, build.getCoord());

            //2)ADD VERTEX & EDGE
                GridState child = new GridState(myBoard.getGridState().getPrograms(), move, build);
                graph.addVertex(child);
                graph.addEdge(father, child);

            //3)IF NOT TERMINAL -> RECURSION
                if (!graph.containsVertex(child) && !child.isTerminal()) addChildren(child);

            //4)IF TERMINAL -> RESET BOARD
                myBoard.setBoardFromGridState(father);


            }
        }
    }


}

