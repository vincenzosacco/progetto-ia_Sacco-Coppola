package org.project.UI.Model;


import org.project.Logic.Game.Board;
import org.project.UI.View.panels.GamePanel;

/**
 * Singleton class used to manage the model of the game.
 */
public class GameModel {
    private static GameModel instance = null;
    private Board board;

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


    public void startGame(int gameMode) {
        switch (gameMode) {
            case GamePanel.AI_VS_AI:
//                board = new Board();
                break;
            case GamePanel.AI_VS_HUMAN:
//                board = new Board();
                break;
            case GamePanel.HUMAN_VS_HUMAN:
                board = new BoardHvH();
                break;
        }
    }

}
