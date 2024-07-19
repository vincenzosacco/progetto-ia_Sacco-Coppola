package org.project.UI.View.panels.home;

import org.project.Logic.Game.Player;
import org.project.Logic.Game.player.ai.PlayerAi;
import org.project.Logic.Game.player.human.PlayerManual;
import org.project.UI.View.panels.MyPanel;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends MyPanel {
    private final side left, right;
    private JPanel center;


    public HomePanel() {
        super("HomePanel");
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        left = new side("PLAYER 1");
        add(left );

        center = new centerPanel();
        add(center);

        right = new side("PLAYER 2");
        add(right);

    }



//--PUBLIC METHODS---------------------------------------------------------------------------------------------------

    /**
     * Method used to set the center panel of the HomePanel.
     * @param panel the panel to set as center.
     */
    public void setCenterPanel(JPanel panel) {
        remove(center);
        center = panel;
        add(center, 1);
        revalidate();
        addNotify();
    }

    public Player[] getPlayers() {
        Player player1, player2;
        Color colorLeft, colorRight;
        Component selectedLeft = left.tabbedPane.getSelectedComponent();
        Component selectedRight = right.tabbedPane.getSelectedComponent();
        
    //--GET COLORS
        if (selectedLeft instanceof side.TabPanel && selectedRight instanceof side.TabPanel) {
            colorLeft = ((side.TabPanel) selectedLeft).choosedColor();
            colorRight = ((side.TabPanel) selectedRight).choosedColor();
        } else {
            throw new RuntimeException("Unexpected component: " + selectedLeft + " " + selectedRight);
        }


    //--CHECK COLORS
        if (colorLeft.equals(colorRight)) {
            showColorError();
            return null;
        }

    //--CREATE PLAYERS
        if (selectedLeft == left.ai && selectedRight == right.ai) {
            player1 = new PlayerAi(colorLeft, ((side.AiTabPanel) selectedLeft).choosedStrategy());
            player2 = new PlayerAi(colorRight, ((side.AiTabPanel) selectedRight).choosedStrategy());
        } else if (selectedLeft == left.human && selectedRight == right.human) {
            player1 = new PlayerManual(colorLeft);
            player2 = new PlayerManual(colorRight);
        } else {
            throw new RuntimeException("Unexpected component: " + selectedLeft + " " + selectedRight);
        }

        return new Player[]{player1, player2};
    }



    public void showColorError() {
        JOptionPane.showMessageDialog(this, "Players must have different colors", "Error", JOptionPane.ERROR_MESSAGE);
    }


    @Override
    public void addNotify() {
        super.addNotify();
        Dimension size = new Dimension((int) (getParent().getWidth() * 0.2), getParent().getHeight());
        left.setPreferredSize(size);
        right.setPreferredSize(size);

        size = new Dimension((int) (getParent().getWidth() * 0.6), getParent().getHeight());
        center.setPreferredSize(size);

        validate();

    }


}
