package org.project.UI.Model;


import org.project.Logic.Game.Board;
import org.project.Logic.Game.player.Player;
import org.project.Logic.Game.player.ai.PlayerAi;
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


    public void startGame(int gameMode, Player[] players) {
        switch (gameMode) {
            case AI_VS_AI:
                PlayerAi[] playersAi;
                try {
                    playersAi = new PlayerAi[]{(PlayerAi) players[0], (PlayerAi) players[1]};
                }
                catch (ClassCastException e) {
                    throw new IllegalArgumentException("Invalid player type");
                }


                board = new BoardAivsAi(playersAi);
                break;

            case AI_VS_HUMAN:
//                board = new Board();
                break;

            case HUMAN_VS_HUMAN:
                PlayerManual[] playersManual ;
                try {
                    playersManual = new PlayerManual[]{(PlayerManual) players[0], (PlayerManual) players[1]};
                }
                catch (ClassCastException e) {
                    throw new IllegalArgumentException("Invalid player type");
                }

                board = new BoardHvH(playersManual);
                break;


            default:
                throw new IllegalArgumentException("Invalid game mode");
        }
    }

}
