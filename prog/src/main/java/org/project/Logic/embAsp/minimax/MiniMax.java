package org.project.Logic.embAsp.minimax;

import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import org.project.Logic.Game.player.ai.PlayerAi;
import org.project.Logic.Game.player.ai.actionSet;
import org.project.Logic.LogicSettings;
import org.project.Logic.embAsp.Group;
import org.project.Logic.embAsp.WondevWomanHandler;
import org.project.Logic.embAsp.cell;
import org.project.UI.Model.BoardAivsAi;
import org.project.UI.Model.GameModel;

public class MiniMax implements Group {
    private final WondevWomanHandler myHandler;
    private final ASPInputProgram encoding ;

    public MiniMax() {
        myHandler = new WondevWomanHandler();
        encoding = new ASPInputProgram();
        encoding.addFilesPath(LogicSettings.PATH_ENCOD_MINIMAX + "/possible_actions.asp");
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
        GraphBuilder.buildGraph(myHandler, myBoard, myUnitCode);

        return null;
    }



}
