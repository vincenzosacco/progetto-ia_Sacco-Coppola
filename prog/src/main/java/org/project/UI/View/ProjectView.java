package org.project.UI.View;

import org.project.UI.Settings;
import org.project.UI.View.panels.GamePanel;
import org.project.UI.View.panels.home.HomePanel;
import org.project.UI.View.panels.MyPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Singleton class used to manage the view of the game.
 */
public class ProjectView extends JFrame {
    private static ProjectView instance = null;

    private CardLayout cardLayout;
    private HomePanel homePanel;
    private GamePanel gamePanel;

    private ProjectView() {
        super();

        setName("ProjectView");
        getContentPane().setName("ProjectView.ContentPane");

        setTitle("WondevWoman");
        setSize(Settings.GAME_SIZE_DEFAULT);
        getContentPane().setSize(Settings.GAME_SIZE_DEFAULT);

    //--LAYOUT
        setLayout(cardLayout = new CardLayout());
        setup();


        moveToScreenCenter();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    public static ProjectView getInstance() {
        if (instance == null) {
            instance = new ProjectView();
        }
        return instance;
    }

    public void startGame(int gameMode) {
        homePanel.setCenterPanel(new GamePanel(gameMode));
    }

//--UTILITY

    /**
     * Method used to move the window to the center of the screen.
     */
    public void moveToScreenCenter(){
        setLocationRelativeTo(null);
    }

    public void setFullScreen(){
        setExtendedState(JFrame.MAXIMIZED_BOTH);

    }

//--PRIVATE
    private void setup() {
        homePanel = new HomePanel();
        getContentPane().add(homePanel, "home");
    }
}
