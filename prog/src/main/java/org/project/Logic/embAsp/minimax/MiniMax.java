package org.project.Logic.embAsp.minimax;

import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import org.project.Logic.Game.player.Unit;
import org.project.Logic.Game.player.ai.PlayerAi;
import org.project.Logic.Game.player.ai.actionSet;
import org.project.Logic.LogicSettings;
import org.project.Logic.embAsp.Group;
import org.project.Logic.embAsp.WondevWomanHandler;
import org.project.UI.Model.BoardAivsAi;
import org.project.UI.Model.GameModel;

public class MiniMax implements Group {
    private final WondevWomanHandler myHandler;
    private final ASPInputProgram encoding ;

    public MiniMax() throws ObjectNotValidException, IllegalAnnotationException {
        myHandler = new WondevWomanHandler();
        myHandler.mapToEmb(cell.class);
        encoding = new ASPInputProgram();
        encoding.addFilesPath(LogicSettings.PATH_ENCOD_MINIMAX + "/possible_actions.asp");
        myHandler.setEncoding(encoding);

   }

    private BoardAivsAi myBoard;
    private Unit myUnit;
    @Override
    public actionSet callEmbAsp(PlayerAi player) throws Exception {
        myBoard = (BoardAivsAi) GameModel.getInstance().getBoard();

    //--CHOSE UNIT
        myUnit = player.getFirstUnit();

    //--BUILD GRAPH
        GraphBuilder.buildGraph(myHandler, myBoard, myUnit.unitCode());

        return null;
    }



}
