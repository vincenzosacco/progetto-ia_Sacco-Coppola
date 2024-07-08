package org.project.UI.View.panels.home;

import org.project.Logic.Game.player.Player;
import org.project.Logic.Game.player.ai.PlayerAi;
import org.project.Logic.Game.player.human.PlayerManual;
import org.project.UI.Model.GameModel;
import org.project.UI.View.panels.MyPanel;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends MyPanel {
    private final side left, right;
    private JPanel center;


    public HomePanel() {
        super();
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

        player1 = switch (left.tabbedPane.getSelectedIndex()) {
            case 0 -> new PlayerAi('A', 0, PlayerAi.GROUP_1);
            case 1 -> new PlayerManual('A', 0);
            default -> throw new IllegalStateException("Unexpected value: " + left.tabbedPane.getSelectedIndex());
        };

        player2 = switch (right.tabbedPane.getSelectedIndex()) {
            case 0 -> new PlayerAi('B', 1, PlayerAi.GROUP_1);
            case 1 -> new PlayerManual('B', 1);
            default -> throw new IllegalStateException("Unexpected value: " + right.tabbedPane.getSelectedIndex());
        };

        return new Player[]{player1, player2};
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
