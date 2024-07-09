package org.project.UI.Model;


import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import org.project.Logic.Game.Board;
import org.project.Logic.Game.player.Player;
import org.project.Logic.Game.player.ai.NullAction;
import org.project.Logic.Game.player.ai.PlayerAi;
import org.project.Logic.Game.player.ai.actionSet;
import org.project.Logic.Game.player.human.PlayerManual;
import org.project.Logic.embAsp.cell;

import java.util.ArrayList;

/**
 * Singleton class used to manage the model of the game.
 */
public class GameModel {
    private static GameModel instance = null;
    private Board board;

    public static final int AI_VS_AI = 0;
    public static final int AI_VS_HUMAN = 1;
    public static final int HUMAN_VS_HUMAN = 2;


    private GameModel() {
        gridState= new ASPInputProgram();
    }

    public static GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }
        return instance;
    }


    public Board getBoard() {
        return board;
    }

    public void startGame(Player[] players) throws Exception {
        if (players[0] instanceof PlayerAi && players[1] instanceof PlayerAi) {
            PlayerAi[] playersAi = {(PlayerAi) players[0], (PlayerAi) players[1]};
            board = new BoardAivsAi(playersAi);
        }
        else if (players[0] instanceof PlayerManual && players[1] instanceof PlayerManual) {
            PlayerManual[] playersManual = {(PlayerManual) players[0], (PlayerManual) players[1]};
            board = new BoardHvH(playersManual);
        }
        else {
//            board = new BoardMista
            throw new RuntimeException("To implement");
        }

        refreshGridState();
    }

//--GAME METHODS--------------------------------------------------------------------------------------------------------

    public synchronized void playTurn(actionSet action) throws Exception {
    //--WIN CONDITION -> can not make action
        if (action instanceof NullAction){
            board.setWin();
            System.out.println("\nPLAYER "+ action.unit().player().getPlayerCode() + "LOSE. CAN'T MAKE ANY ACTION!");
            return;
        }

        System.out.print( action.display());

        //--MOVE AND BUILD
        if(! board.moveUnitSafe(action.unit(), action.move())) {
            throw new RuntimeException("Invalid move " + action.move() + " for unit " + action.unit().unitCode());

        }
        if (! board.buildFloor(action.unit(),action.build()))
            throw new RuntimeException("Invalid build "+ action.build() + " for unit "+ action.unit().unitCode());


    //--WIN CONDITION -> unit on height 3
        if (board.win()){
            System.out.println("\nPLAYER "+ action.unit().player().getPlayerCode() + " WINS!");
            return;
        }

        //--REFRESH GRID STATE
        refreshGridState();
    }

    

//--EMBASP--------------------------------------------------------------------------------------------------------------
    private final ASPInputProgram gridState;

    private synchronized void refreshGridState() throws Exception {
        ArrayList<cell> cells = new ArrayList<>();
        int [][] grid = board.getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                cells.add((new cell(i, j, grid[i][j], board.playerCodeAt(i, j))));
            }
        }

        gridState.clearAll();
        for (cell c : cells) {
            gridState.addObjectInput(c);
         }
    }

    public ASPInputProgram getGridState() throws Exception {
        return new ASPInputProgram(gridState);
    }


}
