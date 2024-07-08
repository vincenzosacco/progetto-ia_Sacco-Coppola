package org.project.UI.Controller.home;

import org.project.Logic.Game.player.Player;
import org.project.Logic.Game.player.ai.PlayerAi;
import org.project.Logic.Game.player.human.PlayerManual;
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

    public PlayButtonController(HomePanel homePanel) {
        this.homePanel = homePanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Player[] players = homePanel.getPlayers();
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
        GameModel.getInstance().startGame(gameMode, players);
        ProjectView.getInstance().startGame(gameMode);
    }


}
