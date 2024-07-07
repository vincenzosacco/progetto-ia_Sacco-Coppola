package org.project.UI.Controller.home;

import org.project.UI.Model.game.GameModel;
import org.project.UI.View.ProjectView;
import org.project.UI.View.panels.GamePanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller class for the HomePanel.<p>
 * This listener is shared by the buttons in the HomePanel and is used to start the game with the selected game mode.
 * </p>
 */
public class HomeController implements ActionListener {
    private int gameMode;

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "AI vs AI":
                gameMode = GamePanel.AI_VS_AI;
                break;
            case "AI vs Umano":
                gameMode = GamePanel.AI_VS_HUMAN;
                break;
            case "Umano vs Umano":
                gameMode = GamePanel.HUMAN_VS_HUMAN;
                break;
            default: throw new IllegalArgumentException("Invalid action command");

        }

        GameModel.getInstance().startGame(gameMode);
        ProjectView.getInstance().startGame(gameMode);
    }
}
