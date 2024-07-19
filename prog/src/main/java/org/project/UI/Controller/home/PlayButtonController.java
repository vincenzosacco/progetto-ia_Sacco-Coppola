package org.project.UI.Controller.home;

import org.project.Logic.Game.Player;
import org.project.Logic.Game.player.ai.PlayerAi;
import org.project.Logic.Game.player.human.PlayerManual;
import org.project.UI.Controller.GameLoop;
import org.project.UI.Model.GameModel;
import org.project.UI.View.ProjectView;
import org.project.UI.View.panels.home.HomePanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Controller used to start the game.
 */
public class PlayButtonController implements ActionListener {
    private final HomePanel homePanel;
    //GameLoop must be executed by new thread otherwise the GUI will freeze
    private final Thread threadGameLoop;

    public PlayButtonController(HomePanel homePanel) {
        this.homePanel = homePanel;
        threadGameLoop = new Thread(GameLoop::startGameLoop);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Player[] players = homePanel.getPlayers();

        if (players == null) {
            return;
        }

        int gameMode;

    //--GAME MODE
        if (players[0] instanceof PlayerAi && players[1] instanceof PlayerAi) {
            gameMode= GameModel.AI_VS_AI;
        }
        else if (players[0] instanceof PlayerManual && players[1] instanceof PlayerManual) {
            gameMode = GameModel.HUMAN_VS_HUMAN;
        }
        else {
            gameMode = GameModel.AI_VS_HUMAN;
        }

    //--START GAME
        try {
            GameModel.getInstance().startGame(players);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        ProjectView.getInstance().startGame(gameMode);
        if (GameLoop.isRunning()) {
            try {
                GameLoop.stopGameLoop(Thread.currentThread());
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

        threadGameLoop.start();
    }


}
