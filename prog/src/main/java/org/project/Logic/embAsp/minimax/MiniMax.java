package org.project.Logic.embAsp.minimax;

import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import org.project.Logic.Game.player.ai.PlayerAi;
import org.project.Logic.Game.player.ai.actionSet;
import org.project.Logic.LogicSettings;
import org.project.Logic.embAsp.Group;
import org.project.Logic.embAsp.WondevWomanHandler;
import org.project.Logic.embAsp.minimax.atoms.vertex;
import org.project.Logic.embAsp.moveIn;
import org.project.Logic.embAsp.buildIn;
import org.project.UI.Model.BoardAivsAi;
import org.project.UI.Model.GameModel;

import java.util.List;


public class MiniMax implements Group {
    private final WondevWomanHandler MinMaxHandler;
    private final ASPInputProgram  encodingMinMax;
    record action(moveIn move, buildIn build) { }

    public MiniMax() {
        MinMaxHandler = new WondevWomanHandler();
        encodingMinMax = new ASPInputProgram();
        encodingMinMax.addFilesPath(LogicSettings.PATH_ENCOD_MINIMAX + "/search_MinMax.asp");
        MinMaxHandler.setEncoding(encodingMinMax);
    }

    private BoardAivsAi myBoard;

    @Override
    public actionSet callEmbAsp(PlayerAi player) throws Exception {
        myBoard = (BoardAivsAi) GameModel.getInstance().getBoard();

    //--CHOSE UNIT
        int maxUnitCode = player.getFirstUnitCode();
        int minUnitCode = myBoard.getUnitCodes().stream().filter(c -> c != maxUnitCode).findFirst().orElseThrow();

    //--MINMAX GRAPH
        MyGraph maxGraph = GraphBuilder.MaxGraph(myBoard, maxUnitCode, minUnitCode);

    //--CHOOSE BEST ACTION
        List<GridState> fathers= maxGraph.getFathers(maxGraph.getBestLeaf());
        if (fathers.size()>1) {
            fathers.forEach(System.out::println);
            //TODO: implementare scelta migliore
        }
        GridState bestState = fathers.getFirst();

        return new actionSet(player, maxUnitCode, bestState.moved.getCoord(), bestState.builded.getCoord());

//        return new actionSet(player, maxUnitCode, maxGraph.getBestLeaf().moved.getCoord(), maxGraph.getBestLeaf().builded.getCoord());
    }





 }


