package org.project.Logic.embAsp.minimax;

import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import org.project.Logic.Game.player.ai.PlayerAi;
import org.project.Logic.Game.player.ai.actionSet;
import org.project.Logic.LogicSettings;
import org.project.Logic.embAsp.Group;
import org.project.Logic.embAsp.WondevWomanHandler;
import org.project.Logic.embAsp.moveIn;
import org.project.Logic.embAsp.buildIn;
import org.project.UI.Model.BoardAivsAi;
import org.project.UI.Model.GameModel;

import java.util.*;


public class MiniMax implements Group {
    private final WondevWomanHandler myHandler;
    private final ASPInputProgram encoding;
    record action(moveIn move, buildIn build) { }

    public MiniMax() {
        myHandler = new WondevWomanHandler();

        encoding = new ASPInputProgram();
        encoding.addFilesPath(LogicSettings.PATH_ENCOD_MINIMAX + "/possible_state.asp");
        myHandler.setEncoding(encoding);

    }

    private BoardAivsAi myBoard;
    private int myUnitCode;

    @Override
    public actionSet callEmbAsp(PlayerAi player) throws Exception {
        myBoard = (BoardAivsAi) GameModel.getInstance().getBoard();

    //--CHOSE UNIT
        myUnitCode = player.getFirstUnitCode();

    //--BUILD GRAPH
        MyGraph<GridState> graph = GraphBuilder.buildGraph(myHandler, myBoard, myUnitCode);

    //--MAKE ACTION
        GridState action = graph.getAdjacents(graph.getRoot()).getFirst(); // get the first adjacent of the root
        moveIn move = action.moved;
        buildIn build = action.builded;

        return new actionSet(player, myUnitCode, move.getCoord(), build.getCoord());
    }


    //--SEARCH PATH FROM ROOT TO BEST
//        Iterator<GridState> it = graph.getVertices().iterator();
//        for (int i = 0; i < 500; i++) it.next();
//        GridState search = it.next();
//
//        Collection<GridState> path = graph.find(graph.getRoot(), graph.getBest());
//
//        // print path
//        for (GridState state : path) {
//            System.out.println(state);
//        }

 }


