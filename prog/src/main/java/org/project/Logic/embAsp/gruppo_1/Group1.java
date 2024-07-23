package org.project.Logic.embAsp.gruppo_1;

import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import org.project.Logic.Game.Player;
import org.project.Logic.Game.player.ai.NullAction;
import org.project.Logic.Game.player.ai.PlayerAi;
import org.project.Logic.Game.player.ai.actionSet;
import org.project.Logic.LogicSettings;
import org.project.Logic.embAsp.Group;
import org.project.Logic.embAsp.WondevWomanHandler;
import org.project.Logic.embAsp.buildIn;
import org.project.Logic.embAsp.moveIn;
import org.project.UI.Model.BoardAivsAi;
import org.project.UI.Model.GameModel;

import java.awt.*;
import java.util.ArrayList;

//TODO: IMPLEMENTA CAMBIO STRATEGIA SE GIOCO PER PRIMO O SECONDO. CHI GIOCA PER PRIMA CONVIENE MUOVERSI LONTANO DALL'AVVERSARIO

public class Group1 implements Group {
    private BoardAivsAi.BoardCopy myBoard;
    private PlayerAi myPlayer;
    private PlayerAi enemyPlayer;
    private WondevWomanHandler myHandler;
    private int myUnitCode;
    private ArrayList<Integer> enemyUnits;

    /**
     * Call the EmbAsp for the player.
     * Use a copy of the board and the player to avoid any modification.<p>
     * After the computation, the method will return the actionSet to be executed.
     *
     * @param player
     * @throws Exception
     * @returns
     */
    public actionSet callEmbAsp(PlayerAi player) throws Exception {
        myBoard = ((BoardAivsAi) GameModel.getInstance().getBoard()).copy();

        //--SET PLAYERS
        myPlayer = player;
        for (Player p : myBoard.getPlayers()) {
            if (!p.equals(player))
                enemyPlayer = (PlayerAi) p;
        }

        //--SET HANDLER
        myHandler = new WondevWomanHandler();

        //--CHOSE UNIT
        myUnitCode = myPlayer.getFirstUnitCode();
        enemyUnits = new ArrayList<>();
        enemyUnits.addAll(enemyPlayer.getUnitCodes());

        //--MOVE
        Point move = makeMove();


        if (move == null)
            return new NullAction(myPlayer, myUnitCode);

        //MOVE IN BOARD
        if (!myBoard.moveUnitSafe(myUnitCode, move))
            throw new RuntimeException("SOMETHING WRONG, CANNOT MOVE TO" + "(" + move.x + "," + move.y + ")" + "IN GROUP. ");

        //--BUILD
        Point build = makeBuild();

        if (build == null)
            return new NullAction(myPlayer, myUnitCode);

        return new actionSet(myPlayer, myUnitCode, move, build);
    }


    private Point makeMove() throws Exception {
        ASPInputProgram moveProgram = new ASPInputProgram();
        moveProgram.addFilesPath(LogicSettings.PATH_ENCOD_GROUP1 + "/move.asp");
        myHandler.setEncoding(moveProgram);

        //--CAN'T MOVE --> WIN CONDITION --> NULL ACTION
        //moveCell(X,Y,H). --> cell where I can move unit
        ArrayList<Point> moveableArea = myBoard.moveableArea(myUnitCode);
        if (moveableArea.isEmpty()) {
            return null;
        }

        //--REFRESH FACTS
        refreshFacts();


        //--CAN MOVE
        for (Point cell : moveableArea)
            myHandler.addFactAsString("moveCell(" + cell.x + "," + cell.y + "," + myBoard.heightAt(cell) + ")");

        //--OUTPUT
        myHandler.startSync();
//        System.out.println("\nMOVE" + myPlayer.getPlayerCode() + "\n" + myHandler.getOptimalAnswerSets().getFirst().toString() + "\n" + myHandler.getFactsString()); //TODO: DELETE
        for (Object atom : myHandler.getOptimalAnswerSets().getFirst().getAtoms()) {
            if (atom instanceof moveIn) {
                return new Point(((moveIn) atom).getX(), ((moveIn) atom).getY());
            }
        }

        throw new RuntimeException("Something wrong in makeMove");
    }

    private Point makeBuild() throws Exception {
        ASPInputProgram buildProgram = new ASPInputProgram();
        buildProgram.addFilesPath(LogicSettings.PATH_ENCOD_GROUP1 + "/build.asp");
        myHandler.setEncoding(buildProgram);

        //--CAN'T BUILD
        //buildCell(X,Y,H). --> cell where my unit can build
        ArrayList<Point> buildableArea = myBoard.buildableArea(myUnitCode);
        if (buildableArea.isEmpty()) {
            return null;
        }

        //--REFRESH FACTS
        refreshFacts();


        //--CAN BUILD
        for (Point cell : buildableArea) {
            myHandler.addFactAsString("buildCell(" + cell.x + "," + cell.y + "," + myBoard.heightAt(cell) + ")");
        }

        myHandler.startSync();
//        System.out.println("\nBUILD" + myPlayer.getPlayerCode() + "\n" + myHandler.getOptimalAnswerSets().getFirst().toString() + "\n" + myHandler.getFactsString()); //TODO DELETE

        //ADDING FACTS
        for (Object atom : myHandler.getOptimalAnswerSets().getFirst().getAtoms()) {
            if (atom instanceof buildIn) {
                return new Point(((buildIn) atom).getX(), ((buildIn) atom).getY());
            }
        }

        throw new RuntimeException("Something wrong in makeBuild");
    }


//--UTILITY-------------------------------------------------------------------------------------------------------------

    /**
     * Refresh facts in myHandler.
     */
    public void refreshFacts() throws Exception {

        //cell(X,Y,H,U)
        myHandler.setFactProgram(myBoard.getGridState());

        //choosedUnit(U)
        myHandler.addFactAsString("choosedUnit(" + myUnitCode + ")");

        //enemyMoveCell(X,Y,H,U).
        for (int enemyUnitCode : enemyPlayer.getUnitCodes())
            for (Point cell : myBoard.moveableArea(enemyUnitCode)) {
                myHandler.addFactAsString("enemyMoveCell(" + cell.x + "," + cell.y + "," + myBoard.heightAt(cell) + "," + enemyUnitCode + ")");
            }
    }


}
