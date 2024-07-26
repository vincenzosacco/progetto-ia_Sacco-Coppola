package org.project.Logic.embAsp.graph;

import org.project.Logic.Game.player.ai.NullAction;
import org.project.Logic.Game.player.ai.PlayerAi;
import org.project.Logic.Game.player.ai.actionSet;
import org.project.Logic.embAsp.Group;
import org.project.Logic.embAsp.buildIn;
import org.project.Logic.embAsp.moveIn;
import org.project.UI.Model.BoardAivsAi;
import org.project.UI.Model.GameModel;

import java.awt.*;


public class Graph implements Group {
    //    private final WondevWomanHandler MinMaxHandler;
//    private final ASPInputProgram  encodingMinMax;
    record action(moveIn move, buildIn build) {
    }

    public Graph() {
//        MinMaxHandler = new WondevWomanHandler();
//        encodingMinMax = new ASPInputProgram();
//        encodingMinMax.addFilesPath(LogicSettings.PATH_ENCOD_GRAPH + "/search_MinMax.asp");
//        MinMaxHandler.setEncoding(encodingMinMax);
    }

    private BoardAivsAi myBoard;

    @Override
    public actionSet callEmbAsp(PlayerAi player) throws Exception {
        myBoard = (BoardAivsAi) GameModel.getInstance().getBoard();

        //--CHOSE UNIT
        int maxUnitCode = player.getFirstUnitCode();
//        int minUnitCode = myBoard.getUnitCodes().stream().filter(c -> c != maxUnitCode).findFirst().orElseThrow();

        //--MINMAX GRAPH
        NewGraph graph = new NewGraph();
        Point rootCoord = myBoard.unitCoord(maxUnitCode);
        moveIn rootMove = new moveIn(rootCoord.x, rootCoord.y, myBoard.heightAt(rootCoord));
        buildIn rootBuild = new buildIn(rootCoord.x, rootCoord.y, myBoard.heightAt(rootCoord));
        GridState rootState = new GridState(myBoard.copy(), rootMove, rootBuild, rootMove.getHeight());

        NewGraph.Node root = graph.buildGraph(rootState, maxUnitCode, 2);

        //--SEARCH CHILDREN NODE WITH MAX UTILITY
        int maxUtility = Integer.MIN_VALUE;
        NewGraph.Node bestChild = null;
        for (NewGraph.Node child : root.children) {
            maxUtility = Math.max(maxUtility, child.utility());
            bestChild = child;
        }

        if (bestChild == null)
            return new NullAction(player, maxUnitCode);

        //--RETURN ACTION
        GridState bestState = graph.depth1Nodes.get(bestChild);
        return new actionSet(player, maxUnitCode, bestState.moved.getCoord(), bestState.builded.getCoord());

    }
}

