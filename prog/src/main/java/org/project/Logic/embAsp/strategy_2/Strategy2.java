package org.project.Logic.embAsp.strategy_2;

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


public class Strategy2 implements Group {
    private BoardAivsAi.BoardCopy myBoard;
    private PlayerAi myPlayer;
    private PlayerAi enemyPlayer;
    private WondevWomanHandler MoveHandler, BuildHandler;
    private int myUnitCode;
    private ArrayList<Integer> enemyUnits;

    public Strategy2() {
        MoveHandler = new WondevWomanHandler();
        ASPInputProgram moveEnc = new ASPInputProgram();
        moveEnc.addFilesPath(LogicSettings.PATH_ENCOD_STRATEGY_2 + "/move2.asp");
        MoveHandler.setEncoding(moveEnc);

        BuildHandler = new WondevWomanHandler();
        ASPInputProgram buildEnc = new ASPInputProgram();
        buildEnc.addFilesPath(LogicSettings.PATH_ENCOD_STRATEGY_2 + "/build2.asp");
        BuildHandler.setEncoding(buildEnc);


    }


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
        init(player);


        Point move = makeMove();
        Point build = makeBuild();


        if (move == null || build == null)
            return new NullAction(myPlayer, myUnitCode);

        return new actionSet(myPlayer, myUnitCode, move, build);

    }

    private Point makeMove() throws Exception {
        //--FACTS
        if (!setFactsMove())
            return null;

        //--SOLVE
        MoveHandler.startSync();
//        System.out.println("\nMOVE" + myPlayer.getPlayerCode() + "\n" + myHandler.getOptimalAnswerSets().getFirst().toString() + "\n" + myHandler.getFactsString()); //TODO: DELETE

        Point move = null;
        for (Object atom : MoveHandler.getOptimalAnswerSets().getFirst().getAtoms())
            if (atom instanceof moveIn)
                move = new Point(((moveIn) atom).getX(), ((moveIn) atom).getY());

        //--MOVE IN BOARD
        if (!myBoard.moveUnitSafe(myUnitCode, move))
            throw new RuntimeException("SOMETHING WRONG, CANNOT MOVE TO" + "(" + move.x + "," + move.y + ")" + "IN GROUP. ");

        //--RETURN
        return move;
    }

    private Point makeBuild() throws Exception {
        //--FACTS
        if (!setFactsBuild())
            return null;

        //--SOLVE
        BuildHandler.startSync();
//        System.out.println("\nBUILD" + myPlayer.getPlayerCode() + "\n" + myHandler.getOptimalAnswerSets().getFirst().toString() + "\n" + myHandler.getFactsString()); //TODO: DELETE
        Point build = null;
        for (Object atom : BuildHandler.getOptimalAnswerSets().getFirst().getAtoms())
            if (atom instanceof buildIn)
                build = new Point(((buildIn) atom).getX(), ((buildIn) atom).getY());

        //--BUILD IN BOARD
        if (!myBoard.buildFloorSafe(myUnitCode, build))
            throw new RuntimeException("SOMETHING WRONG, CANNOT BUILD TO" + "(" + build.x + "," + build.y + ")" + "IN GROUP. ");

        //--RETURN
        return build;
    }

    public boolean setFactsMove() throws Exception {
        MoveHandler.clearFacts();

        ArrayList<Point> moveableArea = myBoard.moveableArea(myUnitCode);
        if (moveableArea.isEmpty()) {
            return false;
        }
        //cell(X,Y,H,U)
        MoveHandler.setFactProgram(myBoard.getGridState());

        //moveCell(X,Y,H). --> cell where I can move unit
        for (Point cell : moveableArea)
            MoveHandler.addFactAsString("moveCell(" + cell.x + "," + cell.y + "," + myBoard.heightAt(cell) + ")");

        //choosedUnit(U)
        MoveHandler.addFactAsString("choosedUnit(" + myUnitCode + ")");

        //enemyMoveCell(X,Y,H,U).
        for (int enemyUnitCode : enemyPlayer.getUnitCodes())
            for (Point cell : myBoard.moveableArea(enemyUnitCode)) {
                MoveHandler.addFactAsString("enemyMoveCell(" + cell.x + "," + cell.y + "," + myBoard.heightAt(cell) + "," + enemyUnitCode + ")");
            }

        return true;
    }

    private boolean setFactsBuild() throws Exception {
        BuildHandler.clearFacts();

        ArrayList<Point> buildableArea = myBoard.buildableArea(myUnitCode);
        if (buildableArea.isEmpty()) {
            return false;
        }
        //cell(X,Y,H,U)
        BuildHandler.setFactProgram(myBoard.getGridState());

        //buildCell(X,Y,H). --> cell where I can build
        for (Point cell : buildableArea)
            BuildHandler.addFactAsString("buildCell(" + cell.x + "," + cell.y + "," + myBoard.heightAt(cell) + ")");

        //choosedUnit(U)
        BuildHandler.addFactAsString("choosedUnit(" + myUnitCode + ")");

        //enemyMoveCell(X,Y,H,U).
        for (int enemyUnitCode : enemyPlayer.getUnitCodes())
            for (Point cell : myBoard.moveableArea(enemyUnitCode)) {
                BuildHandler.addFactAsString("enemyMoveCell(" + cell.x + "," + cell.y + "," + myBoard.heightAt(cell) + "," + enemyUnitCode + ")");
            }

        return true;
    }

    private void init(PlayerAi player) {
        //--SET PLAYERS
        myPlayer = player;
        for (Player p : myBoard.getPlayers()) {
            if (!p.equals(player))
                enemyPlayer = (PlayerAi) p;
        }

        //--CHOSE UNIT
        myUnitCode = myPlayer.getFirstUnitCode();
        enemyUnits = new ArrayList<>();
        enemyUnits.addAll(enemyPlayer.getUnitCodes());
    }
}


