package org.project.UI.Model;


import org.project.Logic.Game.Board;
import org.project.Logic.Game.Player;
import org.project.Logic.Game.player.ai.NullAction;
import org.project.Logic.Game.player.ai.PlayerAi;
import org.project.Logic.Game.player.ai.actionSet;
import org.project.Logic.Game.player.human.PlayerManual;

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
        } else if (players[0] instanceof PlayerManual && players[1] instanceof PlayerManual) {
            PlayerManual[] playersManual = {(PlayerManual) players[0], (PlayerManual) players[1]};
            board = new BoardHvH(playersManual);
        } else {
//            board = new BoardMista
            throw new RuntimeException("To implement");
        }

    }

//--GAME METHODS--------------------------------------------------------------------------------------------------------

    public synchronized void playTurn(actionSet action) {
        //--WIN CONDITION -> can not make action
        if (action instanceof NullAction) {
            System.out.println("\nPLAYER " + action.player().getPlayerCode() + "LOSE. CAN'T MAKE ANY ACTION!");
            for ( Player p : board.getPlayers()){
                if (p.getPlayerCode()!=action.player().getPlayerCode()){
                    winner = p;
                }
            }
            board.setWin();
            return;
        }

        System.out.print(action.display());

        //--MOVE AND BUILD
        Class<? extends Board> boardClass = board.getClass();
        if (!boardClass.equals(BoardAivsAi.class))
            throw new RuntimeException("To implement");

        BoardAivsAi board = (BoardAivsAi) this.board;
        if (!board.moveUnitSafe(action.unitCode(), action.move())) {
            throw new RuntimeException("Invalid move " + action.move() + " for unit " + action.unitCode());

        }
        if (!board.buildFloorSafe(action.unitCode(), action.build()))
            throw new RuntimeException("Invalid build " + action.build() + " for unit " + action.unitCode());


        //--WIN CONDITION -> unit on height 3
        if (board.win()) {
            System.out.println("\nPLAYER " + action.player().getPlayerCode() + " WINS!");
            winner = action.player();
        }
    }

    private Player winner=null;
    public Player getWinner(){
        if (winner==null)
            throw new RuntimeException("No winner yet");
        return winner;
    }
}
