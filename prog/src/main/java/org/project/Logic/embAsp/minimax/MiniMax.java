package org.project.Logic.embAsp.minimax;

import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import org.project.Logic.Game.player.ai.PlayerAi;
import org.project.Logic.Game.player.ai.actionSet;
import org.project.Logic.LogicSettings;
import org.project.Logic.embAsp.Group;
import org.project.Logic.embAsp.WondevWomanHandler;
import org.project.Logic.embAsp.minimax.utility.MyGraphOriented;
import org.project.Logic.embAsp.moveIn;
import org.project.Logic.embAsp.buildIn;
import org.project.UI.Model.BoardAivsAi;
import org.project.UI.Model.GameModel;

import java.util.ArrayList;
import java.util.List;


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
        MyGraphOriented<GridState> graph = GraphBuilder.buildGraph(myHandler, myBoard, myUnitCode);

    //--SEARCH PATH FROM ROOT TO BEST
        List<GridState> a = graph.reverseDFS(graph.getBest(), graph.getRoot(), new ArrayList<>());

        //print a
        for (GridState gridState : a) {
            System.out.println(gridState);
        }


        return null;
    }


}
